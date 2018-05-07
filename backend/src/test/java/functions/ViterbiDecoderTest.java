package functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.VectorUtils.createStateList;

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
import data.State;
import data.trellis.Trellis;

public class ViterbiDecoderTest {

  static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {"76.mat", createStateList(Arrays.asList(1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0), 2), Arrays.asList(1, 0, 1, 1, 0, 0)},
        {"76.mat", createStateList(Arrays.asList(1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1), 2), Arrays.asList(1, 0, 1, 1, 0, 0)},
        {"76.mat", createStateList(Arrays.asList(1, 1), 2), Collections.singletonList(1)},
        {"76.mat", createStateList(Collections.singletonList(1), 2), Collections.emptyList()},
        {"76.mat", createStateList(Collections.emptyList(), 2), Collections.emptyList()},
    });
  }

  @DisplayName("Trellis iterator tests")
  @ParameterizedTest(name = "Read matrix from file \" {0}\", encode \" {1}\", expect \" {2}\"")
  @MethodSource(value = "data")
  public void testEncoder(String matrixFileName, List<State> input, List<Integer> expectedOutput) {
    Matrix matrix = MatrixFactory.build(matrixFileName);
    Trellis trellis = TrellisFactory.build(matrix);
    ViterbiDecoder decoder = new ViterbiDecoder(trellis);
    List<Integer> decoded = decoder.decode(Collections.singletonList(StatesGenerator.generateStates(2)), input, 0.0);
    assertEquals(expectedOutput, decoded);
  }
}
