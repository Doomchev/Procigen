package structure;

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
  
  public double getDouble() {
    return 0.0;
  }
  
  public void setDouble(double value) {
  }

  public int getInt() {
    return (int) Math.round(getDouble());
  }
  
  public Element getElement() {
    return null;
  }
  
  public int getValue() {
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
    Mapping mapping = Serialization.mappings.get(getClass());
    if(mapping != null) return mapping.templates;
    return parameterTemplates.get(getClass());
  }

  public ParameterTemplate getTemplate(int index) {
    return getTemplates()[index];
  }
  
  public void draw(Graphics g, int x, int y, int scrollY, int height) {
  }

  public void renderColors(RenderingBitmap bitmap) {
  }

  public void renderCoords(RenderingBitmap bitmap) {
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
      int type = getTemplate(index).type;
      if(type == ParameterTemplate.LIST || type == ParameterTemplate.STRING)
        continue;
      y0 = propertiesColumn.addBlock(this, index, x0, y0);
      y0 = params[index].updateProperties(x0 + blockIndent, y0);
    }
    return y0;
  }

  public void read() throws IOException {
    ParameterTemplate[] templates = getTemplates();
    params = new Element[templates.length];
    for(int index = 0; index < templates.length; index++) {
      System.out.println("::"  + templates[index].caption);
      params[index] = templates[index].read();
      System.out.println(":::: = " + params[index].toString());
    }
  }

  public void setFileIndex() {
    Info info = getInfo();
    if(info.elementExists.contains(this)) return;
    fileIndex = info.elements.size();
    info.elementExists.add(this);
    info.elements.add(this);
    elementsQuantity++;
    for(Element param : params) param.setFileIndex();
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
    for(int index = 0; index < templates.length; index++)
      templates[index].write(params[index]);
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
