package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import structure.Element;
import static structure.Element.blockHeight;
import static structure.Element.blockIndent;

public class Column extends GUIContainer {
  public Column() {
  }

  public Column(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }
  
  @Override
  public void draw(Graphics g, int x0, int y0) {
    g.setColor(Color.WHITE);
    g.fillRect(x, y, width, height);
    x0 += x;
    y0 += y;
    for(ElementBlock block : blocks) {
      if(block.y >= scrollY + height) break;
      if(block.y + block.height >= scrollY) {
        block.draw(g, x0 + scrollX, y0 + scrollY);
      }
    }
  }
  
  public int addBlock(Element element, int x0, int y0, Element parent) {
    blocks.add(new ElementBlock(element, x0, y0, width - x0, blockHeight
        , parent));
    return y0 + blockHeight;
  }
  
  public int addBlock(Element element, int index, int x0, int y0) {
    blocks.add(new PropertyBlock(element, index, x0, y0, width - x0
        , blockHeight));
    return y0 + blockHeight;
  }

  public int addList(Element element, Element parent, LinkedList<Element> list
      , int x0, int y0) {
    addBlock(element, x0, y0, parent);
    x0 += blockIndent;
    y0 += blockHeight;
    for(Element item: list) y0 = item.update(x0 + blockIndent, y0, element);
    return y0;
  }
  
  public int addList(Element element, Element parent, int x0, int y0) {
    return addList(element, parent, element.getList(), x0, y0);
  }
}
