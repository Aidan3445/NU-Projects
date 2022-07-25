package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.function.Function;

import javax.imageio.ImageIO;


/**
 * This class contains utility methods to read a PPM image from file and save a PPM image to a file.
 */
public class ImageUtil {
  /**
   * Read an image file in PNG, JPG, JPEG, BMP or other similar image formats,
   * not ppm.
   *
   * @param filename the path of the file
   * @return image object interpreted from the file
   */
  public static Image readImage(String filename) throws FileNotFoundException {
    if (ImageUtil.getExtension(filename).equalsIgnoreCase("ppm")) {
      return ImageUtil.readPPM(filename);
    }
    BufferedImage image;
    File f;
    try {
      f = new File(filename);
      image = ImageIO.read(f);
    } catch (IOException e) {
      throw new FileNotFoundException(e.getMessage());
    }
    int width = image.getWidth();
    int height = image.getHeight();
    double[][][] pixels = new double[height][width][4];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Color c = new Color(image.getRGB(j, i));
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();
        int a = c.getAlpha();
        pixels[i][j] = new double[]{r / 255., g / 255., b / 255., a / 255.};
      }
    }
    return new ImageImpl(pixels, 255);
  }

  /**
   * Saves the provided image as an image file with the provided file name and type,
   * which includes the path of where the file should be saved.
   *
   * @param image    the image to save
   * @param filename the name to use, including the path
   * @throws IOException              if an I/O error occurs writing to or creating the file,
   *                                  or the text cannot be encoded using the specified charset
   * @throws IllegalArgumentException if the fileType is not supported
   */
  public static void saveImage(Image image, String filename)
          throws IOException, IllegalArgumentException {
    if (image == null) {
      throw new IllegalArgumentException("Image cannot be null.");
    }
    int height = image.getHeight();
    int width = image.getWidth();
    BufferedImage img;
    String fileType = ImageUtil.getExtension(filename).toLowerCase();
    switch (fileType) {
      // special cases
      case "ppm":
        ImageUtil.savePPM(image, filename);
        return;
      // with alpha
      case "png":
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        break;
      // without alpha
      case "jpg":
      case "jpeg":
      case "bmp":
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        break;
      // unsupported types
      default:
        throw new IllegalArgumentException("The provided file extension is not supported");
    }
    ImageUtil.setBufferedImage(image, img);
    File f = new File(filename);
    ImageIO.write(img, fileType, f);
  }

  /**
   * Read an image file in the PPM format and return the image object.
   *
   * @param filename the path of the file
   * @return new image object from PPM file
   */
  private static Image readPPM(String filename) throws FileNotFoundException {
    Scanner sc;
    sc = new Scanner(new FileInputStream(filename));
    StringBuilder builder = new StringBuilder();
    // read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s).append(System.lineSeparator());
      }
    }
    // now set up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());
    String token;
    token = sc.next();
    if (!token.equals("P3")) {
      throw new FileNotFoundException("Invalid PPM file: plain ASCII file should begin with P3");
    }
    int width = sc.nextInt();
    int height = sc.nextInt();
    int maxValue = sc.nextInt();
    double[][][] pixels = new double[height][width][3];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        pixels[i][j] = new double[]{r / 255., g / 255., b / 255., 1};
      }
    }
    return new ImageImpl(pixels, maxValue);
  }

  /**
   * Saves the provided image as a ppm file with the provided file name, which includes the path
   * of where the file should be saved.
   *
   * @param image    the image to save
   * @param filename the name to use, including the path
   * @throws IOException if an I/O error occurs writing to or creating the file,
   *                     or the text cannot be encoded using the specified charset
   */
  private static void savePPM(Image image, String filename) throws IOException {
    StringBuilder ppm = new StringBuilder();
    ppm.append("P3").append(System.lineSeparator());
    ppm.append("# Created by Aidan and Cilly :)").append(System.lineSeparator());
    ppm.append(image.getWidth())
            .append(" ")
            .append(image.getHeight())
            .append(System.lineSeparator())
            .append(image.getMaxValue())
            .append(System.lineSeparator());
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        double[] rgb = image.getPixel(i, j);
        ppm.append((int) (rgb[0] * 255)).append(System.lineSeparator());
        ppm.append((int) (rgb[1] * 255)).append(System.lineSeparator());
        ppm.append((int) (rgb[2] * 255)).append(System.lineSeparator());
      }
    }
    Files.writeString(Paths.get(filename), ppm.toString(), StandardCharsets.UTF_8);
  }

  /**
   * Sets the pixels of a buffered image using an {@link Image} object.
   *
   * @param image  {@link Image} object to use
   * @param buffer {@link BufferedImage} object to use
   */
  public static void setBufferedImage(Image image, BufferedImage buffer) {
    if (image.getWidth() != buffer.getWidth() || image.getHeight() != buffer.getHeight()) {
      throw new IllegalArgumentException("Width and height must be the same.");
    }
    int width = image.getWidth();
    int height = image.getHeight();
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        double[] rgb = image.getPixel(i, j);
        Color c = new Color((int) (rgb[0] * 255), (int) (rgb[1] * 255),
                (int) (rgb[2] * 255), (int) (rgb[3] * 255));
        buffer.setRGB(j, i, c.getRGB());
      }
    }
  }

  /**
   * Downscales an image by a given percent in each direction.
   *
   * @param image image to downscale
   * @param pctX  percent to shrink image horizontally
   * @param pctY  percent to shrink image vertically
   * @throws IllegalArgumentException if pctX or pctY are less than 0 or greater than 100
   */
  public static Image downscale(Image image, int pctX, int pctY) {
    if (pctX < 0) {
      throw new IllegalArgumentException("Cannot downscale horizontally by negative percent.");
    }
    if (pctY < 0) {
      throw new IllegalArgumentException("Cannot downscale vertically by negative percent.");
    }
    if (pctX > 100) {
      throw new IllegalArgumentException("Cannot downscale horizontally by over 100 percent.");
    }
    if (pctY > 100) {
      throw new IllegalArgumentException("Cannot downscale vertically over 100 percent.");
    }
    double xRatio = pctX / 100.;
    double yRatio = pctY / 100.;
    int oldWidth = image.getWidth();
    int oldHeight = image.getHeight();
    int width = (int) Math.ceil(oldWidth * xRatio);
    int height = (int) Math.ceil(oldHeight * yRatio);
    double[][][] downscaled = new double[height][width][4];
    for (int i = 0; i < oldHeight; i++) {
      double y = i * yRatio;
      int floorY = (int) (Math.min(y, height - 1));
      int ceilingY = Math.min(floorY + 1, height);

      for (int j = 0; j < oldWidth; j++) {
        double x = j * xRatio;
        int floorX = (int) (Math.min(x, width - 1));
        int ceilingX = Math.min(floorX + 1, width);

        double[] rgb = image.getPixel(i, j);
        Function<Integer, Double> calculate = index -> {
          double m = (rgb[index] * (x - floorX)) + (rgb[index] * (ceilingX - x));
          double n = (rgb[index] * (x - floorX)) + (rgb[index] * (ceilingX - x));
          return n * (y - floorY) + m * (ceilingY - y);
        };
        double r = calculate.apply(0);
        double g = calculate.apply(1);
        double b = calculate.apply(2);
        double a = calculate.apply(3);

        downscaled[floorY][floorX] = new double[]{r, g, b, a};
      }
    }
    return new ImageImpl(downscaled, 255);
  }

  /**
   * Create a histogram image from a given {@link Image} object.
   *
   * @param image image to generate histogram from
   * @return the histogram image
   */
  public static Image createHistogram(Image image) {
    int width = image.getWidth();
    int height = image.getHeight();
    int max = 0;
    int[] red = new int[256];
    int[] green = new int[256];
    int[] blue = new int[256];
    int[] intensity = new int[256];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        double[] rgb = image.getPixel(i, j);
        int r = red[(int) (rgb[0] * 255)]++;
        int g = green[(int) (rgb[1] * 255)]++;
        int b = blue[(int) (rgb[2] * 255)]++;
        int in = intensity[(int) ((rgb[0] + rgb[1] + rgb[2]) / 3 * 255)]++;
        max = Math.max(max, Math.max(r, Math.max(g, Math.max(b, in))));
      }
    }
    max++;
    height = 200;
    width = 256;
    double[][][] pixels = new double[height][width][4];
    for (int i = 1; i <= height; i++) {
      for (int j = 0; j < width; j++) {
        double[] px = new double[]{1., 1., 1., 1.};
        if (i <= intensity[j] * height / max) {
          px = ImageUtil.averageColor(px, new double[]{0., 0., 0., 1.});
        }
        if (i <= red[j] * height / max) {
          px = ImageUtil.averageColor(px, new double[]{1., 0., 0., 1.});
        }
        if (i <= green[j] * height / max) {
          px = ImageUtil.averageColor(px, new double[]{0., 1., 0., 1.});
        }
        if (i <= blue[j] * height / max) {
          px = ImageUtil.averageColor(px, new double[]{0., 0., 1., 1.});
        }
        pixels[height - i][j] = px;
      }
    }
    return new ImageImpl(pixels, 255);
  }

  /**
   * Find the average pixel value for two given colors.
   *
   * @param c1 color one to use
   * @param c2 color two to use
   * @return the average color
   */
  private static double[] averageColor(double[] c1, double[] c2) {
    return new double[]{(c1[0] + c2[0]) / 2, (c1[1] + c2[1]) / 2,
        (c1[2] + c2[2]) / 2, (c1[3] + c2[3]) / 2};
  }

  /**
   * Gets the file extension of the provided file name.
   *
   * @param filename the filename to use
   * @return the extension
   */
  private static String getExtension(String filename) {
    int period = filename.lastIndexOf(".");
    return filename.substring(period + 1);
  }
}


