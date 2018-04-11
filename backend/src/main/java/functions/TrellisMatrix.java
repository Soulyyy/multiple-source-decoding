package functions;

import data.State;
import data.StateList;

public class TrellisMatrix {

  private final double[][] matrix;

  private final StateList transitionStates;
  private final StateList encodingStates;

  public TrellisMatrix(double[][] matrix, StateList transitionStates, StateList encodingStates) {
    this.matrix = matrix;
    this.transitionStates = transitionStates;
    this.encodingStates = encodingStates;
  }

  public double get(State transtionState, State encodingState) {
    return 0.0;
  }
}
