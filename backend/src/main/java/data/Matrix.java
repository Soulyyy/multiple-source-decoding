package data;

import java.util.Arrays;

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
