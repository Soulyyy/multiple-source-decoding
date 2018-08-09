package com.multisource.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.multisource.data.MatrixImpl;
import com.multisource.utils.MatrixUtils;

class MatrixUtilsTest {

  @Test
  public void testMatrixTranspose() {
    MatrixImpl matrix = new MatrixImpl(new Integer[][]{{1, 2}});
    MatrixImpl expected = new MatrixImpl(new Integer[][]{{1}, {2}});
    MatrixImpl transposed = MatrixUtils.transpose(matrix);

    Assertions.assertEquals(expected, transposed);
    matrix = new MatrixImpl(new Integer[][]{{1, 2}, {3, 4}});
    expected = new MatrixImpl(new Integer[][]{{1, 3}, {2, 4}});
    transposed = MatrixUtils.transpose(matrix);
    Assertions.assertEquals(expected, transposed);

    matrix = new MatrixImpl(new Integer[][]{{1, 2}, {3, 4}, {5, 6}});
    expected = new MatrixImpl(new Integer[][]{{1, 3, 5}, {2, 4, 6}});
    transposed = MatrixUtils.transpose(matrix);
    Assertions.assertEquals(expected, transposed);
  }

  @Test
  public void testMatrixMultiplication() {
    MatrixImpl first = new MatrixImpl(new Integer[][]{{0, 1}, {0, 0}});
    MatrixImpl second = new MatrixImpl(new Integer[][]{{0, 0}, {1, 0}});
    MatrixImpl expected = new MatrixImpl(new Integer[][]{{1, 0}, {0, 0}});
    MatrixImpl product = MatrixUtils.multiply(first, second);
    Assertions.assertEquals(expected, product);

    first = new MatrixImpl(new Integer[][]{{1, 2, 3}, {4, 5, 6}});
    second = new MatrixImpl(new Integer[][]{{7, 8}, {9, 10}, {11, 12}});
    expected = new MatrixImpl(new Integer[][]{{58, 64}, {139, 154}});
    product = MatrixUtils.multiply(first, second);
    Assertions.assertEquals(expected, product);
  }
}