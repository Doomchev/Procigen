package structure.patterns;

import parameters.ParameterTemplate;
import static java.lang.Math.*;

public class Beam extends Gradient {
  public static final int QUANTITY = 2;
  public static final int ONE = 0, MANY = 1;
  
  static {
    ParameterTemplate[] templates = new ParameterTemplate[3];
    templates[TYPE] = new ParameterTemplate("Type", orientation);
    templates[SHIFT] = new ParameterTemplate("Shift", 0.0);
    templates[QUANTITY] = new ParameterTemplate("Quantity", "One", "Many");
    parameterTemplates.put(Beam.class, templates);
  }
  
  @Override
  public void render(double[] coords, double[] pixels, int operation
      , double multiplier) {
    int dc = params[TYPE].getValue() == VERTICAL ? 0 : 1;
    double shift = params[SHIFT].getDouble();
    boolean many = params[QUANTITY].getValue() == MANY;
    for(int index = 0; index < pixels.length; index++) {
      double x = coords[(index << 1) + dc];
      if(many) {
        x = (x + shift) * 0.5;
        x = (x - Math.floor(x)) * 2.0 - 1.0;
      }
      x = Math.max(0, one - abs(x));
      switch(operation) {
        case ADD:
          pixels[index] += x;
          break;
        case MULTIPLY:
          pixels[index] *= x;
          break;
        case MIN:
          pixels[index] = Math.min(pixels[index], x);
          break;
        case MAX:
          pixels[index] = Math.max(pixels[index], x);
          break;
      }
    }
  }
}
