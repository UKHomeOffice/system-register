package uk.gov.digital.ho.systemregister.util;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toUnmodifiableList;

public final class NullSafeUtils {
    private NullSafeUtils() {
    }

    public static String safelyTrimmed(String value) {
        return value != null ? value.trim() : null;
    }

    public static List<String> allSafelyTrimmed(List<String> values) {
        return values != null
               ? values.stream()
                       .map(NullSafeUtils::safelyTrimmed)
                       .collect(toUnmodifiableList())
               : null;
    }

    public static <T> List<T> safelyCopied(List<T> values) {
        return values != null ? List.copyOf(values) : null;
    }

    public static <T, R> List<R> safelyMapped(List<T> values, Function<T, R> mapper) {
        return values != null
               ? values.stream()
                       .map(mapper)
                       .collect(toUnmodifiableList())
               : null;
    }
}
