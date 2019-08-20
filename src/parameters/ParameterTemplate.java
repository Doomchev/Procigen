package parameters;

import base.Serialization;
import java.io.IOException;
import java.util.LinkedList;
import structure.Element;

public class ParameterTemplate extends Serialization {
  public static final int DOUBLE = 0, ELEMENT = 1, ENUM = 2, LIST = 3, STRING = 4;
  
  public int type;
  public String caption;
  public double initialValue, min = 0.0, max = 0.0, step = 0;
  public LinkedList<Element> elements = null;
  public String[] values = null;

  public ParameterTemplate(String caption, double initialValue, double min
      , double max, double step) {
    this.caption = caption;
    this.initialValue = initialValue;
    this.min = min;
    this.max = max;
    this.step = step;
    this.type = DOUBLE;
  }
  
  public ParameterTemplate(String caption, double initialValue, double min
      , double max) {
    this(caption, initialValue, min, max, 0);
  }
  
  public ParameterTemplate(String caption, double initialValue) {
    this.caption = caption;
    this.initialValue = initialValue;
    this.type = DOUBLE;
  }
  
  public ParameterTemplate(String caption, LinkedList<Element> elements
      , boolean isNew) {
    this.caption = caption;
    this.elements = elements;
    this.type = isNew ? ELEMENT : ELEMENT;
  }
  
  public ParameterTemplate(String caption, String... values) {
    this.caption = caption;
    this.values = values;
    this.type = ENUM;
  }
  
  public ParameterTemplate(int type) {
    this.caption = "";
    this.type = -1 - type;
  }
  
  public ParameterTemplate() {
    this.caption = "";
    this.type = LIST;
  }
  
  public ParameterTemplate(String value) {
    this.caption = value;
    this.type = STRING;
  }

  public Element createParameter() {
    switch(type) {
      case DOUBLE:
        return new DoubleValue(initialValue);
      case ELEMENT:
        return new ElementValue(elements.getFirst());
      case ENUM:
        return new EnumValue(0);
      case LIST:
        return new ListValue();
      case STRING:
        return new StringValue(caption);
    }
    System.err.println("Wrong parameter template.");
    return null;
  }

  public String getText(Element element, int parameterIndex) {
    if(type == ENUM) {
      return values[element.params[parameterIndex].getValue()];
    } else {
      return element.params[parameterIndex].toString();
    }
  }

  public double limit(double value) {
    if(step != 0.0) value = Math.round(value * step) / step;
    if(max != min && value < min) value = min;
    if(max > min && value > max) value = max;
    return value;
  }

  public Element read() throws IOException {
    switch(type) {
      case DOUBLE:
        if(readInt() == 0) return new DoubleValue(reader.readDouble());
        Element element = allElements[readInt()];
        element.setTemplate(this);
        return element;
      case ELEMENT:
        return new ElementValue(allElements[readInt()]);
      case ENUM:
        return new EnumValue(readInt());
      case LIST:
        ListValue value = new ListValue();
        int quantity = readInt();
        for(int index = 0; index < quantity; index++)
          value.list.add(allElements[readInt()]);
        return value;
      case STRING:
        return new StringValue(readString());
    }
    System.err.println("Wrong parameter template.");
    return null;
  }

  public void write(Element param) throws IOException {
    if(type == DOUBLE) {
      param.writeDouble();
    } else {
      param.write();
    }
  }
}
