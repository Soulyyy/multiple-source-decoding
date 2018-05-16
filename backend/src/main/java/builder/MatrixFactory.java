package builder;

import static utils.VectorUtils.getReverseBinaryVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import data.Matrix;
import utils.FileUtils;

public class MatrixFactory {

  public static List<Matrix> build(String path) {
    try {
      List<List<String>> matrixInputs = splitMatricesToLines(path);
      return matrixInputs.stream()
          .map(Collection::stream)
          .map(MatrixFactory::buildMatrix)
          .collect(Collectors.toList());
    }
    catch (NumberFormatException e) {
      throw new IllegalArgumentException("Failed to parse numeric value", e);
    }

  }

  public static List<Matrix> build(List<List<Integer>> numberList) {
    return numberList.stream().map(MatrixFactory::buildMatrix).collect(Collectors.toList());
  }

  private static List<List<String>> splitMatricesToLines(String path) {
    List<String> lines = FileUtils.getResourceAsStream(path).collect(Collectors.toList());
    List<List<String>> matrices = new ArrayList<>();
    List<String> current = new ArrayList<>();
    for (String string : lines) {
      if ("-".equals(string)) {
        matrices.add(current);
        current = new ArrayList<>();
      }
      else {
        current.add(string);
      }
    }
    matrices.add(current);
    return matrices;
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

  private static Matrix buildMatrix(List<Integer> numbers) {
    List<List<Integer>> reverseRows = new ArrayList<>();
    int maxLength = -1;
    for (int number : numbers) {
      List<Integer> reverseVector = getReverseBinaryVector(number);
      if (reverseVector.size() > maxLength) {
        maxLength = reverseVector.size();
      }
      reverseRows.add(reverseVector);
    }
    Integer[][] integerMatrix = new Integer[reverseRows.size()][maxLength];
    for (int i = 0; i < integerMatrix.length; i++) {
      for (int j = 0; j < integerMatrix[0].length; j++) {
        integerMatrix[i][integerMatrix[0].length - j - 1] = reverseRows.get(i).get(j);
      }
    }
    return new Matrix(integerMatrix);
  }

  private static boolean validateLength(Integer[][] matrix) {
    return Arrays.stream(matrix).mapToInt(i -> i.length).distinct().count() == 1;
  }
}
