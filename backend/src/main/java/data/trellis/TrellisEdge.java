package data.trellis;

import java.util.List;

public class TrellisEdge {

  private List<Integer> parityBits;
  private TrellisNode previousNode;
  private TrellisNode targetNode;

  public TrellisEdge(List<Integer> parityBits, TrellisNode previousNode, TrellisNode targetNode) {
    this.parityBits = parityBits;
    this.previousNode = previousNode;
    this.targetNode = targetNode;
  }


  public List<Integer> getParityBits() {
    return parityBits;
  }

  public TrellisNode getPreviousNode() {
    return previousNode;
  }

  public TrellisNode getTargetNode() {
    return targetNode;
  }

  @Override
  public String toString() {
    return parityBits.toString() + " -> " + targetNode.toString();
  }
}
