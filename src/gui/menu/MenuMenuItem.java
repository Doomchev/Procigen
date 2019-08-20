package gui.menu;

import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import javax.swing.JMenu;

public class MenuMenuItem extends MenuItem {
  public JMenu menu;
  
  public MenuMenuItem(String caption, JMenu menu) {
    super(caption);
    this.menu = menu;
  }

  @Override
  public void addTo(JComponent currentMenu) {
    currentMenu.add(menu);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
  }
}
