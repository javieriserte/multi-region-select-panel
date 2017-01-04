package org.jiserte.multiselectimagepanel;

import java.awt.geom.Rectangle2D;

public class ImageRegion {
  
  private Rectangle2D region;
  private String      label;
  
  
  
  public Rectangle2D getRegion() {
    return region;
  }
  public void setRegion(Rectangle2D region) {
    this.region = region;
  }
  public String getLabel() {
    return label;
  }
  public void setLabel(String label) {
    this.label = label;
  }
  
  public ImageRegion(Rectangle2D region, String label) {
    super();
    this.region = region;
    this.label = label;
  }
  
  
  
}
