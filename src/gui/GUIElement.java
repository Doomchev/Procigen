package gui;

import base.Main;
import java.awt.Graphics;

public abstract class GUIElement extends Main {
  public int x, y, width, height;
  
  public abstract void draw(Graphics g, int x0, int y0);

  public void setSize(int width, int height) {
    this.width = width;
    this.height = height;
  }
}
