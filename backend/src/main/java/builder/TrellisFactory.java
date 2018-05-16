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
import data.StateList;
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
    Set<TrellisNode> initialNodes = generateTrellisNodes(matrices.get(0).rows());
    //Wrap the nodes up
    Set<TrellisNode> previousNodes = initialNodes;
    Matrix matrix = matrices.get(0);
    for (int i = 1; i < matrices.size(); i++) {
      matrix = matrices.get(i % matrices.size());
      Set<TrellisNode> currentNodes = generateTrellisNodes(matrix.rows());
      mapTrellisNodes(matrix, previousNodes, currentNodes);
      previousNodes = currentNodes;
    }
    mapTrellisNodes(matrix, previousNodes, initialNodes);
    return new Trellis(initialNodes, getStateLists(matrices));
  }

  private static Set<TrellisNode> generateTrellisNodes(int numberOfRows) {
    return StatesGenerator.generateStates(numberOfRows).getStates().stream()
        .map(TrellisNode::new)
        .collect(Collectors.toSet());
  }

  private static void mapTrellisNodes(Matrix matrix, Set<TrellisNode> sourceNodes, Set<TrellisNode> targetNodes) {
    final Convolution convolution = new Convolution(matrix);
    for (TrellisNode sourceNode : sourceNodes) {
      addTrellisEdges(sourceNode, targetNodes, convolution);
    }
  }

  private static void addTrellisEdges(TrellisNode sourceNode, Set<TrellisNode> targetNodes, Convolution convolution) {
    Map<State, TrellisEdge> mappings = new HashMap<>();

    List<List<Integer>> allPermutationStates = PermutationGenerator.generateAllBinaryPermutations(1);
    for (List<Integer> permutationPrefix : allPermutationStates) {
      //This line is wrong. Might want to reduce per dif, but why in the first place. need to think heavily
      List<Integer> codeword = Stream.concat(permutationPrefix.stream(), sourceNode.getState().asList().stream()).collect(Collectors.toList());
      codeword = codeword.subList(0, convolution.getNumberOfColumns());
      for (TrellisNode targetNode : targetNodes) {
        List<Integer> nodeState = targetNode.getState().asList();
        //if(nodeState.subList(0, codeword.size()).equals(codeword)) {
        if (codeword.size() < nodeState.size() || codeword.subList(0, targetNode.getState().size()).equals(targetNode.getState().asList())) {
          TrellisEdge edge = new TrellisEdge(convolution.convolve(codeword), sourceNode, targetNode);
          mappings.put(new State(permutationPrefix), edge);
        }
      }
    }
    sourceNode.setEdges(mappings);
  }

  private static List<StateList> getStateLists(List<Matrix> matrices) {
    return matrices.stream().map(Matrix::rows).map(StatesGenerator::generateStates).collect(Collectors.toList());
  }
}
