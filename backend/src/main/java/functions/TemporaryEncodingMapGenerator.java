/*
package functions;

import static utils.VectorUtils.computeHammingDistance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import data.ProbabilityMap;
import data.State;
import data.StateList;
import data.trellis.Trellis;

public class TemporaryEncodingMapGenerator {

  public static ProbabilityMap get(StateList initialStates, StateList observationStates, Trellis trellis, double errorRate) {
    Map<State, Map<State, Double>> encodingProbabilities = new HashMap<>();

    for (int i = 0; i < initialStates.size(); i++) {
      Map<State, Double> elementEncodingProbabilities = new HashMap<>();
      for (int j = 0; j < observationStates.size(); j++) {
        Set<State> encodedValues = getStatesWithSuffix(trellis, observationStates.getState(j).asList());
        double encodingProbability = 0.0;
        for(State encodedValue : encodedValues) {
          long hammingDistance = computeHammingDistance(encodedValue.asList(), observationStates.getState(i).asList());
          encodingProbability += computeEncodingProbability(initialStates.getState(i).size(), hammingDistance, errorRate);
        }
        elementEncodingProbabilities.put(observationStates.getState(j), encodingProbability / encodedValues.size());
      }
      encodingProbabilities.put(initialStates.getState(i), elementEncodingProbabilities);
    }

    return new ProbabilityMap(encodingProbabilities);
  }

  private static Set<State> getStatesWithSuffix(Trellis trellis, List<Integer> suffix) {
    return trellis.getNodes().entrySet().stream()
        .filter(e -> e.getKey().asList().size() >= suffix.size())
        .filter(e -> !e.getKey()
            .asList()
            .subList(e.getKey().size() - suffix.size(), e.getKey().size())
            .equals(suffix))
        .map(Map.Entry::getValue)
        .map(TrellisNode::getValue)
        .collect(Collectors.toSet());
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
*/
