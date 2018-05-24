package functions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import builder.MatrixFactory;
import builder.TrellisFactory;
import data.Matrix;
import data.trellis.Trellis;

public class ViterbiDecoderTest {

  static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {"76.mat", Arrays.asList(1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0), Arrays.asList(1, 0, 1, 1, 0, 0)},
        {"76.mat", Arrays.asList(1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1), Arrays.asList(1, 0, 1, 1, 0, 0)},
        {"76-777.mat", Arrays.asList(1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0), Arrays.asList(1, 0, 1, 0, 0, 0)},
        //{"777-76.mat", Arrays.asList(1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1), Arrays.asList(1, 0, 1, 1, 0, 0)},
        {"76-11.mat", Arrays.asList(1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0), Arrays.asList(1, 0, 1, 1, 0, 0)},
        {"76.mat", Arrays.asList(1, 1), Collections.singletonList(1)},
        {"76.mat", Collections.singletonList(1), Collections.emptyList()},
        {"76.mat", Collections.emptyList(), Collections.emptyList()},
    });
  }

  @DisplayName("Trellis iterator tests")
  @ParameterizedTest(name = "Read matrix from file \" {0}\", encode \" {1}\", expect \" {2}\"")
  @MethodSource(value = "data")
  public void testEncoder(String matrixFileName, List<Integer> input, List<Integer> expectedOutput) {
    List<Matrix> matrices = MatrixFactory.build(matrixFileName);
    Trellis trellis = TrellisFactory.build(matrices);
    ViterbiDecoder decoder = new ViterbiDecoder(trellis);
    List<Integer> decoded = decoder.decode(input, 0.0);
    assertEquals(expectedOutput, decoded);
  }
}
