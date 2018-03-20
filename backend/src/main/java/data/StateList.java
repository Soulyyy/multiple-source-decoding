package data;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class StateList {

  @NonNull
  private final List<State> states;

  public List<State> getStates() {
    return states;
  }

  public int size() {
    return states.size();
  }

  public State getState(int index) {
    if (index < 0 || index >= states.size()) {
      throw new IllegalArgumentException("Index " + index + " not between 0 and " + states.size());
    }
    return states.get(index);
  }

  public int getStateLength() {
    State state = states.get(0);
    if (state == null) {
      throw new IllegalStateException("State cannot be null");
    }
    return states.get(0).size();
  }
}
