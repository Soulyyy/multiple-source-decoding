package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import data.State;

public class TestUtils {

  public static List<Integer> splitString(String input) {
    return Arrays.stream(input.split("")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
  }

  public static List<State> createStateList(List<Integer> encodedInput, int length) {
    List<State> states = new ArrayList<>();
    for (int i = 0; i < encodedInput.size() / length; i++) {
      states.add(new State(encodedInput.subList(i * length, i * length + length)));
    }
    return states;
  }
}
