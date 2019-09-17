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
import structure.compositions.SingleComposition;

public class Serialization extends Main {
  private static final int DOUBLE = -1, ELEMENT = -2, ENUM = -3, LIST = -4
      , STRING = -5;

  public static final HashMap<Class, Mapping> mappings = new HashMap<>();
  public static DataOutputStream writer;
  public static DataInputStream reader;
  public static int elementsQuantity;
  public static Element[] allElements;
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
  
  public static class Mapping {
    public ParameterTemplate[] templates;
    public int[] maps;

    public Mapping(ParameterTemplate[] templates, int[] maps) {
      this.templates = templates;
      this.maps = maps;
    }
  }
  
  public static void fill(Class elementClass, int ... params) {
    Mapping mapping = mappings.get(elementClass);
    if(mapping != null) {
      int[] maps = mapping.maps;
      int quantity = maps.length;
      int[] array = new int[quantity];
      System.arraycopy(maps, 0, array, 0, quantity);
      for(int index = 0; index < quantity; index++) {
        int index2 = array[index];
        if(index2 >= 0) maps[index] = params[index2];
      }
    } else {
      mappings.put(elementClass, new Mapping(null, params));
    }
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
      
      if(!fileVersion.lessThan(new Version(0, 9))) {
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
        
        for(int index = 0; index < elementsQuantity; index++)
          allElements[index].read(true);
        
        return;
      } else {

        if(fileVersion.lessThan(new Version(0, 6, 2))) {
          fill(CompositionArray.class, 0, 1, 2, 3, 4, 5, 7, 8, 9, 10, 11);
        }
        if(fileVersion.lessThan(new Version(0, 6, 3))) {
          fill(LinearAlteration.class, 0, 1, 3);
          fill(SineAlteration.class, 0, 1, 3);
          fill(ChainedLinearAlteration.class, 0, 1);
          fill(ChainedSineAlteration.class, 0, 1, 3);
        }
        if(fileVersion.lessThan(new Version(0, 7, 1))) {
          fill(ChainedLinearAlteration.class, 0, 1, 2);
          fill(ChainedSineAlteration.class, 0, 1, 2, 3);
        }
        if(fileVersion.lessThan(new Version(0, 8))) {
          fill(CompositionArray.class, 0, 1, 2, 3, 6, 7, 8, 9, 16, 17, 18, 19);
          fill(SingleComposition.class, 0, 1, 2, 3, 6, 7, 8, 9, 16, 17);
        }

        for(Entry<Class, Mapping> entry : mappings.entrySet()) {
          Class elementClass = entry.getKey();
          Mapping mapping = entry.getValue();
          int[] params = mapping.maps;
          int quantity = params.length;
          ParameterTemplate[] newTemplates = parameterTemplates.get(elementClass);
          ParameterTemplate[] templates = new ParameterTemplate[quantity];
          for(int index = 0; index < params.length; index++) {
            int mapIndex = params[index];
            if(mapIndex >= newTemplates.length) {
              System.err.println(elementClass.getSimpleName() + " < " + mapIndex);
            } else if(mapIndex >= 0) {
              templates[index] = newTemplates[mapIndex];
            } else {
              templates[index] = new ParameterTemplate(mapIndex);
            }
          }
          mapping.templates = templates;
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
          element.read(false);
          Mapping mapping = mappings.get(element.getClass());
          if(mapping != null) {
            int[] map = mapping.maps;
            ParameterTemplate[] newTemplates = parameterTemplates.get(
                element.getClass());
            Element[] params = new Element[newTemplates.length];
            for(int index2 = 0; index2 < map.length; index2++) {
              int mapIndex = map[index2];
              if(mapIndex >= 0) params[mapIndex] = element.params[index2];
            }
            for(int index2 = 0; index2 < params.length; index2++) {
              if(params[index2] == null)
                params[index2] = newTemplates[index2].createParameter();
            }
            element.params = params;
          }
        }
        mappings.clear();
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
