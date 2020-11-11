package uk.gov.digital.ho.systemregister.test.util;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.util.AES;
import uk.gov.digital.ho.systemregister.util.EncryptionError;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
