package com.multisource.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.multisource.builder.MatrixFactory;
import com.multisource.builder.TrellisFactory;
import com.multisource.data.MatrixImpl;
import com.multisource.data.trellis.Trellis;
import com.multisource.functions.ConvolutionalTrellisEncoder;

public class ConvolutionalTrellisEncoderTest {

  static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {"76.mat", Arrays.asList(1, 0, 1, 1, 0, 0), Arrays.asList(1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0)},
        {"76-11.mat", Arrays.asList(1, 0, 1, 1, 0, 0), Arrays.asList(1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0)},
        {"76-777.mat", Arrays.asList(1, 0, 1, 0, 0, 0), Arrays.asList(1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0)},
        {"777-76.mat", Arrays.asList(1, 0, 1, 0, 1, 0), Arrays.asList(1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1)}
    });
  }

  @DisplayName("Trellis iterator tests")
  @ParameterizedTest(name = "Read matrix from file \" {0}\", encode \" {1}\", expect \" {2}\"")
  @MethodSource(value = "data")
  public void testEncoder(String matrixFileName, List<Integer> input, List<Integer> expectedOutput) {
    List<MatrixImpl> matrix = MatrixFactory.build(matrixFileName);
    Trellis trellis = TrellisFactory.build(matrix);
    ConvolutionalTrellisEncoder encoder = new ConvolutionalTrellisEncoder(trellis);
    List<Integer> encoded = encoder.encode(input);
    assertEquals(expectedOutput, encoded);
  }
}
