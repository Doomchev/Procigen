package structure.modifiers;

import structure.Element;

public abstract class Modifier extends Element {  
  public abstract double[] applyModifier(double[] srcPixels);
}
