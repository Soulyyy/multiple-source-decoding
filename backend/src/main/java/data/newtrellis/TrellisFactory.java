package data.newtrellis;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import data.Matrix;
import data.State;
import functions.Convolution;
import utils.PermutationGenerator;

public class TrellisFactory {

  public static Trellis build(Matrix matrix, Double errorRate) {
    return build(Collections.singletonList(matrix), errorRate);
  }

  public static Trellis build(List<Matrix> matrices, Double errorRate) {
    final Convolution convolution = new Convolution(matrices.get(0));
    Set<TrellisNode> initialNodes = generateTrellisNodes(matrices.get(0));
    return new Trellis(initialNodes);
  }

  private static Set<TrellisNode> generateTrellisNodes(Matrix matrix) {
    final Convolution convolution = new Convolution(matrix);
    return generateLayer(matrix, convolution);
  }

  private static Set<TrellisNode> generateLayer(Matrix matrix, Convolution convolution) {
    return PermutationGenerator.generateAllBinaryPermutations(matrix.columns()).stream()
        .map(State::new)
        .map(i -> new TrellisNode(convolution.convolve(i.asList())))
        .collect(Collectors.toSet());
  }
}
