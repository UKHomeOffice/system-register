package uk.gov.digital.ho.systemregister.util;

import java.io.IOException;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.io.IOUtils.toByteArray;

public final class ResourceUtils {
    private ResourceUtils() {
    }

    public static String getResourceAsString(String resource) {
        byte[] bytes = getResourceAsBytes(resource);
        return new String(bytes, UTF_8);
    }

    public static byte[] getResourceAsBytes(String resource) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream stream = classLoader.getResourceAsStream(resource)) {
            return toByteArray(requireNonNull(stream));
        } catch (IOException e) {
            throw new AssertionError("unable to load resource: " + resource, e);
        }
    }
}
