package io.github.javieriserte.multiselectimagepanel;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class PanelTest extends JFrame {
  private static final long serialVersionUID = 1L;
  public PanelTest() throws HeadlessException {
    super();
    this.createGUI();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(
      new Runnable() {
        public void run() {
          PanelTest pt = new PanelTest();
          pt.setVisible(true);
          pt.setPreferredSize(new Dimension(1024,768));
          pt.setSize(new Dimension(1024,768));
          pt.setLocationRelativeTo(null);
          pt.setTitle("Multiple Region Select Image Panel Sample App");
          pt.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
          pt.pack();
        }
      }
    );
  }
  private void createGUI() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      MultiSelectImagePanel msip = new MultiSelectImagePanel();
      InputStream is = this
        .getClass()
        .getResourceAsStream("/SomeRegions.png");
      BufferedImage im = ImageIO.read(is);
      msip.setImage(im);
      msip.addRegion(new Rectangle(10, 10, 100, 100));
      msip.addRegion(new Rectangle(220, 30, 100, 100));
      this.add(
        new JScrollPane(
          msip,
          JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
          JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        )
      );
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
