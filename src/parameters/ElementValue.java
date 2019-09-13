package parameters;

import static base.Main.currentMenu;
import gui.menu.ElementValueMenuItem;
import java.io.IOException;
import structure.Element;

public class ElementValue extends Element {
  public Element element;
  
  public ElementValue(Element element) {
    this.element = element;
  }
  
  @Override
  public Element getElement() {
    return element;
  }  
  
  @Override
  public int updateProperties(int x0, int y0) {
    return element.updateProperties(x0, y0);
  }
  
  @Override
  public void menu(ParameterTemplate template, int x0
      , int y0) {
    for(Element value : template.elements) 
      (new ElementValueMenuItem(this, value)).addTo(currentMenu);
  }
  
  @Override
  public void setFileIndex() {
    element.setFileIndex();
  }

  @Override
  public void write() throws IOException{
    writeInt(element.fileIndex);
  }
  
  @Override
  public String toString() {
    return element.toString();
  }
}
