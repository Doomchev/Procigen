
import static base.Main.canvas;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Render {
  public static CountDownLatch latch;
  public static final int threadsQuantity
      = Runtime.getRuntime().availableProcessors() - 1;
  public static final BufferedImage[] images = new BufferedImage[threadsQuantity];
  public static String FPS = "";
  
  public static class RenderThread extends Thread {
    BufferedImage image;
    int dx, dy;
    Random random = new Random();

    public RenderThread(BufferedImage image, int dx, int dy) {
      this.image = image;
      this.dx = dx;
      this.dy = dy;
    }

    @Override
    public void run() {
      //lock.lock();
      try {
        for(int y = 0; y < image.getHeight(); y++) {
          for(int x = 0; x < image.getWidth(); x++) {
            int intensity = (int) (255.0 * (2 + Math.sin(0.02 * (x + dx))
                + Math.sin(0.02 * (y + dy)))* 0.25);
            image.setRGB(x, y, intensity * 65793 + (255 << 24));
          }
        }
      } finally {
        //lock.unlock();
      }
      latch.countDown();
    }
  }
  
  public static void main(String[] args) throws InterruptedException {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    frame.getContentPane().setBackground(Color.WHITE);
    frame.setVisible(true);

    Dimension windowSize = frame.getSize();
    int windowWidth = (int)windowSize.getWidth() - frame.getInsets().left
        - frame.getInsets().right;
    int windowHeight = (int)windowSize.getHeight() - frame.getInsets().top
        - frame.getInsets().bottom;

    JLabel canvas = new JLabel() {
      @Override
      public void paint(Graphics g) {
        int y = 0;
        for(int index = 0; index < threadsQuantity; index++) {
          g.drawImage(images[index], 0, y, null);
          y += images[index].getHeight();
        }
        g.drawString(FPS, 100, 100);
        latch.countDown();
      }
    };
    canvas.setBackground(Color.WHITE);
    canvas.setSize(new Dimension(windowWidth, windowHeight));
    frame.getContentPane().add(canvas, BorderLayout.CENTER); 
    for(int index = 0; index < threadsQuantity; index++) {
      int y0 = Math.floorDiv(index * windowHeight, threadsQuantity);
      int y1 = Math.floorDiv((index + 1) * windowHeight, threadsQuantity);
      images[index] = new BufferedImage(windowWidth, y1 - y0
          , BufferedImage.TYPE_INT_ARGB);
    }
    
    long startingTime = 0;
    int currentFPS = 0, x0 = 0;
    while(true) {
      long time = System.currentTimeMillis() - startingTime;
      if(System.currentTimeMillis() - startingTime > 1000) {
        FPS = String.valueOf(1000.0 * currentFPS / time);
        startingTime = System.currentTimeMillis();
        currentFPS = 0;
      }
      
      latch = new CountDownLatch(threadsQuantity);
      for(int index = 0; index < threadsQuantity; index++) {
        int y0 = Math.floorDiv(index * windowHeight, threadsQuantity);
        int y1 = Math.floorDiv((index + 1) * windowHeight, threadsQuantity);
        Thread thread = new RenderThread(images[index], x0, y0);
        thread.start();
      }

      latch.await();
      
      latch = new CountDownLatch(1);
      canvas.repaint();
      latch.await();
      currentFPS++;
      x0++;
    }
  }
}
