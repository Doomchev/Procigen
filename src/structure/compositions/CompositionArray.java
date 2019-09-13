package structure.compositions;

import base.RenderingBitmap;
import parameters.ParameterTemplate;

public class CompositionArray extends SingleComposition {
  public static final int ITEMS_OPERATION = 18, QUANTITY = 19;
  public static double[] arrayPixels = null;
  
  static {
    ParameterTemplate[] templates = new ParameterTemplate[20];
    setTemplates(templates, "Composition array");
    templates[ITEMS_OPERATION] = new ParameterTemplate("Items operation", operations);
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
  public void renderCoords(RenderingBitmap bitmap) {
    if(hide) return;
    int quantity = params[QUANTITY].getInt();
    int operation = params[OPERATION].getValue();
    int itemsOperation = params[ITEMS_OPERATION].getValue();
    quantityValue = quantity;
    if(itemsOperation == operation) {
      for(nValue = 0; nValue < quantity; nValue++) {
        cMultiplication = params[MULTIPLICATION].getDouble();
        cIncrement = params[INCREMENT].getDouble();
        render(bitmap, itemsOperation);
      }
    } else {
      /*if(arrayPixels == null) arrayPixels = new double[pixels.length];
      for(nValue = 0; nValue < quantity; nValue++)
        render(arrayPixels, coords, itemsOperation, y0, height);
      mix(arrayPixels, pixels, itemsOperation);*/
    }
  }
}
