package functions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.ProbabilityMap;
import data.State;
import data.StateList;

public class TransitionMapGenerator {

  private static final double ERROR_CORRECTION_VALUE = 0.001;

  public static ProbabilityMap get(StateList transitionStates) {
    Map<State, Map<State, Double>> transitionMap = new HashMap<>();
    for (int i = 0; i < transitionStates.size(); i++) {
      State element = transitionStates.getState(i);
      List<Integer> elementSuffix = element.asList().subList(1, element.size());
      Map<State, Double> elementTransitionMap = new HashMap<>();
      for (int j = 0; j < transitionStates.size(); j++) {
        State transitionElement = transitionStates.getState(j);
        List<Integer> transitionElementPrefix = transitionElement.asList().subList(0, transitionElement.size() - 1);
        if (transitionElementPrefix.equals(elementSuffix)) {
          elementTransitionMap.put(transitionElement, (1.0 * (transitionElementPrefix.size() + 1)) / transitionStates.size());
        }
        else {
          elementTransitionMap.put(transitionElement, ERROR_CORRECTION_VALUE);
        }
        transitionMap.put(element, elementTransitionMap);
      }
    }
    return new ProbabilityMap(transitionMap);
  }
}
