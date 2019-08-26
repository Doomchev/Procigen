package structure.patterns;

import parameters.ParameterTemplate;

public class Gradient extends Pattern {
  public static final int TYPE = 0, SHIFT = 1;
  
  static {
    ParameterTemplate[] templates = new ParameterTemplate[2];
    templates[TYPE] = new ParameterTemplate("Type", orientation);
    templates[SHIFT] = new ParameterTemplate("Shift", 0.0);
    parameterTemplates.put(Gradient.class, templates);
  }
  
  @Override
  public void render(double[] coords, double[] pixels, int operation
      , double multiplier) {
    int dc = params[TYPE].getValue() == VERTICAL ? 0 : 1;
    double shift = params[SHIFT].getDouble();
    for(int index = 0; index < pixels.length; index++) {
      double x = (coords[(index << 1) + dc] * multiplier - shift)
          * cMultiplication + cIncrement;
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
