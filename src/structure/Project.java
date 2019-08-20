package structure;

import gui.menu.FileMenuItem;
import gui.menu.NewMenuItem;
import gui.menu.RenderMenuItem;
import parameters.ParameterTemplate;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Project extends Element {
  public static final int NAME = 0, ELEMENTS = 1, DURATION = 2;
  
  public double[][] threadColors = new double[threadsQuantity][]
      , threadCoords = new double[threadsQuantity][]
      , threadPixels = new double[threadsQuantity][];
  public int[] topY = new int[threadsQuantity];
  
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
      BufferedImage image = new BufferedImage(imageWidth, y1 - y0
            , BufferedImage.TYPE_INT_ARGB);
      images[index] = image;
      int imageSize = image.getWidth() * image.getHeight();
      threadColors[index] = new double[imageSize * 3];
      threadCoords[index] = new double[imageSize * 2];
      threadPixels[index] = new double[imageSize];
      topY[index] = y;
      y += image.getHeight();
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
        , new RenderMenuItem(512, 512)
        , new RenderMenuItem(1920, 1080));
  }
  
  public synchronized void render(int thread) {
    double colors[] = threadColors[thread];
    double coords[] = threadCoords[thread];
    double pixels[] = threadPixels[thread];
    BufferedImage image = images[thread];
    int width = image.getWidth();
    int height = image.getHeight();
    
    for(int index = 0; index < coords.length; index++) coords[index] = 0;
    for(Element element : params[ELEMENTS].getList()) element.render(
        colors, pixels, coords, topY[thread], height);
    int colorIndex = 0;
    for(int index = 0; index < pixels.length; index++) {
      int r = (int) Math.round(colors[colorIndex]);
      int g = (int) Math.round(colors[colorIndex + 1]);
      int b = (int) Math.round(colors[colorIndex + 2]);
      image.setRGB(index % width, Math.floorDiv(index, width), b + (g << 8)
          + (r << 16) + (255 << 24));
      colorIndex += 3;
    }
  }
}
