package structure;

import gui.menu.MoveMenuItem;
import gui.menu.NewMenuItem;
import gui.menu.RemoveMenuItem;
import java.util.LinkedList;
import parameters.ParameterTemplate;

public class Bitmap extends Element {
  public static final int NAME = 0, COMPOSITIONS = 1, PALETTE = 2;
  
  static {
    ParameterTemplate[] templates = new ParameterTemplate[3];
    templates[NAME] = new ParameterTemplate("Bitmap");
    templates[COMPOSITIONS] = new ParameterTemplate();
    templates[PALETTE] = new ParameterTemplate("Palette", palettes, false);
    parameterTemplates.put(Bitmap.class, templates);
  }

  @Override
  public LinkedList<Element> getList() {
    return params[COMPOSITIONS].getList();
  }
  
  @Override
  public int update(int x0, int y0, Element parent) {
    return elementsColumn.addList(this, x0, y0);
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
  public void render(double[] colors, double[] pixels, double[] coords
      , int y0, int height) {
    if(hide) return;
    for(int index = 0; index < pixels.length; index++) pixels[index] = 0;
    for(Element composition : params[COMPOSITIONS].getList())
      composition.render(pixels, coords, y0, height);
    params[PALETTE].getPalette().render(pixels, colors);
  }
}
