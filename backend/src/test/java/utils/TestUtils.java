package utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestUtils {

  public static List<Integer> splitString(String input) {
    return Arrays.stream(input.split("")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
  }
}
