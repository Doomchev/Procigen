package structure;

import base.RenderingBitmap;
import gui.menu.FileMenuItem;
import gui.menu.NewMenuItem;
import gui.menu.RenderMenuItem;
import parameters.ParameterTemplate;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Project extends Element {
  public static Project instance;
  
  public static final int NAME = 0, ELEMENTS = 1, DURATION = 2;
  
  public RenderingBitmap[] bitmaps = new RenderingBitmap[threadsQuantity];
  
  static {
    ParameterTemplate[] templates = new ParameterTemplate[3];
    templates[NAME] = new ParameterTemplate("Project");
    templates[ELEMENTS] = new ParameterTemplate();
    templates[DURATION] = new ParameterTemplate("Duration", 5.0, 0.05, 0.0);
    parameterTemplates.put(Project.class, templates);
  }
  
  @Override
  public void init() {
    super.init();
    int y = 0;
    for(int index = 0; index < threadsQuantity; index++) {
      int y0 = Math.floorDiv(index * imageHeight, threadsQuantity);
      int y1 = Math.floorDiv((index + 1) * imageHeight, threadsQuantity);
      int height = y1 - y0;
      
      RenderingBitmap bitmap = new RenderingBitmap();
      BufferedImage image = new BufferedImage(imageWidth, height
            , BufferedImage.TYPE_INT_ARGB);
      images[index] = image;
      int imageSize = imageWidth * height;
      bitmap.initArrays(imageSize);
      bitmap.tempBitmap = bitmap.copy();
      bitmap.x = 0;
      bitmap.y = y;
      bitmap.width = imageWidth;
      bitmap.height = height;
      bitmap.size = imageSize;
      bitmap.size2 = imageSize * 2;
      bitmaps[index] = bitmap;
      y += height;
    }
  }

  @Override
  public LinkedList<Element> getList() {
    return params[ELEMENTS].getList();
  }

  @Override
  public void menu(int x, int y, Element parent) {
    showMenu(x, y
        , new NewMenuItem("Add new bitmap", this, ELEMENTS, Bitmap.class, null)
        , new FileMenuItem("Open", FileMenuItem.OPEN)
        , new FileMenuItem("Save as", FileMenuItem.SAVE_AS)
        , new RenderMenuItem(512, 512, true)
        , new RenderMenuItem(1920, 1080, true)
        , new RenderMenuItem(4096, 2160, false));
  }
  
  public synchronized void render(int thread) {
    RenderingBitmap bitmap = bitmaps[thread];
    double colors[] = bitmap.colors;
    BufferedImage image = images[thread];
    
    for(Element element : params[ELEMENTS].getList()) element.renderColors(bitmap);
    int colorIndex = 0;
    for(int index = 0; index < bitmap.size; index++) {
      int r = (int) Math.round(colors[colorIndex]);
      int g = (int) Math.round(colors[colorIndex + 1]);
      int b = (int) Math.round(colors[colorIndex + 2]);
      image.setRGB(index % bitmap.width, Math.floorDiv(index, bitmap.width)
          , b + (g << 8) + (r << 16) + (255 << 24));
      colorIndex += 3;
    }
  }
}
