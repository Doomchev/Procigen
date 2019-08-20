package gui.menu;

import base.Main;
import java.awt.event.ActionEvent;

public class RenderMenuItem extends MenuItem {
  int width, height;
  
  public RenderMenuItem(int width, int height) {
    super("Render " + width + " x " + height);
    this.width = width;
    this.height = height;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Main.renderToImages(width, height);
  }
}
