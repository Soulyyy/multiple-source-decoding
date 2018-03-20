package functions;

import static utils.VectorUtils.computeHammingDistance;

import java.util.HashMap;
import java.util.Map;

import data.ProbabilityMap;
import data.State;
import data.StateList;
import data.trellis.Trellis;

public class EncodingMapGenerator {

  public static ProbabilityMap get(StateList transitionStates, StateList encodingStates, Trellis trellis, double errorRate) {
    Map<State, Map<State, Double>> encodingProbabilities = new HashMap<>();

    for (int i = 0; i < transitionStates.size(); i++) {
      Map<State, Double> elementEncodingProbabilities = new HashMap<>();
      encodingStates.getStates().forEach(l -> elementEncodingProbabilities.put(l, 0.0));
      for (int j = 0; j < transitionStates.size(); j++) {
        long hammingDistance = computeHammingDistance(encodingStates.getState(i).asList(), encodingStates.getState(j).asList());
        State encodedValue = trellis.getNode(transitionStates.getState(j)).getValue();
        double encodingProbability = computeEncodingProbability(transitionStates.getState(j).size(), hammingDistance, errorRate);
        elementEncodingProbabilities.put(encodedValue, encodingProbability);
      }
      encodingProbabilities.put(transitionStates.getState(i), elementEncodingProbabilities);
    }
    return new ProbabilityMap(encodingProbabilities);
  }

  private static double computeEncodingProbability(long length, long hammingDistance, double errorRate) {
    if (length == hammingDistance) {
      return Math.pow(errorRate, hammingDistance);
    }
    else if (hammingDistance == 0) {
      return Math.pow(1 - errorRate, length);
    }
    return 1 - Math.pow(1 - errorRate, length - hammingDistance);
  }
}
