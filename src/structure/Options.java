package structure;

import static base.Main.parameterTemplates;
import java.util.LinkedList;
import parameters.ParameterTemplate;

public class Options extends Element {
  public static Options instance = new Options();
  public static final int PALETTES = 0;
  
  static {
    ParameterTemplate[] templates = new ParameterTemplate[1];
    templates[PALETTES] = new ParameterTemplate();
    parameterTemplates.put(Options.class, templates);
  }

  public static LinkedList<Element> getPalettes() {
    return instance.getList();
  } 

  @Override
  public LinkedList<Element> getList() {
    return params[PALETTES].getList();
  }
}
