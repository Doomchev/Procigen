package structure.patterns;

import base.RenderingBitmap;
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
  public void render(RenderingBitmap bitmap) {
    int dc = params[TYPE].getValue() == VERTICAL ? 0 : 1;
    double shift = params[SHIFT].getDouble();
    double[] pixels = bitmap.pixels;
    double[] coords = bitmap.coords;
    for(int index = 0; index < bitmap.size; index++) {
      pixels[index] = (coords[(index << 1) + dc] - shift)
          * cMultiplication + cIncrement;
    }
  }
}
