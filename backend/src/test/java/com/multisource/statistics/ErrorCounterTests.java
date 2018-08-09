package com.multisource.statistics;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import com.multisource.statistics.ErrorCounter;

public class ErrorCounterTests {

  static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {Collections.emptyList(), Collections.emptyList(), 0L},
        {Collections.singletonList(0), Collections.singletonList(1), 1L},
        {Arrays.asList(0, 1, 1, 0, 1), Collections.emptyList(), -1L},
        {Arrays.asList(0, 1, 1, 0, 1), Collections.singletonList(1), -1L},
        {Arrays.asList(0, 1, 1, 0, 1), Arrays.asList(0, 1, 1, 0, 1), 0L},
        {Arrays.asList(0, 1, 1, 0, 1), Arrays.asList(0, 1, 1, 0), -1L},
        {Arrays.asList(0, 1, 1, 0, 1), Arrays.asList(0, 1, 1, 0, 1, 1), -1L},
        {Arrays.asList(0, 1, 1, 0, 1), Arrays.asList(0, 1, 0, 0, 0), 2L},
    });
  }

  @DisplayName("Error Counter Test")
  @ParameterizedTest(name = "Compared \"{0}\" and \"{1}\". Expecting \"{2}\" mismatches.")
  @MethodSource(value = "data")
  public void testErrorCounter(List<Integer> initial, List<Integer> posterior, long numberOfErrorsExpected) {
    if (numberOfErrorsExpected >= 0) {
      long numberOfErrors = ErrorCounter.numberOfErrors(initial, posterior);
      Assertions.assertEquals(numberOfErrorsExpected, numberOfErrors);
    }
    else {
      try {
        ErrorCounter.numberOfErrors(initial, posterior);
      }
      catch (IllegalArgumentException e) {
        Assertions.assertEquals("The initial vector length and the posterior vector length must match. Instead got: " + initial.size() + " and: " + posterior.size(), e.getMessage());

      }
    }
  }
}
