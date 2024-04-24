package com.cqupt.software_10.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class SecurityUtil {
//    private static final String SECRET_KEY = "your_secret_key"; // 用于签名的密钥
    private static final String SECRET_KEY = "66bd90a7525c9c6114d27f81516a41d50ab5cbedda4196cf33b7d5602502594174c4ff9fcd3402cec27d1d47c81b51213e8bd3154cb2d96074f2385e67e78a5d"; // 用于签名的密钥

    // 生成 Token
    public static String generateToken(String userId) {
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + 3600 * 1000); // Token 有效期为 1 小时
        return JWT.create()
                .withClaim("userId", userId) // 存储用户信息，这里以 userId 为例
                .withExpiresAt(expirationTime) // 设置 Token 过期时间
                .sign(Algorithm.HMAC256(SECRET_KEY)); // 使用 HMAC256 算法进行签名
    }

    // 解析 Token 并获取用户ID
    public static String getUserIdFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .build()
                    .verify(token);

            Claim userIdClaim = jwt.getClaim("userId");
            return userIdClaim.asString();
        } catch (Exception e) {
            // Token 解析失败
            return null;
        }
    }
    /**
     *
     *  SHA-256  加密对密码
     * @param data
     * @return
     */

    public static String hashDataSHA256(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashInBytes = md.digest(data.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
