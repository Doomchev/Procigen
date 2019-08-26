package structure.alterations;

import parameters.ParameterTemplate;

public class LinearAlteration extends Alteration {
  public static  final int N_PART = 3;

  static {
    ParameterTemplate[] templates = new ParameterTemplate[4];
    initTemplates(templates);
    parameterTemplates.put(LinearAlteration.class, templates);
  }
  
  public static void initTemplates(ParameterTemplate[] templates) {
    Alteration.initTemplates(templates);
    templates[N_PART] = new ParameterTemplate("N part", 0.0, 0.0, 1.0);
  }
  
  @Override
  public void setTemplate(ParameterTemplate template) {
    this.template = template;
  }

  public double getK() {
    double part = params[N_PART].getDouble();
    double k = part * nValue / quantityValue + timeValue * (1.0 - part)
        + params[SHIFT].getDouble();
    return k - Math.floor(k);
  }

  @Override
  public double getDouble() {
    double start = params[START].getDouble();
    return template.limit(start + (params[END].getDouble() - start) * getK());
  }  
}
