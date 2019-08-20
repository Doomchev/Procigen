package structure.modifiers;

import parameters.ParameterTemplate;
import static base.Main.parameterTemplates;

public class Increment extends Modifier {
  public static final int VALUE = 0;

  static {
    ParameterTemplate[] parameters = new ParameterTemplate[1];
    parameters[VALUE] = new ParameterTemplate("Value", 0.0);
    parameterTemplates.put(Increment.class, parameters);
  }
  
  @Override
  public double[] applyModifier(double[] pixels) {
    double k = params[VALUE].getDouble();
    for(int index = 0; index < pixels.length; index++) pixels[index] += k;
    return pixels;
  }
}