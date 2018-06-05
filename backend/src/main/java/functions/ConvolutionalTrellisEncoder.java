package functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    int arraySize = trellis.getAllNodes().stream().findFirst().map(i -> i.getState().size()).get();
    TrellisNode activeNode = trellis.getNode(new State(Collections.nCopies(arraySize, 0)));
    List<Integer> encoded = new ArrayList<>();
    for (Integer integer : integers) {
      TrellisEdge edge = activeNode.getEdge(new State(Collections.singletonList(integer)));
      encoded.addAll(edge.getParityBits());
      activeNode = edge.getTargetNode();
    }
    return encoded;
  }
}
