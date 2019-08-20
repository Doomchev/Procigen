package gui.menu;

import base.Main;
import static base.Main.refresh;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import structure.Element;

public class CloneMenuItem extends MenuItem {
  public LinkedList<Element> container;
  public Element element, afterElement;
  
  public CloneMenuItem(String caption, LinkedList<Element> container
      , Element element, Element afterElement) {
    super(caption);
    this.container = container;
    this.element = element;
    this.afterElement = afterElement;
  }
  
  public CloneMenuItem(String caption, Element container, Element element
      , Element afterElement) {
    this(caption, container.getList(), element, afterElement);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Element newElement = (Element) element.createNew();
    Main.stopRender();
    if(afterElement == null) {
      container.addFirst(newElement);
    } else {
      container.add(container.indexOf(afterElement) + 1, newElement);
    }
    refresh();
    Main.startRender();
  }
}
