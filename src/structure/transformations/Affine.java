package structure.transformations;

import parameters.ParameterTemplate;
import static java.lang.Math.*;
import static base.Main.parameterTemplates;

public class Affine extends Transformation {
  public static final int DX = 0, DY = 1, XSCALE = 2, YSCALE = 3, ANGLE = 4;
  
  static {
    ParameterTemplate[] templates = new ParameterTemplate[5];
    templates[DX] = new ParameterTemplate("DX", 0.0);
    templates[DY] = new ParameterTemplate("DY", 0.0);
    templates[XSCALE] = new ParameterTemplate("XScale", 1.0);
    templates[YSCALE] = new ParameterTemplate("XScale", 1.0);
    templates[ANGLE] = new ParameterTemplate("Angle", 0.0, 0.0, 1.0);
    parameterTemplates.put(Affine.class, templates);
  }
  
  @Override
  public void applyTransformation(double[] coords) {
    if(hide) return;
    apply(coords, params[DX].getDouble(), params[DY].getDouble()
        , params[XSCALE].getDouble(), params[YSCALE].getDouble()
        , params[ANGLE].getDouble());
  }
  
  public static void apply(double[] coords, double dx, double dy, double scale
      , double angle) {
    apply(coords, dx, dy, scale, scale, angle);
  }
  public static void apply(double[] coords, double dx, double dy, double xScale
      , double yScale, double angle) {
		double xk = 1.0 / xScale;
		double yk = 1.0 / yScale;
		angle = angle * PI2;
		double sinAngle = sin(angle);
		double cosAngle = cos(angle);
		double xk1 = cosAngle * xk;
		double yk1 = -sinAngle * yk;
		double xk2 = sinAngle * xk;
		double yk2 = cosAngle * yk; 
    for(int index = 0; index < coords.length; index += 2) {
      double x = coords[index] - dx;
      double y = coords[index + 1] - dy;
      coords[index] = x * xk1 + y * yk1;
      coords[index + 1] = x * xk2 + y * yk2;
    }
  }
}
