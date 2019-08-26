package structure.alterations;

import parameters.ParameterTemplate;

public class ChainedLinearAlteration extends Alteration {
  static {
    ParameterTemplate[] templates = new ParameterTemplate[3];
    initTemplates(templates);
    parameterTemplates.put(ChainedLinearAlteration.class, templates);
  }

  @Override
  public double getDouble() {
    double start = params[START].getDouble();
    double k = (timeValue + nValue) / quantityValue + params[SHIFT].getDouble();
    return start + (params[END].getDouble() - start)
         * (k - Math.floor(k));
  }  
}
