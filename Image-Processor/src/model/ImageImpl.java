package model;

/**
 * Class represents an image.
 */
public class ImageImpl implements Image {
  private final double[][][] pixels;
  private final int width;
  private final int height;
  private final int maxValue;

  /**
   * Creates an image with the provided array of pixels.
   * @param pixels the array of pixels of the image to use
   * @throws IllegalArgumentException if the provided array is null
   */
  public ImageImpl(double[][][] pixels, int maxValue) throws IllegalArgumentException {
    if (pixels == null) {
      throw new IllegalArgumentException("The provided pixels array cannot be null.");
    }
    this.pixels = pixels;
    this.width = this.pixels[0].length;
    this.height = this.pixels.length;
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (pixels[i][j].length != 4) {
          throw new IllegalArgumentException("All provided pixels must be in RGBA format.");
        }
      }
    }
    this.maxValue = maxValue;
  }

  /**
   * Create a blank image with given width and height.
   *
   * @param width width of blank image
   * @param height height of blank image
   * @param maxValue max value of blank image
   * @throws IllegalArgumentException if width or height is less than 1
   */
  public ImageImpl(int width, int height, int maxValue) throws IllegalArgumentException {
    this.maxValue = maxValue;
    if (width < 1 || height < 1) {
      throw new IllegalArgumentException(
              "Image must have a positive non-zero width and/or height.");
    }
    this.pixels = new double[height][width][4];
    this.width = width;
    this.height = height;
  }

  /**
   * Retrieves a pixel in this image.
   *
   * @param col the row number to use
   * @param row the column number to use
   * @return the pixel as its RGB value
   */
  @Override
  public double[] getPixel(int row, int col) throws IllegalArgumentException {
    if (col > width || row > height) {
      throw new IllegalArgumentException(String.format("Row %d and column %d is out of bounds",
              row, col));
    }
    return this.pixels[row][col];
  }

  /**
   * Gets the maximum color value associated with this image.
   */
  @Override
  public int getMaxValue() {
    return this.maxValue;
  }

  /**
   * Gets the width of this image in pixels.
   *
   * @return the width of this image in pixels
   */
  @Override
  public int getWidth() {
    return this.width;
  }

  /**
   * Gets the height of this image in pixels.
   *
   * @return the height of this image in pixels
   */
  @Override
  public int getHeight() {
    return this.height;
  }
}
