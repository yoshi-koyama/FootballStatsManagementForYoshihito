package football.StatsManagement.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {

  public static void compareJson(String expectedJson, String actualJson) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();

    JsonNode expectedNode = objectMapper.readTree(expectedJson);
    JsonNode actualNode = objectMapper.readTree(actualJson);

    // ここで比較ロジックを実装
    if (!expectedNode.equals(actualNode)) {
      // 異なる場合の処理
      System.out.println("JSONs are not identical. Differences:");
      logDifferences(expectedNode, actualNode);
      // 例外をスローしてテストを失敗させる
      throw new AssertionError("Expected and actual JSON do not match.");
    } else {
      System.out.println("JSONs are semantically identical.");
    }
  }

  // 差異をログに出力するメソッド
  private static void logDifferences(JsonNode expectedNode, JsonNode actualNode) {
    expectedNode.fieldNames().forEachRemaining(field -> {
      if (!actualNode.has(field)) {
        System.out.println("Missing field in actual JSON: " + field);
      } else if (!expectedNode.get(field).equals(actualNode.get(field))) {
        System.out.println("Field '" + field + "' differs. Expected: "
            + expectedNode.get(field) + ", Actual: " + actualNode.get(field));
      }
    });
  }

}
