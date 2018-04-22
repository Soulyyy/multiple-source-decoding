package builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import data.Matrix;
import data.newtrellis.Trellis;
import data.newtrellis.TrellisFactory;

public class TmpTrellisFactoryTest {

  static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {"57.mat", "(101) -> (00) ==> {(010) -> (01), (011) -> (10)}\n" +
            "(010) -> (01) ==> {(101) -> (00), (100) -> (11)}\n" +
            "(000) -> (00) ==> {(000) -> (00), (001) -> (11)}\n" +
            "(011) -> (10) ==> {(110) -> (10), (111) -> (01)}\n" +
            "(110) -> (10) ==> {(101) -> (00), (100) -> (11)}\n" +
            "(001) -> (11) ==> {(010) -> (01), (011) -> (10)}\n" +
            "(100) -> (11) ==> {(000) -> (00), (001) -> (11)}\n" +
            "(111) -> (01) ==> {(110) -> (10), (111) -> (01)}"},
    });
  }

  @DisplayName("Trellis creation tests")
  @ParameterizedTest(name = "Read matrix from file \"{0}}\" to generate trellis, expecting \"{1}\"")
  @MethodSource(value = "data")
  public void testTrellisFactory(String matrixFileName, String expectedTrellis) {
    Matrix matrix = MatrixFactory.build(matrixFileName);
    Trellis trellis = TrellisFactory.build(matrix, 0.0);
    assertEquals(expectedTrellis, trellis.toString());
  }
}
