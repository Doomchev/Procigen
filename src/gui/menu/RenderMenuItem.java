package gui.menu;

import base.Main;
import java.awt.event.ActionEvent;
import structure.Project;

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
    Main.stopRender();
    Main.renderToImages(width, height, video);
    Main.imageWidth = (int) Math.ceil(Main.renderWidth / Main.detalization);
    Main.imageHeight = (int) Math.ceil(Main.renderHeight / Main.detalization);
    Project.instance.init();
    Main.startRender();
  }
}
