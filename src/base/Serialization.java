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
import structure.Options;
import structure.Project;
import structure.alterations.ChainedLinearAlteration;
import structure.alterations.ChainedSineAlteration;
import structure.alterations.LinearAlteration;
import structure.alterations.SineAlteration;
import structure.compositions.CompositionArray;

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
      if(mapIndex >= newTemplates.length) {
        System.err.println(elementClass.getSimpleName() + " < " + mapIndex);
      } else if(mapIndex >= 0) {
        templates[index] = newTemplates[mapIndex];
        map[mapIndex] = index;
      } else {
        templates[index] = new ParameterTemplate(mapIndex);
      }
    }
    templateMapping.put(elementClass, map);
    oldTemplates.put(elementClass, templates);
  }
      
  public static void load(File file, boolean isProject) {
    /*ParameterTemplate[] newTemplates = Main.parameterTemplates.get(Project.class);
    ParameterTemplate[] templates = new ParameterTemplate[2];
    templates[0] = newTemplates[0];
    templates[1] = newTemplates[1];
    oldTemplates.put(Project.class, templates);*/
    
    try {
      reader = new DataInputStream(new FileInputStream(file));
      
      Version fileVersion = Version.read();
      
      if(fileVersion.lessThan(new Version(0, 6, 2))) {
        fill(CompositionArray.class, 11, 0, 1, 2, 3, 4, 5, 7, 8, 9, 10, 11, DOUBLE);
      }
      if(fileVersion.lessThan(new Version(0, 6, 3))) {
        fill(LinearAlteration.class, 3, 0, 1, 3);
        fill(SineAlteration.class, 3, 0, 1, 3);
        fill(ChainedLinearAlteration.class, 2, 0, 1);
        fill(ChainedSineAlteration.class, 3, 0, 1, 3);
      }
      
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
        if(isProject) {
          if(elementClass == Project.class)
            Project.instance = (Project) allElements[elementIndex - 1];
        } else if(elementClass == Options.class) {
          Options.instance = (Options) allElements[elementIndex - 1];
        }
      }
      for(int index = 0; index < elementsQuantity; index++) {
        Element element = allElements[index];
        element.read();
        ParameterTemplate[] templates = oldTemplates.get(element.getClass());
        if(templates != null) {
          ParameterTemplate[] newTemplates = parameterTemplates.get(
              element.getClass());
          int[] map = templateMapping.get(element.getClass());
          Element[] params = new Element[newTemplates.length];
          for(int paramIndex = 0; paramIndex < newTemplates.length; paramIndex++) {
            int mapIndex = map[paramIndex];
            if(mapIndex < 0) {
              params[paramIndex] = newTemplates[paramIndex].createParameter();
            } else {
              params[paramIndex] = element.params[mapIndex];
            }
          }
          element.params = params;
        }
      }
      
      oldTemplates.clear();
      reader.close();
      
      if(isProject) {
        stopRender();
        Project.instance.init();
        updateProject();
        selectedElement = null;
        updateProperties();
        startRender();
      }
      
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
      elementsQuantity = 0;
      parent.setFileIndex();
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
