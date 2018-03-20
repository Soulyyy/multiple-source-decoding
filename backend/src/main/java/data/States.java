package data;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class States {

  @NonNull
  private final Set<List<Integer>> states;

  public Set<List<Integer>> getStates() {
    return states;
  }
}
