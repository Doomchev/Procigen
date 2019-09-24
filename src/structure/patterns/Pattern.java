package structure.patterns;

import base.RenderingBitmap;
import gui.menu.MoveMenuItem;
import gui.menu.NewMenuItem;
import gui.menu.RemoveMenuItem;
import gui.menu.ToggleMenuItem;
import java.util.LinkedList;
import parameters.ParameterTemplate;
import structure.Element;

public abstract class Pattern extends Element {
  public final static double one = 0.999999999999999;
  public static final int TRANSFORMATIONS = 0, OPERATION = 1;
  
  public static void setTemplates(ParameterTemplate[] templates) {
    templates[TRANSFORMATIONS] = new ParameterTemplate("Transformations");
    templates[OPERATION] = new ParameterTemplate("Operation", operations);
  }
  
  @Override
  public int update(int x0, int y0, Element parent) {
    return elementsColumn.addList(this, parent, x0, y0);
  }

  @Override
  public LinkedList<Element> getList() {
    return params[TRANSFORMATIONS].getList();
  }

  @Override
  public void menu(int x, int y, Element parent) {
    showMenu(x, y, submenu("Add new pattern", parent, patternTypes, this)
        , submenu("Add new transformation", this, transformationTypes, null)
        , new ToggleMenuItem(this)
        , new MoveMenuItem(parent, this, -1)
        , new MoveMenuItem(parent, this, 1)
        , new RemoveMenuItem(parent, this));
  }
  
  public abstract void renderPattern(RenderingBitmap bitmap);
  
  @Override
  public void render(RenderingBitmap bitmap) {
    if(hide) return;
    LinkedList<Element> list = params[TRANSFORMATIONS].getList();
    RenderingBitmap patternBitmap = bitmap.patternBitmap;
    for(Element transformation : list) {
      if(transformation.hide) continue;
      transformation.applyTransformation(patternBitmap);
    }
    renderPattern(patternBitmap);
    bitmap.compositionBitmap.paste(patternBitmap, params[OPERATION].getValue());
  }
  
  @Override
  public Pattern getPattern() {
    return this;
  }
}
