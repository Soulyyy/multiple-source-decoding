package functions;

import static utils.VectorUtils.computeHammingDistance;

import java.util.HashMap;
import java.util.Map;

import data.ProbabilityMap;
import data.State;
import data.StateList;
import data.trellis.Trellis;

public class EncodingMapGenerator {

  private static final double ERROR_CORRECTION_VALUE = 0.001;

  public static ProbabilityMap get(StateList transitionStates, StateList encodingStates, Trellis trellis, double errorRate) {
    Map<State, Map<State, Double>> encodingProbabilities = new HashMap<>();

    for (int i = 0; i < encodingStates.size(); i++) {
      Map<State, Double> elementEncodingProbabilities = new HashMap<>();
      transitionStates.getStates().forEach(l -> elementEncodingProbabilities.put(l, ERROR_CORRECTION_VALUE));
      for (int j = 0; j < encodingProbabilities.size(); j++) {
        long hammingDistance = computeHammingDistance(encodingStates.getState(i).asList(), encodingStates.getState(j).asList());
        State encodedValue = trellis.getNode(encodingStates.getState(i)).getValue();
        double encodingProbability = computeEncodingProbability(transitionStates.getState(j).size(), hammingDistance, errorRate);
        elementEncodingProbabilities.put(encodedValue, encodingProbability);
      }
      encodingProbabilities.put(encodingStates.getState(i), elementEncodingProbabilities);
    }
    return new ProbabilityMap(encodingProbabilities);
  }

  private static double computeEncodingProbability(long length, long hammingDistance, double errorRate) {
    if (length == hammingDistance) {
      return Math.pow(errorRate, hammingDistance) + ERROR_CORRECTION_VALUE;
    }
    else if (hammingDistance == 0) {
      return Math.pow(1 - errorRate, length) +ERROR_CORRECTION_VALUE;
    }
    return 1 - Math.pow(1 - errorRate, length - hammingDistance)+ ERROR_CORRECTION_VALUE;
  }
}
