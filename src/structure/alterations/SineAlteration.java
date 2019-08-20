package structure.alterations;

import parameters.ParameterTemplate;

public class SineAlteration extends LinearAlteration {
  static {
    ParameterTemplate[] templates = new ParameterTemplate[3];
    initTemplates(templates);
    parameterTemplates.put(SineAlteration.class, templates);
  }

  @Override
  public double getDouble() {
    double start = params[START].getDouble();
    return template.limit(start + (params[END].getDouble() - start) * (1.0
        + Math.cos(getK() * PI2)) * 0.5);
  }  
}
