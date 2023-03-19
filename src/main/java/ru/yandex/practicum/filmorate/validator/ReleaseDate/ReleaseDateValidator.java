package ru.yandex.practicum.filmorate.validator.ReleaseDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.Date;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseValidator, Date> {

    private static final Date CINEMA_BIRTHDAY = Date.valueOf("1895-12-28");

    @Override
    public void initialize(ReleaseValidator validator) { }

    @Override
    public boolean isValid(Date releaseDate, ConstraintValidatorContext cxt) {
        return releaseDate.after(CINEMA_BIRTHDAY);
    }
}
