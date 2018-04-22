package data.newtrellis;

import java.util.List;

public class TrellisEdge {

  double weight;
  List<Integer> parityBits;
  TrellisNode targetNode;

  public TrellisEdge(double weight, List<Integer> parityBits, TrellisNode targetNode) {
    this.weight = weight;
    this.parityBits = parityBits;
    this.targetNode = targetNode;
  }
}
