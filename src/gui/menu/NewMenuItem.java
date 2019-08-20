package gui.menu;

import base.Main;
import static base.Main.refresh;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import structure.Element;

public class NewMenuItem extends MenuItem {
  public Class elementClass;
  public Element parent, afterElement;
  
  public NewMenuItem(String caption, Element container, Class elementClass
      , Element afterElement) {
    super(caption);
    this.parent = container;
    this.elementClass = elementClass;
    this.afterElement = afterElement;
  }
  
  public NewMenuItem(String caption, Element parent, int index
      , Class elementClass, Element afterElement) {
    this(caption, parent, elementClass, afterElement);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    try {
      Element element = (Element) elementClass.newInstance();
      element.init();
      Main.stopRender();
      LinkedList<Element> list = parent.getList();
      if(afterElement == null) {
        list.addFirst(element);
      } else {
        list.add(list.indexOf(afterElement) + 1, element);
      }
      refresh();
      Main.startRender();
    } catch (InstantiationException | IllegalAccessException ex) {
    }
  }
}
