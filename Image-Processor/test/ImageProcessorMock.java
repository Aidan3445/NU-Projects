import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Image;
import model.ImageImpl;
import model.ImageProcessingModel;

/**
 * A mock model for controller testing.
 */
public class ImageProcessorMock implements ImageProcessingModel {
  private final StringBuilder log;

  /**
   * Creates a log to read from with the provided StringBuilder object.
   *
   * @param log the StringBuilder object to use
   */
  public ImageProcessorMock(StringBuilder log) {
    this.log = log;
  }


  /**
   * Loads and sets the image name to the provided name.
   *
   * @param name  the name to refer to this image
   * @param image the image to be loaded into the model
   */
  @Override
  public void load(String name, Image image) {
    this.log.append("load: ");
    this.log.append(String.format("Name: %s", name));
  }

  /**
   * Create a greyscale image by getting the red value of every pixel.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void redComponent(String name, String dest) throws IllegalArgumentException {
    this.log.append("red-component: ");
    this.log.append(String.format("Name of image to use: %s New name: %s", name, dest));
  }

  /**
   * Create a greyscale image by getting the green value of every pixel.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void greenComponent(String name, String dest) throws IllegalArgumentException {
    this.log.append("green-component: ");
    this.log.append(String.format("Name of image to use: %s New name: %s", name, dest));
  }

  /**
   * Create a greyscale image by getting the blue value of every pixel.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void blueComponent(String name, String dest) throws IllegalArgumentException {
    this.log.append("blue-component: ");
    this.log.append(String.format("Name of image to use: %s New name: %s", name, dest));
  }

  /**
   * Create a greyscale image by getting the maximum value for rgb of every pixel.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void valueComponent(String name, String dest) throws IllegalArgumentException {
    this.log.append("value-component: ");
    this.log.append(String.format("Name of image to use: %s New name: %s", name, dest));
  }

  /**
   * Create a greyscale image by calculating the luma of every pixel.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void lumaComponent(String name, String dest) throws IllegalArgumentException {
    this.log.append("luma-component: ");
    this.log.append(String.format("Name of image to use: %s New name: %s", name, dest));
  }

  /**
   * Create a greyscale image by calculating the intensity of every pixel.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void intensityComponent(String name, String dest) throws IllegalArgumentException {
    this.log.append("intensity-component: ");
    this.log.append(String.format("Name of image to use: %s New name: %s", name, dest));
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
    this.log.append("greyscale: ");
    this.log.append(String.format("Name of image to use: %s New name: %s", name, dest));
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
    this.log.append("sepia: ");
    this.log.append(String.format("Name of image to use: %s New name: %s", name, dest));
  }

  /**
   * Brighten the image by a given increment.
   *
   * @param increment amount to brighten by, if negative will darken image
   * @param name      the name of the image to use
   * @param dest      the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void brighten(int increment, String name, String dest) throws IllegalArgumentException {
    this.log.append("brighten: ");
    this.log.append(String.format("Increment: %d Name of image to use: %s New name: %s",
            increment, name, dest));
  }

  /**
   * Blurs the image by a given amount.
   *
   * @param amount amount to blur by
   * @param name   the name of the image to use
   * @param dest   the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void gaussianBlur(int amount, String name, String dest) throws IllegalArgumentException {
    this.log.append("gaussian-blur: ");
    this.log.append(String.format("Amount: %d Name of image to use: %s New name: %s",
            amount, name, dest));
  }

  /**
   * Sharpens the image.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void sharpen(String name, String dest) throws IllegalArgumentException {
    this.log.append("sharpen: ");
    this.log.append(String.format("Name of image to use: %s New name: %s",
            name, dest));
  }

  /**
   * Flips an image horizontally.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void horizontalFlip(String name, String dest) throws IllegalArgumentException {
    this.log.append("horizontal-flip: ");
    this.log.append(String.format("Name of image to use: %s New name: %s", name, dest));
  }

  /**
   * Flips an image vertically.
   *
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   * @throws IllegalArgumentException if the string name has not been loaded
   */
  @Override
  public void verticalFlip(String name, String dest) throws IllegalArgumentException {
    this.log.append("vertical-flip: ");
    this.log.append(String.format("Name of image to use: %s New name: %s", name, dest));
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
    this.log.append("get-image: ");
    this.log.append(String.format("Name of image to get: %s", name));
    return new ImageImpl(new double[][][]{{{0., 0., 0., 0.}}}, 255);
  }

  /**
   * Get a list of names for all images held in the model.
   *
   * @return the list of names
   */
  @Override
  public List<String> getAll() {
    return new ArrayList<String>(Arrays.asList("TEST.notimg"));
  }
}
