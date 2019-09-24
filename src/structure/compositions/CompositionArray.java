package structure.compositions;

import base.RenderingBitmap;
import gui.menu.MoveMenuItem;
import gui.menu.NewMenuItem;
import gui.menu.RemoveMenuItem;
import gui.menu.ToggleMenuItem;
import java.util.LinkedList;
import parameters.ParameterTemplate;
import structure.Element;
import structure.transformations.Affine;

public class CompositionArray extends Element {
  public static final int NAME = 0, TRANSFORMATIONS = 1, X = 2, Y = 3
      , VECTOR_RADIUS = 4, VECTOR_ANGLE = 5, SCALE = 6, ANGLE = 7
      , INCREMENT = 8, MULTIPLICATION = 9, TIME_SHIFT = 10
      , HORIZONTAL_LIMITS = 11, VERTICAL_LIMITS = 12, LEFT = 13, RIGHT = 14
      , TOP = 15, BOTTOM = 16, OPERATION = 17, PATTERNS = 18
      , ITEMS_OPERATION = 19, QUANTITY = 20;
  
  static {
    ParameterTemplate[] templates = new ParameterTemplate[21];
    templates[NAME] = new ParameterTemplate("Composition array", true);
    templates[TRANSFORMATIONS] = new ParameterTemplate("Transformations");
    templates[X] = new ParameterTemplate("X", 0.0);
    templates[Y] = new ParameterTemplate("Y", 0.0);
    templates[VECTOR_RADIUS] = new ParameterTemplate("Vector radius", 0.0);
    templates[VECTOR_ANGLE] = new ParameterTemplate("Vectior angle", 0.0);
    templates[SCALE] = new ParameterTemplate("Scale", 1.0);
    templates[ANGLE] = new ParameterTemplate("Angle", 0.0);
    templates[INCREMENT] = new ParameterTemplate("Increment", 0.0, 0.0, 1.0);
    templates[MULTIPLICATION] = new ParameterTemplate("Multiplication", 0.5);
    templates[TIME_SHIFT] = new ParameterTemplate("Time shift", 0.0);
    templates[HORIZONTAL_LIMITS] = new ParameterTemplate("Horizontal limits", hLimits);
    templates[VERTICAL_LIMITS] = new ParameterTemplate("Vertical limits", vLimits);
    templates[LEFT] = new ParameterTemplate("Left limit", 0.0);
    templates[RIGHT] = new ParameterTemplate("Right limit", 0.0);
    templates[TOP] = new ParameterTemplate("Top limit", 0.0);
    templates[BOTTOM] = new ParameterTemplate("Bottom limit", 0.0);
    templates[OPERATION] = new ParameterTemplate("Operation", operations);
    templates[PATTERNS] = new ParameterTemplate("Patterns");
    templates[ITEMS_OPERATION] = new ParameterTemplate("Items operation", operations);
    templates[QUANTITY] = new ParameterTemplate("Items quantity", 1.0, 1.0
        , 0.0, 1.0);
    parameterTemplates.put(CompositionArray.class, templates);
  }
  
  @Override
  public int update(int x0, int y0, Element parent) {
    return elementsColumn.addList(this, parent, x0, y0);
  }

  @Override
  public LinkedList<Element> getList() {
    return params[PATTERNS].getList();
  }

  @Override
  public void menu(int x, int y, Element parent) {
    showMenu(x, y, new NewMenuItem("Add new composition", parent
        , CompositionArray.class, this)
        , submenu("Add new pattern", this, patternTypes, null)
        , new ToggleMenuItem(this)
        , new MoveMenuItem(parent, this, -1)
        , new MoveMenuItem(parent, this, 1)
        , new RemoveMenuItem(parent, this));
  }
  
  @Override
  public void render(RenderingBitmap bitmap) {
    if(hide) return;
    bitmap.quantity = params[QUANTITY].getInt(bitmap);
    int operation = params[OPERATION].getValue();
    int itemsOperation = params[ITEMS_OPERATION].getValue();
    if(itemsOperation == operation) {
      for(bitmap.n = 0; bitmap.n < bitmap.quantity; bitmap.n++) {
        double time = params[TIME_SHIFT].getDouble(bitmap) + timeValue;
        bitmap.time = time - Math.floor(time);
        bitmap.multiplication = params[MULTIPLICATION].getDouble(bitmap);
        bitmap.increment = params[INCREMENT].getDouble(bitmap);
        render(bitmap, itemsOperation);
      }
    } else {
      /*if(arrayPixels == null) arrayPixels = new double[pixels.length];
      for(nValue = 0; nValue < quantity; nValue++)
        render(arrayPixels, coords, itemsOperation, y0, height);
      mix(arrayPixels, pixels, itemsOperation);*/
    }
  }
  
  public void render(RenderingBitmap bitmap, int operation) {
    double vRadius = params[VECTOR_RADIUS].getDouble(bitmap);
    double vAngle = params[VECTOR_ANGLE].getDouble(bitmap) * PI2;
    double x = params[X].getDouble(bitmap) + vRadius * Math.cos(vAngle);
    double y = params[Y].getDouble(bitmap) + vRadius * Math.sin(vAngle);
    double scale = params[SCALE].getDouble(bitmap);
    double angle = params[ANGLE].getDouble(bitmap);
    
    int vLim = params[VERTICAL_LIMITS].getValue();
    int hLim = params[HORIZONTAL_LIMITS].getValue();
    int x0 = bitmap.x;
    int y0 = bitmap.y;
    int x1 = x0 + bitmap.width;
    int y1 = y0 + bitmap.height;
    if((hLim & LEFT_LIMIT) > 0) {
      double fx0 = params[LEFT].getDouble(bitmap) * scale + x;
      int xx = (int) Math.floor(xToPixels(fx0));
      if(xx > x0) x0 = xx;
    }
    if((hLim & RIGHT_LIMIT) > 0) {
      double fx1 = params[RIGHT].getDouble(bitmap) * scale + x;
      int xx = (int) Math.ceil(xToPixels(fx1)) + 1;
      if(xx < x1) x1 = xx;
    }
    if((vLim & TOP_LIMIT) > 0) {
      double fy0 = params[TOP].getDouble(bitmap) * scale + y;
      int yy = (int) Math.floor(yToPixels(fy0));
      if(yy > y0) y0 = yy;
    }
    if((vLim & BOTTOM_LIMIT) > 0) {
      double fy1 = params[BOTTOM].getDouble(bitmap) * scale + y;
      int yy = (int) Math.ceil(yToPixels(fy1)) + 1;
      if(yy < y1) y1 = yy;
    }
    if(x0 >= x1 || y0 >= y1) return;
    
    RenderingBitmap patternBitmap = bitmap.patternBitmap;
    patternBitmap.setPosition(x0, y0, x1 - x0, y1 - y0);
    bitmap.copyParametersTo(patternBitmap);
    
    RenderingBitmap compositionBitmap = bitmap.compositionBitmap;
    compositionBitmap.setPosition(x0, y0, x1 - x0, y1 - y0);
    bitmap.copyParametersTo(compositionBitmap);
    
    for(Element pattern : params[PATTERNS].getList()) {
      patternBitmap.initCoords();
      Affine.apply(patternBitmap, x, y, scale, angle);
      pattern.render(bitmap);
    }
    bitmap.paste(compositionBitmap, operation);
  }
  
  public double xToPixels(double x) {
    return x * imageHeight / 12.0 + imageWidth / 2;
  }
  
  public double yToPixels(double y) {
    return y * imageHeight / 12.0 + imageHeight / 2;
  }
}
