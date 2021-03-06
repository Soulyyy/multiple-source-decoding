package com.multisource.functions;

import static com.multisource.utils.VectorUtils.computeHammingDistance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.multisource.data.State;
import com.multisource.data.StateList;
import com.multisource.data.trellis.Trellis;
import com.multisource.data.trellis.TrellisEdge;
import com.multisource.data.trellis.TrellisNode;
import com.multisource.utils.VectorUtils;

public class ViterbiDecoder {

  private static final Logger log = LoggerFactory.getLogger("Viterbi");

  private final Trellis trellis;

  public ViterbiDecoder(Trellis trellis) {
    this.trellis = trellis;
  }

  public List<Integer> decode(List<Integer> integerList, double errorRate) {
    List<StateList> states = trellis.getStates();
    List<State> encoded = getStatesFromIntegers(integerList, states);
    log.info("Viterbi decoding using states: {} for vector: {}", states, encoded);

    StateList encodingStates = states.get(0);
    int maxEncodingStatesSize = states.stream().mapToInt(StateList::size).max().getAsInt();
    Double[][] distanceMap = initializeDistanceMap(maxEncodingStatesSize, encoded.size());
    State[][] stateMap = new State[maxEncodingStatesSize][encoded.size()];
    Queue<QueueNode> nodeQueue = initializeQueue(encodingStates);

    while (!nodeQueue.isEmpty()) {
      QueueNode currentNode = nodeQueue.poll();
      if (currentNode.depth < encoded.size()) {
        for (Map.Entry<State, TrellisEdge> edgeEntry : currentNode.trellisNode.getEdges().entrySet()) {
          TrellisEdge edge = edgeEntry.getValue();
          double distance = computeDistance(edge.getParityBits(), encoded.get(currentNode.depth).asList(), errorRate);
          encodingStates = states.get(currentNode.depth % states.size());
          StateList nextStates = states.get((currentNode.depth + 1) % states.size());
          int stateIndex = nextStates.getIndex(edge.getTargetNode().getState());
          Double expectedDistance = distanceMap[stateIndex][currentNode.depth + 1];
          Double newDistance = distanceMap[encodingStates.getIndex(currentNode.trellisNode.getState())][currentNode.depth] + distance;
          if (expectedDistance == null || newDistance < expectedDistance) {
            distanceMap[stateIndex][currentNode.depth + 1] = newDistance;
            stateMap[stateIndex][currentNode.depth] = currentNode.trellisNode.getState();
            nodeQueue.add(new QueueNode(edge.getTargetNode(), currentNode.depth + 1, distance));
          }
        }
      }
    }
    return backtrack(distanceMap, stateMap, states);
  }

  private List<State> getStatesFromIntegers(List<Integer> encoded, List<StateList> states) {
    List<Integer> stateLengths = states.stream()
        .map(s -> s.getStates().stream().findFirst().get().size())
        .collect(Collectors.toList());
    return VectorUtils.createVariableLengthStateList(encoded, stateLengths);
  }

  private Double[][] initializeDistanceMap(int stateSize, int vectorSize) {
    Double[][] distanceMap = new Double[stateSize][vectorSize + 1];
    distanceMap[0][0] = 0.0;
    IntStream.range(1, stateSize).forEach(i -> distanceMap[i][0] = Double.MAX_VALUE);
    return distanceMap;
  }

  private Queue<QueueNode> initializeQueue(StateList encodingStates) {
    Queue<QueueNode> nodeQueue = new LinkedList<>();
    nodeQueue.add(new QueueNode(trellis.getNode(encodingStates.getState(0)), 0, 0.0));
    IntStream.range(1, encodingStates.size())
        .mapToObj(i -> trellis.getNode(encodingStates.getState(i)))
        .map(n -> new QueueNode(n, 0, Double.MAX_VALUE))
        .forEach(nodeQueue::add);
    return nodeQueue;
  }

  private double computeDistance(List<Integer> state, List<Integer> expected, double errorRate) {
    long hammingDistance = computeHammingDistance(state, expected);
    return hammingDistance * (1 - errorRate) + errorRate * (state.size() - hammingDistance);
  }

  private List<Integer> backtrack(Double[][] distanceMap, State[][] stateMap, List<StateList> states) {
    fillDistanceMap(distanceMap);
    int minIndex = getStartingIndex(distanceMap);
    List<Integer> response = new ArrayList<>();
    int previousStateIndex;
    int currentStateIndex = minIndex;
    StateList curState = states.get(0);
    for (int i = 0; i < stateMap[0].length; i++) {
      StateList previousState = curState;
      previousStateIndex = currentStateIndex;
      curState = states.get((i + 1) % states.size());
      currentStateIndex = curState.getStateIndexMap().get(stateMap[previousStateIndex][stateMap[0].length - 1 - i]);
      int decodedValue;
      if (currentStateIndex == previousStateIndex) {
        decodedValue = previousState.size() / 2 > currentStateIndex ? 0 : 1;
      }
      else {
        decodedValue = currentStateIndex < previousStateIndex ? 1 : 0;
      }
      response.add(decodedValue);
    }
    Collections.reverse(response);
    return response;
  }

  private void fillDistanceMap(Double[][] distanceMap) {
    for (int i = 0; i < distanceMap.length; i++) {
      for (int j = 0; j < distanceMap[0].length; j++) {
        if (distanceMap[i][j] == null) {
          distanceMap[i][j] = Double.MAX_VALUE;
        }
      }
    }
  }

  private int getStartingIndex(Double[][] distanceMap) {
    return IntStream.range(0, distanceMap.length)
        .mapToObj(i -> new ImmutablePair<>(i, distanceMap[i][distanceMap[0].length - 1]))
        .min(Comparator.comparing(Pair::getRight))
        .map(p -> p.left)
        .orElseThrow(() -> new IllegalStateException("Distances must have a minimal value"));
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
