package structure.alterations;

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
  public double getDouble() {
    double start = params[START].getDouble();
    int n = (nValue + params[N_SHIFT].getInt()) % quantityValue;
    double k = 1.0 * (timeValue + n) / quantityValue + params[SHIFT].getDouble();
    k = k - Math.floor(k);
    return start + (params[END].getDouble() - start) * k;
  }  
}
