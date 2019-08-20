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
import parameters.ParameterTemplate;
import structure.Element;
import structure.Project;

public class Serialization extends Main {
  private static final int DOUBLE = -1, ELEMENT = -2, ENUM = -3, LIST = -4
      , STRING = -5;

  public static final HashMap<Class, ParameterTemplate[]> oldTemplates = new HashMap<>();
  public static final HashMap<Class, int[]> templateMapping = new HashMap<>();
  public static DataOutputStream writer;
  public static DataInputStream reader;
  public static int elementsQuantity;
  public static Element[] allElements;
  public static final HashMap<Class, Info> infos = new HashMap<>();
  //public static final HashMap<String, Info>
  
  public static class Info {
    public final HashSet<Element> elementExists = new HashSet<>();
    public final LinkedList<Element> elements = new LinkedList<>();
  }
  
  public static void fill(Class elementClass, int quantity, int ... params) {
    ParameterTemplate[] newTemplates = parameterTemplates.get(elementClass);
    ParameterTemplate[] templates = new ParameterTemplate[quantity];
    int[] map = new int[newTemplates.length];
    for(int index = 0; index < map.length; index++) map[index] = -1;
    for(int index = 0; index < quantity; index++) {
      int mapIndex = params[index];
      if(mapIndex >= 0) {
        templates[index] = newTemplates[mapIndex];
        map[mapIndex] = index;
      } else {
        templates[index] = new ParameterTemplate(mapIndex);
      }
    }
    templateMapping.put(elementClass, map);
    oldTemplates.put(elementClass, templates);
  }
      
  public static void load(File file) {
    /*ParameterTemplate[] newTemplates = Main.parameterTemplates.get(Project.class);
    ParameterTemplate[] templates = new ParameterTemplate[2];
    templates[0] = newTemplates[0];
    templates[1] = newTemplates[1];
    oldTemplates.put(Project.class, templates);*/
    
    stopRender();
    try {
      reader = new DataInputStream(new FileInputStream(file));
      
      Version fileVersion = Version.read();
      
      elementsQuantity = readInt();
      allElements = new Element[elementsQuantity];
      int classesQuantity = readInt();
      int elementIndex = 0;
      for(int index = 0; index < classesQuantity; index++) {
        Class elementClass = Class.forName(readString());
        int classInstancesQuantity = readInt();
        for(int index2 = 0; index2 < classInstancesQuantity; index2++) {
          allElements[elementIndex] = (Element) elementClass.newInstance();
          elementIndex++;
        }
        if(elementClass == Project.class)
          project = (Project) allElements[elementIndex - 1];
      }
      for(int index = 0; index < elementsQuantity; index++) {
        Element element = allElements[index];
        element.read();
        ParameterTemplate[] templates = oldTemplates.get(element.getClass());
        if(templates != null) {
          int[] map = templateMapping.get(element.getClass());
          Element[] params = new Element[templates.length];
          for(int paramIndex = 0; paramIndex < templates.length; paramIndex++) {
            int mapIndex = map[paramIndex];
            if(mapIndex < 0) {
              params[paramIndex] = templates[paramIndex].createParameter();
            } else {
              params[paramIndex] = element.params[mapIndex];
            }
          }
          element.params = params;
        }
      }
      
      reader.close();
      project.init();
      
      /*Element[] params = project.params;
      project.params = new Element[3];
      project.params[0] = params[0];
      project.params[1] = params[1];
      project.params[2] = new DoubleValue(5.0);
      oldTemplates = null;*/
      
      updateProject();
      selectedElement = null;
      updateProperties();
    } catch (FileNotFoundException ex) {
      System.out.println(ex.toString());
    } catch (IOException | ClassNotFoundException | InstantiationException
        | IllegalAccessException ex) {
      System.out.println(ex.toString());
    }
    startRender();
  }
  
  public static void save(File file) {
    try {
      writer = new DataOutputStream(new FileOutputStream(file));
      currentVersion.write();
      infos.clear();
      elementsQuantity = 0;
      Main.project.setFileIndex();
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
