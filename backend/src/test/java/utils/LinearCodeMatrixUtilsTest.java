package utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import builder.MatrixFactory;
import data.MatrixImpl;

class LinearCodeMatrixUtilsTest {

  @Test
  public void testGetParityCheckMatrix() {
    MatrixImpl input = MatrixFactory.build("743hamming.mat").get(0);
    MatrixImpl expected = MatrixFactory.build("374hamming.mat").get(0);

    MatrixImpl result = LinearCodeMatrixUtils.getParityCheckMatrix(input);
    Assertions.assertEquals(expected, result);
  }

  @Test
  public void testGetGeneratorMatrix() {
    MatrixImpl input = MatrixFactory.build("374hamming.mat").get(0);
    MatrixImpl expected = MatrixFactory.build("743hamming.mat").get(0);

    MatrixImpl result = LinearCodeMatrixUtils.getGeneratorMatrix(input);
    Assertions.assertEquals(expected, result);
  }

  @Test
  public void testGeneratorParityProduct() {
    MatrixImpl generatorMatrix = MatrixFactory.build("743hamming.mat").get(0);
    MatrixImpl parityCheckMatrix = MatrixFactory.build("374hamming.mat").get(0);
  }
}
