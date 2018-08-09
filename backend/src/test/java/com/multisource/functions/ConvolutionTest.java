package com.multisource.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.multisource.utils.VectorUtils.splitString;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.multisource.builder.MatrixFactory;
import com.multisource.data.MatrixImpl;
import com.multisource.functions.Convolution;

class ConvolutionTest {

  static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {"57.mat", "000", "00"},
        {"57.mat", "010", "01"},
        {"57.mat", "100", "11"},
        {"57.mat", "011", "10"}
    });
  }

  @DisplayName("Binary Convolution Test")
  @ParameterizedTest(name = "Input:\"{1}\", expected output:\"{2}\"")
  @MethodSource(value = "data")
  public void convolve(String matrixPath, String input, String expectedOutput) {
    List<MatrixImpl> matrices = MatrixFactory.build(matrixPath);
    List<Integer> inputList = splitString(input);
    List<Integer> expectedList = splitString(expectedOutput);
    Convolution convolution = new Convolution(matrices.get(0));
    List<Integer> actualOutput = convolution.convolve(inputList);
    assertEquals(expectedList, actualOutput);
  }
}