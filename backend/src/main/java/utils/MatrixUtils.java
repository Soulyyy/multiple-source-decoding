package utils;

import data.MatrixImpl;

public class MatrixUtils {

  public static MatrixImpl transpose(MatrixImpl matrix) {
    Integer[][] transposedIntegerMatrix = new Integer[matrix.columns()][matrix.rows()];
    for (int i = 0; i < transposedIntegerMatrix.length; i++) {
      for (int j = 0; j < transposedIntegerMatrix[0].length; j++) {
        transposedIntegerMatrix[i][j] = matrix.get(j, i);
      }
    }
    return new MatrixImpl(transposedIntegerMatrix);
  }

  public static MatrixImpl multiply(MatrixImpl first, MatrixImpl second) {
    Integer[][] multiplicationMatrix = new Integer[first.rows()][second.columns()];
    for (int i = 0; i < first.rows(); i++) {
      for (int j = 0; j < second.columns(); j++) {
        int element = 0;
        for (int h = 0; h < first.columns(); h++) {
          element += first.get(i, h) * second.get(h, j);
        }
        multiplicationMatrix[i][j] = (element  % 2);
      }
    }
    return new MatrixImpl(multiplicationMatrix);
  }
}
