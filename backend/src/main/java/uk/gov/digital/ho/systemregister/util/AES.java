package uk.gov.digital.ho.systemregister.util;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.jboss.logging.Logger;

public class AES {
    private static final Logger LOG = Logger.getLogger(AES.class);
    private static final SecureRandom secureRandom = new SecureRandom();
    private final static int GCM_IV_LENGTH = 12;

    public static byte[] encrypt(String plaintext, String key) throws EncryptionError {
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
            byte[] iv = new byte[GCM_IV_LENGTH];
            secureRandom.nextBytes(iv);
            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            byte[] cipherText = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
            byteBuffer.put(iv);
            byteBuffer.put(cipherText);
            return byteBuffer.array();
        } catch (Exception e) {
            LOG.error("Error encrypting data: " + e.getMessage());
            throw new EncryptionError();
        }
    }

    public static String decrypt(byte[] cipherMessage, String key) throws EncryptionError {
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            AlgorithmParameterSpec gcmIv = new GCMParameterSpec(128, cipherMessage, 0, GCM_IV_LENGTH);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmIv);

            byte[] plainText = cipher.doFinal(cipherMessage, GCM_IV_LENGTH, cipherMessage.length - GCM_IV_LENGTH);

            return new String(plainText, StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOG.error("Error encrypting data: " + e.getMessage());
            throw new EncryptionError();
        }
    }
}