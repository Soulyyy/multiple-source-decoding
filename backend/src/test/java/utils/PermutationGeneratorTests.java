package utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class PermutationGeneratorTests {


  static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {1, new Integer[][]{{0}, {1}}},
        {0, new Integer[][]{}},
        {-1, new Integer[][]{}},
        {4, new Integer[][]{{0, 0, 0, 0}, {0, 0, 0, 1}, {0, 0, 1, 0}, {0, 1, 0, 0},
            {1, 0, 0, 0}, {1, 0, 0, 1}, {1, 0, 1, 0}, {1, 1, 0, 0},
            {0, 0, 1, 1}, {0, 1, 0, 1}, {0, 1, 1, 0}, {0, 1, 1, 1},
            {1, 0, 1, 1}, {1, 1, 0, 1}, {1, 1, 1, 0}, {1, 1, 1, 1}}},
    });
  }

  @DisplayName("Binary Input Test")
  @ParameterizedTest(name = "Parsed \"{0}\", expected \"{1}\"")
  @MethodSource(value = "data")
  public void testGenerator(int size, Integer[][] elements) {
    List<List<Integer>> permutations = PermutationGenerator.generateAllBinaryPermutations(size);
    List<List<Integer>> expected = Arrays.stream(elements).map(Arrays::asList).collect(Collectors.toList());

    Assertions.assertTrue(permutations.containsAll(expected) && expected.containsAll(permutations));
  }


}
