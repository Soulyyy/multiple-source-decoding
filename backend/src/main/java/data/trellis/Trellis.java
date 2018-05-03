package data.trellis;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import data.State;

public class Trellis {

  private final Map<State, TrellisNode> trellisNodes = new HashMap<>();

  public Trellis(Set<TrellisNode> trellisNodeSet) {
    for (TrellisNode node : trellisNodeSet) {
      trellisNodes.put(node.getState(), node);
    }
  }

  public TrellisNode getNode(List<Integer> stateBits) {
    return getNode(new State(stateBits));
  }

  public TrellisNode getNode(State state) {
    return trellisNodes.get(state);
  }

  public Collection<TrellisNode> getAllNodes() {
    return trellisNodes.values();
  }
}
