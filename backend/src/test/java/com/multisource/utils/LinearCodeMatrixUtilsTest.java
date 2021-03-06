package com.multisource.utils;

import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.multisource.builder.MatrixFactory;
import com.multisource.data.MatrixImpl;
import com.multisource.utils.LinearCodeMatrixUtils;
import com.multisource.utils.MatrixUtils;

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
    MatrixImpl parityCheckMatrix = LinearCodeMatrixUtils.getParityCheckMatrix(generatorMatrix);
    MatrixImpl result = MatrixUtils.multiply(generatorMatrix, MatrixUtils.transpose(parityCheckMatrix));
    Integer[][] expectedIntegerMatrix = IntStream.range(0, generatorMatrix.rows())
        .mapToObj(r -> IntStream.range(0, parityCheckMatrix
            .rows()).mapToObj(i -> 0)
            .toArray(Integer[]::new))
        .toArray(Integer[][]::new);
    MatrixImpl expected = new MatrixImpl(expectedIntegerMatrix);
    Assertions.assertEquals(expected, result);
  }
}
