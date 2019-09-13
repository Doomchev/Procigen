package structure.patterns;

import base.RenderingBitmap;
import structure.Element;

public abstract class Pattern extends Element {
  public final static double one = 0.999999999999999;
  
  public abstract void render(RenderingBitmap bitmap);
  
  @Override
  public Pattern getPattern() {
    return this;
  }
}
