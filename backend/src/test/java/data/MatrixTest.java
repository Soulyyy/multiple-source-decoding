package data;

import java.util.Arrays;
import java.util.Collection;

public class MatrixTest {

  static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {new Integer[][]{{1, 0, 1}, {1, 1, 1}}},
        {new Integer[][]{{1, 1}, {2}}},
        {new String[][]{{"Tere", "Sina"}, {"Mina", "sõna"}}},
        {null},
    });
  }

}
