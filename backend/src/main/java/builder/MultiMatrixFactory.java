package builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import data.Matrix;
import utils.FileUtils;

public class MultiMatrixFactory {

  public static List<Matrix> build(String path) {
    List<List<String>> matrixInputs = splitMatricesToLines(path);
    return matrixInputs.stream()
        .map(Collection::stream)
        .map(MatrixFactory::buildMatrix)
        .collect(Collectors.toList());

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
}
