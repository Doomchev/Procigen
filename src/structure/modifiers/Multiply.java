package structure.modifiers;

import parameters.ParameterTemplate;
import static base.Main.parameterTemplates;

public class Multiply extends Modifier {
  public static final int MULTIPLIER = 0;
  
  static {
    ParameterTemplate[] parameters = new ParameterTemplate[1];
    parameters[MULTIPLIER] = new ParameterTemplate("Multiplier", 1.0);
    parameterTemplates.put(Multiply.class, parameters);
  }

  @Override
  public double[] applyModifier(double[] pixels) {
    double k = params[MULTIPLIER].getDouble();
    for(int index = 0; index < pixels.length; index++) pixels[index] *= k;
    return pixels;
  }
}
