package view.panels;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import model.Image;
import model.ImageUtil;

/**
 * Class represents the histogram panel of the image processor.
 */
public class HistogramPanelImpl extends JPanel implements HistogramPanel {
  private BufferedImage hGramImage;

  /**
   * Constructs a histogram panel.
   */
  public HistogramPanelImpl() {
    super();
    this.setLayout(new BorderLayout());
    this.setBackground(Color.white);

    // histogram
    this.hGramImage = new BufferedImage(256, 200, BufferedImage.TYPE_INT_ARGB);
    this.setMinimumSize(new Dimension(256, 200));
  }

  /**
   * Update the histogram to display the given image's RGBI data.
   * @param image image to use
   * @throws IllegalArgumentException if the image is null
   */
  @Override
  public void update(Image image) throws IllegalArgumentException {
    if (image == null) {
      throw new IllegalArgumentException("Image cannot be null.");
    }
    Image hGram = ImageUtil.createHistogram(image);
    this.hGramImage = new BufferedImage(
            hGram.getWidth(), hGram.getHeight(), BufferedImage.TYPE_INT_ARGB);
    ImageUtil.setBufferedImage(hGram, this.hGramImage);
    this.revalidate();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(this.hGramImage, 15, 50, Color.white, this);
  }
}