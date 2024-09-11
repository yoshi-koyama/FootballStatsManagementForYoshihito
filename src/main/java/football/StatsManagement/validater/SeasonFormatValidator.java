package football.StatsManagement.validater;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeasonFormatValidator implements ConstraintValidator<SeasonFormat, String> {
  private static final Pattern SEASON_PATTERN = Pattern.compile("^(\\d{4})-(\\d{2})$");

  @Override
  public void initialize(SeasonFormat constraintAnnotation) {
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isEmpty()) {
      return false;
    }

    Matcher matcher = SEASON_PATTERN.matcher(value);
    if (!matcher.matches()) {
      return false;
    }

    // Extract year parts
    int startYear = Integer.parseInt(matcher.group(1));
    int endYear = Integer.parseInt(matcher.group(2));

    // Validate the year sequence
    return endYear == (startYear % 100) + 1;
  }

}
