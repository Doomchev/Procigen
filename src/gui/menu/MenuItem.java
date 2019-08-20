package gui.menu;

import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JMenuItem;

public abstract class MenuItem extends JMenuItem implements ActionListener {
  public MenuItem(String caption) {
    super(caption);
  }

  public void addTo(JComponent menu) {
    addActionListener(this);
    menu.add(this);
  }
}
