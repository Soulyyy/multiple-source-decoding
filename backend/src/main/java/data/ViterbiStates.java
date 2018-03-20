package data;

public class ViterbiStates {

  private final States observedStates;

  private final States decodeStates;

  public ViterbiStates(States observedStates, States decodeStates) {
    this.observedStates = observedStates;
    this.decodeStates = decodeStates;
  }
}
