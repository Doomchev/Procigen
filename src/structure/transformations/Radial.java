package structure.transformations;

import base.RenderingBitmap;
import parameters.ParameterTemplate;

public class Radial extends Transformation {
  static {
    ParameterTemplate[] templates = new ParameterTemplate[0];
    parameterTemplates.put(Radial.class, templates);
  }
  

  @Override
  public void applyTransformation(RenderingBitmap bitmap) {
    if(hide) return;
    double[] coords = bitmap.coords;
    for(int index = 0; index < bitmap.size2; index += 2) {
      double x = coords[index];
      double y = coords[index + 1];
      double radius = Math.sqrt(x * x + y * y);
      double angle = Math.atan2(y, x) / PI2;
      coords[index] = radius;
      coords[index + 1] = angle;
    }
  }
}
