package data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Matrix {

  private final Integer[][] matrix;

  public Matrix(Integer[][] matrix) {
    this.matrix = matrix;
  }

  public int rows() {
    return matrix.length;
  }

  public int columns() {
    return matrix[0].length;
  }

  public List<Integer> getRow(int i) {
    return Arrays.asList(matrix[i]);
  }

  public List<Integer> getColumn(int j) {
    return IntStream.range(0, this.rows())
        .mapToObj(i -> matrix[i][j])
        .collect(Collectors.toList());
  }

  public Integer[][] asArray() {
    return matrix;
  }

  public Integer get(int i, int j) {
    return matrix[i][j];
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    Arrays.stream(matrix).forEach(a -> {
      Arrays.stream(a).forEach(i -> sb.append(i).append(","));
      sb.setLength(sb.length() - 1);
      sb.append("\n");
      sb.append(" ");
    });
    if (sb.length() > 1) {
      sb.setLength(sb.length() - 2);
    }
    sb.append("]");
    return sb.toString();
  }
}
