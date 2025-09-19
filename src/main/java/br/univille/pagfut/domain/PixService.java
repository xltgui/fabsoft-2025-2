package br.univille.pagfut.domain;


import br.univille.pagfut.api.pix.PixPaymentRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;


import org.springframework.stereotype.Service;

@Service
public class PixService {
    // Constantes para tipos de chave PIX
    private static final String PIX_KEY_TYPE_CPF = "CPF";
    private static final String PIX_KEY_TYPE_PHONE = "PHONE";
    private static final String PIX_KEY_TYPE_EMAIL = "EMAIL";
    private static final String PIX_KEY_TYPE_RANDOM = "RANDOM";

    // Padrões regex para validação
    private static final Pattern CPF_PATTERN = Pattern.compile("^[0-9]{11}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,11}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern RANDOM_KEY_PATTERN = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    public String generatePixQrCode(PixPaymentRequest request) throws WriterException, IOException {
        String payload = generatePixPayload(request.key(), request.amount(), request.receiver(), request.description());
        return generateQrCodeBase64(payload, 300, 300);
    }

    public String generatePixPayload(String pixKey, BigDecimal amount, String receiver, String description) {

        StringBuilder payload = new StringBuilder();

        // Payload Format Indicator (00)
        payload.append("000201");

        // Merchant Account Information (26)
        StringBuilder merchantAccountInformation = new StringBuilder();

        // Subcampo 00: GUID
        merchantAccountInformation.append("0014br.gov.bcb.pix");

        // Subcampo 01: Chave Pix
        String formattedPixKey = pixKey;

        // Lógica de detecção e formatação da chave Pix
        /*if (pixKey.matches("\\d{11}")) {
            // Se tem 11 dígitos, é um CPF ou celular sem DDI.
            // Para ser robusto, o ideal é ter um campo 'tipo' no request.
            // Como não tem, a gente assume que a chave não precisa de formatação.
            formattedPixKey = pixKey;
        } else if (pixKey.matches("\\d{13}") && pixKey.startsWith("55")) {
            // Se a chave tem 13 dígitos e começa com '55', é um telefone já formatado.
            formattedPixKey = pixKey;
        } else if (pixKey.matches("\\d{10}")) {
            // Se a chave tem 10 dígitos, é um telefone sem o nono dígito e sem DDI.
            formattedPixKey = "55" + pixKey;
        }*/

        merchantAccountInformation
                .append("01") // Identificador do subcampo de chave Pix
                .append(String.format("%02d", formattedPixKey.length()))
                .append(formattedPixKey);

        payload.append("26")
                .append(String.format("%02d", merchantAccountInformation.length()))
                .append(merchantAccountInformation);

        // Merchant Category Code (52)
        payload.append("52040000");

        // Transaction Currency (53)
        payload.append("5303986");

        // Transaction Amount (54)
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            // Corrigido para usar ponto como separador decimal
            String amountStr = String.format("%.2f", amount.doubleValue()).replace(',', '.');
            payload.append("54")
                    .append(String.format("%02d", amountStr.length()))
                    .append(amountStr);
        }

        // Country Code (58)
        payload.append("5802BR");

        // Merchant Name (59)
        payload.append("59")
                .append(String.format("%02d", receiver.length()))
                .append(receiver);

        // Additional Data Field (62) - TXID
        String txid = "PAGFUT" + System.currentTimeMillis();
        payload.append("62")
                .append(String.format("%02d", 4 + txid.length()))
                .append("05")
                .append(String.format("%02d", txid.length()))
                .append(txid);

        // CRC16 (63)
        String dataWithoutCrc = payload.toString() + "6304";
        String crc = calculateCRC16(dataWithoutCrc.getBytes());
        payload.append("6304").append(crc);

        return payload.toString();
    }

    /**
     * Determina o tipo da chave PIX baseado em seu formato
     */
    private String determinePixKeyType(String pixKey) {
        if (pixKey == null || pixKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Chave PIX não pode ser nula ou vazia");
        }

        String cleanKey = pixKey.trim();

        if (CPF_PATTERN.matcher(cleanKey).matches()) {
            return PIX_KEY_TYPE_CPF;
        } else if (PHONE_PATTERN.matcher(cleanKey).matches()) {
            return PIX_KEY_TYPE_PHONE;
        } else if (EMAIL_PATTERN.matcher(cleanKey).matches()) {
            return PIX_KEY_TYPE_EMAIL;
        } else if (RANDOM_KEY_PATTERN.matcher(cleanKey).matches()) {
            return PIX_KEY_TYPE_RANDOM;
        } else {
            // Se não se encaixar em nenhum padrão específico, trata como chave aleatória
            return PIX_KEY_TYPE_RANDOM;
        }
    }

    /**
     * Valida se a chave PIX é válida baseada em seu tipo
     */
    public boolean validatePixKey(String pixKey) {
        try {
            determinePixKeyType(pixKey);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Gera o payload formatado para cópia e cola com informações do tipo de chave
     */
    public String generateFormattedPixPayload(String pixKey, BigDecimal amount, String receiver, String description) {
        String keyType = determinePixKeyType(pixKey);
        String payload = generatePixPayload(pixKey, amount, receiver, description);

        return String.format(
                "Tipo de Chave: %s\nChave: %s\nValor: R$ %s\nRecebedor: %s\nDescrição: %s\n\nPayload PIX:\n%s",
                getKeyTypeDescription(keyType),
                pixKey,
                amount != null ? String.format("%.2f", amount) : "0.00",
                receiver,
                description != null ? description : "Nenhuma",
                payload
        );
    }

    /**
     * Retorna a descrição amigável do tipo de chave
     */
    private String getKeyTypeDescription(String keyType) {
        switch (keyType) {
            case PIX_KEY_TYPE_CPF:
                return "CPF";
            case PIX_KEY_TYPE_PHONE:
                return "Telefone/Celular";
            case PIX_KEY_TYPE_EMAIL:
                return "E-mail";
            case PIX_KEY_TYPE_RANDOM:
                return "Chave Aleatória";
            default:
                return "Desconhecido";
        }
    }

    private String calculateCRC16(byte[] bytes) {
        int crc = 0xFFFF;
        for (byte b : bytes) {
            crc ^= (b & 0xFF) << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ 0x1021;
                } else {
                    crc <<= 1;
                }
            }
        }
        crc &= 0xFFFF;
        return String.format("%04X", crc);
    }

    public String generateQrCodeBase64(String text, int width, int height)
            throws WriterException, IOException {

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);

        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
