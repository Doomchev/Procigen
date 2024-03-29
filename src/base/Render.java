package base;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import javax.imageio.ImageIO;
import structure.Project;

public class Render extends Main {
  public static void start() {
    Thread render = new Thread() {
      @Override
      public void run() {
        while(true) {
          timeValue = 0.001 * (System.currentTimeMillis() - startingTime)
              / Project.instance.params[Project.DURATION].getDouble(null);
          timeValue = timeValue - Math.floor(timeValue);
          try {
            latch = new CountDownLatch(threadsQuantity);
            int y = 0;
            for(int index = 0; index < threadsQuantity; index++) {
              new RenderThread(images[index], 0, y, index).start();
              y += images[index].getHeight();
            }
            latch.await();
            repaintLatch = new CountDownLatch(1);
            renderPanel.repaint();
            repaintLatch.await();
          } catch (InterruptedException ex) {
            return;
          }
          if(changesThread != null) {
            changesThread.start();
            while(changesThread.isAlive()) yield();
            changesThread = null;
          }
        }
      }
    };
    render.start();
  }

  public static void renderToImages(int width, int height, boolean video) {
    imageWidth = width;
    imageHeight = height;
    Project.instance.init();
    BufferedImage image = new BufferedImage(width, height
        , BufferedImage.TYPE_INT_ARGB);
    int quantity = (int) Math.ceil(30.0
        * Project.instance.params[Project.DURATION].getDouble(null));
    long start = System.currentTimeMillis();
    frame.setTitle("Starting...");
    for(int num = 0; num < quantity; num++) {
      timeValue = 1.0 * num / quantity;
      File file = new File("D:/output/" + (num + 10000) + ".png");
      if(file.exists()) continue;
      try {
        latch = new CountDownLatch(threadsQuantity);
        int y = 0;
        for(int index = 0; index < threadsQuantity; index++) {
          //Project.instance.render(index);
          new RenderThread(images[index], 0, y, index).start();
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
          ImageIO.write(image, "png", file);
        } catch (IOException ex) {
        }
        int secs = (int) (1.0 * (System.currentTimeMillis() - start) / 1000.0
            * (quantity - num) / num);
        frame.setTitle(Math.floorDiv(secs, 60) + "m " + (secs % 60) + "s");
      } catch (InterruptedException ex) {
        return;
      }
      if(!video) break;
    }
    frame.setTitle("Procigen");
  }
}
