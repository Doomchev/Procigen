package structure.alterations;

import base.RenderingBitmap;
import parameters.ParameterTemplate;
import structure.Element;

public class Alteration extends Element {
  public static final int START = 0, END = 1, SHIFT = 2;
  public ParameterTemplate template;
  
  public static void initTemplates(ParameterTemplate[] templates) {
    templates[START] = new ParameterTemplate("Start", 0.0);
    templates[END] = new ParameterTemplate("End", 1.0);
    templates[SHIFT] = new ParameterTemplate("Shift", 0.0);
  }
  
  public double getTime(RenderingBitmap bitmap) {
    double k = bitmap.time + params[SHIFT].getDouble(bitmap);
    return k - Math.floor(k);
  }

  @Override
  public void setTemplate(ParameterTemplate template) {
    this.template = template;
  }
}
