package gui.menu;

import base.Main;
import base.Serialization;
import java.awt.event.ActionEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import javax.swing.JFileChooser;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import javax.swing.filechooser.FileFilter;
import structure.Project;

public class FileMenuItem extends MenuItem {
  public static final SimpleDateFormat sdf
      = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
  
  public final static int OPEN = 0, SAVE = 1, SAVE_AS = 2;
  public int operation;

  public FileMenuItem(String caption, int operation) {
    super(caption);
    this.operation = operation;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
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
    })
        ;
    switch(operation) {
      case OPEN:
        if(chooser.showDialog(null, "Select file to open") == APPROVE_OPTION)
          Serialization.load(chooser.getSelectedFile(), true);
        break;
      case SAVE_AS:
        if(chooser.showDialog(null, "Select file to save as") == APPROVE_OPTION) {
          String fileName = chooser.getSelectedFile().getPath();
          if(!fileName.endsWith(".pgi")) fileName += ".pgi";
          File file = new File(fileName);
          if(file.exists()) backupFile(file);
          Serialization.save(file, Project.instance);
        }
        break;
    }
  }

  private void backupFile(File file) {
    String backupDir = file.getParent() + "/backup/" + file.getName();
    String backupName = backupDir + "/" 
        + sdf.format(new Timestamp(file.lastModified())) + ".pgi";
    new File(backupDir).mkdir();
    try {
      InputStream in = new BufferedInputStream(new FileInputStream(file));
      OutputStream out = new BufferedOutputStream(new FileOutputStream(
          new File(backupName)));
      byte[] buffer = new byte[1024];
      int lengthRead;
      while ((lengthRead = in.read(buffer)) > 0) {
          out.write(buffer, 0, lengthRead);
          out.flush();
      }
    } catch (FileNotFoundException ex) {
    } catch (IOException ex) {
    }
  }
}
