package builder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import data.Matrix;
import data.State;
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
    Map<State, TrellisNode> nodeMap = new HashMap<>();
    for (TrellisNode node : initialNodes) {
      nodeMap.put(node.getKey(), node);
    }
    Set<TrellisNode> sourceNodes = initialNodes;
    for (int i = 1; i < matrices.size(); i++) {
      int index = i % matrices.size();
      Matrix matrix = matrices.get(index);
      Set<TrellisNode> targetNodes = generateTrellisNodes(matrix);
      mapLayer(sourceNodes, targetNodes);
      sourceNodes = targetNodes;
    }
    mapLayer(sourceNodes, initialNodes);
    return new Trellis(nodeMap);
  }

  private static Set<TrellisNode> generateTrellisNodes(Matrix matrix) {
    final Convolution convolution = new Convolution(matrix);
    return generateLayer(matrix, convolution);
  }

  private static Set<TrellisNode> generateLayer(Matrix matrix, Convolution convolution) {
    return PermutationGenerator.generateAllBinaryPermutations(matrix.rows()).stream()
        .map(State::new)
        .map(i -> new TrellisNode(i, new State(convolution.convolve(i.asList()))))
        .collect(Collectors.toSet());
  }

  private static void mapLayer(Set<TrellisNode> sourceLayer, Set<TrellisNode> targetLayer) {
    sourceLayer.forEach(s -> targetLayer.stream()
        .filter(t -> isElementMatch(s, t))
        .forEach(s::addEdge));
  }

  private static boolean isElementMatch(TrellisNode sourceNode, TrellisNode targetNode) {
    State sourceKey = sourceNode.getKey();
    State targetKey = targetNode.getKey();
    //If source is smaller than target max, match everything
    if (targetKey.size() - 1 > sourceKey.size()) {
      List<Integer> compareKey = targetKey.asList().subList(targetKey.size() - 1 - sourceKey.size(), targetKey.size() - 1);
      return sourceKey.equals(compareKey);
    }
    else if (targetKey.size() == 1) {
      return true;
    }
    else {
      List<Integer> targetCompareKey = targetKey.asList().subList(0, targetKey.size() - 1);
      List<Integer> sourceCompareKey = sourceKey.asList().subList(sourceKey.size() - targetCompareKey.size(), sourceKey.size());
      return sourceCompareKey.equals(targetCompareKey);
    }
  }
}
