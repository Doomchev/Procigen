package structure.alterations;

import parameters.ParameterTemplate;

public class ChainedLinearAlteration extends Alteration {
  static {
    ParameterTemplate[] templates = new ParameterTemplate[2];
    initTemplates(templates);
    parameterTemplates.put(ChainedLinearAlteration.class, templates);
  }

  public double getK() {
    return 1.0;
  }

  @Override
  public double getDouble() {
    double start = params[START].getDouble();
    return template.limit(start + (params[END].getDouble() - start)
         * (timeValue + nValue) / quantityValue);
  }  
}
