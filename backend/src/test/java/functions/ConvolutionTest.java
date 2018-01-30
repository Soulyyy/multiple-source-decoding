package functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.splitString;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import builder.MatrixFactory;
import data.Matrix;

class ConvolutionTest {

  static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {"11", "010", "57"}
    });
  }

  @DisplayName("Binary Convolution Test")
  @ParameterizedTest(name = "Input:\"{0}\", expected output:\"{1}\"")
  @MethodSource(value = "data")
  public void convolve(String input, String expectedOutput, String matrixPath) {
    Matrix matrix = MatrixFactory.build(matrixPath);
    List<Integer> inputList = splitString(input);
    List<Integer> expectedList = splitString(expectedOutput);
    Convolution convolution = new Convolution(matrix);
    List<Integer> actualOutput = convolution.convolve(inputList);
    assertEquals(expectedList, actualOutput);
  }
}