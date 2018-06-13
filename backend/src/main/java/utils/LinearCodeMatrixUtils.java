package utils;

import data.MatrixImpl;

public class LinearCodeMatrixUtils {


  public static MatrixImpl getParityCheckMatrix(MatrixImpl generatorMatrix) {
    int n = generatorMatrix.columns();
    int k = generatorMatrix.rows();
    int rows = n - k;
    Integer[][] subMatrix = MatrixUtils.transpose(new MatrixImpl(getSubMatrix(generatorMatrix.asArray(), k, n))).asArray();
    Integer[][] integerParityCheckMatrix = combineMatrix(subMatrix, rows, n, 0);
    return new MatrixImpl(integerParityCheckMatrix);
  }

  public static MatrixImpl getGeneratorMatrix(MatrixImpl parityCheckMatrix) {
    int n = parityCheckMatrix.columns();
    int k = parityCheckMatrix.rows();
    int blockSize = n - k;
    Integer[][] subMatrix = MatrixUtils.transpose(new MatrixImpl(getSubMatrix(parityCheckMatrix.asArray(), 0, blockSize))).asArray();
    Integer[][] integerGeneratorMatrix = combineMatrix(subMatrix, blockSize, n, blockSize);
    return new MatrixImpl(integerGeneratorMatrix);
  }

  private static Integer[][] getSubMatrix(Integer[][] matrix, int startColumn, int endColumn) {
    Integer[][] subMatrix = new Integer[matrix.length][endColumn - startColumn];
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < subMatrix[0].length; j++) {
        subMatrix[i][j] = matrix[i][j + startColumn];
      }
    }
    return subMatrix;
  }

  private static Integer[][] combineMatrix(Integer[][] subMatrix, int rows, int columns, int subMatrixStart) {
    Integer[][] matrix = new Integer[rows][columns];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < subMatrix[0].length; j++) {
        matrix[i][subMatrixStart + j] = subMatrix[i][j];
      }
    }
    return addIdentityMatrix(matrix, (subMatrixStart + subMatrix[0].length) % matrix[0].length, rows);
  }

  private static Integer[][] addIdentityMatrix(Integer[][] matrix, int startColumn, int rows) {
    for (int i = 0; i < matrix.length; i++) {
      for (int j = startColumn; j < startColumn + rows; j++) {
        int val = 0;
        if (i == j - startColumn) {
          val = 1;
        }
        matrix[i][j] = val;
      }
    }
    return matrix;
  }

}
