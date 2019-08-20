package gui;

import java.util.LinkedList;
import structure.Element;

public abstract class GUIContainer extends GUIElement {
  public Element object;
  public final LinkedList<ElementBlock> blocks = new LinkedList<>();
  public int elementX = 0, elementY = 0, scrollX = 0, scrollY = 0;
  
  public void clear() {
    blocks.clear();
    elementX = 0;
    elementY = 0;
  }

  public ElementBlock getBlock(int x, int y) {
    for(ElementBlock block : blocks) if(block.atPosition(x, y)) return block;
    return null;
  }
}
