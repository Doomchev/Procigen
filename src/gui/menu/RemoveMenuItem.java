package gui.menu;

import base.Main;
import java.awt.event.ActionEvent;
import structure.Element;

public class RemoveMenuItem extends MenuItem {
  Element element, parent;

  public RemoveMenuItem(Element parent, Element element) {
    super("Remove");
    this.parent = parent;
    this.element = element;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Main.changesThread = new Thread() {
      @Override
      public void run() {
        parent.getList().remove(element);
        Main.updateProject();
      }
    };
  }
}
