package structure;

import base.RenderingBitmap;
import gui.menu.RenameMenuItem;
import java.awt.Color;
import java.io.IOException;

public final class Palette extends Element {
  public String name;
  public Col[] colors;
  public Range[] ranges;
  
  public static class Range {
    public double rk, rb, gk, gb, bk, bb;

    public Range(double x0, Col color0, double x1, Col color1) {
      rk = (color1.r - color0.r)/(x1 - x0);
      rb = color0.r - rk * x0;
      gk = (color1.g - color0.g)/(x1 - x0);
      gb = color0.g - gk * x0;
      bk = (color1.b - color0.b)/(x1 - x0);
      bb = color0.b - bk * x0;
    }
    
    public void setColor(double[] rgb, int index, double value) {
      rgb[index] = rk * value + rb;
      rgb[index + 1] = gk * value + gb;
      rgb[index + 2] = bk * value + bb;
    }
  }
  
  public static class Col {
    public int size;
    public double r, g, b;

    public Col(int size, double r, double g, double b) {
      this.size = size;
      this.r = r;
      this.g = g;
      this.b = b;
    }

    public Col(double r, double g, double b) {
      this(1, r, g, b);
    }

    public Col(int size, double intensity) {
      this(size, intensity, intensity, intensity);
    }

    public Col(double intensity) {
      this(1, intensity, intensity, intensity);
    }

    public Col copy() {
      return new Col(size, r, g, b);
    }

    public Color get() {
      return new Color((int) r, (int) g, (int) b);
    }
  }

  public Palette() {
  }
     
  public Palette(String name, Col... colors) {
    this.name = name;
    this.colors = colors;
    this.params = new Element[0];
    init();
  }

  @Override
  public void init() {
    params = new Element[0];
    int size = 0, pos = 0;
    for(Col col : colors) size += col.size;
    ranges = new Range[size];
    for(int index = 0; index < colors.length; index++) {
      Col color = colors[index];
      Col color2 = colors[index + 1 == colors.length ? 0 : index + 1];
      Range range = new Range(1.0 * pos / size, color, 1.0 * (pos + color.size)
          / size, color2);
      for(int n = 0; n < color.size; n++) {
        ranges[pos] = range;
        pos++;
      }
    }    
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void rename(String name) {
    this.name = name;
  }
  
  @Override
  public int updateProperties(int x0, int y0) {
    return y0;
  }
  
  @Override
  public int update(int x0, int y0, Element parent) {
    return elementsColumn.addBlock(this, x0, y0, parent);
  }

  @Override
  public void menu(int x, int y, Element parent) {
    showMenu(x, y, new RenameMenuItem(this));
  }
  
  public void render(RenderingBitmap bitmap) {
    double size = ranges.length;
    int pos = 0;
    double[] bitmapPixels = bitmap.pixels;
    double[] bitmapColors = bitmap.colors;
    for(int index = 0; index < bitmap.size; index++) {
      double value = bitmapPixels[index];
      value = value - Math.floor(value);
      if(value >= 1.0) value = 0.0;
      ranges[(int) Math.floor(value * size)].setColor(bitmapColors, pos, value);
      pos += 3;
    }
  }
  
  @Override
  public Element createNew() {
    Palette palette = new Palette();
    palette.name = name;
    palette.colors = new Col[colors.length];
    for(int index = 0; index < colors.length; index++)
      palette.colors[index] = colors[index].copy();
    palette.init();
    return palette;
  }
  
  public boolean isStandard() {
    return false;
  }
  
  @Override
  public void read() throws IOException {
    name = readString();
    int colorsQuantity = readInt();
    colors = new Col[colorsQuantity];
    for(int index = 0; index < colorsQuantity; index++) {
      int size = readInt();
      double r = reader.readDouble();
      double g = reader.readDouble();
      double b = reader.readDouble();
      colors[index] = new Col(size, r, g, b);
    }
    init();
  }

  @Override
  public void write() throws IOException {
    writeString(name);
    writeInt(colors.length);
    for(Col color : colors) {
      writeInt(color.size);
      writer.writeDouble(color.r);
      writer.writeDouble(color.g);
      writer.writeDouble(color.b);
    }
  }
  
  @Override
  public Palette getPalette() {
    return this;
  }

  @Override
  public String toString() {
    return name;
  }
}
