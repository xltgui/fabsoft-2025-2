package br.univille.pagfut.domain.pix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class PixQrCodeService {

    private static final String PAYLOAD_INDICATOR = "000201";
    private static final String MERCHANT_ACCOUNT_INFO_GUI = "26" + "0014br.gov.bcb.pix";
    private static final String MERCHANT_CATEGORY_CODE = "52040000";
    private static final String TRANSACTION_CURRENCY = "5303986";
    private static final String COUNTRY_CODE = "5802BR";
    private static final String ADDITIONAL_DATA_FIELD = "62070503***";
    private static final String CRC16_START = "6304";

    public byte[] generateStaticQrCode(String payload) throws IOException, WriterException {
        //String payload = generateStaticPayload(pixKey, amount, recipientName, recipientCity);
        return generateQrCodeImage(payload);
    }

    public String generateStaticPayload(String pixKey, KeyType keyType, String recipientName, String recipientCity, String amount) {
        StringBuilder payloadBuilder = new StringBuilder();

        pixKey = verifyKeyType(pixKey, keyType);

        // 00 - Payload Format Indicator
        payloadBuilder.append(PAYLOAD_INDICATOR);

        String pixKeyField = "01" + String.format("%02d", pixKey.length()) + pixKey;
        String merchantAccountInfo = "26" + String.format("%02d", ("0014br.gov.bcb.pix" + pixKeyField).length()) + "0014br.gov.bcb.pix" + pixKeyField;
        payloadBuilder.append(merchantAccountInfo);

        // 52 - Merchant Category Code
        payloadBuilder.append(MERCHANT_CATEGORY_CODE);

        // 53 - Transaction Currency
        payloadBuilder.append(TRANSACTION_CURRENCY);

        if (amount != null && !amount.isEmpty()) {
            String amountField = "54" + String.format("%02d", amount.length()) + amount;
            payloadBuilder.append(amountField);
        }

        // 58 - Country Code
        payloadBuilder.append(COUNTRY_CODE);

        // 59 - Merchant Name
        String nameField = "59" + String.format("%02d", recipientName.length()) + recipientName;
        payloadBuilder.append(nameField);

        // 60 - Merchant City
        String cityField = "60" + String.format("%02d", recipientCity.length()) + recipientCity;
        payloadBuilder.append(cityField);

        // 62 - Additional Data Field (TXID)
        payloadBuilder.append(ADDITIONAL_DATA_FIELD);

        // 63 - CRC16
        String crcPayload = payloadBuilder.toString() + CRC16_START;
        String crc16 = getCRC16(crcPayload);
        payloadBuilder.append(CRC16_START).append(crc16);

        return payloadBuilder.toString();
    }



    private byte[] generateQrCodeImage(String payload) throws IOException, WriterException {
        int size = 256;
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(payload, BarcodeFormat.QR_CODE, size, size, hints);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }

    private String getCRC16(String payload) {
        int crc = 0xFFFF;
        for (char c : payload.toCharArray()) {
            crc ^= (int) c << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) > 0) {
                    crc = (crc << 1) ^ 0x1021;
                } else {
                    crc <<= 1;
                }
            }
        }
        return String.format("%04X", crc & 0xFFFF);
    }

    private String verifyKeyType(String pixKey, KeyType keyType) {
        switch (keyType) {
            case PHONE:
                if (pixKey.startsWith("+")) {
                    return pixKey.replaceAll("[^0-9]", "");
                } else {
                    return "+55" + pixKey.replaceAll("[^0-9]", "");
                }
            case CPF:
                return pixKey;
            case EMAIL:
                return pixKey;
            case RANDOM:
                return pixKey;
            default:
                throw new IllegalArgumentException("Invalid key type");
        }
    }
}