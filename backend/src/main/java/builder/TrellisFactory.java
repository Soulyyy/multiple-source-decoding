package builder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import data.MatrixImpl;
import data.State;
import data.StateList;
import data.trellis.Trellis;
import data.trellis.TrellisEdge;
import data.trellis.TrellisNode;
import functions.Convolution;
import functions.StatesGenerator;
import utils.PermutationGenerator;

public class TrellisFactory {

  public static Trellis build(MatrixImpl matrix) {
    return build(Collections.singletonList(matrix));
  }

  public static Trellis build(List<MatrixImpl> matrices) {
    Set<TrellisNode> initialNodes = generateTrellisNodes(matrices.get(0).rows());
    //Wrap the nodes up
    Set<TrellisNode> previousNodes = initialNodes;
    MatrixImpl matrix = matrices.get(0);
    MatrixImpl nextMatrix = matrices.get(0);
    for (int i = 1; i < matrices.size(); i++) {
      nextMatrix = matrices.get(i % matrices.size());
      Set<TrellisNode> currentNodes = generateTrellisNodes(nextMatrix.rows());
      mapTrellisNodes(matrix, nextMatrix, previousNodes, currentNodes);
      matrix = matrices.get(i % matrices.size());
      previousNodes = currentNodes;
    }
    mapTrellisNodes(matrix, nextMatrix, previousNodes, initialNodes);
    return new Trellis(initialNodes, getStateLists(matrices));
  }

  private static Set<TrellisNode> generateTrellisNodes(int numberOfRows) {
    return StatesGenerator.generateStates(numberOfRows).getStates().stream()
        .map(TrellisNode::new)
        .collect(Collectors.toSet());
  }

  private static void mapTrellisNodes(MatrixImpl matrix, MatrixImpl secondMatrix, Set<TrellisNode> sourceNodes, Set<TrellisNode> targetNodes) {
    final Convolution convolution = new Convolution(matrix);
    for (TrellisNode sourceNode : sourceNodes) {
      addTrellisEdges(sourceNode, targetNodes, convolution, secondMatrix);
    }
  }

  private static void addTrellisEdges(TrellisNode sourceNode, Set<TrellisNode> targetNodes, Convolution convolution, MatrixImpl secondMatrix) {
    Map<State, TrellisEdge> mappings = new HashMap<>();

    List<List<Integer>> allPermutationStates = PermutationGenerator.generateExpandedBinaryValues(Math.max(1, convolution.getNumberOfColumns() - secondMatrix.columns()));
    for (List<Integer> permutationPrefix : allPermutationStates) {
      List<Integer> codeword = Stream.concat(permutationPrefix.stream(), sourceNode.getState().asList().stream()).collect(Collectors.toList());
      List<Integer> convolutionCodeword = codeword.subList(permutationPrefix.size() - 1, convolution.getNumberOfColumns() + permutationPrefix.size() - 1);
      for (TrellisNode targetNode : targetNodes) {
        List<Integer> nodeState = targetNode.getState().asList();
        if (codeword.size() < nodeState.size() || codeword.subList(0, targetNode.getState().size()).equals(targetNode.getState().asList())) {
          TrellisEdge edge = new TrellisEdge(convolution.convolve(convolutionCodeword), sourceNode, targetNode);
          mappings.put(new State(convolutionCodeword.subList(0, 1)), edge);
        }
      }
    }
    sourceNode.setEdges(mappings);
  }

  private static List<StateList> getStateLists(List<MatrixImpl> matrices) {
    return matrices.stream().map(MatrixImpl::rows).map(StatesGenerator::generateStates).collect(Collectors.toList());
  }
}
