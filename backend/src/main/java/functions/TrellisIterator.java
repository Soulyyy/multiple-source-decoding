package functions;

import java.util.List;
import java.util.Map;

import data.trellis.Trellis;
import data.trellis.TrellisNode;

public class TrellisIterator {

  Trellis trellis;

  public TrellisIterator(Trellis trellis) {
    this.trellis = trellis;
  }

  public List<Integer> iterate(List<Integer> integers) {
    int length = getTrellisNodeKeyLength();
    TrellisNode curNode = trellis.getNode(integers.subList(0, length));
    List<Integer> encoded = curNode.getValue();
    for (int i = 0; i < integers.size(); i++) {
      length = getTrellisNodeKeyLength(curNode);
      if (i + length > integers.size()) {
        break;
      }
      curNode = curNode.getEdge(integers.subList(i, i+length));
      encoded.addAll(curNode.getValue());
    }
    return encoded;
  }

  private int getTrellisNodeKeyLength() {
    return trellis.getNodes().entrySet().stream()
        .map(Map.Entry::getKey)
        .mapToInt(List::size)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Didn't find key length"));
  }

  private int getTrellisNodeKeyLength(TrellisNode node) {
    return node.getEdges().entrySet().stream()
        .map(Map.Entry::getValue)
        .map(TrellisNode::getKey)
        .mapToInt(List::size)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Didn't find key length"));
  }
}
