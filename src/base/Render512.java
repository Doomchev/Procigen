package base;

import java.io.File;
import javax.swing.JFileChooser;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

public class Render512 {
  public static void main(String[] args) {
    JFileChooser chooser = new JFileChooser(new File("./examples"));
    chooser.setFileFilter(new FileFilter() {
      @Override
      public boolean accept(File file) {
        return file.getName().endsWith(".pgi");
      }

      @Override
      public String getDescription() {
        return "Procedurally generated image (*.pgi)";
      }
    });
    if(chooser.showDialog(null, "Select file to open") == APPROVE_OPTION) {
      Main.frame = new JFrame();
      Main.frame.setVisible(true);
      Serialization.load(new File("configuration.bin"), false);
      Serialization.load(chooser.getSelectedFile(), true);
      Main.renderToImages(512, 512, true);
      Main.frame.dispose();
    }
  }
}
