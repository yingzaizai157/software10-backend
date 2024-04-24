package com.cqupt.software_10.util;
import java.security.SecureRandom;

public class GenerateTokenSECRETKEY {
    public static String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[64]; // 64 bytes = 512 bits
        random.nextBytes(bytes);
        return bytesToString(bytes);
    }

    private static String bytesToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String secretKey = generateSecretKey();
        System.out.println("Generated Secret Key: " + secretKey);
//        66bd90a7525c9c6114d27f81516a41d50ab5cbedda4196cf33b7d5602502594174c4ff9fcd3402cec27d1d47c81b51213e8bd3154cb2d96074f2385e67e78a5d
    }


}
