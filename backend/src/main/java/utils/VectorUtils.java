package utils;

import java.util.List;
import java.util.stream.IntStream;

public class VectorUtils {

  public static long computeHammingDistance(List<Integer> first, List<Integer> second) {
    if (first == null || second == null || first.size() != second.size()) {
      throw new IllegalArgumentException("Can't compute hamming distance of " + first + " and " + second);
    }
    return IntStream.range(0, first.size()).filter(i -> first.get(i).equals(second.get(i))).count();
  }
}
