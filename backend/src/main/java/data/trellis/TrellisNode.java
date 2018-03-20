package data.trellis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import data.State;
import lombok.NonNull;

public class TrellisNode {

  @NonNull
  private State key;

  @NonNull
  private State value;

  private Map<State, TrellisNode> edges;

  public TrellisNode(State key, State value) {
    this.key = key;
    this.value = value;
    this.edges = new HashMap<>();
  }

  public void addEdge(TrellisNode node) {
    edges.put(node.getKey(), node);
  }

  public Map<State, TrellisNode> getEdges() {
    return edges;
  }

  public TrellisNode getEdge(State key) {
    return edges.get(key);
  }

  public State getKey() {
    return key;
  }

  public State getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "(" + key +
        ") -> (" +
        value +
        ")";
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
