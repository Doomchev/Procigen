package structure.transformations;

import parameters.ParameterTemplate;

public class LoopSector extends Transformation {
  public static final int SECTOR_SIZE = 0, TYPE = 1;
  
  static {
    ParameterTemplate[] templates = new ParameterTemplate[2];
    templates[SECTOR_SIZE] = new ParameterTemplate("Sector size", 3.0, 2.0
        , 0.0, 1.0);
    templates[TYPE] = new ParameterTemplate("Type", orientation);
    parameterTemplates.put(LoopSector.class, templates);
  }
  

  @Override
  public void applyTransformation(double[] coords) {
    if(hide) return;
    double sectorSize = PI2 / params[SECTOR_SIZE].getDouble();
    double k = 1.0 / sectorSize;
    double dAngle = 0.5 * sectorSize;
    double dAngle2 = dAngle + (params[TYPE].getValue() == VERTICAL ? 0.25 * PI2
        : 0);
    for(int index = 0; index < coords.length; index += 2) {
      double x = coords[index];
      double y = coords[index + 1];
      double radius = Math.sqrt(x * x + y * y);
      double angle = (Math.atan2(y, x) + dAngle2) * k;
      angle = (angle - Math.floor(angle)) * sectorSize - dAngle;
      coords[index] = Math.cos(angle) * radius;
      coords[index + 1] = Math.sin(angle) * radius;
    }
  }
}
