package uk.gov.digital.ho.systemregister.util;

import org.jboss.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AES {
    private static final Logger LOG = Logger.getLogger(AES.class);
    private static final SecureRandom secureRandom = new SecureRandom();
    private final static int GCM_IV_LENGTH = 12;

    public static byte[] encrypt(String plaintext, String key) throws EncryptionError {
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(UTF_8), "AES");
            byte[] iv = new byte[GCM_IV_LENGTH];
            secureRandom.nextBytes(iv);
            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            byte[] cipherText = cipher.doFinal(plaintext.getBytes(UTF_8));

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
            SecretKey secretKey = new SecretKeySpec(key.getBytes(UTF_8), "AES");
            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            AlgorithmParameterSpec gcmIv = new GCMParameterSpec(128, cipherMessage, 0, GCM_IV_LENGTH);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmIv);

            byte[] plainText = cipher.doFinal(cipherMessage, GCM_IV_LENGTH, cipherMessage.length - GCM_IV_LENGTH);

            return new String(plainText, UTF_8);
        } catch (Exception e) {
            LOG.error("Error encrypting data: " + e.getMessage());
            throw new EncryptionError();
        }
    }
}
