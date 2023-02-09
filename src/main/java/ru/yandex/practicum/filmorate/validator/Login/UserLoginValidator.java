package ru.yandex.practicum.filmorate.validator.Login;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserLoginValidator implements ConstraintValidator<NotHaveSpace, String> {

    @Override
    public void initialize(NotHaveSpace validator) { }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext cxt) {
        return !login.contains(" ");
    }
}
