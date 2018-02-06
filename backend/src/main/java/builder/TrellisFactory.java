package builder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import data.Matrix;
import data.trellis.Trellis;
import data.trellis.TrellisNode;
import functions.Convolution;
import utils.PermutationGenerator;

public class TrellisFactory {

  public static Trellis build(Matrix matrix) {
    return build(Collections.singletonList(matrix));
  }

  public static Trellis build(List<Matrix> matrices) {
    Set<TrellisNode> initialNodes = generateTrellisNodes(matrices.get(0));
    Map<List<Integer>, TrellisNode> nodeMap = new HashMap<>();
    for (TrellisNode node : initialNodes) {
      nodeMap.put(node.getKey(), node);
    }
    Set<TrellisNode> sourceNodes = initialNodes;
    for (int i = 1; i < matrices.size() + 1; i++) {
      int index = i % matrices.size();
      Matrix matrix = matrices.get(index);
      Set<TrellisNode> targetNodes = generateTrellisNodes(matrix);
      mapLayer(sourceNodes, targetNodes);
      sourceNodes = targetNodes;
    }
    return new Trellis(nodeMap);
  }

  private static Set<TrellisNode> generateTrellisNodes(Matrix matrix) {
    final Convolution convolution = new Convolution(matrix);
    return generateLayer(matrix, convolution);
  }

  private static Set<TrellisNode> generateLayer(Matrix matrix, Convolution convolution) {
    return PermutationGenerator.generateAllBinaryPermutations(matrix.rows()).stream()
        .map(i -> new TrellisNode(i, convolution.convolve(i)))
        .collect(Collectors.toSet());
  }

  private static void mapLayer(Set<TrellisNode> sourceLayer, Set<TrellisNode> targetLayer) {
    sourceLayer.forEach(s -> targetLayer.stream()
        .filter(t -> isElementMatch(s, t))
        .forEach(s::addEdge));
  }

  private static boolean isElementMatch(TrellisNode sourceNode, TrellisNode targetNode) {
    List<Integer> sourceKey = sourceNode.getKey();
    List<Integer> targetKey = targetNode.getKey();
    //If source is smaller than target max, match everything
    if (targetKey.size() - 1 > sourceKey.size()) {
      List<Integer> compareKey = targetKey.subList(targetKey.size() - 1 - sourceKey.size(), targetKey.size() - 1);
      return sourceKey.equals(compareKey);
    }
    else if (targetKey.size() == 1) {
      return true;
    }
    else {
      List<Integer> targetCompareKey = targetKey.subList(0, targetKey.size() - 1);
      List<Integer> sourceCompareKey = sourceKey.subList(sourceKey.size() - targetCompareKey.size(), sourceKey.size());
      return sourceCompareKey.equals(targetCompareKey);
    }
  }
}
