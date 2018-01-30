package functions;

import java.util.ArrayList;
import java.util.List;

import data.Matrix;


public class Convolution {

  public List<Integer> convolute(List<Integer> input, Matrix matrix) {
    if (input.size() != matrix.rows()) {
      throw new IllegalArgumentException("Input length of " + input.size() + " does not match matrix row count " + matrix.rows());
    }
    List<Integer> convolutedVector = new ArrayList<>();
    for (int i = 0; i < matrix.rows(); i++) {
      Integer element = 0;
      for (int j = 0; j < matrix.columns(); j++) {
        element += matrix.get(i, j) * input.get(i);
      }
      element = element % 2;
      convolutedVector.add(element);
    }
    return convolutedVector;
  }
}
