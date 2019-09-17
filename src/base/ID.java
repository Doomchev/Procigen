package base;

import java.util.HashMap;
import java.util.LinkedList;

public class ID {
  public static final HashMap<String, ID> map = new HashMap<>();
  public static final LinkedList<Type> list = new LinkedList<>();

  public static class Type {
    String id;
    int type;

    public Type(String id, int type) {
      this.id = id;
      this.type = type;
    }
  }
  
  public static void clear() {
    map.clear();
    list.clear();
  }
  
  public static ID create(String string, int type) {
    ID id = new ID(string, list.size());
    map.put(string, id);
    list.add(new Type(string, type));
    return id;
  }
  
  public static ID get(String string, int type) {
    ID id = map.get(string);
    if(id == null) return create(string, type); else return id;
  }
  
  public String caption;
  public int number;

  public ID(String caption, int number) {
    this.caption = caption;
    this.number = number;
  }
}
