package com.multisource.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.multisource.data.State;

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

  public static List<State> createVariableLengthStateList(List<Integer> encodedInput, List<Integer> lengths) {
    List<State> states = new ArrayList<>();
    int lengthsIndex = 0;
    int vectorStartIndex = 0;
    int vectorEndIndex = lengths.get(0);
    while (vectorEndIndex <= encodedInput.size()) {
      List<Integer> stateVector = encodedInput.subList(vectorStartIndex, vectorEndIndex);
      states.add(new State(stateVector));
      lengthsIndex = (lengthsIndex + 1) % lengths.size();
      int curLength = lengths.get(lengthsIndex);
      vectorStartIndex = vectorEndIndex;
      vectorEndIndex += curLength;
    }
    return states;
  }

  public static List<Integer> splitString(String input) {
    return Arrays.stream(input.split("")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
  }

  public static List<Integer> getBinaryVector(int number) {
    List<Integer> binaryVector = getReverseBinaryVector(number);
    Collections.reverse(binaryVector);
    return binaryVector;
  }

  public static List<Integer> getReverseBinaryVector(int number) {
    if (number == 0) {
      return Collections.singletonList(0);
    }
    List<Integer> binaryVector = new ArrayList<>();
    while (number > 0) {
      binaryVector.add(number % 2);
      number = number / 2;
    }
    return binaryVector;
  }
}
