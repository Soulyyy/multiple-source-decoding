package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import data.State;

public class VectorUtils {

  public static long computeHammingDistance(List<Integer> first, List<Integer> second) {
    if (first == null || second == null || first.size() != second.size()) {
      throw new IllegalArgumentException("Can't compute hamming distance of " + first + " and " + second);
    }
    return IntStream.range(0, first.size()).filter(i -> !first.get(i).equals(second.get(i))).count();
  }

  public static List<Integer> generateUniformlyRandomVector(int length) {
    return generateVectorWithOneProbability(length, 0.5);
  }

  public static List<Integer> generateVectorWithOneProbability(int length, double oneProbability) {
    if (oneProbability < 0 || oneProbability > 1) {
      throw new IllegalArgumentException("Probability of 1 must be between 0 and 1, not: " + oneProbability);
    }
    return IntStream.range(0, length).mapToObj(i -> generateRandomInteger(oneProbability)).collect(Collectors.toList());
  }


  public static Integer generateRandomInteger(double oneProbability) {
    return (Math.random() > oneProbability) ? 0 : 1;
  }

  public static synchronized List<Integer> createVectorErrors(List<Integer> vector, double errorRate) {
    return vector.stream().map(i -> ((Math.random() < errorRate ? 1 : 0) + i) % 2).collect(Collectors.toList());
  }

  public static List<State> createStateList(List<Integer> encodedInput, int length) {
    List<State> states = new ArrayList<>();
    for (int i = 0; i < encodedInput.size() / length; i++) {
      states.add(new State(encodedInput.subList(i * length, i * length + length)));
    }
    return states;
  }

  public static List<Integer> splitString(String input) {
    return Arrays.stream(input.split("")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
  }
}
