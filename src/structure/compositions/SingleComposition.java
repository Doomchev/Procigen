package structure.compositions;

import parameters.ParameterTemplate;
import structure.Element;
import structure.transformations.Affine;

public class SingleComposition extends Composition {
  public static final int X = 2, Y = 3, SCALE = 4, ANGLE = 5, MULTIPLICATION = 6
      , PATTERN = 7, OPERATION = 8;
  
  static {
    ParameterTemplate[] templates = new ParameterTemplate[9];
    setTemplates(templates, "Composition");
    parameterTemplates.put(SingleComposition.class, templates);
  }
  
  public static void setTemplates(ParameterTemplate[] templates, String name) {
    Composition.setTemplates(templates, name);
    templates[X] = new ParameterTemplate("X", 0.0);
    templates[Y] = new ParameterTemplate("Y", 0.0);
    templates[SCALE] = new ParameterTemplate("Scale", 1.0);
    templates[ANGLE] = new ParameterTemplate("Angle", 0.0, 0.0, 1.0);
    templates[MULTIPLICATION] = new ParameterTemplate("Multiplication", 0.5);
    templates[PATTERN] = new ParameterTemplate("Pattern", patternTypes, true);
    templates[OPERATION] = new ParameterTemplate("Operation", operations);
  }
  
  @Override
  public void render(double[] pixels, double[] coords, int y0, int height) {
    nValue = 0;
    render(pixels, coords, params[OPERATION].getValue(), y0, height);
  }
  
  public void render(double[] pixels, double[] coords, int operation, int y0
      , int height) {
    double x = params[X].getDouble();
    double y = params[Y].getDouble();
    double scale = params[SCALE].getDouble();
    double angle = params[ANGLE].getDouble();
    double multiplier = params[MULTIPLICATION].getDouble();
    initCoords(coords, y0, height);
    Affine.apply(coords, x, y, scale, angle);
    for(Element transformations : params[TRANSFORMATIONS].getList())
      transformations.applyTransformation(coords);
    params[PATTERN].getPattern().render(coords, pixels, operation, multiplier);
  }
}
