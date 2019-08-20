package parameters;

import java.io.IOException;
import structure.Element;

public class StringValue extends Element {
  public String value;

  public StringValue(String value) {
    this.value = value;
  }
  
  @Override
  public int updateProperties(int x0, int y0) {
    return y0;
  }

  @Override
  public Element createNew() {
    return new StringValue(value);
  }
  
  @Override
  public void setFileIndex() {
  }

  @Override
  public void write() throws IOException{
    writeString(value);
  }

  @Override
  public String toString() {
    return value;
  }
}
