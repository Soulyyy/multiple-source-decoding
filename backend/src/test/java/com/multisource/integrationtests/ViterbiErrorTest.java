package com.multisource.integrationtests;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.multisource.builder.MatrixFactory;
import com.multisource.builder.TrellisFactory;
import com.multisource.data.MatrixImpl;
import com.multisource.data.trellis.Trellis;
import com.multisource.functions.ConvolutionalTrellisEncoder;
import com.multisource.functions.ViterbiDecoder;
import com.multisource.statistics.ErrorCounter;
import com.multisource.utils.VectorUtils;

public class ViterbiErrorTest {

  private static final int VECTOR_LENGTH = 10_000;
  private static final double ERROR_BOUND = 0.05;

  static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {"76.mat", 0.0},
        {"76.mat", 1.0},
        {"76.mat", 0.5},
        {"76.mat", 0.1},
        {"76.mat", 0.2},
        {"76.mat", 0.9},
    });
  }


  @DisplayName("Error Counter Test")
  @ParameterizedTest(name = "Using matrix \"{0}\" and channel error rate \"{1}\".")
  @MethodSource(value = "data")
  public void testViterbiErrors(String matrixName, double errorRate) {
    List<MatrixImpl> matrix = MatrixFactory.build(matrixName);
    Trellis trellis = TrellisFactory.build(matrix);
    ConvolutionalTrellisEncoder trellisEncoder = new ConvolutionalTrellisEncoder(trellis);
    //List<StateList> states =
    ViterbiDecoder viterbiDecoder = new ViterbiDecoder(trellis);

    List<Integer> initialVector = VectorUtils.generateUniformlyRandomVector(VECTOR_LENGTH);
    List<Integer> encodedVector = trellisEncoder.encode(initialVector);
    List<Integer> vectorWithErrors = VectorUtils.createVectorErrors(encodedVector, errorRate);
    List<Integer> decodedVector = viterbiDecoder.decode(vectorWithErrors, errorRate);

    long numberOfErrors = ErrorCounter.numberOfErrors(initialVector, decodedVector);
    System.out.println(numberOfErrors);

  }

}
