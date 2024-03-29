package structure.alterations;

import base.RenderingBitmap;
import parameters.ParameterTemplate;

public class SineAlteration extends LinearAlteration {
  static {
    ParameterTemplate[] templates = new ParameterTemplate[4];
    initTemplates(templates);
    parameterTemplates.put(SineAlteration.class, templates);
  }

  @Override
  public double getDouble(RenderingBitmap bitmap) {
    double start = params[START].getDouble(bitmap);
    return start + (params[END].getDouble(bitmap) - start) * (1.0
        + Math.cos(getK(bitmap) * PI2)) * 0.5;
  }  
}
