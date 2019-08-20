package structure.compositions;

import gui.menu.MoveMenuItem;
import gui.menu.RemoveMenuItem;
import gui.menu.ToggleMenuItem;
import java.util.LinkedList;
import parameters.ParameterTemplate;
import structure.Bitmap;
import structure.Element;

public abstract class Composition extends Element {
  public static final int NAME = 0, TRANSFORMATIONS = 1;
  
  public static void setTemplates(ParameterTemplate[] templates, String name) {
    templates[NAME] = new ParameterTemplate(name);
    templates[TRANSFORMATIONS] = new ParameterTemplate();
  }
  
  @Override
  public int update(int x0, int y0, Element parent) {
    return elementsColumn.addList(this, x0, y0);
  }

  @Override
  public void menu(int x, int y, Element parent) {
    showMenu(x, y, submenu("Add new transformation", this
        , transformationTypes, null), new ToggleMenuItem(this)
        , new MoveMenuItem(parent, this, -1)
        , new MoveMenuItem(parent, this, 1)
        , new RemoveMenuItem(parent, this));
  }

  @Override
  public LinkedList<Element> getList() {
    return params[TRANSFORMATIONS].getList();
  }

  public void mix(double[] sourcePixels, double[] destPixels, int operation) {
    switch(operation) {
      case ADD:
        for(int index = 0; index < destPixels.length; index++)
          destPixels[index] += sourcePixels[index];
        break;
      case MULTIPLY:
        for(int index = 0; index < destPixels.length; index++)
          destPixels[index] *= sourcePixels[index];
        break;
      case MIN:
        for(int index = 0; index < destPixels.length; index++)
          destPixels[index] = Math.min(destPixels[index], sourcePixels[index]);
        break;
      case MAX:
        for(int index = 0; index < destPixels.length; index++)
          destPixels[index] = Math.max(destPixels[index], sourcePixels[index]);
        break;
    }  }
}
