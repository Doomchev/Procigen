package structure.alterations;

import parameters.ParameterTemplate;
import structure.Element;

public class Alteration extends Element {
  public static final int START = 0, END = 1;
  public ParameterTemplate template;
  
  public static void initTemplates(ParameterTemplate[] templates) {
    templates[START] = new ParameterTemplate("Start", 0.0);
    templates[END] = new ParameterTemplate("End", 1.0);
  }

  @Override
  public void setTemplate(ParameterTemplate template) {
    this.template = template;
  }
}
