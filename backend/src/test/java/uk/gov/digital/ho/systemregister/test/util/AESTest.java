package uk.gov.digital.ho.systemregister.test.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.jboss.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import uk.gov.digital.ho.systemregister.util.AES;
import uk.gov.digital.ho.systemregister.util.EncryptionError;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class AESTest {
    private static final Logger LOG = Logger.getLogger(AESTest.class);

    @Test
    public void Decrypt() throws EncryptionError {
        final String origonalString = "{a:\" ";
        final byte[] encryptedString = AES.encrypt(origonalString, "1234567812345678");

        final String actual = AES.decrypt(encryptedString, "1234567812345678");

        assertEquals(origonalString, actual);
    }
}
