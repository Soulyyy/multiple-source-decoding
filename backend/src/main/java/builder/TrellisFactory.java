package builder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import data.Matrix;
import data.State;
import data.trellis.Trellis;
import data.trellis.TrellisEdge;
import data.trellis.TrellisNode;
import functions.Convolution;
import functions.StatesGenerator;
import utils.PermutationGenerator;

public class TrellisFactory {

  public static Trellis build(Matrix matrix) {
    return build(Collections.singletonList(matrix));
  }

  public static Trellis build(List<Matrix> matrices) {
    Matrix matrix = matrices.get(0);
    Set<TrellisNode> initialNodes = generateTrellisNodes(matrix.rows());
    //Wrap the nodes up
    Set<TrellisNode> lastNodes = initialNodes;
    mapTrellisNodes(matrix, initialNodes, lastNodes);
    return new Trellis(initialNodes);
  }

  private static Set<TrellisNode> generateTrellisNodes(int numberOfColumns) {
    return StatesGenerator.generateStates(numberOfColumns).getStates().stream()
        .map(TrellisNode::new)
        .collect(Collectors.toSet());
  }

  private static void mapTrellisNodes(Matrix matrix, Set<TrellisNode> sourceNodes, Set<TrellisNode> targetNodes) {
    final Convolution convolution = new Convolution(matrix);
    for (TrellisNode sourceNode : sourceNodes) {
      addTrellisEdges(sourceNode, targetNodes, convolution, matrix.columns() - matrix.rows());
    }
  }

  private static void addTrellisEdges(TrellisNode sourceNode, Set<TrellisNode> targetNodes, Convolution convolution, int difference) {
    Map<State, TrellisEdge> mappings = new HashMap<>();

    List<List<Integer>> allPermutationStates = PermutationGenerator.generateAllBinaryPermutations(difference);
    for (List<Integer> permutationPrefix : allPermutationStates) {
      List<Integer> codeword = Stream.concat(permutationPrefix.stream(), sourceNode.getState().asList().stream()).collect(Collectors.toList());
      for (TrellisNode targetNode : targetNodes) {
        if (codeword != null && codeword.subList(0, targetNode.getState().size()).equals(targetNode.getState().asList())) {
          TrellisEdge edge = new TrellisEdge(convolution.convolve(codeword), targetNode);
          mappings.put(new State(permutationPrefix), edge);
        }
      }
    }
    sourceNode.setEdges(mappings);
  }
}
