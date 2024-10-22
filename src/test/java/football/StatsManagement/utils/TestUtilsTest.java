package football.StatsManagement.utils;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

class TestUtilsTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void compareJson_identicalJsons_shouldPass() throws Exception {
    String expectedJson = "{\"name\":\"John\", \"age\":30}";
    String actualJson = "{\"name\":\"John\", \"age\":30}";

    // No exception should be thrown
    assertDoesNotThrow(() -> TestUtils.compareJson(expectedJson, actualJson));
  }

  @Test
  void compareJson_differentJsons_shouldThrowAssertionError() {
    String expectedJson = "{\"name\":\"John\", \"age\":30}";
    String actualJson = "{\"name\":\"John\", \"age\":31}";

    // Check that an AssertionError is thrown
    Error error = assertThrows(AssertionError.class, () -> {
      TestUtils.compareJson(expectedJson, actualJson);
    });

    assertEquals("Expected and actual JSON do not match.", error.getMessage());
  }

  @Test
  void compareJson_missingFieldInActualJson_shouldLogDifference() {
    String expectedJson = "{\"name\":\"John\", \"age\":30}";
    String actualJson = "{\"name\":\"John\"}";

    // Check that an AssertionError is thrown
    Error error = assertThrows(AssertionError.class, () -> {
      TestUtils.compareJson(expectedJson, actualJson);
    });

    assertEquals("Expected and actual JSON do not match.", error.getMessage());
  }

  @Test
  void compareJson_extraFieldInActualJson_shouldLogDifference() {
    String expectedJson = "{\"name\":\"John\"}";
    String actualJson = "{\"name\":\"John\", \"age\":30}";

    // Check that an AssertionError is thrown
    Error error = assertThrows(AssertionError.class, () -> {
      TestUtils.compareJson(expectedJson, actualJson);
    });

    assertEquals("Expected and actual JSON do not match.", error.getMessage());
  }
}