package io;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BinaryReader {


  public List<Integer> readString(String input) {
    return validateInput(input);
  }

  private List<Integer> validateInput(String input) {
    if (input.length() == 0) {
      throw new IllegalArgumentException("Input is empty");
    }
    StringBuilder sb = new StringBuilder();
    input.chars().filter(i -> i == '0' || i == '1').forEach(i -> sb.append((char) i));
    if (sb.toString().length() != input.length()) {
      throw new IllegalArgumentException("Input contains non binary characters: \"" + input + "\"");
    }
    return Arrays.stream(input.split(""))
        .mapToInt(Integer::parseInt)
        .boxed()
        .collect(Collectors.toList());
  }

}
