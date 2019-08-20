package structure.patterns;

import structure.Element;

public abstract class Pattern extends Element {
  public final static double one = 0.999999999999999;
  
  public abstract void render(double[] coords, double[] pixels, int operation
      , double multiplier);
  
  @Override
  public Pattern getPattern() {
    return this;
  }
}
