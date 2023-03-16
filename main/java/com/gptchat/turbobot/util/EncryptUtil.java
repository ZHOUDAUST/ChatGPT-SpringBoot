package com.gptchat.turbobot.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.Security;
import java.util.Base64;

/**
 * @author zhouda
 * @since 2023-03-16
 */
public class EncryptUtil {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptUtil.class);

    /**
     * 算法
     */
    private static final String ALGORITHM = "AES/GCM/NoPadding";

    /**
     * 默认编码
     */
    private static final String DEFAULT_CODING = "utf-8";

    /**
     * aes key
     */
    private static final String AES_KEY = "6fd85bf9631b4be6b8724a9675139293";

    /**
     * aes iv
     */
    private static final String AES_IV = "0123456789ABCDEF";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }


    /**
     * 密码加密
     *
     * @param password 密码
     * @return 加密后的内容
     */
    public static String encrypt(String password) {
        try {
            byte[] input = password.getBytes(DEFAULT_CODING);
            MessageDigest md = MessageDigest.getInstance("SHA256");
            byte[] digest = md.digest(AES_KEY.getBytes(DEFAULT_CODING));
            SecretKeySpec skc = new SecretKeySpec(digest, "AES");
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, AES_IV.getBytes(DEFAULT_CODING));
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skc, gcmParameterSpec);
            byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
            int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
            cipher.doFinal(cipherText, ctLength);
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            LOGGER.error("加密发生异常, password = [{}]", password, e);
            throw new RuntimeException("加密发生异常");
        }
    }

    /**
     * 密码解密
     *
     * @param content 密码密文
     * @return 密码明文
     */
    public static String decrypt(String content) {
        try {
            byte[] keyBytes = AES_KEY.getBytes(DEFAULT_CODING);
            MessageDigest md = MessageDigest.getInstance("SHA256");
            byte[] digest = md.digest(keyBytes);
            SecretKeySpec secretKeySpec = new SecretKeySpec(digest, "AES");
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, AES_IV.getBytes(DEFAULT_CODING));
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);
            byte[] plain = cipher.doFinal(Base64.getDecoder().decode(content));
            return new String(plain, DEFAULT_CODING);
        } catch (Exception e) {
            LOGGER.error("解密发生异常, content = [{}]", content, e);
            throw new RuntimeException("解密发生异常");
        }
    }
}
