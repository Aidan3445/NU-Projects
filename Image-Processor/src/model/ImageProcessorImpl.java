package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class represents an image processor that holds images and
 * interoperates the pixels of an {@link Image} object and performs an effect.
 */
public class ImageProcessorImpl implements ImageProcessingModel {
  private final Map<String, Image> images;

  /**
   * Construct a new image processor with no open/stored files.
   */
  public ImageProcessorImpl() {
    this.images = new HashMap<String, Image>();
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
    Image i = this.images.get(name);
    if (i == null) {
      throw new IllegalArgumentException(
              String.format("Image with the provided name '%s' does not exist", name));
    }
    return i;
  }

  /**
   * Get a list of names for all images held in the model.
   *
   * @return the list of names
   */
  @Override
  public List<String> getAll() {
    Set<String> set = this.images.keySet();
    List<String> list = new ArrayList<String>(set);
    Collections.sort(list);
    return list;
  }

  /**
   * Loads and  sets the image from the map name to the provided name.
   *
   * @param name  the name to refer to this image
   * @param image the image to be loaded into the model
   */
  @Override
  public void load(String name, Image image) {
    this.images.put(name, image);
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
    this.colorComponent(0, name, dest);
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
    this.colorComponent(1, name, dest);
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
    this.colorComponent(2, name, dest);
  }

  /**
   * Create a color component image from the map by getting the corresponding rgb value for
   * every pixel.
   * @param index the index to use to retrieve the rgb value
   * @param name the name of the image to use
   * @param dest the name to refer to this image
   */
  private void colorComponent(int index, String name, String dest) {
    Function3<Image, Integer, Integer, double[]> color = (original, i, j) -> {
      double[] rgb = original.getPixel(i, j);
      return new double[]{rgb[index], rgb[index], rgb[index], 1};
    };
    this.process(name, dest, color);
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
    Function3<Image, Integer, Integer, double[]> value = (original, i, j) -> {
      double[] rgb = original.getPixel(i, j);
      double v = Math.max(Math.max(rgb[0], rgb[1]), rgb[2]);
      return new double[]{v, v, v, 1};
    };
    this.process(name, dest, value);
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
    Function3<Image, Integer, Integer, double[]> luma = (original, i, j) -> {
      double[] rgb = original.getPixel(i, j);
      double l = 0.2126 * rgb[0]
              + 0.7152 * rgb[1]
              + 0.0722 * rgb[2];
      return new double[]{l, l, l, 1};
    };
    this.process(name, dest, luma);
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
    Function3<Image, Integer, Integer, double[]> intensity = (original, i, j) -> {
      double[] rgb = original.getPixel(i, j);
      double t = (rgb[0] + rgb[1] + rgb[2]) / 3;
      return new double[]{t, t, t, 1};
    };
    this.process(name, dest, intensity);
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
    double[][] mat = {{0.2126, 0.7152, 0.0722},
        {0.2126, 0.7152, 0.0722},
        {0.2126, 0.7152, 0.0722}};
    this.colorTransform(name, dest, mat);
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
    double[][] mat = {{0.393, 0.769, 0.189},
        {0.349, 0.686, 0.168},
        {0.272, 0.534, 0.131}};
    this.colorTransform(name, dest, mat);
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
    Function3<Image, Integer, Integer, double[]> brighten = (original, i, j) -> {
      double[] rgb = original.getPixel(i, j);
      double incrementDouble = increment / 255.;
      return new double[]{Math.max(0, Math.min(1, rgb[0] + incrementDouble)),
              Math.max(0, Math.min(1, rgb[1] + incrementDouble)),
              Math.max(0, Math.min(1, rgb[2] + incrementDouble)), 1};
    };
    this.process(name, dest, brighten);
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
    if (amount <= 0) {
      throw new IllegalArgumentException("Blur amount must be a positive integer.");
    }
    Function3<Image, Integer, Integer, double[]> blur = (original, i, j) -> original.getPixel(i, j);
    int size = amount * 2 + 1;
    double stDevSqr = Math.pow(amount, -2);
    double[][] k = new double[size][size];
    for (int i = -amount; i <= amount; i++) {
      for (int j = -amount; j <= amount; j++) {
        k[amount + i][amount + j] = (1 / (2 * Math.PI * stDevSqr))
                * Math.exp((-(Math.pow(i, 2) + Math.pow(j, 2)) / 2 * stDevSqr));
      }
    }
    this.kernel(name, dest, k, blur);
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
    Function3<Image, Integer, Integer, double[]> sharpen =
        (original, i, j) -> original.getPixel(i, j);
    double[][] k = {{-1 / 8., -1 / 8., -1 / 8., -1 / 8., -1 / 8.},
        {-1 / 8., 1 / 4., 1 / 4., 1 / 4., -1 / 8.},
        {-1 / 8., 1 / 4., 1, 1 / 4., -1 / 8.},
        {-1 / 8., 1 / 4., 1 / 4., 1 / 4., -1 / 8.},
        {-1 / 8., -1 / 8., -1 / 8., -1 / 8., -1 / 8.}};
    this.kernel(name, dest, k, sharpen);
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
    Function3<Image, Integer, Integer, double[]> horizontal = (original, i, j) -> {
      int width = original.getWidth();
      return original.getPixel(i, width - j - 1);
    };
    this.process(name, dest, horizontal);
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
    Function3<Image, Integer, Integer, double[]> vertical = (original, i, j) -> {
      int height = original.getHeight();
      return original.getPixel(height - i - 1, j);
    };
    this.process(name, dest, vertical);
  }

  /**
   * Create an image by applying the provided color transformation (in the form of a matrix)
   * to the provided image.
   *
   * @param name   the name of the image to use
   * @param dest   the name to refer to this image
   * @param matrix the color transformation in the form of a matrix to use
   */
  private void colorTransform(String name, String dest, double[][] matrix) {
    Function3<Image, Integer, Integer, double[]> sepia = (original, i, j) -> {
      double[] rgb = original.getPixel(i, j);
      double r = rgb[0];
      double g = rgb[1];
      double b = rgb[2];
      double a = rgb[3];
      double[] out = new double[]{r * matrix[0][0] + g * matrix[0][1] + b * matrix[0][2],
          r * matrix[1][0] + g * matrix[1][1] + b * matrix[1][2],
          r * matrix[2][0] + g * matrix[2][1] + b * matrix[2][2], a};
      return out;
    };
    this.process(name, dest, sepia);
  }

  /**
   * Create an image by applying the provided image
   * processing effect on a single pixel (kernel size 0).
   *
   * @param name   the name of the image to use
   * @param dest   the name to refer to this image
   * @param effect the effect to use to create a new image
   * @throws IllegalArgumentException if an image with the provided name does not exist
   */
  private void process(String name, String dest,
                       Function3<Image, Integer, Integer, double[]> effect)
          throws IllegalArgumentException {
    double[][] k = {{1}};
    this.kernel(name, dest, k, effect);
  }

  /**
   * Create an image by applying the provided image processing
   * effect on a set of pixels with determined by the given kernel.
   *
   * @param name   the name of the image to use
   * @param dest   the name to refer to this image
   * @param kernel the kernel to use
   * @param effect the effect to use to create a new image
   * @throws IllegalArgumentException if an image with the provided name does not exist; or
   *                                  if the kernel has even dimensions or is size 0
   */
  private void kernel(String name, String dest, double[][] kernel,
                      Function3<Image, Integer, Integer, double[]> effect)
          throws IllegalArgumentException {
    Image original = this.getImage(name);
    int height = original.getHeight();
    int width = original.getWidth();
    int kernelHeight = kernel.length;
    int kernelWidth;
    try {
      kernelWidth = kernel[0].length;
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Kernel size must not be zero");
    }
    if (kernelHeight * kernelWidth % 2 == 0) {
      System.out.printf("%d, %d%n", kernelHeight, kernelWidth);
      throw new IllegalArgumentException("Kernel must have odd dimensions");
    }
    double[][][] newPixels = new double[height][width][3];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        double r = 0;
        double g = 0;
        double b = 0;
        double a = 0;
        double totalWeights = 0;
        for (int k = 0; k < kernelHeight; k++) {
          for (int l = 0; l < kernelWidth; l++) {
            int row = i + k - kernelHeight / 2;
            int col = j + l - kernelWidth / 2;
            if (row >= 0 && col >= 0 && row < height && col < width) {
              double[] pixel = effect.apply(original, row, col);
              r += kernel[k][l] * pixel[0];
              g += kernel[k][l] * pixel[1];
              b += kernel[k][l] * pixel[2];
              a += kernel[k][l] * pixel[3];
              totalWeights += kernel[k][l];
            }
          }
        }
        r /= totalWeights;
        g /= totalWeights;
        b /= totalWeights;
        a /= totalWeights;
        newPixels[i][j] = new double[]{Math.max(0, Math.min(r, 1)), Math.max(0, Math.min(g, 1)),
                Math.max(0, Math.min(b, 1)), Math.max(0, Math.min(a, 1))};
      }
    }
    this.images.put(dest, new ImageImpl(newPixels, original.getMaxValue()));
  }
}
