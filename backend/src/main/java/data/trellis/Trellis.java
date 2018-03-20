package data.trellis;

import java.util.Map;

import data.State;

public class Trellis {

  private Map<State, TrellisNode> nodes;

  public Trellis(Map<State, TrellisNode> nodes) {
    this.nodes = nodes;
  }

  public TrellisNode getNode(State key) {
    return nodes.get(key);
  }

  public Map<State, TrellisNode> getNodes() {
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
