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
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class PixQrCodeService {

    public String generatePixQrCode(PixPaymentRequest request) throws WriterException, IOException {

        String payload = generatePixPayload(request.key(), request.amount(), request.receiver(), request.description());
        return generateQrCodeBase64(payload, 300, 300);
    }

    private String generatePixPayload(String pixKey, BigDecimal amount,String receiver, String description) throws WriterException, IOException {

        StringBuilder payload = new StringBuilder();

        // Payload Format Indicator (00)
        payload.append("000201");

        // Merchant Account Information (26)
        payload.append("26")
                .append(String.format("%02d", 50)) // Tamanho do campo GUI + chave PIX
                .append("0014br.gov.bcb.pix")
                .append(String.format("%02d", pixKey.length()))
                .append(pixKey);

        // Merchant Category Code (52) - 0000 para genérico
        payload.append("52040000");

        // Transaction Currency (53) - BRL (986)
        payload.append("5303986");

        // Transaction Amount (54) - Opcional
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            String amountStr = String.format("%.2f", amount.doubleValue());
            payload.append("54")
                    .append(String.format("%02d", amountStr.length()))
                    .append(amountStr);
        }

        // Country Code (58) - BR
        payload.append("5802BR");

        // Merchant Name (59)
        payload.append("59")
                .append(String.format("%02d", receiver.length()))
                .append(receiver);

        /*
        // Merchant City (60)
        payload.append("60")
                .append(String.format("%02d", cidadeRecebedor.length()))
                .append(cidadeRecebedor);
         */

        // Additional Data Field (62) - TXID
        String txid = "PAGFUT" + System.currentTimeMillis();
        payload.append("62")
                .append(String.format("%02d", txid.length() + 5)) // 05 é o tamanho do campo 05 + TXID
                .append("05")
                .append(String.format("%02d", txid.length()))
                .append(txid);

        // CRC16 (63)
        String dataWithoutCrc = payload.toString() + "6304";
        String crc = calculateCRC16(dataWithoutCrc.getBytes());
        payload.append("6304").append(crc);

        return payload.toString();
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

    private String generateQrCodeBase64(String text, int width, int height)
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
