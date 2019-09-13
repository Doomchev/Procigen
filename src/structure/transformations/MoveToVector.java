package structure.transformations;

import static base.Main.PI2;
import static base.Main.parameterTemplates;
import base.RenderingBitmap;
import parameters.ParameterTemplate;

public class MoveToVector extends Transformation {
  public static final int RADIUS = 0, ANGLE = 1;
  
  static {
    ParameterTemplate[] templates = new ParameterTemplate[2];
    templates[RADIUS] = new ParameterTemplate("Radius", 1.0);
    templates[ANGLE] = new ParameterTemplate("Angle", 0.0);
    parameterTemplates.put(MoveToVector.class, templates);
  }
  

  @Override
  public void applyTransformation(RenderingBitmap bitmap) {
    if(hide) return;
    double radius = params[RADIUS].getDouble();
    double angle = PI2 * params[ANGLE].getDouble();
    double dx = Math.cos(angle) * radius;
    double dy = Math.sin(angle) * radius;
    double[] coords = bitmap.coords;
    for(int index = 0; index < bitmap.size2; index += 2) {
      coords[index] += dx;
      coords[index + 1] += dy;
    }
  }
}