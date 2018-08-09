package com.multisource.functions;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.multisource.data.MatrixImpl;


public class Convolution {

  private MatrixImpl matrix;

  public Convolution(MatrixImpl matrix) {
    this.matrix = matrix;
  }

  public List<Integer> convolve(List<Integer> input) {
    if (input.size() != matrix.columns()) {
      throw new IllegalArgumentException("Input length of " + input.size() + " does not match matrix column count " + matrix.columns());
    }
    return IntStream.range(0, matrix.rows())
        .mapToObj
            (i -> IntStream.range(0, matrix.columns())
                .map(j -> matrix.columns() - j - 1)
                .mapToObj(j -> matrix.get(i, j) * input.get(j))
                .reduce((a, b) -> a + b)
                .orElse(0)
            )
        .map(i -> i % 2)
        .collect(Collectors.toList());
  }

  public int getNumberOfColumns() {
    return matrix.columns();
  }

  public int getNumberOfRows() {
    return matrix.rows();
  }
}
