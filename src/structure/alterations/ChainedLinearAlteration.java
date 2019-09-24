package structure.alterations;

import base.RenderingBitmap;
import parameters.ParameterTemplate;

public class ChainedLinearAlteration extends Alteration {
  public static final int N_SHIFT = 3;
  
  static {
    ParameterTemplate[] templates = new ParameterTemplate[4];
    initTemplates(templates);
    templates[N_SHIFT] = new ParameterTemplate("N shift", 0.0, 0.0, 0.0, 1.0);
    parameterTemplates.put(ChainedLinearAlteration.class, templates);
  }

  @Override
  public double getDouble(RenderingBitmap bitmap) {
    double start = params[START].getDouble(bitmap);
    int n = (bitmap.n + params[N_SHIFT].getInt(bitmap)) % bitmap.quantity;
    double k = 1.0 * (getTime(bitmap) + n) / bitmap.quantity
        + params[SHIFT].getDouble(bitmap);
    k = k - Math.floor(k);
    return start + (params[END].getDouble(bitmap) - start) * k;
  }  
}
