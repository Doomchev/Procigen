package structure;

import base.ID;
import base.RenderingBitmap;
import base.Serialization;
import base.Serialization.Info;
import parameters.ParameterTemplate;
import java.awt.Graphics;
import java.io.IOException;
import java.util.LinkedList;
import structure.patterns.Pattern;

public abstract class Element extends Serialization implements Cloneable {
  public Element[] params = null;
  public int fileIndex = -1;
  public boolean hide = false;
  
  public void init() {
    ParameterTemplate[] template = parameterTemplates.get(getClass());
    if(template == null) return;
    if(params != null) return;
    params = new Element[template.length];
    for(int index = 0; index < template.length; index++)
      params[index] = template[index].createParameter();
  }

  public void setTemplate(ParameterTemplate template) {
  }
  
  public double getDouble(RenderingBitmap bitmap) {
    return 0.0;
  }
  
  public void setDouble(double value) {
  }

  public int getInt(RenderingBitmap bitmap) {
    return (int) Math.round(getDouble(bitmap));
  }
  
  public Element getElement() {
    return null;
  }
  
  public int getValue() {
    return 0;
  }

  public boolean isValue() {
    return false;
  }
  
  public double getRnd(int increment) {
    return 0;
  }

  public LinkedList<Element> getList() {
    return null;
  }
  
  public Pattern getPattern() {
    return (Pattern) getElement();
  }

  public Palette getPalette() {
    return (Palette) getElement();
  }

  public String getName() {
    return "";
  }

  public void rename(String showInputDialog) {
  }
  
  public ParameterTemplate[] getTemplates() {
    return parameterTemplates.get(getClass());
  }

  public ParameterTemplate getTemplate(int index) {
    return getTemplates()[index];
  }
  
  public void draw(Graphics g, int x, int y, int scrollY, int height) {
  }

  public void render(RenderingBitmap bitmap) {
  }

  public void applyTransformation(RenderingBitmap bitmap) {
  }

  public int update(int x0, int y0, Element parent) {
    return y0;
  }

  public void toggle() {
    hide = !hide;
  }

  public void menu(int x, int y, Element parent) {
  }
  
  public Element createNew() {
    try {
      Element element = getClass().newInstance();
      element.init();
      return element;
    } catch (InstantiationException | IllegalAccessException ex) {
      return null;
    }
  }

  public void menu(ParameterTemplate template, int x0, int y0) {
  }

  public int updateProperties(int x0, int y0) {
    for(int index = 0; index < params.length; index++) {
      if(getTemplate(index).hidden) continue;
      y0 = propertiesColumn.addBlock(this, index, x0, y0);
      y0 = params[index].updateProperties(x0 + blockIndent, y0);
    }
    return y0;
  }
  
  public boolean isStandard() {
    return true;
  }

  public void read() throws IOException {
  }

  public void setFileIndex() {
    Info info = getInfo();
    if(info.elementExists.contains(this)) return;
    fileIndex = info.elements.size();
    info.elementExists.add(this);
    info.elements.add(this);
    elementsQuantity++;
    ParameterTemplate[] templates = getTemplates();
    for(int index = 0; index < params.length; index++) {
      Element param = params[index];
      ParameterTemplate template = templates[index];
      if(!template.isDefault(param)) ID.get(template.caption, template.type);
      param.setFileIndex();
    }
  }
  
  public Info getInfo() {
    Info info = infos.get(getClass());
    if(info == null) {
      info = new Info();
      infos.put(getClass(), info);
    }
    return info;
  }

  public void write() throws IOException {
    ParameterTemplate[] templates = getTemplates();
    int count = 0;
    for(int index = 0; index < templates.length; index++) {
      if(!templates[index].isDefault(params[index])) count++;
    }
    writeInt(count);
    for(int index = 0; index < templates.length; index++) {
      Element param = params[index];
      ParameterTemplate template = templates[index];
      if(!template.isDefault(param))
        template.write(param);
    }
  }

  public void writeDouble() throws IOException {
    writeInt(1);
    writeInt(fileIndex);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
