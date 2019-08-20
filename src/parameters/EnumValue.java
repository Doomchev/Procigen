package parameters;

import static base.Main.currentMenu;
import gui.menu.EnumValueMenuItem;
import java.io.IOException;
import structure.Element;

public class EnumValue extends Element {
  public int value;
  
  public EnumValue(int value) {
    this.value = value;
  }
  
  @Override
  public int getValue() {
    return value;
  }  
  
  @Override
  public int updateProperties(int x0, int y0) {
    return y0;
  }
  
  @Override
  public void menu(ParameterTemplate template, int x0
      , int y0) {
    String[] values = template.values;
    for(int index = 0; index < values.length; index++)
      (new EnumValueMenuItem(values[index], this, index)).addTo(currentMenu);
  }
  
  @Override
  public void setFileIndex() {
  }

  @Override
  public void write() throws IOException {
    writeInt(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
