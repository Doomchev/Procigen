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
    switch(operation) {
      case ADD:
        for(int index = 0; index < pixels.length; index++)
          pixels[index] += coords[(index << 1) + dc] * multiplier - shift;
        break;
      case MULTIPLY:
        for(int index = 0; index < pixels.length; index++)
          pixels[index] *= coords[(index << 1) + dc] * multiplier - shift;
        break;
      case MIN:
        for(int index = 0; index < pixels.length; index++)
          pixels[index] = Math.min(pixels[index], coords[(index << 1) + dc]
              * multiplier - shift);
        break;
      case MAX:
        for(int index = 0; index < pixels.length; index++)
          pixels[index] = Math.max(pixels[index], coords[(index << 1) + dc]
              * multiplier - shift);
        break;
    }
  }
}
