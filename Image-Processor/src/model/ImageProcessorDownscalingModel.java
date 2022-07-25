package model;

import java.util.List;

/**
 * Decorator for image processor that allows downscaling.
 */
public class ImageProcessorDownscalingModel extends ImageProcessorImpl
        implements ImageProcessingDownscaling {
  private final ImageProcessingModel model;

  /**
   * Construct a new image processor with downscaling and initialize its child model.
   *
   * @param model model to pass other functions onto
   * @throws IllegalArgumentException if the model is null
   */
  public ImageProcessorDownscalingModel(ImageProcessingModel model)
          throws IllegalArgumentException {
    super();
    if (model == null) {
      throw new IllegalArgumentException("The provided model is null.");
    }
    this.model = model;
  }

  /**
   * Get an image with a given name from an image processing model.
   *
   * @param name name of the image as it is kept within the model
   * @return the image object
   * @throws IllegalArgumentException if the name does not exist in the model
   */
  @Override
  public Image getImage(String name) throws IllegalArgumentException {
    return this.model.getImage(name);
  }

  /**
   * Get a list of names for all images held in the model.
   *
   * @return the list of names
   */
  @Override
  public List<String> getAll() {
    return this.model.getAll();
  }

  /**
   * Loads and  sets the image from the map name to the provided name.
   *
   * @param name  the name to refer to this image
   * @param image the image to be loaded into the model
   */
  @Override
  public void load(String name, Image image) {
    this.model.load(name, image);
  }

  /**
   * Create a greyscale image from the map by getting the red value of every pixel.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void redComponent(String name, String dest) throws IllegalArgumentException {
    this.model.redComponent(name, dest);
  }

  /**
   * Create a greyscale image from the map by getting the green value of every pixel.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void greenComponent(String name, String dest) throws IllegalArgumentException {
    this.model.greenComponent(name, dest);
  }

  /**
   * Create a greyscale image from the map by getting the blue value of every pixel.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void blueComponent(String name, String dest) throws IllegalArgumentException {
    this.model.blueComponent(name, dest);
  }

  /**
   * Create a greyscale image from the map by getting the maximum value for rgb of every pixel.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void valueComponent(String name, String dest) throws IllegalArgumentException {
    this.model.valueComponent(name, dest);
  }

  /**
   * Create a greyscale image from the map by calculating the luma of every pixel.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void lumaComponent(String name, String dest) throws IllegalArgumentException {
    this.model.lumaComponent(name, dest);
  }

  /**
   * Create a greyscale image from the map by calculating the intensity of every pixel.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void intensityComponent(String name, String dest) throws IllegalArgumentException {
    this.model.intensityComponent(name, dest);
  }

  /**
   * Create a greyscale image of the provided image.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void greyscale(String name, String dest) throws IllegalArgumentException {
    this.model.greyscale(name, dest);
  }

  /**
   * Create a sepia toned image of the provided image.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void sepia(String name, String dest) throws IllegalArgumentException {
    this.model.sepia(name, dest);
  }

  /**
   * Brighten or darken an image from the map by a given increment.
   *
   * @param increment amount to brighten by, if negative will darken image
   * @param name      the name of the image to use
   * @param dest      the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void brighten(int increment, String name, String dest) throws IllegalArgumentException {
    this.model.brighten(increment, name, dest);
  }

  /**
   * Blurs an image from the map image by a given amount.
   *
   * @param amount amount to blur by (absolute value)
   * @param name   the name of the image to use
   * @param dest   the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded or the given amount is
   *                                  less than or equal to 0
   */
  @Override
  public void gaussianBlur(int amount, String name, String dest) throws IllegalArgumentException {
    this.model.gaussianBlur(amount, name, dest);
  }

  /**
   * Sharpens an image from the map.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void sharpen(String name, String dest) throws IllegalArgumentException {
    this.model.sharpen(name, dest);
  }

  /**
   * Flips an image from the map horizontally.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void horizontalFlip(String name, String dest) throws IllegalArgumentException {
    this.model.horizontalFlip(name, dest);
  }

  /**
   * Flips an image from the map vertically.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void verticalFlip(String name, String dest) throws IllegalArgumentException {
    this.model.verticalFlip(name, dest);
  }

  /**
   * Downscales an image by a given percent in each direction.
   *
   * @param pctX percent to shrink image horizontally
   * @param pctY percent to shrink image vertically
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if pctX or pctY are less than 0 or greater than 100
   */
  @Override
  public void downscale(int pctX, int pctY, String name, String dest) {
    Image image = ImageUtil.downscale(model.getImage(name), pctX, pctY);
    this.model.load(dest, image);
  }
}
