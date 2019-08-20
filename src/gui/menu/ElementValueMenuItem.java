package gui.menu;

import base.Main;
import java.awt.event.ActionEvent;
import parameters.ElementValue;
import structure.Element;

public class ElementValueMenuItem extends MenuItem {
  public ElementValue parameter;
  public Element element;

  public ElementValueMenuItem(ElementValue parameter, Element element) {
    super(element.toString());
    this.parameter = parameter;
    this.element = element;
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    parameter.element = element.createNew();
    Main.updateProperties();
    Main.propertiesPanel.repaint();
  }
}
