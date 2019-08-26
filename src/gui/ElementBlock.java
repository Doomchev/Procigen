package gui;

import base.Main;
import java.awt.Color;
import java.awt.Graphics;
import structure.Element;
import structure.Palette;

public class ElementBlock extends GUIElement {
  public Element element, parent;

  public ElementBlock(Element element, int x, int y, int width, int height
      , Element parent) {
    this.element = element;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.parent = parent;
  }

  public boolean atPosition(int x0, int y0) {
    return x0 >= x && x0 < x + width && y0 >= y && y0 < y + height;
  }
  
  public boolean isSelected() {
    return element == Main.selectedElement;
  }

  public void onclick(int x0, int y0) {
    Palette palette = element.getPalette();
    selectedElement = element;
    if(palette != null) {
      selectedPalette = palette;
      scalePanel.repaint();
    } else {
      selectedProperty = null;
      updateProperties();
    }
    elementsPanel.repaint();
  }

  public void menu(int x, int y) {
    element.menu(x, y, parent);
  }

  @Override
  public void draw(Graphics g, int x0, int y0) {
    int gr = isSelected() ? 192 : 224;
    x0 += x;
    y0 += y;
    g.setColor(new Color(gr, gr, 224, 255));
    g.fillRect(x0, y0, width, height);
    g.setColor(Color.BLACK);
    g.drawString(toString(), x0 + 2, y0 + Main.fm.getHeight());
  }

  @Override
  public String toString() {
    return element.toString();
  }
}
