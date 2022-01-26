package code;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author luoyu
 */
public class ChapterOne {

  public static void main(String[] args) throws InterruptedException, IOException {
    String expletive = "Expletive";
    String PC13 = "deleted";
    String message = "Expletive" + PC13;
    String message2 = "Expletive" + "deleted";
    String message3 = "Expletivedeleted";

    System.out.println(message3 == message);
    System.out.println(String.join(",", "/"));
    System.out.println(Arrays.toString(",1,,1,1,".split(",")));
    System.out.printf("%d %<x", 16);
    System.out.println(1);
  }
}
