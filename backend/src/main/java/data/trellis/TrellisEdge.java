package data.trellis;

import java.util.List;

public class TrellisEdge {

  private double weight;
  private List<Integer> parityBits;
  private TrellisNode targetNode;

  public TrellisEdge(double weight, List<Integer> parityBits, TrellisNode targetNode) {
    this.weight = weight;
    this.parityBits = parityBits;
    this.targetNode = targetNode;
  }

  public double getWeight() {
    return weight;
  }

  public List<Integer> getParityBits() {
    return parityBits;
  }

  public TrellisNode getTargetNode() {
    return targetNode;
  }

  @Override
  public String toString() {
    return weight + " " + parityBits.toString();
  }
}
