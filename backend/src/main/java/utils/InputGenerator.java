package utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InputGenerator {

  public static List<Integer> generateInputWithOneProbability(int length, double oneProbability) {
    if (oneProbability < 0 || oneProbability > 1) {
      throw new IllegalArgumentException("Probability of 1 must be between 0 and 1, not: " + oneProbability);
    }
    return IntStream.range(0, length).mapToObj(i -> generateRandomInteger(oneProbability)).collect(Collectors.toList());
  }

  public static Integer generateRandomInteger(double oneProbability) {
    return (Math.random() > oneProbability) ? 0 : 1;
  }


}
