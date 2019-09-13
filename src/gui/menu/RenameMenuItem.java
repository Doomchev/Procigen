package gui.menu;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import structure.Element;

public class RenameMenuItem extends MenuItem {
  Element element;

  public RenameMenuItem(Element element) {
    super("Rename");
    this.element = element;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    element.rename(JOptionPane.showInputDialog("Enter name", element.getName()));
  }

}
