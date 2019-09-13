package base;

public class RenderingBitmap extends Main {
  public double[] colors, coords, pixels;
  public int size, size2, x, y, width, height;
  public RenderingBitmap tempBitmap;

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
    size = width * height;
    size2 = size * 2;
  }

  public void initArrays(int imageSize) {
    colors = new double[imageSize * 3];
    coords = new double[imageSize * 2];
    pixels = new double[imageSize];
  }
  
  public RenderingBitmap copy() {
    RenderingBitmap bitmap = new RenderingBitmap();
    bitmap.initArrays(pixels.length);
    return bitmap;
  }

  public void paste(RenderingBitmap bitmap, int operation) {
    int dy = bitmap.y - y;
    int y0 = dy < 0 ? -dy : 0;
    int y1 = bitmap.height;
    if(bitmap.y + y1 > y + height) y1 = y + height - bitmap.y;
    if(y0 >= y1) return;
    
    int dx = bitmap.x - x;
    int x0 = dx < 0 ? -dx : 0;
    int x1 = bitmap.width;
    if(bitmap.x + x1 > x + width) x1 = x + width - bitmap.x;
    int length = x1 - x0;
    if(length <= 0) return;
    
    if(length == width && dx == 0) {
      paste(bitmap, dy * width, 0, length * (y1 - y0), operation);
    } else {
      for(int yy = y0; yy < y1; yy++)
        paste(bitmap, (yy + dy) * width + dx, yy * bitmap.width + x0, length
            , operation);
    }
  }

  public void paste(RenderingBitmap bitmap, int x0, int x1, int size
      , int operation) {
    double[] pixels2 = bitmap.pixels;
    switch(operation) {
      case ADD:
        for(int xx = 0; xx < size; xx++)
          pixels[x0 + xx] += pixels2[x1 + xx];
        break;
      case MULTIPLY:
        for(int xx = 0; xx < size; xx++)
          pixels[x0 + xx] *= x;
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
}
