package parameters;

import java.io.IOException;
import structure.Element;

public class DoubleValue extends Element {
  double value;
  
  public DoubleValue(double value) {
    this.value = value;
  }
  
  @Override
  public double getDouble() {
    return value;
  }
  
  @Override
  public void setDouble(double value) {
    this.value = value;
  }

  @Override
  public void setTemplate(ParameterTemplate template) {
    value = template.limit(value);
  }
  
  @Override
  public int updateProperties(int x0, int y0) {
    return y0;
  }

  @Override
  public Element createNew() {
    return new DoubleValue(0.0);
  }
  
  @Override
  public void setFileIndex() {
  }

  @Override
  public void write() throws IOException {
    writer.writeDouble(value);
  }

  @Override
  public void writeDouble() throws IOException {
    writeInt(0);
    writer.writeDouble(value);
  }

  @Override
  public String toString() {
    return shorten(value);
  }
}
