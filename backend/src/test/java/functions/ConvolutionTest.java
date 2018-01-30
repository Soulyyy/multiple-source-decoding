package functions;

import java.util.Arrays;
import java.util.Collection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import builder.MatrixFactory;
import data.Matrix;

class ConvolutionTest {

  Matrix matrix = MatrixFactory.build("57");

  static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {},
        {new Integer[][]{{1, 0, 1}, {1, 1, 1}}},
        {new Integer[][]{{1, 1}, {2}}},
        {new String[][]{{"Tere", "Sina"}, {"Mina", "sõna"}}},
        {null},
    });
  }

  @DisplayName("Binary Convolution Test")
  @ParameterizedTest(name = "Input:\"{0}\", expected output:\"{1}\"")
  @MethodSource(value = "data")
  void convolute() {
  }
}