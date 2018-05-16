package data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;


public class StateList {

  public StateList(List<State> states) {
    this.states = states;
    this.stateIndexMap = generateIndexMap(states);
  }

  @NonNull
  private final List<State> states;

  @NonNull
  @Getter
  private final Map<State, Integer> stateIndexMap;

  public List<State> getStates() {
    return states;
  }

  public int size() {
    return states.size();
  }

  public State getState(Integer index) {
    if (index < 0 || index >= states.size()) {
      throw new IllegalArgumentException("Index " + index + " not between 0 and " + states.size());
    }
    return states.get(index);
  }

  public int getIndex(State state) {
    return stateIndexMap.get(state);
  }

  public int getStateLength() {
    State state = states.get(0);
    if (state == null) {
      throw new IllegalStateException("State cannot be null");
    }
    return states.get(0).size();
  }

  private Map<State, Integer> generateIndexMap(List<State> states) {
    Map<State, Integer> stateMap = new HashMap<>();
    IntStream.range(0, states.size()).forEach(i -> stateMap.put(states.get(i), i));
    return stateMap;
  }
}
