package functions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import data.Matrix;


public class Convolution {

  private Matrix matrix;

  public Convolution(Matrix matrix) {
    this.matrix = matrix;
  }

  public List<Integer> convolve(List<Integer> input) {
    if (input.size() != matrix.rows()) {
      throw new IllegalArgumentException("Input length of " + input.size() + " does not match matrix row count " + matrix.rows());
    }
    return IntStream.range(0, matrix.columns())
        .mapToObj(i -> IntStream.range(0, matrix.rows())
            .mapToObj(j -> matrix.get(j, i) * input.get(j))
            .reduce((a, b) -> a + b)
            .orElse(0)
        )
        .map(i -> i % 2)
        .collect(Collectors.toList());
  }
}
