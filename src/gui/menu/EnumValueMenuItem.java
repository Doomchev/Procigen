package gui.menu;

import base.Main;
import java.awt.event.ActionEvent;
import parameters.EnumValue;

public class EnumValueMenuItem extends MenuItem {
  public EnumValue parameter;
  public int value;
  
  public EnumValueMenuItem(String caption, EnumValue parameter, int value) {
    super(caption);
    this.parameter = parameter;
    this.value = value;
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    parameter.value = value;
    Main.propertiesPanel.repaint();
  }
}
