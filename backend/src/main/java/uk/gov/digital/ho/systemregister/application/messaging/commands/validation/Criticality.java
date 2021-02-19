package uk.gov.digital.ho.systemregister.application.messaging.commands.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//TODO update to custom validator with dynamic values in default message
@Constraint(validatedBy = {})
@Pattern(regexp = "high|low|medium|cni|unknown", message = "Criticality must be one of the following values: high, low, medium, cni, unknown")
@Retention(RUNTIME)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@SuppressWarnings("unused")
public @interface Criticality {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
