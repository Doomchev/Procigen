
import java.util.Random;

public class RandomTest {
  public static void main(String[] args) {
    Random random = new Random();
    for(int n = 0; n < 20; n++) {
      random.setSeed(n);
      random.nextDouble();
      System.out.println(random.nextDouble());
    }
  }
}
