package functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import data.trellis.Trellis;
import data.trellis.TrellisNode;

public class ConvolutionalTrellisEncoder {

  Trellis trellis;

  public ConvolutionalTrellisEncoder(Trellis trellis) {
    this.trellis = trellis;
  }

  public List<Integer> encode(List<Integer> integers) {
    int length = getTrellisNodeKeyLength();
    integers = prependZeros(integers, length);
    TrellisNode curNode = trellis.getNode(integers.subList(0, length));
    List<Integer> encoded = new ArrayList<>();
    for (int i = 1; i < integers.size(); i++) {
      encoded.addAll(curNode.getValue());
      if (i + length > integers.size()) {
        break;
      }
      curNode = curNode.getEdge(integers.subList(i, i + length));
      length = getTrellisNodeKeyLength(curNode);
    }
    return encoded;
  }

  private List<Integer> prependZeros(List<Integer> integers, int size) {
    List<Integer> extendedList = IntStream.range(0, size).mapToObj(i -> 0).collect(Collectors.toList());
    extendedList.addAll(integers);
    return extendedList;
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
