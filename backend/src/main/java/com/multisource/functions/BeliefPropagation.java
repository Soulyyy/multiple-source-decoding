package com.multisource.functions;

import java.util.List;

import com.multisource.data.MatrixImpl;

public class BeliefPropagation {

  private static final double ONE_PROBABILITY = 0.5;

  public final double noise;

  MatrixImpl parityCheckMatrix;

  public BeliefPropagation(MatrixImpl parityCheckMatrix, double noise) {
    this.parityCheckMatrix = parityCheckMatrix;
    this.noise = noise;
  }

  public List<Integer> decode(List<Integer> codeword) {
    //if(codeword.size() != parityCheckMatrix)
    return codeword;
  }


}
