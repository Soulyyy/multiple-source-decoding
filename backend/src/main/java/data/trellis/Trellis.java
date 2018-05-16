package data.trellis;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import data.State;
import data.StateList;

public class Trellis {

  private final Map<State, TrellisNode> trellisNodes = new HashMap<>();
  private final List<StateList> states;

  public Trellis(Set<TrellisNode> trellisNodeSet, List<StateList> states) {
    for (TrellisNode node : trellisNodeSet) {
      trellisNodes.put(node.getState(), node);
    }
    this.states = states;
  }

  public TrellisNode getNode(List<Integer> stateBits) {
    return getNode(new State(stateBits));
  }

  public TrellisNode getNode(State state) {
    return trellisNodes.get(state);
  }

  public List<StateList> getStates() {
    return states;
  }

  public Collection<TrellisNode> getAllNodes() {
    return trellisNodes.values();
  }
}
