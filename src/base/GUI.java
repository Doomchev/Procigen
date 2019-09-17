package base;

import gui.ElementBlock;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import structure.Palette;

public class GUI extends Main {
  public static void init() {
    frame = new JFrame();
    frame.addWindowListener(new WindowAdapter(){
        @Override
        public void windowClosing(WindowEvent e){
          //Serialization.save(new File("configuration.bin"), Options.instance);
        }
    });
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //frame.setSize(new Dimension(800, 400));
    frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);

    Container pane = frame.getContentPane();
    pane.setBackground(Color.WHITE);
    pane.setLayout(null);
    frame.setTitle("Procigen");
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
        if(selectedPalette != null) {
          int x = 0;
          Palette.Col[] colors = selectedPalette.colors;
          for(int index = 0; index < colors.length; index++) {
            Palette.Col color0 = colors[index];
            int size = color0.size * colorWidth;
            g.setColor(color0.get());
            g.fillRect(x, 0, size, scaleBarHeight);
            Palette.Col color1 = colors[(index + 1) % colors.length];
            for(int xx = 0; xx < size; xx++) {
              double k1 = 1.0 * xx / size;
              double k0 = 1.0 - k1;
              g.setColor(new Color((int) (color0.r * k0 + color1.r * k1)
                  , (int) (color0.g * k0 + color1.g * k1)
                  , (int) (color0.b * k0 + color1.b * k1)));
              g.fillRect(x + xx, 0, 1, scaleBarHeight / 2);
            }
            x += size + 1;
          }
        } else if(selectedProperty != null) {
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
        if(mouseInsideScaleBar(e)) {
          if(selectedPalette != null) {
            Render.stop();
            if(e.getButton() == MouseEvent.BUTTON1) {
              Palette.Col col = selectedColor(e.getX());
              if(col == null) return;
              Color color = JColorChooser.showDialog(frame, "Choose a color"
                  , col.get());
              if(color == null) return;
              col.r = color.getRed();
              col.g = color.getGreen();
              col.b = color.getBlue();
              scalePanel.repaint();
            } else {
              Palette.Col[] colors = selectedPalette.colors;
              int length = colors.length;
              int index = selectedColorIndex(e.getX());
              Palette.Col[] newColors;
              if(e.isControlDown()) {
                newColors = new Palette.Col[length - 1];
                if(index < 0) return;
                if(index > 0) System.arraycopy(colors, 0, newColors, 0, index);
                if(index < length - 1) System.arraycopy(colors, index + 1
                    , newColors, index, length - index - 1);
              } else {
                newColors = new Palette.Col[length + 1];
                Palette.Col newColor = new Palette.Col(255, 255, 255);
                if(index < 0) {
                  System.arraycopy(colors, 0, newColors, 0, length);
                  newColors[length] = newColor;
                } else {
                  if(index > 0) System.arraycopy(colors, 0, newColors, 0, index);
                  System.arraycopy(colors, index, newColors, index + 1, length
                      - index);
                  newColors[index] = newColor;
                }
              }
              selectedPalette.colors = newColors;
            }
            selectedPalette.init();
            scalePanel.repaint();
            Render.start();
          } else if(selectedProperty != null) {
            mouseStartingPos = e.getX();
            scaleStartingPos = scalePos;
          }
        }
      }
      @Override
      public void mouseReleased(MouseEvent e) {
        mouseStartingPos = -1;
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
        if(selectedPalette != null) {
          Palette.Col color = selectedColor(e.getX());
          if(color == null) return;
          if (e.getWheelRotation() > 0) {
            if(color.size > 1) color.size--;
          } else {
            color.size++;
          }
          Render.stop();
          selectedPalette.init();
          Render.start();
        } else if (e.getWheelRotation() < 0) {
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
  }
}
