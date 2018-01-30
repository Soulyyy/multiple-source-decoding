package utils;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamUtils {

  public static <T> Stream<List<T>> sliding(List<T> list, int windowSize) {
    if (windowSize > list.size()) {
      return Stream.empty();
    }
    return IntStream.range(0, list.size() - windowSize + 1)
        .mapToObj(i -> list.subList(i, i + windowSize));
  }
}
