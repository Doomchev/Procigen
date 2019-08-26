package gui;

import base.Main;
import parameters.ParameterTemplate;
import gui.menu.AlterationMenuItem;
import javax.swing.JPopupMenu;
import structure.Element;
import structure.Palette;

public class PropertyBlock extends ElementBlock {
  public ParameterTemplate template;
  public int parameterIndex;
  
  public PropertyBlock(Element element, int index, int x, int y, int width
      , int height) {
    super(element, x, y, width, height, null);
    this.parameterIndex = index;
    this.template = parameterTemplates.get(element.getClass())[index];
  }
  
  @Override
  public boolean isSelected() {
    return this == selectedProperty;
  }

  @Override
  public boolean atPosition(int x0, int y0) {
    int x1 = x + propertiesColumn.x;
    int y1 = y + propertiesColumn.y;
    return x0 >= x1 && x0 < x1 + width && y0 >= y1 && y0 < y1 + height;
  }
  
  @Override
  public void onclick(int x0, int y0) {
    Element param = element.params[parameterIndex];
    Palette palette = param.getPalette();
    if(palette != null) { 
      selectedPalette = palette;
    } else {
      if(template.type != ParameterTemplate.DOUBLE) return;
      Main.selectedPalette = null;
      selectedProperty = this;
      scalePos = param.getDouble();
      propertiesPanel.repaint();
    }
    scalePanel.repaint();
  }

  @Override
  public void menu(int x0, int y0) {
    selectedProperty = null;
    if(currentMenu != null) currentMenu.setVisible(false);
    currentMenu = new JPopupMenu();
    if(template.type == ParameterTemplate.DOUBLE) {
      for(Element alteration : alterations)
        (new AlterationMenuItem(alteration.toString(), element, parameterIndex
            , alteration, template)).addTo(currentMenu);
    } else {
      element.params[parameterIndex].menu(template, x0, y0);
    }
    currentMenu.show(propertiesPanel, x0, y0);
  }

  @Override
  public String toString() {
    return template.caption + ": " + template.getText(element, parameterIndex);
  }
}
