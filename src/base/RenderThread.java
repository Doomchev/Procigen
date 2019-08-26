package base;

import java.awt.image.BufferedImage;
import structure.Project;

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
    Project.instance.render(num);
    Main.latch.countDown();
  }
}