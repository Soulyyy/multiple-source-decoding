package utils;

import data.MatrixImpl;

public class LinearCodeMatrixUtils {


  public static MatrixImpl getParityCheckMatrix(MatrixImpl generatorMatrix) {
    int n = generatorMatrix.columns();
    int k =  generatorMatrix.rows();
    return generatorMatrix;
  }

  public static MatrixImpl getGeneratorMatrix(MatrixImpl parityCheckMatrix) {
    int n = parityCheckMatrix.columns();
    int k =  parityCheckMatrix.rows();
    return parityCheckMatrix;
  }



}
