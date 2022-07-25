package view.panels;

import model.Image;

/**
 * Interface represents the methods for a histogram panel object.
 */
public interface HistogramPanel {
  /**
   * Update the histogram to display the given image's RGBI data.
   * @param image image to use
   * @throws IllegalArgumentException if the image is null
   */
  void update(Image image) throws IllegalArgumentException;
}
