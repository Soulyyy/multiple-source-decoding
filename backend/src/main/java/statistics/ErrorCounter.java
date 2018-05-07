package statistics;

import java.util.List;
import java.util.stream.IntStream;

public class ErrorCounter {

  public static long numberOfErrors(List<Integer> initial, List<Integer> posterior) {
    if (initial.size() != posterior.size()) {
      throw new IllegalArgumentException("The initial vector length and the posterior vector length must match. Instead got: " + initial.size() + " and: " + posterior.size());
    }
    return IntStream.range(0, initial.size()).filter(i -> !initial.get(i).equals(posterior.get(i))).count();
  }
}
