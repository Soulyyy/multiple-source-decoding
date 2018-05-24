package builder;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import data.Matrix;
import data.trellis.Trellis;
import data.trellis.TrellisEdge;
import data.trellis.TrellisNode;

public class TrellisFactoryTest {

  static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {"76.mat"
            , new Integer[][][]{
            new Integer[][]{
                new Integer[]{0, 0},
                new Integer[]{0, 0},
                new Integer[]{0, 0},
                new Integer[]{1, 1},
                new Integer[]{1, 0}
            },
            new Integer[][]{
                new Integer[]{1, 0},
                new Integer[]{1, 1},
                new Integer[]{0, 1},
                new Integer[]{0, 0},
                new Integer[]{1, 1}
            },
            new Integer[][]{
                new Integer[]{0, 1},
                new Integer[]{1, 0},
                new Integer[]{0, 0},
                new Integer[]{0, 1},
                new Integer[]{1, 0}
            },
            new Integer[][]{
                new Integer[]{1, 1},
                new Integer[]{0, 1},
                new Integer[]{0, 1},
                new Integer[]{1, 0},
                new Integer[]{1, 1}
            },
        }
        },
        {"76-11.mat"
            , new Integer[][][]{
            new Integer[][]{
                new Integer[]{0, 0},
                new Integer[]{0, 0},
                new Integer[]{0, 0},
                new Integer[]{1, 1},
                new Integer[]{1, 1}
            },
            new Integer[][]{
                new Integer[]{1, 0},
                new Integer[]{1, 1},
                new Integer[]{0, 0},
                new Integer[]{0, 0},
                new Integer[]{1, 1}
            },
            new Integer[][]{
                new Integer[]{0, 1},
                new Integer[]{1, 0},
                new Integer[]{0, 0},
                new Integer[]{0, 1},
                new Integer[]{1, 1}
            },
            new Integer[][]{
                new Integer[]{1, 1},
                new Integer[]{0, 1},
                new Integer[]{0, 0},
                new Integer[]{1, 0},
                new Integer[]{1, 1}
            },
        }
        }
    });
  }

  @DisplayName("Trellis creation tests")
  @ParameterizedTest(name = "Read matrix from file \"{0}\" to generate trellis, expecting \"{1}\"")
  @MethodSource(value = "data")
  public void testTrellisFactory(String matrixFileName, Integer[][][] verificationArrays) {
    List<Matrix> matrices = MatrixFactory.build(matrixFileName);
    Trellis trellis = TrellisFactory.build(matrices);
    for (Integer[][] verificationArray : verificationArrays) {
      verifyNode(trellis, verificationArray);
    }
  }

  private void verifyNode(Trellis trellis, Integer[][] verificationArray) {
    TrellisNode node = trellis.getNode(Arrays.asList(verificationArray[0]));

    TrellisEdge zeroEdge = node.getEdge(Collections.singletonList(0));
    assertEquals(zeroEdge.getParityBits(), Arrays.asList(verificationArray[1]));
    assertEquals(zeroEdge.getTargetNode().getState().asList(), Arrays.asList(verificationArray[2]));

    TrellisEdge oneEdge = node.getEdge(Collections.singletonList(1));
    assertEquals(oneEdge.getParityBits(), Arrays.asList(verificationArray[3]));
    assertEquals(oneEdge.getTargetNode().getState().asList(), Arrays.asList(verificationArray[4]));
  }
}
