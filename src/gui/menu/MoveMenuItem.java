package gui.menu;

import base.Main;
import base.Render;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import structure.Element;

public class MoveMenuItem extends MenuItem {
  public Element element, parent;
  public int direction;

  public MoveMenuItem(Element parent, Element element, int direction) {
    super(direction < 0 ? "Move up" : "Move down");
    this.parent = parent;
    this.element = element;
    this.direction = direction;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    LinkedList<Element> list = parent.getList();
    int elementIndex = list.indexOf(element) + direction;
    if(elementIndex < 0 || elementIndex >= list.size()) return;
    Render.stop();
    list.remove(element);
    list.add(elementIndex, element);
    Main.updateProject();
    Render.start();
  }

}
