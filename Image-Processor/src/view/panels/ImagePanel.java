package view.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import model.ImageImpl;
import model.ImageProcessingViewModel;
import model.ImageUtil;

/**
 * Class represents the visible panel of the image processor.
 */
public class ImagePanel extends JPanel {
  private final ImageProcessingViewModel model;
  private BufferedImage image;

  /**
   * Construct a new image processor panel with a given image processor model.
   *
   * @param model the model to use
   * @param d the Dimension object to use
   * @throws IllegalArgumentException if the provided {@link ImageProcessingViewModel} is null or
   *                                  if the provided {@link Dimension} is null
   */
  public ImagePanel(ImageProcessingViewModel model, Dimension d)
          throws IllegalArgumentException {
    super();
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }
    if (d == null) {
      throw new IllegalArgumentException("Dimension cannot be null.");
    }
    this.model = model;
    this.setBackground(Color.white);
    d = new Dimension(d.width * 8 / 10, d.height);
    this.setPreferredSize(d);
    this.setImage();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.setImage();
    int[] pos = this.imageAlign();
    g.drawImage(this.image, pos[0], pos[1], Color.white, this);
  }

  /**
   * Aligns this image to be centered within the panel.
   * @return the x and y coordinate for the upper left-hand corner.
   */
  private int[] imageAlign() {
    int[] pos = new int[2];
    int imageWidth = this.image.getWidth();
    int panelWidth = this.getWidth();
    int imageHeight = this.image.getHeight();
    int panelHeight = this.getHeight();
    if (imageWidth < panelWidth) {
      pos[0] = (panelWidth / 2) - (imageWidth / 2);
    }
    if (imageHeight < panelHeight) {
      pos[1] = (panelHeight / 2) - (imageHeight / 2);
    }
    return pos;
  }

  /**
   * Creates a buffered image of this image.
   */
  private void setImage() {
    model.Image i;
    try {
      i = this.model.getImage("current");
    } catch (IllegalArgumentException e) {
      i = new ImageImpl(1, 1, 255);
    }
    int width = i.getWidth();
    int height = i.getHeight();
    BufferedImage b = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    ImageUtil.setBufferedImage(i, b);
    this.image = b;
    this.setPreferredSize(new Dimension(width, height));
    this.revalidate();
  }
}
