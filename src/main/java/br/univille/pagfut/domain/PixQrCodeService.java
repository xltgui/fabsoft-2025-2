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
import java.util.Map;

@Service
public class PixQrCodeService {

    public String generatePixQrCode(PixPaymentRequest request) throws WriterException, IOException {
        String payload = generatePixPayload(request.key(), request.amount(), request.receiver(), request.description());
        return generateQrCodeBase64(payload, 300, 300);
    }

    public String generatePixPayload(String pixKey, BigDecimal amount, String receiver, String description) {
        StringBuilder payload = new StringBuilder();

        payload.append("000201"); // Payload Format Indicator (00)

        // Merchant Account Information (26)
        //String merchantAccountInformation = "0014br.gov.bcb.pix01" + String.format("%02d", pixKey.length()) + pixKey;

        // Subcampo 00: GUID
        String guidSubField = "0014br.gov.bcb.pix";

        // Subcampo 01: Chave Pix
        String pixKeySubField = "01" + String.format("%02d", pixKey.length()) + pixKey;

        // Combina os subcampos
        String merchantAccountInformation = guidSubField + pixKeySubField;

        payload.append("26").append(String.format("%02d", merchantAccountInformation.length())).append(merchantAccountInformation);

        payload.append("52040000"); // Merchant Category Code (52)
        payload.append("5303986"); // Transaction Currency (53)

        // Transaction Amount (54)
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            String amountStr = String.format("%.2f", amount).replace(',', '.');
            payload.append("54").append(String.format("%02d", amountStr.length())).append(amountStr);
        }

        payload.append("5802BR"); // Country Code (58)
        payload.append("59").append(String.format("%02d", receiver.length())).append(receiver); // Merchant Name (59)

        // Additional Data Field (62) - TXID
        // Corrigido para calcular o tamanho corretamente
        String txid = "PAGFUT17582";
        String txidSubfield = "05" + String.format("%02d", txid.length()) + txid;
        payload.append("62").append(String.format("%02d", txidSubfield.length())).append(txidSubfield);

        // CRC16 (63)
        String dataWithoutCrc = payload.toString() + "6304";
        String crc = calculateCorrectCRC16(dataWithoutCrc.getBytes(StandardCharsets.US_ASCII));
        payload.append("6304").append(crc);

        return payload.toString();
    }

    private String calculateCorrectCRC16(byte[] bytes) {
        int crc = 0xFFFF;
        int polynomial = 0x1021;

        for (byte b : bytes) {
            crc ^= (b & 0xFF) << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ polynomial;
                } else {
                    crc <<= 1;
                }
            }
        }
        crc &= 0xFFFF;
        return String.format("%04X", crc);
    }

    public String generateQrCodeBase64(String text, int width, int height) throws WriterException, IOException {
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