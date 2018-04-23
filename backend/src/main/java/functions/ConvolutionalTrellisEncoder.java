package functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import data.State;
import data.trellis.Trellis;
import data.trellis.TrellisEdge;
import data.trellis.TrellisNode;

public class ConvolutionalTrellisEncoder {

  Trellis trellis;

  public ConvolutionalTrellisEncoder(Trellis trellis) {
    this.trellis = trellis;
  }

  public List<Integer> encode(List<Integer> integers) {
    TrellisNode activeNode = trellis.getNode(Arrays.asList(0, 0));
    List<Integer> encoded = new ArrayList<>();
    for (Integer integer : integers) {
      TrellisEdge edge = activeNode.getEdge(Collections.singletonList(integer));
      encoded.addAll(edge.getParityBits());
      activeNode = edge.getTargetNode();
    }
    return encoded;
  }
}
