package structure.alterations;

import base.RenderingBitmap;
import parameters.ParameterTemplate;

public class ChainedSineAlteration extends Alteration {
  public static final int MULTIPLIER = 3, N_SHIFT = 4;

  static {
    ParameterTemplate[] templates = new ParameterTemplate[5];
    initTemplates(templates);
    templates[MULTIPLIER] = new ParameterTemplate("Multiplier", 1.0, 1.0, 0.0, 1.0);
    templates[N_SHIFT] = new ParameterTemplate("N shift", 0.0, 0.0, 0.0, 1.0);
    parameterTemplates.put(ChainedSineAlteration.class, templates);
  }

  @Override
  public double getDouble(RenderingBitmap bitmap) {
    double start = params[START].getDouble(bitmap);
    int n = (bitmap.n + params[N_SHIFT].getInt(bitmap)) % bitmap.quantity;
    double angle = 1.0 * (getTime(bitmap) + n) / bitmap.quantity
        + params[SHIFT].getDouble(bitmap);
    angle = angle - Math.floor(angle);
    double k = (1.0 - Math.cos(angle * PI2
        * params[MULTIPLIER].getDouble(bitmap))) * 0.5;
    return start + (params[END].getDouble(bitmap) - start) * k;
  }  
}
