package data.trellis;

import java.util.List;

public class TrellisEdge {

  private List<Integer> parityBits;
  private TrellisNode targetNode;

  public TrellisEdge(List<Integer> parityBits, TrellisNode targetNode) {
    this.parityBits = parityBits;
    this.targetNode = targetNode;
  }

  public List<Integer> getParityBits() {
    return parityBits;
  }

  public TrellisNode getTargetNode() {
    return targetNode;
  }

  @Override
  public String toString() {
    return parityBits.toString()+ " -> " + targetNode.toString();
  }
}
