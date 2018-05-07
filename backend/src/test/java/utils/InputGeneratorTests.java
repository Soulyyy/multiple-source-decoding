package utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class InputGeneratorTests {

  private static final double ERROR_CONSTANT = 0.05;

  static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {1, 0.0},
        {1, 1.0},
        {100_000, 0.5},
        {100_000, 0.1},
    });
  }

  @DisplayName("Input Generator Test")
  @ParameterizedTest(name = "Input with length \"{0}\" and probability of one \"{1}\"")
  @MethodSource(value = "data")
  public void testInputGenerator(int length, double oneProbability) {
    List<Integer> integers = InputGenerator.generateInputWithOneProbability(length, oneProbability);
    long count = integers.stream().filter(i -> !i.equals(1)).count();
    double fractionOfOnes = (1.0 * count) / length;
    boolean isInRange = fractionOfOnes <= oneProbability + ERROR_CONSTANT || fractionOfOnes >= oneProbability - ERROR_CONSTANT;
    Assertions.assertTrue(isInRange, "Fraction of ones is in the expected range. Expected: " + oneProbability+" actual: " + fractionOfOnes);
  }
}
