package structure.transformations;

import base.RenderingBitmap;
import parameters.ParameterTemplate;

public class Swirl extends Transformation {
  public static final int MULTIPLIER = 0;
  
  static {
    ParameterTemplate[] templates = new ParameterTemplate[1];
    templates[MULTIPLIER] = new ParameterTemplate("Multiplier", 1.0);
    parameterTemplates.put(Swirl.class, templates);
  }
  

  @Override
  public void applyTransformation(RenderingBitmap bitmap) {
    if(hide) return;
    double multiplier = params[MULTIPLIER].getDouble(bitmap);
    double[] coords = bitmap.coords;
    for(int index = 0; index < bitmap.size2; index += 2) {
      double x = coords[index];
      double y = coords[index + 1];
      double radius = Math.sqrt(x * x + y * y);
      double angle = Math.atan2(y, x) + radius * multiplier;
      coords[index] = Math.cos(angle) * radius;
      coords[index + 1] = Math.sin(angle) * radius;
    }
  }
}
