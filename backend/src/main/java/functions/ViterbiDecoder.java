package functions;

import static utils.VectorUtils.computeHammingDistance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.ProbabilityMap;
import data.State;
import data.StateList;
import data.trellis.Trellis;
import data.trellis.TrellisEdge;
import data.trellis.TrellisNode;

public class ViterbiDecoder {

  private static final Logger log = LoggerFactory.getLogger("Viterbi");

  private final Trellis trellis;

  public ViterbiDecoder(Trellis trellis) {
    this.trellis = trellis;
  }

  public List<Integer> decode(StateList transitionStates, StateList encodingStates, List<State> encoded, double errorRate) {
    //Initialize distance and state maps
    Double[][] distanceMap = new Double[encodingStates.size()][encoded.size() + 1];
    distanceMap[0][0] = 0.0;
    IntStream.range(1, encodingStates.size()).forEach(i -> distanceMap[i][0] = Double.MAX_VALUE);
    State[][] stateMap = new State[encodingStates.size()][encoded.size()];
    Map<State, Integer> stateIndexMap = generateIndexMap(encodingStates);
    //Use a queue
    Queue<QueueNode> nodeQueue = new LinkedList<>();
    nodeQueue.add(new QueueNode(trellis.getNode(encodingStates.getState(0)), 0, 0.0));
    nodeQueue.add(new QueueNode(trellis.getNode(encodingStates.getState(1)), 0, Double.MAX_VALUE));
    nodeQueue.add(new QueueNode(trellis.getNode(encodingStates.getState(2)), 0, Double.MAX_VALUE));
    nodeQueue.add(new QueueNode(trellis.getNode(encodingStates.getState(3)), 0, Double.MAX_VALUE));
    while (!nodeQueue.isEmpty()) {
      QueueNode currentNode = nodeQueue.poll();
      if (currentNode.depth < encoded.size()) {
        for (Map.Entry<State, TrellisEdge> edgeEntry : currentNode.trellisNode.getEdges().entrySet()) {
          TrellisEdge edge = edgeEntry.getValue();
          double distance = computeDistance(edge.getParityBits(), encoded.get(currentNode.depth).asList(), errorRate);
          int stateIndex = stateIndexMap.get(edge.getTargetNode().getState());
          Double expectedDistance = distanceMap[stateIndex][currentNode.depth + 1];
          Double newDistance = distanceMap[stateIndexMap.get(currentNode.trellisNode.getState())][currentNode.depth] + distance;
          if (expectedDistance == null || newDistance < expectedDistance) {

            distanceMap[stateIndex][currentNode.depth + 1] = newDistance;
            stateMap[stateIndex][currentNode.depth] = currentNode.trellisNode.getState();
            nodeQueue.add(new QueueNode(edge.getTargetNode(), currentNode.depth + 1, distance));
          }
        }
      }
    }
    //decode
    //find start index
    int minIndex = -1;
    Double minWeight = Double.MAX_VALUE;
    for (int i = 0; i < distanceMap.length; i++) {
      if (distanceMap[i][distanceMap[0].length - 1] < minWeight) {
        minIndex = i;
        minWeight = distanceMap[i][distanceMap[0].length - 1];
      }
    }

    Stack<Integer> stack = new Stack<>();

    //backtrack
    int previousStateIndex;
    int currentStateIndex = minIndex;
    for (int i = 0; i < stateMap[0].length; i++) {
      previousStateIndex = currentStateIndex;
      currentStateIndex = stateIndexMap.get(stateMap[previousStateIndex][stateMap[0].length - 1 - i]);
      System.out.println("i is: " + i);
      System.out.println("Previous state is: " + encodingStates.getState(previousStateIndex));
      System.out.println("Current state is: " + encodingStates.getState(currentStateIndex));
      if (currentStateIndex >= previousStateIndex) {
        stack.push(0);
        System.out.println("added: 0");
      }
      else {
        stack.push(1);
        System.out.println("added: 1");
      }
    }
    List<Integer> response = new ArrayList<>();
    while (!stack.isEmpty()) {
      response.add(stack.pop());
    }
    return response;
  }

  private double computeDistance(List<Integer> state, List<Integer> expected, double errorRate) {
    long hammingDistance = computeHammingDistance(state, expected);
    return hammingDistance * (1 - errorRate) + errorRate * (state.size() - hammingDistance);
  }

  private Map<State, Integer> generateIndexMap(StateList stateList) {
    Map<State, Integer> stateMap = new HashMap<>();
    for (int i = 0; i < stateList.size(); i++) {
      stateMap.put(stateList.getState(i), i);
    }
    return stateMap;
  }

  private void updateStatePointer(State state, StateList encodingStates, StatePointer[][] statePointers, int i, int j, double errorRate) {
    int h = 0;
    for (State encodingState : encodingStates.getStates()) {
      double distance = computeDistance(state.asList(), encodingState.asList(), errorRate);
      double oldState = statePointers[h][i - 1].distance;
      if (statePointers[j][i] == null || statePointers[j][i].distance > distance + oldState) {
        statePointers[j][i] = new StatePointer(distance + oldState, h);
      }
      h++;
    }
  }

  private class StatePointer {
    int index;
    double distance;

    public StatePointer(double distance, int index) {
      this.distance = distance;
      this.index = index;
    }

    public void update(double distance, int index) {
      if (this.distance > distance) {
        this.distance = distance;
        this.index = index;
      }
    }
  }

  private class QueueNode {

    final TrellisNode trellisNode;
    final int depth;
    final Double weight;

    QueueNode(TrellisNode trellisNode, int depth, Double weight) {

      this.trellisNode = trellisNode;
      this.depth = depth;
      this.weight = weight;
    }
  }

  /*public List<Integer> decode(StateList transitionStates, StateList encodingStates, List<State> encoded, double errorRate) {
    log.trace("Starting decoding with vector {} and error rate {}", encoded, errorRate);

    ProbabilityMap transitionProbabilities = TransitionMapGenerator.getNew(transitionStates);
    ProbabilityMap encodingProbabilities = TemporaryEncodingMapGenerator.get(transitionStates, encodingStates, trellis, errorRate);

    log.trace("Transition probabilities: {}", transitionProbabilities);
    log.trace("Encoding probabilities: {}", encodingProbabilities);

    double[][] mostLikelyPath = initializeProbabilityPathMatrix(transitionStates, encoded.size());
    Integer[][] mostLikelyResult = new Integer[transitionStates.size()][encoded.size()];
    Arrays.stream(mostLikelyResult[0]).forEach(i -> i = 0);

    List<Map.Entry<State, Map<State, Double>>> transitionEntryList = new ArrayList<>(transitionProbabilities.get().entrySet());
    List<Map.Entry<State, Map<State, Double>>> encodingEntryList = new ArrayList<>(encodingProbabilities.get().entrySet());
    log.trace("Transition entry list is: {}", transitionEntryList);

    for (int i = 1; i < encoded.size(); i++) {
      State encodedElement = encoded.get(i);
      computeMaxAndArgMax(transitionProbabilities, encodingProbabilities, transitionStates, encodingEntryList, encodedElement, mostLikelyPath, mostLikelyResult, i);
    }
    return reconstructAnswer(mostLikelyPath, mostLikelyResult, transitionStates, encoded.size());

  }*/

  private double[][] initializeProbabilityPathMatrix(StateList transitionStates, int encodedLength) {
    ProbabilityMap transitionProbabilities = TransitionMapGenerator.getNew(transitionStates);
    double[][] mostLikelyPath = new double[transitionProbabilities.size()][encodedLength];
    Arrays.stream(mostLikelyPath[0]).forEach(i -> i = 1.0);
    IntStream.range(0, mostLikelyPath.length).forEach(i -> mostLikelyPath[i][0] = 1.0);
    return mostLikelyPath;
  }

  private void computeMaxAndArgMax(ProbabilityMap transitionProbabilities,
                                   ProbabilityMap encodingProbabilities,
                                   StateList transitionStates,
                                   List<Map.Entry<State, Map<State, Double>>> encodingEntryList,
                                   State encodedElement,
                                   double[][] mostLikelyPath,
                                   Integer[][] mostLikelyResult,
                                   int i) {
    for (int j = 0; j < transitionProbabilities.size(); j++) {
      //Compute max
      double curMax = -1;
      int argMax = -1;
      for (int k = 0; k < transitionProbabilities.size(); k++) {
        double transitionProbability = transitionProbabilities.getProbabilityEntry(transitionStates.getState(k)).get(transitionStates.getState(j));
        double encodingProbability = encodingProbabilities.getProbabilityEntry(transitionStates.getState(j)).get(encodedElement);
        double value = mostLikelyPath[k][i - 1] * transitionProbability * encodingProbability;
        if (curMax < value) {
          curMax = value;
          argMax = k;
        }
      }
      mostLikelyPath[j][i] = curMax;
      mostLikelyResult[j][i] = argMax;
    }
  }

  private List<Integer> reconstructAnswer(double[][] mostLikelyPath, Integer[][] mostLikelyResult, StateList transitionStates, int encodingLength) {
    int lastElementIndex = -1;
    double lastElementProbability = -1;
    for (int i = 0; i < mostLikelyPath.length; i++) {
      double probability = mostLikelyPath[i][mostLikelyPath.length - 1];
      if (lastElementProbability < probability) {
        lastElementProbability = probability;
        lastElementIndex = mostLikelyResult[i][mostLikelyPath.length - 1];
      }
    }
    Integer[] resultPath = new Integer[encodingLength];
    resultPath[resultPath.length - 1] = lastElementIndex;
    for (int i = 2; i <= resultPath.length; i++) {
      resultPath[resultPath.length - i] = mostLikelyResult[resultPath[resultPath.length - i + 1]][resultPath.length - i + 1];
    }
    log.trace("Most likely path probabilities: {}", Arrays.deepToString(mostLikelyPath));
    log.info("Got result path of: {}", Arrays.toString(resultPath));
    List<Integer> decoded = new ArrayList<>();
    for (int i = 0; i < resultPath.length; i++) {
      decoded.addAll(transitionStates.getState(resultPath[i]).asList());
    }
    return decoded;
  }

}
