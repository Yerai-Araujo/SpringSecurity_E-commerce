package com.tfg.wapp.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

@Service
public class TwoFactorAuthService {

    private final GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
    //private static final String ISSUER = "FastFoodApp";

    // Generate a secret key for a new user
    public String generateSecretKey() {
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        return key.getKey();
    }

    // Verify the OTP entered by the user
    public boolean isOtpValid(String secretKey, int otp) {
        return googleAuthenticator.authorize(secretKey, otp);
    }
    
    public static byte[] generateQRCode(String qrCodeData, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, width, height);

        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, new MatrixToImageConfig());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "PNG", baos);
        return baos.toByteArray();
    }
}
