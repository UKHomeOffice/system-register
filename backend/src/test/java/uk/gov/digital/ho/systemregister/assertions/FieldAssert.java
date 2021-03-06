package uk.gov.digital.ho.systemregister.assertions;

import org.assertj.core.api.AbstractObjectAssert;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("UnusedReturnValue")
public class FieldAssert extends AbstractObjectAssert<FieldAssert, Field> {
    @SuppressWarnings({"CdiInjectionPointsInspection", "QsPrivateBeanMembersInspection"})
    private FieldAssert(Field field) {
        super(field, FieldAssert.class);
    }

    public static FieldAssert assertThatField(String name, Class<?> cls) {
        try {
            return new FieldAssert(cls.getDeclaredField(name));
        } catch (NoSuchFieldException e) {
            throw new AssertionError("no such field " + name, e);
        }
    }

    private static boolean findTypeArgumentAnnotation(AnnotatedType[] typeArguments, Class<? extends Annotation> annotation) {
        for (AnnotatedType typeArgument : typeArguments) {
            if (typeArgument.isAnnotationPresent(annotation)) {
                return true;
            }
        }
        return false;
    }

    @SafeVarargs
    public final FieldAssert hasAnnotations(Class<? extends Annotation>... annotations) {
        Set<Class<? extends Annotation>> missing = new HashSet<>();
        for (Class<? extends Annotation> annotation : annotations) {
            if (!actual.isAnnotationPresent(annotation)) {
                missing.add(annotation);
            }
        }
        assertThat(missing)
                .overridingErrorMessage(() -> String.format(
                        "%nExpecting field%n  <%s.%s>%nto have annotations:%n  <%s>%nbut the following annotations were not found:%n  <%s>",
                        actual.getDeclaringClass().getSimpleName(),
                        actual.getName(),
                        Arrays.asList(annotations),
                        missing))
                .isEmpty();
        return this;
    }

    @SafeVarargs
    public final FieldAssert hasTypeArgumentAnnotations(Class<? extends Annotation>... annotations) {
        var type = actual.getAnnotatedType();
        assertThat(type).isInstanceOf(AnnotatedParameterizedType.class);

        Set<Class<? extends Annotation>> missing = new HashSet<>();
        var parameterizedType = (AnnotatedParameterizedType) type;
        var typeArguments = parameterizedType.getAnnotatedActualTypeArguments();
        for (Class<? extends Annotation> annotation : annotations) {
            boolean matched = findTypeArgumentAnnotation(typeArguments, annotation);
            if (!matched) {
                missing.add(annotation);
            }
        }

        assertThat(missing)
                .overridingErrorMessage(() -> String.format(
                        "%nExpecting type argument of%n  <%s>%non%n  <%s.%s>%nto have annotations:%n  <%s>%nbut the following annotations were not found:%n  <%s>",
                        parameterizedType.getType(),
                        actual.getDeclaringClass().getSimpleName(),
                        actual.getName(),
                        Arrays.asList(annotations),
                        missing))
                .isEmpty();
        return this;
    }
}
