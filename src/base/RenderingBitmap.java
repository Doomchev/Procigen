package base;

public class RenderingBitmap extends Main {
  public double[] colors, coords, pixels;
  public int size, size2, x, y, width, height, n = 0, quantity = 1;
  public RenderingBitmap compositionBitmap, patternBitmap;
  public double increment = 0.0, multiplication = 1.0, time = 0.0;

  public RenderingBitmap(int size, int arrays) {
    initArrays(size, arrays);
  }

  public void setPosition(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    size = width * height;
    size2 = size * 2;
    for(int i = 0; i < size; i++) pixels[i] = 0;
  }
    
  public void initCoords() {
    int pos = 0;
    int dx = imageWidth / 2;
    int dy = imageHeight / 2;
    double scale = 12.0 / imageHeight;
    for(int yy = y; yy < y + height; yy++) {
      for(int xx = x; xx < x + width; xx++) {
        coords[pos] = (xx - dx) * scale;
        coords[pos + 1] = (yy - dy) * scale;
        pos += 2;
      }
    }
  }

  public static final int COLORS = 1, PIXELS = 2, COORDS = 4
      , ALL = COLORS | PIXELS | COORDS;
  
  public final void initArrays(int imageSize, int arrays) {
    if((arrays & COLORS) > 0) colors = new double[imageSize * 3];
    if((arrays & PIXELS) > 0) pixels = new double[imageSize];
    if((arrays & COORDS) > 0) coords = new double[imageSize * 2];
  }

  public void paste(RenderingBitmap destBitmap, int operation) {
    int dy = destBitmap.y - y;
    int y0 = dy < 0 ? -dy : 0;
    int y1 = destBitmap.height;
    if(destBitmap.y + y1 > y + height) y1 = y + height - destBitmap.y;
    if(y0 >= y1) return;
    
    int dx = destBitmap.x - x;
    int x0 = dx < 0 ? -dx : 0;
    int x1 = destBitmap.width;
    if(destBitmap.x + x1 > x + width) x1 = x + width - destBitmap.x;
    int length = x1 - x0;
    if(length <= 0) return;
    
    if(length == width && dx == 0) {
      paste(destBitmap, dy * width, 0, length * (y1 - y0), operation);
    } else {
      for(int yy = y0; yy < y1; yy++)
        paste(destBitmap, (yy + dy) * width + dx, yy * destBitmap.width + x0
            , length, operation);
    }
  }

  public void paste(RenderingBitmap destBitmap, int x0, int x1, int size
      , int operation) {
    double[] pixels2 = destBitmap.pixels;
    switch(operation) {
      case ADD:
        for(int xx = 0; xx < size; xx++)
          pixels[x0 + xx] += pixels2[x1 + xx];
        break;
      case MULTIPLY:
        for(int xx = 0; xx < size; xx++)
          pixels[x0 + xx] *= pixels2[x1 + xx];
        break;
      case MIN:
        for(int xx = 0; xx < size; xx++)
          pixels[x0 + xx] = Math.min(pixels[x0 + xx], pixels2[x1 + xx]);
        break;
      case MAX:
        for(int xx = 0; xx < size; xx++)
          pixels[x0 + xx] = Math.max(pixels[x0 + xx], pixels2[x1 + xx]);
        break;
    }
  }

  public void copyParametersTo(RenderingBitmap bitmap) {
    bitmap.n = n;
    bitmap.quantity = quantity;
    bitmap.increment = increment;
    bitmap.multiplication = multiplication;
    bitmap.time = time;
  }
}
