package builder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import data.Matrix;

public class MultiMatrixFactoryTest {

  static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {"76.mat", Collections.singletonList("[1,1,1\n 1,1,0]")},
        {"76-11.mat", Arrays.asList("[1,1,1\n 1,1,0]", "[1\n 1]")},
    });
  }

  @DisplayName("Multiple Matrices Creation Test")
  @ParameterizedTest(name = "Read file \"{0}}\" as a matrices, expecting content {1}")
  @MethodSource(value = "data")
  public void testMultiMatrixFactory(String path, List<String> expected) {
    List<Matrix> matrices = MultiMatrixFactory.build(path);
    Assertions.assertEquals(expected.size(), matrices.size());
    for (int i = 0; i < matrices.size(); i++) {
      Assertions.assertEquals(expected.get(i), matrices.get(i).toString());
    }
  }
}
