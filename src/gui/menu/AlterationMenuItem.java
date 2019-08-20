package gui.menu;

import base.Main;
import parameters.ParameterTemplate;
import java.awt.event.ActionEvent;
import structure.Element;

public class AlterationMenuItem extends MenuItem {
  public Element element, value;
  public int parameterIndex;
  public ParameterTemplate template;

  public AlterationMenuItem(String caption, Element element, int parameterIndex
      , Element value, ParameterTemplate template) {
    super(caption);
    this.element = element;
    this.value = value;
    this.parameterIndex = parameterIndex;
    this.template = template;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Element newValue = value.createNew();
    newValue.setTemplate(template);
    Main.stopRender();
    element.params[parameterIndex] = newValue;
    Main.updateProperties();
    Main.startRender();
  }
}
