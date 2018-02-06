package builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import data.Matrix;
import data.trellis.Trellis;

public class TrellisFactoryTest {

  static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {"57", "(10) -> (101) ==> {(00) -> (000), (01) -> (111)}\n" +
            "(00) -> (000) ==> {(00) -> (000), (01) -> (111)}\n" +
            "(11) -> (010) ==> {(10) -> (101), (11) -> (010)}\n" +
            "(01) -> (111) ==> {(10) -> (101), (11) -> (010)}"},
    });
  }

  @DisplayName("Trellis creation tests")
  @ParameterizedTest(name = "Read matrix from file \"{0}}\" to generate trellis, expecting \"{1}\"")
  @MethodSource(value = "data")
  public void testTrellisFactory(String matrixFileName, String expectedTrellis) {
    Matrix matrix = MatrixFactory.build(matrixFileName);
    Trellis trellis = TrellisFactory.build(matrix);
    assertEquals(expectedTrellis, trellis.toString());
  }
}
