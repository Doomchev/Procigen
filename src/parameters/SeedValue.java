package parameters;

import java.io.IOException;
import java.util.Random;
import structure.Element;

public class SeedValue extends Element {
  public static Random random = new Random();
  
  public long seed;
  
  public SeedValue(long seed) {
    this.seed = seed;
  }
  
  public SeedValue() {
    this.seed = random.nextLong();
  }
  
  @Override
  public double getRnd(int increment) {
    random.setSeed(seed + increment * 333667);
    random.nextDouble();
    return random.nextDouble();
  }
  
  @Override
  public int updateProperties(int x0, int y0) {
    return y0;
  }
  
  @Override
  public void menu(ParameterTemplate template, int x0, int y0) {
    seed = random.nextLong();
  }
  
  @Override
  public void setFileIndex() {
  }

  @Override
  public void write() throws IOException {
    writer.writeLong(seed);
  }

  @Override
  public String toString() {
    return String.valueOf(seed);
  }
}
