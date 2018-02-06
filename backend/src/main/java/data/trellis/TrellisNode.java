package data.trellis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TrellisNode {

  private List<Integer> key;

  private List<Integer> value;

  private Map<List<Integer>, TrellisNode> edges;

  public TrellisNode(List<Integer> key, List<Integer> value) {
    this.key = key;
    this.value = value;
    this.edges = new HashMap<>();
  }

  public void addEdge(TrellisNode node) {
    edges.put(node.getKey(), node);
  }

  public Map<List<Integer>, TrellisNode> getEdges() {
    return edges;
  }

  public TrellisNode getEdge(List<Integer> key)  {
    return edges.get(key);
  }

  public List<Integer> getKey() {
    return key;
  }

  public List<Integer> getValue() {
    return value;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("(");
    key.forEach(sb::append);
    sb.append(") -> (");
    value.forEach(sb::append);
    sb.append(")");
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof List) {
      return key.equals(o);
    }
    else if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TrellisNode node = (TrellisNode) o;
    return Objects.equals(key, node.key);
  }

  @Override
  public int hashCode() {

    return Objects.hash(key);
  }
}
