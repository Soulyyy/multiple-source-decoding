package data;

import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class State {

  @NonNull
  List<Integer> state;

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    state.forEach(sb::append);
    return sb.toString();
  }

  public int size() {
    return state.size();
  }

  public List<Integer> asList() {
    return state;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    State state1 = (State) o;
    return Objects.equals(state, state1.state);
  }

  @Override
  public int hashCode() {

    return Objects.hash(state);
  }
}
