package data.trellis;

import java.util.List;
import java.util.Map;

public class Trellis {

  private Map<List<Integer>, TrellisNode> nodes;

  public Trellis(Map<List<Integer>, TrellisNode> nodes) {
    this.nodes = nodes;
  }

  public TrellisNode getNode(List<Integer> key) {
    return nodes.get(key);
  }

  public Map<List<Integer>, TrellisNode> getNodes() {
    return nodes;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    nodes.forEach((key, value) -> sb.append(value.toString()).append(" ==> {")
        .append(value.getEdges().entrySet()
            .stream()
            .map(Map.Entry::getValue)
            .map(TrellisNode::toString)
            .reduce((a, b) -> a + ", " + b)
            .get())
        .append("}")
        .append("\n"));
    return sb.toString().trim();
  }
}
