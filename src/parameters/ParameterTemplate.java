package parameters;

import base.ID;
import base.Serialization;
import java.io.IOException;
import java.util.LinkedList;
import structure.Element;

public class ParameterTemplate extends Serialization {
  public static final int DOUBLE = 0, ELEMENT = 1, ENUM = 2, LIST = 3
      , STRING = 4, NEW_ELEMENT = 5;
  
  public int type, id;
  public String caption;
  public double initialValue, min = 0.0, max = 0.0, step = 0;
  public LinkedList<Element> elements = null;
  public String[] values = null;
  public boolean hidden = false;

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
    this.type = isNew ? NEW_ELEMENT : ELEMENT;
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
  
  public ParameterTemplate(String caption) {
    this.caption = caption;
    this.hidden = true;
    this.type = LIST;
  }
  
  public ParameterTemplate(String value, boolean hidden) {
    this.caption = value;
    this.type = STRING;
    this.hidden = hidden;
  }

  public Element createParameter() {
    switch(type) {
      case DOUBLE:
        return new DoubleValue(initialValue);
      case ELEMENT:
        return new ElementValue(elements.getFirst());
      case NEW_ELEMENT:
        return new ElementValue(elements.getFirst().createNew());
      case ENUM:
        return new EnumValue(0);
      case LIST:
        return new ListValue();
      case STRING:
        return new StringValue(caption);
    }
    System.err.println("Wrong parameter template "  + type);
    return null;
  }
  
  public boolean isDefault(Element element) {
    switch(type) {
      case DOUBLE:
        return element.getDouble() == initialValue;
      case ELEMENT:
        return element.getElement() == elements.getFirst();
      case ENUM:
        return element.getValue() == 0;
      case LIST:
        return element.getList().isEmpty();
      case STRING:
        return element.getName().equals(caption);
      default:
        return false;
    }
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
    if(max > min && value > max) value = max;
    if(max != min && value < min) value = min;
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
      case NEW_ELEMENT:
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

  public static void readValue(int type) throws IOException {
    switch(type) {
      case DOUBLE:
        if(readInt() == 0)
          reader.readDouble();
        else
          readInt();
      case LIST:
        int quantity = readInt();
        for(int index = 0; index < quantity; index++) readInt();
      case STRING:
        readString();
      default:
        readInt();
    }
  }

  public void write(Element param) throws IOException {
    writeInt(ID.get(caption, type).number);
    if(type == DOUBLE) {
      param.writeDouble();
    } else {
      param.write();
    }
  }

  @Override
  public String toString() {
    return caption;
  }
}
