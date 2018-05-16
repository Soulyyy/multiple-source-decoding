package functions;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import data.State;
import data.StateList;
import utils.PermutationGenerator;

public class StatesGenerator {

  public static StateList generateStates(int length) {
    List<State> states = PermutationGenerator.generateAllBinaryPermutations(length).stream()
        .map(State::new)
        .collect(Collectors.toList());
    return new StateList(states);
  }

  public static List<StateList> generateStatesList(List<Integer> lengths) {
    return lengths.stream().map(StatesGenerator::generateStates).collect(Collectors.toList());
  }
}
