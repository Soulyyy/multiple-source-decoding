package builder;

import java.util.Arrays;
import java.util.stream.Stream;

import com.sun.org.apache.bcel.internal.generic.IREM;

import data.Matrix;
import utils.FileUtils;

public class MatrixFactory {

  public static Matrix build(String path) {
    try {
      return buildInternal(path);
    }
    catch (NumberFormatException e) {
      throw new IllegalArgumentException("Failed to parse numeric value", e);
    }
  }


  private static Matrix buildInternal(String path) {
    return buildMatrix(FileUtils.getResourceAsStream(path));
  }

  static Matrix buildMatrix(Stream<String> contents) {
    Integer[][] integerMatrix = contents.map(s -> s.split(","))
        .map(a -> Arrays.stream(a)
            .mapToInt(Integer::parseInt)
            .boxed()
            .toArray(Integer[]::new))
        .toArray(Integer[][]::new);

    Matrix matrix = new Matrix(integerMatrix);
    if (!validateLength(integerMatrix)) {
      throw new IllegalArgumentException("Matrix size not uniform:\n" + matrix.toString(), null);
    }
    return matrix;
  }

  private static boolean validateLength(Integer[][] matrix) {
    return Arrays.stream(matrix).mapToInt(i -> i.length).distinct().count() == 1;
  }
}
