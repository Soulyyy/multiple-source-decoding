package functions;

import java.util.ArrayList;
import java.util.List;

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
    List<Integer> convolutedVector = new ArrayList<>();
    for (int i = 0; i < matrix.columns(); i++) {
      Integer element = 0;
      for (int j = 0; j < matrix.rows(); j++) {
        element += matrix.get(j, i) * input.get(j);
      }
      element = element % 2;
      convolutedVector.add(element);
    }
    return convolutedVector;
  }
}
