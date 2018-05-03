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
    Double[][] distanceMap = new Double[encodingStates.size()][encoded.size() + 1];
    distanceMap[0][0] = 0.0;
    IntStream.range(1, encodingStates.size()).forEach(i -> distanceMap[i][0] = Double.MAX_VALUE);
    State[][] stateMap = new State[encodingStates.size()][encoded.size()];
    Map<State, Integer> stateIndexMap = generateIndexMap(encodingStates);
    Queue<QueueNode> nodeQueue = initializeQueue(encodingStates);
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
    return backtrack(distanceMap, stateMap, stateIndexMap);
  }

  private Queue<QueueNode> initializeQueue(StateList encodingStates) {
    Queue<QueueNode> nodeQueue = new LinkedList<>();
    nodeQueue.add(new QueueNode(trellis.getNode(encodingStates.getState(0)), 0, 0.0));
    nodeQueue.add(new QueueNode(trellis.getNode(encodingStates.getState(1)), 0, Double.MAX_VALUE));
    nodeQueue.add(new QueueNode(trellis.getNode(encodingStates.getState(2)), 0, Double.MAX_VALUE));
    nodeQueue.add(new QueueNode(trellis.getNode(encodingStates.getState(3)), 0, Double.MAX_VALUE));
    return nodeQueue;
  }

  private Map<State, Integer> generateIndexMap(StateList stateList) {
    Map<State, Integer> stateMap = new HashMap<>();
    for (int i = 0; i < stateList.size(); i++) {
      stateMap.put(stateList.getState(i), i);
    }
    return stateMap;
  }

  private double computeDistance(List<Integer> state, List<Integer> expected, double errorRate) {
    long hammingDistance = computeHammingDistance(state, expected);
    return hammingDistance * (1 - errorRate) + errorRate * (state.size() - hammingDistance);
  }

  private List<Integer> backtrack(Double[][] distanceMap, State[][] stateMap, Map<State, Integer> stateIndexMap) {
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
      if (currentStateIndex >= previousStateIndex) {
        stack.push(0);
      }
      else {
        stack.push(1);
      }
    }
    List<Integer> response = new ArrayList<>();
    while (!stack.isEmpty()) {
      response.add(stack.pop());
    }
    return response;
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

}
