package base;

import java.awt.image.BufferedImage;

public class RenderThread extends Thread {
  BufferedImage image;
  int dx, dy, num;
  
  public RenderThread(BufferedImage image, int dx, int dy, int num) {
    this.image = image;
    this.dx = dx;
    this.dy = dy;
    this.num = num;
  }

  @Override
  public void run() {
    Main.project.render(num);
    Main.latch.countDown();
  }
}