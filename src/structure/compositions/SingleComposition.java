package structure.compositions;

import base.RenderingBitmap;
import parameters.ParameterTemplate;
import structure.Element;
import structure.transformations.Affine;

public class SingleComposition extends Composition {
  public static final int X = 2, Y = 3, VECTOR_RADIUS = 4, VECTOR_ANGLE = 5
      , SCALE = 6, ANGLE = 7, INCREMENT = 8, MULTIPLICATION = 9
      , HORIZONTAL_LIMITS = 10, VERTICAL_LIMITS = 11, LEFT = 12, RIGHT = 13
      , TOP = 14, BOTTOM = 15, PATTERN = 16, OPERATION = 17;
  
  static {
    ParameterTemplate[] templates = new ParameterTemplate[18];
    setTemplates(templates, "Composition");
    parameterTemplates.put(SingleComposition.class, templates);
  }
  
  public static void setTemplates(ParameterTemplate[] templates, String name) {
    Composition.setTemplates(templates, name);
    templates[X] = new ParameterTemplate("X", 0.0);
    templates[Y] = new ParameterTemplate("Y", 0.0);
    templates[VECTOR_RADIUS] = new ParameterTemplate("Vector radius", 0.0);
    templates[VECTOR_ANGLE] = new ParameterTemplate("Vectior angle", 0.0);
    templates[SCALE] = new ParameterTemplate("Scale", 1.0);
    templates[ANGLE] = new ParameterTemplate("Angle", 0.0);
    templates[INCREMENT] = new ParameterTemplate("Increment", 0.0, 0.0, 1.0);
    templates[MULTIPLICATION] = new ParameterTemplate("Multiplication", 0.5);
    templates[HORIZONTAL_LIMITS] = new ParameterTemplate("Horizontal limits", hLimits);
    templates[VERTICAL_LIMITS] = new ParameterTemplate("Vertical limits", vLimits);
    templates[LEFT] = new ParameterTemplate("Left limit", 0.0);
    templates[RIGHT] = new ParameterTemplate("Right limit", 0.0);
    templates[TOP] = new ParameterTemplate("Top limit", 0.0);
    templates[BOTTOM] = new ParameterTemplate("Bottom limit", 0.0);
    templates[PATTERN] = new ParameterTemplate("Pattern", patternTypes, true);
    templates[OPERATION] = new ParameterTemplate("Operation", operations);
  }

  @Override
  public void renderCoords(RenderingBitmap bitmap) {
    nValue = 0;
    render(bitmap, params[OPERATION].getValue());
  }
  
  public void render(RenderingBitmap bitmap, int operation) {
    double vRadius = params[VECTOR_RADIUS].getDouble();
    double vAngle = params[VECTOR_ANGLE].getDouble() * PI2;
    double x = params[X].getDouble() + vRadius * Math.cos(vAngle);
    double y = params[Y].getDouble() + vRadius * Math.sin(vAngle);
    double scale = params[SCALE].getDouble();
    double angle = params[ANGLE].getDouble();
    int vLim = params[VERTICAL_LIMITS].getValue();
    int hLim = params[HORIZONTAL_LIMITS].getValue();
    int x0 = bitmap.x;
    int y0 = bitmap.y;
    int x1 = x0 + bitmap.width;
    int y1 = y0 + bitmap.height;
    if((hLim & LEFT_LIMIT) > 0) {
      double fx0 = params[LEFT].getDouble() * scale + x;
      int xx = (int) Math.floor(xToPixels(fx0));
      if(xx > x0) x0 = xx;
    }
    if((hLim & RIGHT_LIMIT) > 0) {
      double fx1 = params[RIGHT].getDouble() * scale + x;
      int xx = (int) Math.ceil(xToPixels(fx1)) + 1;
      if(xx < x1) x1 = xx;
    }
    if((vLim & TOP_LIMIT) > 0) {
      double fy0 = params[TOP].getDouble() * scale + y;
      int yy = (int) Math.floor(yToPixels(fy0));
      if(yy > y0) y0 = yy;
    }
    if((vLim & BOTTOM_LIMIT) > 0) {
      double fy1 = params[BOTTOM].getDouble() * scale + y;
      int yy = (int) Math.ceil(yToPixels(fy1)) + 1;
      if(yy < y1) y1 = yy;
    }
    if(x0 >= x1 || y0 >= y1) return;
    RenderingBitmap tempBitmap = bitmap.tempBitmap;
    tempBitmap.x = x0;
    tempBitmap.y = y0;
    tempBitmap.width = x1 - x0;
    tempBitmap.height = y1 - y0;
    tempBitmap.initCoords();
    Affine.apply(tempBitmap, x, y, scale, angle);
    for(Element transformations : params[TRANSFORMATIONS].getList())
      transformations.applyTransformation(tempBitmap);
    params[PATTERN].getPattern().render(tempBitmap);
    bitmap.paste(tempBitmap, operation);
  }
  
  public double xToPixels(double x) {
    return x * imageHeight / 12.0 + imageWidth / 2;
  }
  
  public double yToPixels(double y) {
    return y * imageWidth / 12.0 + imageHeight / 2;
  }
}
