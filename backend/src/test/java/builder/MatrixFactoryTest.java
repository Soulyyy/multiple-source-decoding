package builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import data.MatrixImpl;

public class MatrixFactoryTest {

  static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {"76.mat", Collections.singletonList("[1,1,1\n 1,1,0]")},
        {"76-11.mat", Arrays.asList("[1,1,1\n 1,1,0]", "[1\n 1]")},
        {"57.mat", Collections.singletonList("[1,0,1\n 1,1,1]")},
        {"non-aligned-matrix", Collections.singletonList("Matrix size not uniform:\n[1,2,3\n 4,5\n 6,7,8,9]")},
        {"non-integer-matrix", Collections.singletonList("Failed to parse numeric value")},
        {"non-matrix", Collections.singletonList("Failed to parse numeric value")},
        {"file that, by some chance, does not exist", Collections.singletonList("Failed to load file from resources with the name: \"file that, by some chance, does not exist\"")},
    });
  }

  static Collection<Object[]> integerData() {
    return Arrays.asList(new Object[][]{
        {Collections.singletonList(Arrays.asList(7, 6)), Collections.singletonList("[1,1,1\n 1,1,0]")},
        {Arrays.asList(Arrays.asList(7,6), Arrays.asList(1, 1)), Arrays.asList("[1,1,1\n 1,1,0]", "[1\n 1]")},
        {Collections.singletonList(Arrays.asList(5, 7)), Collections.singletonList("[1,0,1\n 1,1,1]")},
    });
  }

  @DisplayName("Multiple Matrices Creation Test from file")
  @ParameterizedTest(name = "Read file \"{0}}\" as a matrices, expecting content {1}")
  @MethodSource(value = "data")
  public void testMultiMatrixFactoryFile(String path, List<String> expected) {
    try {
      List<MatrixImpl> matrices = MatrixFactory.build(path);
      Assertions.assertEquals(expected.size(), matrices.size());
      for (int i = 0; i < matrices.size(); i++) {
        try {
          Assertions.assertEquals(expected.get(i), matrices.get(i).toString());
        }
        catch (IllegalArgumentException e) {
          assertEquals(expected.get(i), e.getMessage());
        }
      }
    }catch (Exception  e) {
      assertEquals(expected.get(0), e.getMessage());
    }

  }

  @DisplayName("Multiple Matrices Creation Test from integer list")
  @ParameterizedTest(name = "Read list \"{0}}\" as a numbers, expecting content {1}")
  @MethodSource(value = "integerData")
  public void testMultiMatrixFactoryIntegers(List<List<Integer>> matrixNumbers, List<String> expected) {
    List<MatrixImpl> matrices = MatrixFactory.build(matrixNumbers);
    Assertions.assertEquals(expected.size(), matrices.size());
    for (int i = 0; i < matrices.size(); i++) {
      try {
        Assertions.assertEquals(expected.get(i), matrices.get(i).toString());
      }
      catch (IllegalArgumentException e) {
        assertEquals(expected.get(i), e.getMessage());
      }
    }
  }
}
