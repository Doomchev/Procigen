package gui.menu;

import java.awt.event.ActionEvent;
import structure.Element;

public class ToggleMenuItem extends MenuItem {
  Element element;

  public ToggleMenuItem(Element element) {
    super(element.hide ? "Show" : "Hide");
    this.element = element;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    element.toggle();
  }
}
