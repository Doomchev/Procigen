package base;

import parameters.ParameterTemplate;
import gui.Column;
import gui.menu.MenuItem;
import gui.menu.MenuMenuItem;
import gui.menu.CloneMenuItem;
import gui.PropertyBlock;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import structure.Element;
import structure.Options;
import structure.Palette;
import structure.Palette.Col;
import structure.Project;

public class Main {
  public static final Version currentVersion = new Version(0, 9);
  public static final double PI2 = Math.PI * 2.0;
  public static final int elementsColumnWidth = 200, propertiesColumnWidth = 200
      , scaleBarHeight = 60, blockHeight = 20, blockIndent = 5, colorWidth = 32;
  public static final int ELEMENTS = 1, RENDER = 2, PROPERTIES = 4, SCALE_BAR = 8;
  
  public static final int threadsQuantity
      = Runtime.getRuntime().availableProcessors() - 1;
  public static final BufferedImage[] images = new BufferedImage[threadsQuantity];

  public static int detalization = 4, renderWidth, renderHeight, scaleBarWidth
      , mouseStartingPos, nValue, quantityValue, imageWidth, imageHeight;
  public static double scaleScale = 50.0, scalePos = 0.0, scaleStartingPos
      , markStep, timeValue, cMultiplication, cIncrement;
  public static long startingTime = System.currentTimeMillis();
  
  public static Column elementsColumn = new Column(), propertiesColumn = new Column();
  
  public static final LinkedList<Element> compositionTypes = new LinkedList<>();
  public static final LinkedList<Element> transformationTypes =new LinkedList<>();
  public static final LinkedList<Element> patternTypes = new LinkedList<>();
  public static final LinkedList<Element> alterations = new LinkedList<>();
  public static final HashMap<Class, ParameterTemplate[]> parameterTemplates
      = new HashMap<>();
  
  public static CountDownLatch latch, repaintLatch;
  public static final Thread[] threads = new Thread[threadsQuantity + 1];
  
  public static double compositionIndex;
  public static Font scaleFont = new Font("Arial", Font.PLAIN, 10);
  public static FontMetrics fm;
  public static Element selectedElement = null;
  public static PropertyBlock selectedProperty = null;
  public static Palette selectedPalette = null;
  public static JPanel elementsPanel, renderPanel, propertiesPanel, scalePanel;
  public static JPopupMenu currentMenu;
  public static JFrame frame;
  
  public static final int NO = 0, YES = 1;
  public static final String[] yesOrNo = array("No", "Yes");
  public static final int VERTICAL = 0, HORIZONTAL = 1;
  public static final String[] orientation = array("Vertical", "Horizontal");
  public static final int ADD = 0, MULTIPLY = 1, MIN = 2, MAX = 3;
  public static final String[] operations = array("Add", "Multiply", "Min", "Max");
  public static final String[] hLimits = array("None", "Left", "Right", "Both");
  public static final String[] vLimits = array("None", "Top", "Bottom", "Both");
  public static final int LEFT_LIMIT = 1, RIGHT_LIMIT = 2;
  public static final int TOP_LIMIT = 1, BOTTOM_LIMIT = 2;

  public static <ElementType> ElementType[] array(ElementType... values) {
    return values;
  }
                
  public static void main(String[] args) {
    java.awt.EventQueue.invokeLater(() -> {
      GUI.init();
      Templates.init();
      updateProject();
      Render.start();
    });
  }
  
  public static Col selectedColor(int x) {
    int index = selectedColorIndex(x);
    return index < 0 ? null : selectedPalette.colors[index];
  }
  
  public static int selectedColorIndex(int x) {
    Col[] colors = selectedPalette.colors;
    for(int index = 0; index < colors.length; index++) {
      x -= colors[index].size * colorWidth + 1;
      if(x < 0) return index;
    }
    return -1;
  }
  
  public static boolean mouseInsideScaleBar(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();
    return x >= 0 && x < scaleBarWidth && y >= 0 && y < scaleBarWidth;
  }
  
  public static double xToField(int sX) {
    return scalePos + (sX - 0.5 * scaleBarWidth) / scaleScale;
  }
  public static int xToScreen(double x) {
    return (int) ((x - scalePos) * scaleScale + 0.5 * scaleBarWidth);
  }
  public static double sizeToField(int sX) {
    return 1.0 * sX / scaleScale;
  }
  public static int sizeToScreen(double x) {
    return (int) (x * scaleScale);
  }
  
  public static String shorten(double value) {
    String str = String.valueOf(value);
    int dot = str.indexOf(".");
    if(dot >= 0 && dot < str.length() - 5) return str.substring(0, dot + 5);
    return str;
  }
  
  public static <ElementType extends Element> void addNew (
      LinkedList<ElementType> list, ElementType element) {
    list.add(element);
    element.init();
  }
  
  public static void addNew(Element container, int index, Element element) {
    container.params[index].getList().add(element);
    element.init();
  }
  
  public static void updateProject() {
    elementsColumn.clear();
    int y = elementsColumn.addList(Project.instance, 0, 0);
    elementsColumn.addList(Options.instance, 0, y);
    elementsPanel.repaint();
  }
  
  public static void updateProperties() {
    propertiesColumn.clear();
    if(selectedElement != null)
      selectedElement.updateProperties(0, 0);
    propertiesPanel.repaint();
  }

  public static void refresh() {
    updateProject();
    if(currentMenu != null) currentMenu.setVisible(false);
    currentMenu = null;
  }

  public void showMenu(int x, int y, MenuItem... menuItems) {
    if(currentMenu != null) currentMenu.setVisible(false);
    currentMenu = new JPopupMenu();
    for(MenuItem menuItem : menuItems) menuItem.addTo(currentMenu);
    currentMenu.show(elementsPanel, x, y);
  }

  public MenuMenuItem submenu(String caption, LinkedList<Element> container
      , LinkedList<Element> elements, Element afterElement) {
    JMenu menu = new JMenu(caption);
    for(Element element : elements) (new CloneMenuItem(element.toString()
        , container, element, afterElement)).addTo(menu);
    return new MenuMenuItem(caption, menu);
  }

  public MenuMenuItem submenu(String caption, Element container
      , LinkedList<? extends Element> elements, Element afterElement) {
    JMenu menu = new JMenu(caption);
    for(Element element : elements) (new CloneMenuItem(element.toString()
        , container, element, afterElement)).addTo(menu);
    return new MenuMenuItem(caption, menu);
  }
}
