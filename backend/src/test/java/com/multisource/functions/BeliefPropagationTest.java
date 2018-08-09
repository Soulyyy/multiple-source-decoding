package com.multisource.functions;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.multisource.builder.MatrixFactory;
import com.multisource.data.MatrixImpl;
import com.multisource.functions.BeliefPropagation;

class BeliefPropagationTest {

  static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {"46examplematrix.mat", Arrays.asList(1, 0, 1, 1, 0, 0), Arrays.asList(1, 0, 0, 1, 1, 0)},
    });
  }

  @DisplayName("Belief Propagation Test")
  @ParameterizedTest(name = "Input:\"{1}\", expected output:\"{2}\"")
  @MethodSource(value = "data")
  void testDecode(String matrixPath, List<Integer> input, List<Integer> expeced) {
    MatrixImpl matrix = MatrixFactory.build(matrixPath).get(0);
    BeliefPropagation beliefPropagation = new BeliefPropagation(matrix, 0.2);
    List<Integer> actual = beliefPropagation.decode(input);
    Assertions.assertEquals(expeced, actual);

  }
}