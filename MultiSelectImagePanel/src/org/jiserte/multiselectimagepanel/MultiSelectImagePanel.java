package org.jiserte.multiselectimagepanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MultiSelectImagePanel extends JPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 2348249578466166739L;

  private static int toolbarWidth = 100;

  private static int toolbarHeigth = 20;

  private List<Rectangle> regions;
  private List<String> regionsLabels;

  private int selectedRegionIndex;
  private boolean isDragging = false;

  private Point dragStart;

  private BufferedImage background;

  protected int draggingIndex;

  protected int q;

  public MultiSelectImagePanel() {
    super();

    this.regions = new ArrayList<>();

    this.regionsLabels = new ArrayList<>();

    this.background = null;

    this.selectedRegionIndex = -1;

    this.addMouseMotionListener(new MouseMotionListener() {

      @Override
      public void mouseMoved(MouseEvent e) {

        MultiSelectImagePanel parent = MultiSelectImagePanel.this;

        int regionIndex = parent.selectedRegionIndex;

        if (!parent.inToolbarArea(e.getX(), e.getY())) {

          regionIndex = parent.regionIndexFromPoint(e.getX(), e.getY());

          parent.selectedRegionIndex = regionIndex;

        }

        if (regionIndex >= 0) {

          Rectangle rectangle = parent.regions.get(regionIndex);

          int cx1i = (int) rectangle.getX() + 2;
          int cx1e = (int) rectangle.getX() + 2 + 12;
          int cx2i = (int) rectangle.getX() + (int) rectangle.getWidth() - 12;
          int cx2e = (int) rectangle.getX() + (int) rectangle.getWidth() - 2;

          int cy1i = (int) rectangle.getY() + 2;
          int cy1e = (int) rectangle.getY() + 2 + 12;
          int cy2i = (int) rectangle.getY() + (int) rectangle.getHeight() - 12;
          int cy2e = (int) rectangle.getY() + (int) rectangle.getHeight() - 2;

          int tbyi = (int) rectangle.getY() + (int) rectangle.getHeight() + 2;
          int tbye = (int) rectangle.getY() + (int) rectangle.getHeight() + 22;

          int tbx1i = (int) rectangle.getX() + (int) rectangle.getWidth() / 2
              - MultiSelectImagePanel.toolbarWidth/2 + 5;
          int tbx1e = (int) rectangle.getX() + (int) rectangle.getWidth() / 2
              - 1;
          int tbx2i = (int) rectangle.getX() + (int) rectangle.getWidth() / 2 +1 ;
          int tbx2e = (int) rectangle.getX() + (int) rectangle.getWidth() / 2
              + MultiSelectImagePanel.toolbarWidth/2 - 5;

          int q1 = (e.getX() >= cx1i && e.getX() < cx1e) ? 1 : 0;
          int q2 = (e.getX() >= cx2i && e.getX() < cx2e) ? 2 : 0;
          int q3 = (e.getY() >= cy1i && e.getY() < cy1e) ? 3 : 0;
          int q4 = (e.getY() >= cy2i && e.getY() < cy2e) ? 4 : 0;

          int q = q1 * q3 + q1 * q4 + q2 * q3 + q2 * q4;

          if (q == 0) {

            q = (e.getX() >= tbx1i && e.getX() <= tbx1e && e.getY() >= tbyi
                && e.getY() <= tbye) ? 9 : 0;

            q = q + ((e.getX() >= tbx2i && e.getX() <= tbx2e && e.getY() >= tbyi
                && e.getY() <= tbye) ? 10 : 0);

          }

          parent.q = q;
        }

        parent.updateUI();

      }

      @Override
      public void mouseDragged(MouseEvent e) {

        MultiSelectImagePanel parent = MultiSelectImagePanel.this;

        if (SwingUtilities.isLeftMouseButton(e) && parent.isDragging) {

          int i = parent.draggingIndex;

          if (i >= 0) {

            Rectangle rectangle = parent.regions.get(i);

            Point lastLocation = new Point((int) rectangle.getX(),
                (int) rectangle.getY());

            int q = parent.q;

            if (q == 0) {

              int currentX = (int) (lastLocation.getX() + e.getX()
                  - parent.dragStart.getX());
              int currentY = (int) (lastLocation.getY() + e.getY()
                  - parent.dragStart.getY());

              rectangle.setLocation(currentX, currentY);

              parent.dragStart = new Point(e.getX(), e.getY());

            }

            if (q == 8) {

              int currentW = (int) (rectangle.getWidth() + e.getX()
                  - parent.dragStart.getX());
              int currentH = (int) (rectangle.getHeight() + e.getY()
                  - parent.dragStart.getY());

              currentW = Math.max(24, currentW);
              currentH = Math.max(24, currentH);

              rectangle.setSize(currentW, currentH);

              parent.dragStart = new Point(e.getX(), e.getY());

            }

            if (q == 6) {

              int currentW = (int) (rectangle.getWidth() + e.getX()
                  - parent.dragStart.getX());
              int currentH = (int) (rectangle.getHeight()
                  - (e.getY() - parent.dragStart.getY()));
              int currentY = (int) (lastLocation.getY() + e.getY()
                  - parent.dragStart.getY());

              currentY = Math.min(currentY,
                  (int) lastLocation.getY() + (int) rectangle.getHeight() - 24);
              currentW = Math.max(24, currentW);
              currentH = Math.max(24, currentH);

              rectangle.setLocation((int) rectangle.getX(), currentY);
              rectangle.setSize(currentW, currentH);

              parent.dragStart = new Point(e.getX(), e.getY());

            }

            if (q == 4) {

              int currentW = (int) (rectangle.getWidth() - e.getX()
                  + parent.dragStart.getX());
              int currentH = (int) (rectangle.getHeight() + e.getY()
                  - parent.dragStart.getY());
              int currentX = (int) (lastLocation.getX() + e.getX()
                  - parent.dragStart.getX());

              currentX = Math.min(currentX,
                  (int) lastLocation.getX() + (int) rectangle.getWidth() - 24);
              
              currentW = Math.max(24, currentW);
              currentH = Math.max(24, currentH);

              rectangle.setLocation(currentX, (int) rectangle.getY());
              rectangle.setSize(currentW, currentH);

              parent.dragStart = new Point(e.getX(), e.getY());

            }

            if (q == 3) {

              int currentW = (int) (rectangle.getWidth() - e.getX()
                  + parent.dragStart.getX());
              int currentH = (int) (rectangle.getHeight() - e.getY()
                  + parent.dragStart.getY());
              int currentX = (int) (lastLocation.getX() + e.getX()
                  - parent.dragStart.getX());
              int currentY = (int) (lastLocation.getY() + e.getY()
                  - parent.dragStart.getY());

              currentY = Math.min(currentY,
                  (int) lastLocation.getY() + (int) rectangle.getHeight() - 24);
              
              currentX = Math.min(currentX,
                  (int) lastLocation.getX() + (int) rectangle.getWidth() - 24);
              
              currentW = Math.max(24, currentW);
              currentH = Math.max(24, currentH);

              rectangle.setLocation(currentX, currentY);
              rectangle.setSize(currentW, currentH);

              parent.dragStart = new Point(e.getX(), e.getY());

            }

            parent.updateUI();

          }

        }

      }
    });

    this.addMouseListener(new MouseListener() {

      @Override
      public void mouseReleased(MouseEvent e) {
        MultiSelectImagePanel parent = MultiSelectImagePanel.this;
        parent.isDragging = false;
      }

      @Override
      public void mousePressed(MouseEvent e) {
        MultiSelectImagePanel parent = MultiSelectImagePanel.this;
        int currentRegionIndex = parent.regionIndexFromPoint(e.getX(),
            e.getY());
        if (currentRegionIndex >= 0) {
          parent.dragStart = new Point(e.getPoint());
          parent.isDragging = true;
          parent.draggingIndex = currentRegionIndex;
        }
      }

      @Override
      public void mouseExited(MouseEvent e) { }

      @Override
      public void mouseEntered(MouseEvent e) { }

      @Override
      public void mouseClicked(MouseEvent e) {

        MultiSelectImagePanel parent = MultiSelectImagePanel.this;

        int regionIndex = parent.selectedRegionIndex;

        if (SwingUtilities.isLeftMouseButton(e)) {
          if (!parent.inToolbarArea(e.getX(), e.getY())) {
            regionIndex = parent.regionIndexFromPoint(e.getX(), e.getY());
            parent.selectedRegionIndex = regionIndex;
          }
          if (e.getClickCount() == 1) {
            if (regionIndex >= 0) {
              switch (parent.q) {
              case 9: // Add Text
                String s = (String) JOptionPane.showInputDialog(parent,
                    "Choose a label:", "User Input", JOptionPane.PLAIN_MESSAGE,
                    null, null, "region...");
                if ((s != null) && (s.length() > 0)) {
                  parent.regionsLabels.set(selectedRegionIndex, s);
                }
                break;

              case 10: // Remove
                parent.regions.remove(regionIndex);
                parent.regionsLabels.remove(regionIndex);
                parent.selectedRegionIndex = -1;
                parent.q = 0;
                break;
              default:
                break;
              }
              parent.updateUI();
            }
          }
          if (e.getClickCount() == 2) {
            parent.addRegion(new Rectangle(e.getX(), e.getY(), 30, 30));
            parent.updateUI();
          }
        }

      }
    });

  }

  @Override
  protected void paintComponent(Graphics g) {

    super.paintComponent(g);

    Graphics2D graphics2d = (Graphics2D) g;

    graphics2d.drawImage(this.background, 0, 0, null);

    graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    graphics2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
        RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    
    graphics2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
        RenderingHints.VALUE_STROKE_PURE);
    
    graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    
    
    // ---------------------------------------------------------------------- //
    // Draw area boxes of non selected regions
    for (int i = 0; i < this.regions.size(); i++) {

      if (i != this.selectedRegionIndex) {
        graphics2d.setStroke(new BasicStroke(1));
        graphics2d.setColor(new Color(230, 230, 230));
        BufferedImage im = new BufferedImage(
            (int)this.regions.get(i).getWidth(),
            (int)this.regions.get(i).getHeight(),
            BufferedImage.TYPE_INT_RGB);
        
        ((Graphics2D)im.getGraphics()).drawImage(
            this.background,
            0,
            0,
            (int) (this.regions.get(i).getWidth()),
            (int) (this.regions.get(i).getHeight()),
            (int) this.regions.get(i).getX(),
            (int) this.regions.get(i).getY(),
            (int) (this.regions.get(i).getX() + this.regions.get(i).getWidth()),
            (int) (this.regions.get(i).getY() + this.regions.get(i).getHeight()),
            null);
        
        RescaleOp rescaleOp = new RescaleOp(1.2f, 15, null);
        rescaleOp.filter(im, im);

        graphics2d.draw(this.regions.get(i));
        
        graphics2d.drawImage(
            im,
            (int) this.regions.get(i).getX(),
            (int) this.regions.get(i).getY(),
            (int) (this.regions.get(i).getX() + this.regions.get(i).getWidth()),
            (int) (this.regions.get(i).getY() + this.regions.get(i).getHeight()),
            0,
            0,
            (int) (this.regions.get(i).getWidth()),
            (int) (this.regions.get(i).getHeight()),
            null);
        
      }

    }
    // ---------------------------------------------------------------------- //

    if (this.selectedRegionIndex >= 0) {

      Color lightGreen = new Color(100, 220, 100);
      Color lightRed = new Color(220, 100, 100);
      Color lightBlue = new Color(255,255, 255);
      Color darkBlue = new Color(0, 140, 180);

      // -------------------------------------------------------------------- //
      // Draw Region Area box
      graphics2d.setColor(lightGreen);
      graphics2d.setStroke(new BasicStroke(1));

      Rectangle rectangle = this.regions.get(this.selectedRegionIndex);

      graphics2d.draw(rectangle);
      // -------------------------------------------------------------------- //

      // -------------------------------------------------------------------- //
      // Draw Region Label
      Font font = new Font("Arial", 0, 10);
      graphics2d.setFont(font);
      FontMetrics met = this.getFontMetrics(font);
      String regionLabel = this.regionsLabels.get(this.selectedRegionIndex);
      int regionLabelWidth = met.stringWidth(regionLabel);
      double centerX = rectangle.getX() + rectangle.getWidth() / 2;
      graphics2d.drawString(regionLabel, (int) (centerX - regionLabelWidth / 2),
          (int) rectangle.getY() - 2);
      // -------------------------------------------------------------------- //

      // -------------------------------------------------------------------- //
      // Draw Corner resizing markers
      Rectangle r = new Rectangle(0, 0, 8, 8);
      graphics2d.setStroke(new BasicStroke(1));

      graphics2d.setColor(this.q == 3 ? lightRed : lightGreen);
      r.setLocation((int) rectangle.getX() + 4, (int) rectangle.getY() + 4);
      graphics2d.fill(r);

      graphics2d.setColor(this.q == 4 ? lightRed : lightGreen);
      r.setLocation((int) rectangle.getX() + 4,
          (int) rectangle.getY() - 11 + (int) rectangle.getHeight());
      graphics2d.fill(r);

      graphics2d.setColor(this.q == 6 ? lightRed : lightGreen);
      r.setLocation((int) rectangle.getX() - 11 + (int) rectangle.getWidth(),
          (int) rectangle.getY() + 4);
      graphics2d.fill(r);

      graphics2d.setColor(this.q == 8 ? lightRed : lightGreen);
      r.setLocation((int) rectangle.getX() - 11 + (int) rectangle.getWidth(),
          (int) rectangle.getY() - 11 + (int) rectangle.getHeight());
      graphics2d.fill(r);
      // -------------------------------------------------------------------- //

      // -------------------------------------------------------------------- //
      // Draw toolbar
      Rectangle toolbar = new Rectangle(0, 0, toolbarWidth, toolbarHeigth);
      graphics2d.setColor(new Color(180, 180, 180, 230));

      graphics2d.setColor(new Color(0, 0, 0, 230));
      double bottomY = rectangle.getY() + rectangle.getHeight();
      toolbar.setLocation(
          new Point((int) (centerX - toolbarWidth/2), (int) (bottomY + 2)));

      graphics2d.fillRoundRect((int) toolbar.getX(), (int) toolbar.getY(),
          (int) toolbar.getWidth(), (int) toolbar.getHeight(), 10, 10);

      graphics2d.setColor(new Color(255, 255, 255, 50));
      graphics2d.drawRoundRect((int) toolbar.getX(), (int) toolbar.getY(),
          (int) toolbar.getWidth(), (int) toolbar.getHeight(), 10, 10);
      
      
      graphics2d.setFont(new Font("Arial", Font.BOLD, 12));
      FontMetrics fmt = graphics2d.getFontMetrics();
      String labelString = "Label";
      int labelWidth = fmt.stringWidth(labelString);
      graphics2d.setColor((q == 9) ? lightBlue : darkBlue);

      
      int optGap = 5;
      graphics2d.drawString(labelString, (int) centerX - labelWidth-optGap,
          (int) toolbar.getY() + 15);
      
      String closeString = "Cerrar";

      graphics2d.setColor((q == 10) ? lightBlue : darkBlue);
      graphics2d.drawString(closeString, (int) centerX + optGap,
          (int) toolbar.getY() + 15);
      // -------------------------------------------------------------------- //

    }

  }

  private boolean inToolbarArea(int x, int y) {

    if (this.selectedRegionIndex >= 0) {

      Rectangle currentRegion = this.regions.get(this.selectedRegionIndex);

      Rectangle toolbarR = new Rectangle(
          (int) (currentRegion.getX() + currentRegion.getWidth() / 2 - MultiSelectImagePanel.toolbarWidth/2),
          (int) (currentRegion.getY() + currentRegion.getHeight()), 
          MultiSelectImagePanel.toolbarWidth, 
          MultiSelectImagePanel.toolbarHeigth+2);

      boolean result = x >= toolbarR.getX()
          && x <= toolbarR.getX() + toolbarR.getWidth() && y >= toolbarR.getY()
          && y <= toolbarR.getY() + toolbarR.getHeight();

      return result;

    }
    return false;

  }

  public void addRegion(Rectangle region) {

    this.regions.add(region);

    this.regionsLabels.add("Region " + this.regions.size());

  }

  public void clearRegions() {

    this.regions.clear();
    this.regionsLabels.clear();

    this.selectedRegionIndex = -1;
    this.q = 0;

  }

  public void setImage(BufferedImage image) {

    this.background = image;

    this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
    this.updateUI();
  }

  public int regionIndexFromPoint(int x, int y) {

    for (int i = 0; i < this.regions.size(); i++) {

      Rectangle rectangle = this.regions.get(i);

      if (x >= rectangle.getX() && x < rectangle.getX() + rectangle.getWidth()
          && y >= rectangle.getY()
          && y < rectangle.getY() + rectangle.getHeight()) {

        return i;

      }

    }

    return -1;

  }
  
  public List<Rectangle> getRegions() {
    return this.regions;
  }
  
  public List<String> getLabels() {
    return this.regionsLabels;
  }

  public void setRegionsAndLabels(List<Rectangle> regions,
      List<String> labels) {

    this.regions = regions;
    this.regionsLabels = labels;
    
    this.updateUI();
    
  }

}
