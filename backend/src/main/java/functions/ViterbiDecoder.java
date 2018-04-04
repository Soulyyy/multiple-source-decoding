package functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    log.trace("Starting decoding with vector {} and error rate {}", encoded, errorRate);

    ProbabilityMap transitionProbabilities = TransitionMapGenerator.getNew(transitionStates, errorRate);
    ProbabilityMap encodingProbabilities = TemporaryEncodingMapGenerator.get(transitionStates, encodingStates, trellis, errorRate);

    log.trace("Transition probabilities: {}", transitionProbabilities);
    log.trace("Encoding probabilities: {}", encodingProbabilities);

    double[][] mostLikelyPath = initializeProbabilityPathMatrix(encodingStates, encoded.size(), errorRate);
    Integer[][] mostLikelyResult = new Integer[encodingProbabilities.size()][encoded.size()];
    Arrays.stream(mostLikelyResult[0]).forEach(i -> i = 0);

    List<Map.Entry<State, Map<State, Double>>> transitionEntryList = new ArrayList<>(transitionProbabilities.get().entrySet());
    List<Map.Entry<State, Map<State, Double>>> encodingEntryList = new ArrayList<>(encodingProbabilities.get().entrySet());
    log.trace("Transition entry list is: {}", transitionEntryList);

    for (int i = 1; i < encoded.size(); i++) {
      State encodedElement = encoded.get(i);
      computeMaxAndArgMax(transitionProbabilities, encodingProbabilities, transitionStates, encodingEntryList, encodedElement, mostLikelyPath, mostLikelyResult, i);
    }
    return reconstructAnswer(mostLikelyPath, mostLikelyResult, transitionStates, encoded.size());

  }

  private double[][] initializeProbabilityPathMatrix(StateList transitionStates, int encodedLength, double errorRate) {
    ProbabilityMap transitionProbabilities = TransitionMapGenerator.getNew(transitionStates, errorRate);
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
