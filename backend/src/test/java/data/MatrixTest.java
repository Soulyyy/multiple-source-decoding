package data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class MatrixTest {

  static Collection<Object[]> rowData() {
    return Arrays.asList(new Object[][]{
        {new Integer[][]{{1, 0, 1}, {1, 1, 1}}, 2, new Integer[][]{{1, 0, 1}, {1, 1, 1}}},
    });
  }

  static Collection<Object[]> columnData() {
    return Arrays.asList(new Object[][]{
        {new Integer[][]{{1, 0, 1}, {1, 1, 1}}, 3, new Integer[][]{{1, 1}, {0, 1}, {1, 1}}},
    });
  }

  @DisplayName("Matrix row test")
  @ParameterizedTest()
  @MethodSource(value = "rowData")
  public void testGetRow(Integer[][] integerMatrix, int rowCount, Integer[][] rows) {
    MatrixImpl matrix = new MatrixImpl(integerMatrix);
    assertEquals(rowCount, matrix.rows());
    IntStream.range(0, matrix.rows()).forEach(i -> assertEquals(Arrays.asList(rows[i]), matrix.getRow(i)));


  }

  @DisplayName("Matrix column Test")
  @ParameterizedTest()
  @MethodSource(value = "columnData")
  public void testGetColumn(Integer[][] integerMatrix, int columnCount, Integer[][] columns) {
    MatrixImpl matrix = new MatrixImpl(integerMatrix);
    assertEquals(columnCount, matrix.columns());
    IntStream.range(0, matrix.columns()).forEach(i -> assertEquals(Arrays.asList(columns[i]), matrix.getColumn(i)));
  }

}
