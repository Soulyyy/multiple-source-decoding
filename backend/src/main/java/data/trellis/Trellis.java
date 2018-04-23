package data.trellis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Trellis {

  private final Map<List<Integer>, TrellisNode> trellisNodes = new HashMap<>();

  public Trellis(Set<TrellisNode> trellisNodeSet) {
    for (TrellisNode node : trellisNodeSet) {
      trellisNodes.put(node.getNodeBits(), node);
    }
  }

  public TrellisNode getNode(List<Integer> key) {
    return trellisNodes.get(key);
  }
}
