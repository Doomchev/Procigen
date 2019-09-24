package structure.patterns;

import base.RenderingBitmap;
import parameters.ParameterTemplate;

public class Gradient extends Pattern {
  public static final int TYPE = 2, SHIFT = 3;
  
  static {
    ParameterTemplate[] templates = new ParameterTemplate[4];
    setTemplates(templates);
    templates[TYPE] = new ParameterTemplate("Type", orientation);
    templates[SHIFT] = new ParameterTemplate("Shift", 0.0);
    parameterTemplates.put(Gradient.class, templates);
  }
  
  @Override
  @SuppressWarnings("MismatchedReadAndWriteOfArray")
  public void renderPattern(RenderingBitmap bitmap) {
    int dc = params[TYPE].getValue() == VERTICAL ? 0 : 1;
    double shift = params[SHIFT].getDouble(bitmap);
    double[] pixels = bitmap.pixels;
    double[] coords = bitmap.coords;
    for(int index = 0; index < bitmap.size; index++) {
      pixels[index] = (coords[(index << 1) + dc] - shift)
          * bitmap.multiplication + bitmap.increment;
    }
  }
}
