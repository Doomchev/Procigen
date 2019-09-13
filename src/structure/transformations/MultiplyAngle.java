package structure.transformations;

import base.RenderingBitmap;
import parameters.ParameterTemplate;

public class MultiplyAngle extends Transformation {
  public static final int MULTIPLIER = 0, TYPE = 1;
  
  static {
    ParameterTemplate[] templates = new ParameterTemplate[1];
    templates[MULTIPLIER] = new ParameterTemplate("Multiplier", 0.5);
    parameterTemplates.put(MultiplyAngle.class, templates);
  }
  

  @Override
  public void applyTransformation(RenderingBitmap bitmap) {
    if(hide) return;
    double sectorSize = params[MULTIPLIER].getDouble();
    double[] coords = bitmap.coords;
    for(int index = 0; index < bitmap.size2; index += 2) {
      double x = coords[index];
      double y = coords[index + 1];
      double radius = Math.sqrt(x * x + y * y);
      double angle = Math.atan2(y, x) * sectorSize;
      coords[index] = Math.cos(angle) * radius;
      coords[index + 1] = Math.sin(angle) * radius;
    }
  }
}
