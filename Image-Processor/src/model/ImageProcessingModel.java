package model;

/**
 * Interface represents the image processing operations
 * that can be performed on an image.
 */
public interface ImageProcessingModel extends ImageProcessingViewModel {
  /**
   * Loads and  sets the image name to the provided name.
   *
   * @param name the name to refer to this image
   * @param image the image to be loaded into the model
   */
  public void load(String name, Image image);

  /**
   * Create a greyscale image by getting the red value of every pixel.
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  void redComponent(String name, String dest) throws IllegalArgumentException;

  /**
   * Create a greyscale image by getting the green value of every pixel.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  void greenComponent(String name, String dest) throws IllegalArgumentException;

  /**
   * Create a greyscale image by getting the blue value of every pixel.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  void blueComponent(String name, String dest) throws IllegalArgumentException;

  /**
   * Create a greyscale image by getting the maximum value for rgb of every pixel.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  void valueComponent(String name, String dest) throws IllegalArgumentException;

  /**
   * Create a greyscale image by calculating the luma of every pixel.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  void lumaComponent(String name, String dest) throws IllegalArgumentException;

  /**
   * Create a greyscale image by calculating the intensity of every pixel.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  void intensityComponent(String name, String dest) throws IllegalArgumentException;

  /**
   * Create a greyscale image of the provided image.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  void greyscale(String name, String dest) throws IllegalArgumentException;

  /**
   * Create a sepia toned image of the provided image.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  void sepia(String name, String dest) throws IllegalArgumentException;

  /**
   * Brighten or darken the image by a given increment.
   *
   * @param increment amount to brighten by, if negative will darken image
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  void brighten(int increment, String name, String dest) throws IllegalArgumentException;

  /**
   * Blurs an image from the map by a given amount.
   *
   * @param amount amount to blur by
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded or the given amount is
   *                                  less than or equal to 0
   */
  void gaussianBlur(int amount, String name, String dest) throws IllegalArgumentException;

  /**
   * Sharpens the image.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  void sharpen(String name, String dest) throws IllegalArgumentException;

  /**
   * Flips an image horizontally.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  void horizontalFlip(String name, String dest) throws IllegalArgumentException;

  /**
   * Flips an image vertically.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  void verticalFlip(String name, String dest) throws IllegalArgumentException;
}
