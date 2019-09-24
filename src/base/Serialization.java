package base;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import parameters.ListValue;
import parameters.ParameterTemplate;
import structure.Element;
import structure.Options;
import structure.Project;
import structure.compositions.CompositionArray;

public class Serialization extends Main {
  public static DataOutputStream writer;
  public static DataInputStream reader;
  public static int elementsQuantity;
  public static Element[] allElements;
  public static Node[] allNodes;
  public static final HashMap<Class, Info> infos = new HashMap<>();
  public static String[] ids;
  public static int[] types;
  //public static final HashMap<String, Info>

  public static class Type {
    String id;
    int type;
  }
  
  public static class Info {
    public final HashSet<Element> elementExists = new HashSet<>();
    public final LinkedList<Element> elements = new LinkedList<>();
  }
  
  public static class Node {
    public Class nodeClass;
    public final LinkedList<Field> fields = new LinkedList<>();

    public Node(Class nodeClass) {
      this.nodeClass = nodeClass;
    }
  }
  
  public static class Field {
    public int idIndex;
    public Element value;

    public Field(int idIndex, Element value) {
      this.idIndex = idIndex;
      this.value = value;
    }
  }
  
  public static int getID(String id) {
    int length = ids.length;
    for(int index = 0; index < length; index++) 
      if(ids[index].equals(id)) return index;
    String[] newIds = new String[length + 1];
    System.arraycopy(ids, 0, newIds, 0, length);
    newIds[length] = id;
    ids = newIds;
    return length;
  }
  
  public static Element getField(Element element, int idIndex) {
    for(int index = 0; index < allElements.length; index++)
      if(allElements[index] == element)
        for(Field field : allNodes[index].fields)
          if(field.idIndex == idIndex) return field.value;
    return null;
  }
  
  public static int findIndex(Element element) {
    for(int index = 0; index < allElements.length; index++)
      if(allElements[index] == element) return index;
    return -1;
  }
      
  public static void load(File file, boolean isProject) {
    try {
      reader = new DataInputStream(new FileInputStream(file));
      
      Version fileVersion = Version.read();
      
      ID.clear();
      int idsQuantity = readInt();
      types = new int[idsQuantity];
      ids = new String[idsQuantity];
      for(int index = 0; index < idsQuantity; index++) {
        ids[index] = readString();
        types[index] = readInt();
      }

      elementsQuantity = readInt();
      allElements = new Element[elementsQuantity];
      allNodes = new Node[elementsQuantity];
      int classesQuantity = readInt();
      int elementIndex = 0;
      for(int index = 0; index < classesQuantity; index++) {
        Class elementClass = Class.forName(readString());
        int classInstancesQuantity = readInt();
        for(int index2 = 0; index2 < classInstancesQuantity; index2++) {
          allElements[elementIndex] = (Element) elementClass.newInstance();
          allNodes[elementIndex] = new Node(elementClass);
          elementIndex++;
        }
        if(isProject) {
          if(elementClass == Project.class)
            Project.instance = (Project) allElements[elementIndex - 1];
        } else if(elementClass == Options.class) {
          Options.instance = (Options) allElements[elementIndex - 1];
        }
      }
      
      for(int index = 0; index < elementsQuantity; index++) {
        Element element = allElements[index];
        if(element.isStandard()) {
          Node node = allNodes[index];
          int fieldsQuantity = readInt();
          for(int fieldIndex = 0; fieldIndex < fieldsQuantity; fieldIndex++) {
            int idIndex = readInt();
            Element value = ParameterTemplate.readValue(types[idIndex]);
            node.fields.add(new Field(idIndex, value));
          }
        } else {
          element.read();
        }
      }
      
      if(fileVersion.lessThan(new Version(0, 10))) {
        int palettes = getID("Patterns");
        int pattern = getID("Pattern");
        int transformations = getID("Transformations");
        for(int index = 0; index < elementsQuantity; index++) {
          Element element = allElements[index];
          if(element.getClass() == CompositionArray.class) {
            ListValue list = new ListValue();
            Element patternField = getField(element, pattern).getElement();
            list.list.add(patternField);
            allNodes[index].fields.add(new Field(palettes, list));
            allNodes[findIndex(patternField)].fields.add(
                new Field(transformations, getField(element, transformations)));
          }
        }
      }
      
      for(int index = 0; index < elementsQuantity; index++) {
        Node node = allNodes[index];
        Element element = allElements[index];
        if(!element.isStandard()) continue;
        ParameterTemplate[] templates = element.getTemplates();
        element.params = new Element[templates.length];
        Element[] params = element.params;
        for(Field field : node.fields) {
          String id = ids[field.idIndex];
          for(int index2 = 0; index2 < templates.length; index2++) {
            ParameterTemplate template = templates[index2];
            if(template.caption.equals(id)) {
              params[index2] = field.value;
              break;
            }
          }
        }
        for(int index2 = 0; index2 < templates.length; index2++)
          if(params[index2] == null)
            params[index2] = templates[index2].createParameter();
      }

      reader.close();      
    } catch (FileNotFoundException ex) {
      System.out.println(ex.toString());
    } catch (IOException | ClassNotFoundException | InstantiationException
        | IllegalAccessException ex) {
      System.out.println(ex.toString());
    }
  }
  
  public static void save(File file, Element parent) {
    try {
      writer = new DataOutputStream(new FileOutputStream(file));
      currentVersion.write();
      
      infos.clear();
      ID.clear();
      elementsQuantity = 0;
      parent.setFileIndex();
      
      writeInt(ID.list.size());
      for(ID.Type type : ID.list) {
        writeString(type.id);
        writeInt(type.type);
      }
      
      int delta = 0;
      writeInt(elementsQuantity);
      writeInt(infos.size());
      for(Entry<Class, Info> entry : infos.entrySet()) {
        Info info = entry.getValue();
        for(Element element : info.elements) element.fileIndex += delta;
        delta += info.elements.size();
        writeString(entry.getKey().getName());
        writeInt(info.elements.size());
      }
      for(Entry<Class, Info> entry : infos.entrySet())
        for(Element element : entry.getValue().elements) element.write();
      writer.close();
    } catch (IOException ex) {
    }
  }
  
  public static int readInt() throws IOException {
    int num = 0;
    while(true) {
      int byteNum = reader.readUnsignedByte();
      boolean ending = byteNum < 128;
      if(ending) {
        return num + byteNum;
      } else {
        num = (num + (byteNum & 127)) << 7;
      }
    }
  }
  
  public static final byte firstByte = (byte) 127;
  
  public static void writeInt(int value) throws IOException {
    while(true) {
      int byteValue = (byte) value & 127;
      value = value >> 7;
      if(value > 0) byteValue += firstByte;
      writer.writeByte(byteValue);
      if(value == 0) return;
    }
  }
  
  public static String readString() throws IOException {
    int charsQuanity = readInt();
    String string = "";
    for(int charPos = 0; charPos < charsQuanity; charPos++)
      string += (char) readInt();
    return string;
  }
  
  public static void writeString(String string) throws IOException {
    writeInt(string.length());
    for(int charPos = 0; charPos < string.length(); charPos++)
      writeInt(string.charAt(charPos));
  }
}
