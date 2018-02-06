package functions;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import builder.MatrixFactory;
import builder.TrellisFactory;
import data.Matrix;
import data.trellis.Trellis;

public class TrellisIteratorTest {

  static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {"57", Arrays.asList(1,0,0,1,1,0,1), Arrays.asList(1,0,0,1,1,0,1)},
    });
  }

  @DisplayName("Trellis iterator tests")
  @ParameterizedTest(name = "Read matrix from file \" {0}\", encode \" {1}\", expect \" {1}\"")
  @MethodSource(value = "data")
  public void testTrellisIterator(String matrixFileName, List<Integer> input, List<Integer> expectedOuptut) {
    Matrix matrix = MatrixFactory.build(matrixFileName);
    Trellis trellis = TrellisFactory.build(matrix);
    TrellisIterator trellisIterator = new TrellisIterator(trellis);
    //List<Integer> encodedValue = trellisIterator.iterate(input);
  }
}
