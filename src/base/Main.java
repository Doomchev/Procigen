package base;

import parameters.ParameterTemplate;
import gui.Column;
import gui.ElementBlock;
import gui.menu.MenuItem;
import gui.menu.MenuMenuItem;
import gui.menu.CloneMenuItem;
import gui.PropertyBlock;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import structure.Element;
import structure.Project;

public class Main {
  public static final Version currentVersion = new Version(0, 6);
  public static final double PI2 = Math.PI * 2.0;
  public static final int elementsColumnWidth = 200, propertiesColumnWidth = 200
      , scaleBarHeight = 60, blockHeight = 20, blockIndent = 5;
  public static final int ELEMENTS = 1, RENDER = 2, PROPERTIES = 4, SCALE_BAR = 8;
  
  public static final int threadsQuantity
      = Runtime.getRuntime().availableProcessors() - 1;
  public static final BufferedImage[] images = new BufferedImage[threadsQuantity];

  public static int detalization = 4, renderWidth, renderHeight, scaleBarWidth
      , mouseStartingPos, nValue, quantityValue, imageWidth, imageHeight;
  public static double scaleScale = 50.0, scalePos = 0.0, scaleStartingPos
      , markStep, timeValue;
  public static long startingTime = System.currentTimeMillis();
  
  public static Project project;
  public static Column elementsColumn = new Column(), propertiesColumn = new Column();
  
  public static final LinkedList<Element> compositionTypes = new LinkedList<>();
  public static final LinkedList<Element> transformationTypes =new LinkedList<>();
  public static final LinkedList<Element> patternTypes = new LinkedList<>();
  public static final LinkedList<Element> palettes = new LinkedList<>();
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
  public static JPanel elementsPanel, renderPanel, propertiesPanel, scalePanel;
  public static JPopupMenu currentMenu;
  public static JFrame frame;
  
  public static final int NO = 0, YES = 1;
  public static final String[] yesOrNo = array("No", "Yes");
  public static final int VERTICAL = 0, HORIZONTAL = 1;
  public static final String[] orientation = array("Vertical", "Horizontal");
  public static final int ADD = 0, MULTIPLY = 1, MIN = 2, MAX = 3;
  public static final String[] operations = array("Add", "Multiply", "Min", "Max");

  public static <ElementType> ElementType[] array(ElementType... values) {
    return values;
  }
                
  public static void renderToImages(int width, int height) {
    stopRender();
    imageWidth = width;
    imageHeight = height;
    project.init();
    BufferedImage image = new BufferedImage(width, height
        , BufferedImage.TYPE_INT_ARGB);
    
    java.awt.EventQueue.invokeLater(() -> {
      int quantity = (int) Math.ceil(30.0 * project.params[Project.DURATION]
          .getDouble());
      for(int num = 0; num < quantity; num ++) {
        frame.setTitle(num + " / " + quantity);
        timeValue = 1.0 * num / quantity;
        try {
          latch = new CountDownLatch(threadsQuantity);
          int y = 0;
          for(int index = 0; index < threadsQuantity; index++) {
            threads[index + 1] = new RenderThread(images[index], 0, y, index);
            threads[index + 1].start();
            y += images[index].getHeight();
          }
          latch.await();

          y = 0;
          Graphics graphics = image.createGraphics();
          for(int index = 0; index < threadsQuantity; index++) {
            BufferedImage imagePart = images[index];
            graphics.drawImage(imagePart, 0, y, null);
            y += imagePart.getHeight();
          }
          graphics.dispose();
          try {
            ImageIO.write(image, "png", new File("D:/output/" + (num + 10000)
                + ".png"));
          } catch (IOException ex) {
          }
        } catch (InterruptedException ex) {
          return;
        }
      }
      imageWidth = (int) Math.ceil(renderWidth / detalization);
      imageHeight = (int) Math.ceil(renderHeight / detalization);
      project.init();
      startRender();
    });
  }

  public static void startRender() {
    if(threads[0] != null) return;
    System.out.println("base.Main.startRender()");
    threads[0] = new Thread() {
      @Override
      public void run() {
        while(true) {
          timeValue = 0.001 * (System.currentTimeMillis() - startingTime)
              / project.params[Project.DURATION].getDouble();
          timeValue = timeValue - Math.floor(timeValue);
          
          try {
            latch = new CountDownLatch(threadsQuantity);
            int y = 0;
            for(int index = 0; index < threadsQuantity; index++) {
              threads[index + 1] = new RenderThread(images[index], 0, y, index);
              threads[index + 1].start();
              y += images[index].getHeight();
            }
            latch.await();
            
            repaintLatch = new CountDownLatch(1);
            Main.renderPanel.repaint();
            repaintLatch.await();
          } catch (InterruptedException ex) {
            return;
          }
        }
      }
    };
    threads[0].start();
  }

  public static void stopRender() {
    if(threads[0] == null) return;
    System.out.println("base.Main.stopRender()");
    for(Thread thread : threads) if(thread != null) thread.interrupt();
    threads[0] = null;
  }

  public static void main(String[] args) {
    java.awt.EventQueue.invokeLater(() -> {
      frame = new JFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
      
      Container pane = frame.getContentPane();
      pane.setBackground(Color.WHITE);
      pane.setLayout(null);
      frame.setVisible(true);
      
      Dimension windowSize = frame.getSize();
      int windowWidth = (int)windowSize.getWidth() - frame.getInsets().left
          - frame.getInsets().right;
      int windowHeight = (int)windowSize.getHeight() - frame.getInsets().top
          - frame.getInsets().bottom;
      elementsColumn.setSize(elementsColumnWidth, windowHeight);
      propertiesColumn.setSize(propertiesColumnWidth, windowHeight);
      renderWidth = windowWidth - elementsColumnWidth - propertiesColumnWidth;
      renderHeight = windowHeight - scaleBarHeight;
      imageWidth = (int) Math.ceil(renderWidth / detalization);
      imageHeight = (int) Math.ceil(renderHeight / detalization);
      scaleBarWidth = renderWidth;
      
      elementsPanel = new JPanel() {
        @Override
        public void paint(Graphics g) {
          elementsColumn.draw(g, 0, 0);
        }
      };
      pane.add(elementsPanel);
      elementsPanel.setBounds(0, 0, elementsColumnWidth, windowHeight);
      
      renderPanel = new JPanel() {
        @Override
        public void paint(Graphics g) {
          int y = 0;
          for(int index = 0; index < threadsQuantity; index++) {
            BufferedImage image = images[index];
            int width = image.getWidth() * detalization;
            int height = image.getHeight() * detalization;
            g.drawImage(image, 0, y, width, height, null);
            y += height;
          }
          if(repaintLatch != null) repaintLatch.countDown();
        }
      };
      pane.add(renderPanel);
      renderPanel.setBounds(elementsColumnWidth, 0, renderWidth, renderHeight);
      
      propertiesPanel = new JPanel() {
        @Override
        public void paint(Graphics g) {
          propertiesColumn.draw(g, 0, 0);
        }
      };
      pane.add(propertiesPanel);
      propertiesPanel.setBounds(windowWidth - propertiesColumnWidth, 0
          , propertiesColumnWidth, windowHeight);
      
      scalePanel = new JPanel() {
        @Override
        public void paint(Graphics g) {
          g.setColor(Color.WHITE);
          g.fillRect(0, 0, scaleBarWidth, scaleBarHeight);
          if(selectedProperty != null) {
            if(scaleScale == 0.0) return;
            g.setColor(new Color(128, 255, 128));
            g.fillRect(scaleBarWidth / 2 - 1, 20, 3, 26);
            g.setColor(Color.BLACK);
            g.setFont(scaleFont);
            FontMetrics sfm = g.getFontMetrics();
            String caption = shorten(scalePos);
            g.drawString(caption, (scaleBarWidth - sfm.stringWidth(caption))
                / 2, 15);

            int intMarkStep = 9;
            while(true) {
              if(scaleScale / intMarkStep / 4 * 10000 < 3) break;
              intMarkStep *= 4;
              if(scaleScale / intMarkStep / 2.5 * 10000 < 3) break;
              intMarkStep *= 2.5;
            }
            markStep = 0.0001 * intMarkStep;

            int start = (int) Math.floor((scalePos - scaleBarWidth / 2
                / scaleScale) * markStep);
            int end = (int) Math.ceil((scalePos + scaleBarWidth / 2
                / scaleScale) * markStep);

            for(int xx = start; xx <= end; xx++) {
              double value = 10000.0 * xx / intMarkStep;
              int sX = xToScreen(value);
              String strValue = shorten(value);
              int lineHeight = Math.floorMod(xx, 3) == 0 ? 8 : 4;
              if(Math.floorMod(xx, 9) == 0) {
                lineHeight = Math.floorMod(xx, 36) == 0 ? 24 : 16;
                g.drawString(strValue, sX - sfm.stringWidth(strValue) / 2
                    , 55);
              }
              //System.out.println(sX);
              g.fillRect(sX, 45 - lineHeight, 1, lineHeight);
            }
          }
        }
      };
      pane.add(scalePanel);
      scalePanel.setBounds(elementsColumnWidth, renderHeight, renderWidth
          , scaleBarHeight);
      
      MouseListener listener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
          int x = e.getX();
          int y = e.getY();
          ElementBlock block = (e.getSource() == elementsPanel ? elementsColumn
              : propertiesColumn).getBlock(x, y);
          if(block == null) return;
          switch(e.getButton()) {
            case MouseEvent.BUTTON1:
              block.onclick(x, y);
              break;  
            case MouseEvent.BUTTON3:
              block.menu(x, y);
              break;  
          }
        }
        @Override
        public void mousePressed(MouseEvent e) {
        }
        @Override
        public void mouseReleased(MouseEvent e) {
        }
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        @Override
        public void mouseExited(MouseEvent e) {
        }
      };
      elementsPanel.addMouseListener(listener);
      propertiesPanel.addMouseListener(listener);
      scalePanel.addMouseListener(new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
        }
        @Override
        public void mousePressed(MouseEvent e) {
          if(selectedProperty != null && mouseInsideScaleBar(e))
            mouseStartingPos = e.getX();
        }
        @Override
        public void mouseReleased(MouseEvent e) {
          mouseStartingPos = -1;
          scaleStartingPos = scalePos;
        }
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        @Override
        public void mouseExited(MouseEvent e) {
        }
      });
      scalePanel.addMouseWheelListener((MouseWheelEvent e) -> {
        if(mouseInsideScaleBar(e)) {
          if (e.getWheelRotation() < 0) {
            if(scaleScale < 100000) scaleScale *= 3;
          } else {
            if(scaleScale > 0.1) scaleScale /= 3;
          }
          scalePanel.repaint();
        }
      });
      scalePanel.addMouseMotionListener(new MouseMotionListener() {
        @Override
        public void mouseDragged(MouseEvent e) {
          if(mouseStartingPos < 0) return;
          //stopRender();
          scalePos = scaleStartingPos + sizeToField(mouseStartingPos - e.getX());
          scalePos = Math.round(scalePos * markStep) / markStep;
          scalePos = selectedProperty.template.limit(scalePos);
          selectedProperty.element.params[selectedProperty.parameterIndex]
              .setDouble(scalePos);
          scalePanel.repaint();
          propertiesPanel.repaint();
          //startRender();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
      });
      
      fm = frame.getFontMetrics(frame.getFont());
      
      Templates.init();
      
      updateProject();
      startRender();
    });
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

  public static void initCoords(double [] coords, int y0, int height) {
    int pos = 0;
    int dx = imageWidth / 2;
    int dy = imageHeight / 2;
    double scale = 12.0 / imageHeight;
    for(int y = 0; y < height; y++) {
      for(int x = 0; x < imageWidth; x++) {
        coords[pos] = (x - dx) * scale;
        coords[pos + 1] = (y0 + y - dy) * scale;
        pos += 2;
      }
    }
  }
  
  public static void updateProject() {
    elementsColumn.clear();
    elementsColumn.addList(project, 0, 0);
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
