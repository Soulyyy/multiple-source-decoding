package builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import data.Matrix;

public class MatrixFactoryTest {

  static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {"57.mat", "[1,0,1\n 1,1,1]"},
        {"non-aligned-matrix", "Matrix size not uniform:\n[1,2,3\n 4,5\n 6,7,8,9]"},
        {"non-integer-matrix", "Failed to parse numeric value"},
        {"non-matrix", "Failed to parse numeric value"},
        {"file that, by some chance, does not exist", "Failed to load file from resources with the name: \"file that, by some chance, does not exist\""},
    });
  }

  @DisplayName("Matrix creation tests")
  @ParameterizedTest(name = "Read file \"{0}}\" as a matix, expecting content {1}")
  @MethodSource(value = "data")
  public void testMatrixFactory(String path, String expected) {
    try {
      Matrix matrix = MatrixFactory.build(path);
      assertEquals(expected, matrix.toString());
    }
    catch (IllegalArgumentException e) {
      assertEquals(expected, e.getMessage());
    }
  }
}
