package ru.yandex.practicum.filmorate.validator.ReleaseDate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
@Documented
@Inherited
@Constraint(validatedBy = ReleaseDateValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ReleaseValidator {

    String message() default "{ReleaseDate}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
