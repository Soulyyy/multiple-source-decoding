package data.newtrellis;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TrellisNode {

  private final List<Integer> nodeBits;

  private Map<List<Integer>, TrellisEdge> edges;

  public TrellisNode(List<Integer> nodeBits) {
    this.nodeBits = nodeBits;
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
    return Objects.equals(nodeBits, that.nodeBits);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeBits);
  }
}
