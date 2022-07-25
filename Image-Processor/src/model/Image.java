package model;

/**
 * Image interface represents the operations available to an image.
 */
public interface Image {
  /**
   * Retrieves a pixel in this image.
   * @param row the row number to use
   * @param col the column number to use
   * @return the pixel as its RGB value
   */
  double[] getPixel(int row, int col);

  /**
   * Gets the maximum color value associated with this image.
   */
  int getMaxValue();

  /**
   * Gets the width of this image in pixels.
   * @return the width of this image in pixels
   */
  int getWidth();

  /**
   * Gets the height of this image in pixels.
   * @return the height of this image in pixels
   */
  int getHeight();
}
