package com.tfg.wapp.controllers;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.WriterException;
import com.tfg.wapp.services.TwoFactorAuthService;

@RestController
public class QRCodeController {

    @GetMapping(value = "/qr_code_img", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQRCodeImage(@RequestParam String username, @RequestParam String secretKey) throws WriterException, IOException {
        // Generate OTP URL
        String qrCodeUrl = "otpauth://totp/FastFoodApp:" + username + "?secret=" + secretKey + "&issuer=FastFoodApp";

        // Generate QR Code Image
        byte[] qrCodeImage = TwoFactorAuthService.generateQRCode(qrCodeUrl, 200, 200);

        return ResponseEntity.ok(qrCodeImage);
    }
}