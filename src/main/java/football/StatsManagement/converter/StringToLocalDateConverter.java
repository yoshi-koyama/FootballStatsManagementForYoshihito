package football.StatsManagement.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToLocalDateConverter implements Converter<String, LocalDate> {
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

  @Override
  public LocalDate convert(String source) {
    try {
      return LocalDate.parse(source, DATE_FORMATTER);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid date format, expected yyyyMMdd", e);
    }
  }

  
}
