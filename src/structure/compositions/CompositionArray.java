package structure.compositions;

import parameters.ParameterTemplate;

public class CompositionArray extends SingleComposition {
  public static final int ITEMS_OPERATION = 9, QUANTITY = 10;
  public static double[] arrayPixels = null;
  
  static {
    ParameterTemplate[] templates = new ParameterTemplate[11];
    setTemplates(templates, "Composition array");
    templates[ITEMS_OPERATION] = new ParameterTemplate("Operation", operations);
    templates[QUANTITY] = new ParameterTemplate("Items quantity", 1.0, 1.0
        , 0.0, 1.0);
    parameterTemplates.put(CompositionArray.class, templates);
  }

  @Override
  public void init() {
    super.init();
    arrayPixels = null;
  }
  
  @Override
  public void render(double[] pixels, double[] coords, int y0, int height) {
    if(hide) return;
    int quantity = params[QUANTITY].getInt();
    int operation = params[OPERATION].getValue();
    int itemsOperation = params[ITEMS_OPERATION].getValue();
    quantityValue = quantity;
    if(itemsOperation == operation) {
      for(nValue = 0; nValue < quantity; nValue++)
        render(pixels, coords, itemsOperation, y0, height);
    } else {
      if(arrayPixels == null) arrayPixels = new double[pixels.length];
      for(nValue = 0; nValue < quantity; nValue++)
        render(arrayPixels, coords, itemsOperation, y0, height);
      mix(arrayPixels, pixels, itemsOperation);
    }
    double multiplier = params[MULTIPLICATION].getDouble();
    for(int index = 0; index < pixels.length; index++) pixels[index] *= multiplier;
  }
}
