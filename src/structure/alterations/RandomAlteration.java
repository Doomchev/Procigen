package structure.alterations;

import base.RenderingBitmap;
import parameters.ParameterTemplate;

public class RandomAlteration extends Alteration {
  public static final int SEED = 3;

  static {
    ParameterTemplate[] templates = new ParameterTemplate[4];
    initTemplates(templates);
    templates[SEED] = new ParameterTemplate(ParameterTemplate.SEED, "Seed");
    parameterTemplates.put(RandomAlteration.class, templates);
  }

  @Override
  public double getDouble(RenderingBitmap bitmap) {
    double start = params[START].getDouble(bitmap);
    return start + (params[END].getDouble(bitmap) - start)
        * params[SEED].getRnd(bitmap.n);
  }  
}
