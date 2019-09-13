package structure.transformations;

import static base.Main.PI2;
import static base.Main.parameterTemplates;
import base.RenderingBitmap;
import parameters.ParameterTemplate;

public class Sine extends Transformation {
  public static final int H_SIZE = 0, H_AMPLITUDE = 1, H_SHIFT = 2
      , V_SIZE = 3, V_AMPLITUDE = 4, V_SHIFT = 5;
  
  static {
    ParameterTemplate[] templates = new ParameterTemplate[6];
    templates[H_SIZE] = new ParameterTemplate("Horizontal size", 0.0);
    templates[H_AMPLITUDE] = new ParameterTemplate("Horizontal amplitude", 1.0);
    templates[H_SHIFT] = new ParameterTemplate("Horizontal shift", 0.0, 0.0, 1.0);
    templates[V_SIZE] = new ParameterTemplate("Vertical size", 0.0);
    templates[V_AMPLITUDE] = new ParameterTemplate("Vertical amplitude", 1.0);
    templates[V_SHIFT] = new ParameterTemplate("Vertical shift", 0.0, 0.0, 1.0);
    parameterTemplates.put(Sine.class, templates);
  }
  

  @Override
  public void applyTransformation(RenderingBitmap bitmap) {
    if(hide) return;
    double hSize = params[H_SIZE].getDouble();
    double hAmplitude = 1.0 / params[H_AMPLITUDE].getDouble();
    double hShift = params[H_SHIFT].getDouble();
    double vSize = params[V_SIZE].getDouble();
    double vAmplitude = 1.0 / params[V_AMPLITUDE].getDouble();
    double vShift = params[V_SHIFT].getDouble();
    double[] coords = bitmap.coords;
    for(int index = 0; index < bitmap.size2; index += 2) {
      coords[index] += Math.sin((coords[index + 1] * hSize + hShift) * PI2)
          * hAmplitude;
      coords[index + 1] += Math.sin((coords[index] * vSize + vShift) * PI2)
          * vAmplitude;
    }
  }
}
