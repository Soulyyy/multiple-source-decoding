package functions;

import static utils.VectorUtils.computeHammingDistance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.ProbabilityMap;
import data.State;
import data.StateList;
import data.trellis.Trellis;

public class ViterbiDecoder {

  private static final Logger log = LoggerFactory.getLogger("Viterbi");

  private final Trellis trellis;

  public ViterbiDecoder(Trellis trellis) {
    this.trellis = trellis;
  }

  public List<Integer> decode(StateList transitionStates, StateList encodingStates, List<State> encoded, double errorRate) {
    /*
    Pmst, errorid on saadud info kohta, mitte encoded, see between 2 same type maatriks
     */
    Integer[][] mostLikelyPath = new Integer[encodingStates.size()][encoded.size()];
    Integer[][] mostLikelyResult = new Integer[encodingStates.size()][encoded.size()];
    Arrays.stream(mostLikelyResult[0]).forEach(i -> i = 0);
    StatePointer[][] statePointers = new StatePointer[encodingStates.size()][encoded.size()];
    statePointers[0][0] = new StatePointer(0, -1);
    IntStream.range(1, encodingStates.size()).forEach(i -> statePointers[i][0] = new StatePointer(Integer.MAX_VALUE, -1));

    for (int i = 1; i < encoded.size(); i++) {
      State state = encoded.get(i);
      for (int j = 0; j < encodingStates.size(); j++) {
        updateStatePointer(state, encodingStates, statePointers, i, j, errorRate);
        //System.out.println(computeDistance(state.asList(), node.getValue().getValue().asList(), errorRate) + " " + state.toString() + " " + node.getValue().getValue().toString());
      }
    }
    StatePointer currentPointer = null;
    for (int i = 0; i < statePointers.length; i++) {
      if (currentPointer == null || statePointers[i][statePointers.length - 1].distance < currentPointer.distance) {
        currentPointer = statePointers[i][statePointers.length - 1];
      }
    }
    Stack<State> stack = new Stack<>();
    for (int i = 1; i < statePointers[0].length; i++) {
      stack.push(encodingStates.getState(currentPointer.index));
      currentPointer = statePointers[currentPointer.index][statePointers[0].length - i -1];
    }
    return new ArrayList<>((stack).stream().map(State::asList).collect(ArrayList::new, List::addAll, List::addAll));
  }

  private double computeDistance(List<Integer> state, List<Integer> expected, double errorRate) {
    long hammingDistance = computeHammingDistance(state, expected);
    return hammingDistance * (1 - errorRate) + errorRate * (state.size() - hammingDistance);
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
