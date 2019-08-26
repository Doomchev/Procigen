package structure.alterations;

import parameters.ParameterTemplate;

public class ChainedSineAlteration extends Alteration {
  public static final int MULTIPLIER = 3;

  static {
    ParameterTemplate[] templates = new ParameterTemplate[4];
    initTemplates(templates);
    templates[MULTIPLIER] = new ParameterTemplate("Multiplier", 1.0, 1.0, 0.0, 1.0);
    parameterTemplates.put(ChainedSineAlteration.class, templates);
  }

  @Override
  public double getDouble() {
    double start = params[START].getDouble();
    double angle = 1.0 * (timeValue + nValue) / quantityValue * PI2
        * params[MULTIPLIER].getDouble();
    double k = (1.0 - Math.cos(angle)) * 0.5 + params[SHIFT].getDouble();
    return template.limit(start + (params[END].getDouble() - start) * (k
        - Math.floor(k)));
  }  
}
