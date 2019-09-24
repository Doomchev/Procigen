package structure.alterations;

import base.RenderingBitmap;
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

  public double getK(RenderingBitmap bitmap) {
    double part = params[N_PART].getDouble(bitmap);
    return part * bitmap.n / bitmap.quantity + getTime(bitmap) * (1.0 - part);
  }

  @Override
  public double getDouble(RenderingBitmap bitmap) {
    double start = params[START].getDouble(bitmap);
    return start + (params[END].getDouble(bitmap) - start) * getK(bitmap);
  }  
}
