package parameters;

import static base.Serialization.writeInt;
import java.io.IOException;
import java.util.LinkedList;
import structure.Element;

public class ListValue extends Element {
  public final LinkedList<Element> list = new LinkedList<>();

  @Override
  public LinkedList<Element> getList() {
    return list;
  }
  
  @Override
  public int updateProperties(int x0, int y0) {
    return y0;
  }
  
  @Override
  public void setFileIndex() {
    for(Element element : list) element.setFileIndex();
  }

  @Override
  public void write() throws IOException{
    writeInt(list.size());
    for(Element element : list) writeInt(element.fileIndex);
  }
}
