package football.StatsManagement;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FootballExceptionHandler {

  @ExceptionHandler(FootballException.class)
  public ResponseEntity<String> handleException(FootballException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
  }

  // @AssertTrueで検証に失敗した場合の例外処理
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
    // ConstraintViolationException からエラー詳細を取得
    StringBuilder errorMessages = new StringBuilder("検証エラーが発生しました：\n");

    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
    for (ConstraintViolation<?> violation : violations) {
      errorMessages.append("Property: ").append(violation.getPropertyPath())
          .append(", Invalid Value: ").append(violation.getInvalidValue())
          .append(", Message: ").append(violation.getMessage())
          .append("\n");
    }

    return ResponseEntity.badRequest().body(errorMessages.toString());
  }
}
