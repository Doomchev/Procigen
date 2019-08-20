package structure.transformations;

import gui.menu.MoveMenuItem;
import gui.menu.RemoveMenuItem;
import gui.menu.ToggleMenuItem;
import structure.Element;

public abstract class Transformation extends Element {
  @Override
  public void menu(int x, int y, Element parent) {
    showMenu(x, y
        , submenu("Add new transformation", parent, transformationTypes, this)
        , new ToggleMenuItem(this)
        , new MoveMenuItem(parent, this, -1)
        , new MoveMenuItem(parent, this, 1)
        , new RemoveMenuItem(parent, this));
  }
  
  @Override
  public int update(int x0, int y0, Element parent) {
    return elementsColumn.addBlock(this, x0, y0, parent);
  }
}
