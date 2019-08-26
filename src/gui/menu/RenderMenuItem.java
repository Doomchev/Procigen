package gui.menu;

import base.Main;
import java.awt.event.ActionEvent;

public class RenderMenuItem extends MenuItem {
  int width, height;
  boolean video;
  
  public RenderMenuItem(int width, int height, boolean video) {
    super("Render " + width + " x " + height);
    this.width = width;
    this.height = height;
    this.video = video;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Main.renderToImages(width, height, video);
  }
}
