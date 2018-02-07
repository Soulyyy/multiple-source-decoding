package functions;

import java.util.List;

import data.trellis.Trellis;

public class ViterbiDecoder {

  private Trellis trellis;

  public ViterbiDecoder(Trellis trellis) {
    this.trellis = trellis;
  }

  public List<Integer> decode(List<Integer> encoded) {
    Integer[][] mostLikelyPath = new Integer[0][];
    Integer[][] mostLikelyResult = new Integer[0][];
    return encoded;
  }
}
