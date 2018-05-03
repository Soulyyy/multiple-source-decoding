package data;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class ProbabilityMap {

  @NonNull
  private final Map<State, Map<State, Double>> probabilityMap;

  public Map<State, Map<State, Double>> get() {
    return probabilityMap;
  }

  public int size() {
    return probabilityMap.size();
  }

  @Override
  public String toString() {
    return probabilityMap.toString();
  }
}
