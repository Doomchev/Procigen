package structure;

import base.RenderingBitmap;
import gui.menu.MoveMenuItem;
import gui.menu.NewMenuItem;
import gui.menu.RemoveMenuItem;
import java.util.LinkedList;
import parameters.ParameterTemplate;

public class Bitmap extends Element {
  public static final int NAME = 0, COMPOSITIONS = 1, PALETTE = 2;
  
  static {
    ParameterTemplate[] templates = new ParameterTemplate[3];
    templates[NAME] = new ParameterTemplate("Bitmap", true);
    templates[COMPOSITIONS] = new ParameterTemplate("Compositions");
    templates[PALETTE] = new ParameterTemplate("Palette"
        , Options.getPalettes(), true);
    parameterTemplates.put(Bitmap.class, templates);
  }

  @Override
  public LinkedList<Element> getList() {
    return params[COMPOSITIONS].getList();
  }
  
  @Override
  public int update(int x0, int y0, Element parent) {
    return elementsColumn.addList(this, parent, x0, y0);
  }

  @Override
  public void menu(int x, int y, Element parent) {
    showMenu(x, y
        , new NewMenuItem("Add new bitmap", parent, Bitmap.class, this)
        , submenu("Add new composition", this, compositionTypes, null)
        , new MoveMenuItem(parent, this, -1)
        , new MoveMenuItem(parent, this, 1)
        , new RemoveMenuItem(parent, this));
  }
  
  @Override
  public void render(RenderingBitmap bitmap) {
    if(hide) return;
    emptyBitmap.copyParametersTo(bitmap);
    for(int index = 0; index < bitmap.size; index++) bitmap.pixels[index] = 0;
    for(Element composition : params[COMPOSITIONS].getList())
      composition.render(bitmap);
    params[PALETTE].getPalette().render(bitmap);
  }
}
