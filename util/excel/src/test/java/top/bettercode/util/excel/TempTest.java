package top.bettercode.util.excel;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

/**
 * @author Peter Wu
 */
public class TempTest {

  @Test
  void name1() {
    System.err.println("0." + String.format("%0" + 2 + "d", 0));
  }

  @Test
  public void name() {
    for (int c = 0; c < 200; c++) {
      String x = getString(c);
      System.err.println(x);
    }
  }

  @NotNull
  private String getString(int i) {
    StringBuilder chars = new StringBuilder();
    do {
      chars.append((char) ('A' + i % 26));
    } while ((i = ((i / 26)-1)) >= 0);
    return chars.reverse().toString();
  }
}
