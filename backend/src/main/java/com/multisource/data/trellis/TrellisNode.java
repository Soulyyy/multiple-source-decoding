package com.multisource.data.trellis;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.multisource.data.State;
import lombok.Getter;
import lombok.Setter;

public class TrellisNode {

  @Getter
  private final State state;

  private Map<State, TrellisEdge> edges;

  @Getter
  @Setter
  private List<Integer> history;

  public TrellisNode(State state) {
    this.state = state;
  }

  public Map<State, TrellisEdge> getEdges() {
    return edges;
  }

  public TrellisEdge getEdge(List<Integer> nextStateBits) {
    return getEdge(new State(nextStateBits));
  }

  public TrellisEdge getEdge(State nextState) {
    return edges.get(nextState);
  }

  public void setEdges(Map<State, TrellisEdge> edges) {
    this.edges = edges;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TrellisNode that = (TrellisNode) o;
    return Objects.equals(state, that.state);
  }

  @Override
  public int hashCode() {
    return Objects.hash(state);
  }

  @Override
  public String toString() {
    return this.state.toString();
  }
}
