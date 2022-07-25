import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import model.Image;
import model.ImageProcessingModel;
import model.ImageUtil;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * An abstract class for testing the image processor.
 */
public abstract class AbstractImageProcessorTest {
  protected ImageProcessingModel model;
  protected final String resPath;

  /**
   * Initialize the path for the images to be pulled from and saved to.
   *
   * @param resPath the resource path to use
   */
  protected AbstractImageProcessorTest(String resPath) {
    this.resPath = resPath;
  }

  @Before
  public void setup() {
    this.model = this.createModel();
  }

  /**
   * Create a model of a type specific to the child test class.
   *
   * @return the model that was created
   */
  protected abstract ImageProcessingModel createModel();

  /**
   * Load images N from file system as integers from 0 to N.
   *
   * @param filenames varargs names of files within the {@link #resPath} folder
   */
  public void loadImages(String... filenames) {
    for (int i = 0; i < filenames.length; i++) {
      try {
        Image image = ImageUtil.readImage(this.resPath + filenames[i]);
        this.model.load(Integer.toString(i), image);
      } catch (FileNotFoundException e) {
        fail("File should exist");
      }
    }
  }

  /**
   * Checks that the pixels in the provided N array(s) are the same.
   *
   * @param rgb the array(s) to check
   */
  public boolean checkAllSamePixels(int width, int height, double[]... rgb) {
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        for (int k = 0; k < rgb.length; k++) {
          assertArrayEquals(rgb[k],
                  this.model.getImage(Integer.toString(k)).getPixel(i, j), 0.001);
        }
      }
    }
    return true;
  }

  /**
   * Checks that the pixels in the provided images are identical.
   *
   * @param image1 the name of first image to compare
   * @param image2 the name of second image to compare
   */
  public boolean testIdentical(Image image1, Image image2) {
    assertEquals(image1.getWidth(), image2.getWidth());
    assertEquals(image1.getHeight(), image2.getHeight());
    int width = image1.getWidth();
    int height = image2.getHeight();
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        assertArrayEquals(image1.getPixel(i, j), image2.getPixel(i, j), 0.001);
      }
    }
    return true;
  }

  @Test
  public void getImage() {
    try {
      this.model.getImage("0");
      fail("Name should not exist, should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Image with the provided name '0' does not exist",
              e.getMessage());
    }
    this.loadImages("red4x4.ppm");
    assertTrue(this.checkAllSamePixels(4, 4, new double[]{1., 0., 0., 1.}));
  }

  @Test
  public void getAll() {
    assertEquals(new ArrayList<String>(), this.model.getAll());
    this.loadImages("penguin.ppm", "penguin.png",
            "penguin.jpeg", "penguin.jpg", "penguin.bmp");
    assertEquals(new ArrayList<String>(Arrays.asList("0", "1", "2", "3", "4")),
            this.model.getAll());
    this.model.sepia("0", "NEW");
    assertEquals(new ArrayList<String>(Arrays.asList("0", "1", "2", "3", "4", "NEW")),
            this.model.getAll());
  }

  @Test
  public void load() {
    this.loadImages("red4x4.ppm");
    assertTrue(this.checkAllSamePixels(4, 4, new double[]{1., 0., 0., 1.}));
  }

  @Test
  public void save() {
    this.loadImages("blue4x4.ppm");
    try {
      ImageUtil.saveImage(this.model.getImage("0"), this.resPath + "blue2-4x4.ppm");
    } catch (IOException e) {
      fail(String.format("Should have been able to save to %s blue2-4x4.ppm", this.resPath));
    }
    this.loadImages("blue4x4.ppm", "blue2-4x4.ppm");

    // deletes file so test can be run multiple times.
    File blue = new File(this.resPath + "blue2-4x4.ppm");
    if (blue.delete()) {
      System.out.println("Deleted the file: " + blue.getName());
    } else {
      System.out.println("Failed to delete the file.");
    }
    assertTrue(this.checkAllSamePixels(4, 4,
            new double[]{0., 0., 1., 1.}, new double[]{0., 0., 1., 1.}));
  }

  @Test
  public void loadAndSaveDifferentFileTypes() {
    this.loadImages("penguin.bmp", "penguin.jpeg", "penguin.jpg",
            "penguin.png", "penguin.ppm");
    try {
      ImageUtil.saveImage(this.model.getImage("0"), this.resPath + "penguin2.jpeg");
      ImageUtil.saveImage(this.model.getImage("1"), this.resPath + "penguin2.jpg");
      ImageUtil.saveImage(this.model.getImage("2"), this.resPath + "penguin2.png");
      ImageUtil.saveImage(this.model.getImage("3"), this.resPath + "penguin2.ppm");
      ImageUtil.saveImage(this.model.getImage("4"), this.resPath + "penguin2.bmp");
    } catch (IOException e) {
      fail(String.format("Should have been able to save to %s", this.resPath));
    }
    this.loadImages("penguin2.bmp", "penguin2.jpeg", "penguin2.jpg",
            "penguin2.png", "penguin2.ppm");

    // deletes the files so test can be run multiple times.
    File f1 = new File(this.resPath + "penguin2.bmp");
    File f2 = new File(this.resPath + "penguin2.jpeg");
    File f3 = new File(this.resPath + "penguin2.jpg");
    File f4 = new File(this.resPath + "penguin2.png");
    File f5 = new File(this.resPath + "penguin2.ppm");
    if (f1.delete() && f2.delete() && f3.delete() && f4.delete() && f5.delete()) {
      System.out.println("Deleted the file: " + f1.getName());
      System.out.println("Deleted the file: " + f2.getName());
      System.out.println("Deleted the file: " + f3.getName());
      System.out.println("Deleted the file: " + f4.getName());
      System.out.println("Deleted the file: " + f5.getName());
    } else {
      System.out.println("Failed to delete the files.");
    }
  }

  @Test
  public void redComponent() {
    this.loadImages("red4x4.ppm", "blue4x4.ppm");
    this.model.redComponent("0", "0");
    this.model.redComponent("1", "1");
    assertTrue(this.checkAllSamePixels(4, 4,
            new double[]{1., 1., 1., 1.}, new double[]{0., 0., 0., 1.}));
  }

  @Test
  public void greenComponent() {
    this.loadImages("green4x4.ppm", "red4x4.ppm");
    this.model.greenComponent("0", "0");
    this.model.greenComponent("1", "1");
    assertTrue(this.checkAllSamePixels(4, 4,
            new double[]{1., 1., 1., 1.}, new double[]{0., 0., 0., 1.}));
  }

  @Test
  public void blueComponent() {
    this.loadImages("blue4x4.ppm", "green4x4.ppm");
    this.model.blueComponent("0", "0");
    this.model.blueComponent("1", "1");
    assertTrue(this.checkAllSamePixels(4, 4,
            new double[]{1., 1., 1., 1.}, new double[]{0., 0., 0., 1.}));
  }

  @Test
  public void valueComponent() {
    this.loadImages("red4x4.ppm", "green4x4.ppm", "blue4x4.ppm");
    this.model.valueComponent("0", "0");
    this.model.valueComponent("1", "1");
    this.model.valueComponent("2", "2");
    assertTrue(this.checkAllSamePixels(4, 4,
            new double[]{1., 1., 1., 1.},
            new double[]{1., 1., 1., 1.},
            new double[]{1., 1., 1., 1.}));
  }

  @Test
  public void lumaComponent() {
    this.loadImages("red4x4.ppm", "green4x4.ppm", "blue4x4.ppm");
    this.model.lumaComponent("0", "0");
    this.model.lumaComponent("1", "1");
    this.model.lumaComponent("2", "2");
    assertTrue(this.checkAllSamePixels(4, 4,
            new double[]{.2126, .2126, .2126, 1.},
            new double[]{.7152, .7152, .7152, 1.},
            new double[]{.0722, .0722, .0722, 1.}));
  }

  @Test
  public void intensityComponent() {
    this.loadImages("red4x4.ppm", "green4x4.ppm", "blue4x4.ppm");
    this.model.intensityComponent("0", "0");
    this.model.intensityComponent("1", "1");
    this.model.intensityComponent("2", "2");
    assertTrue(this.checkAllSamePixels(4, 4,
            new double[]{.3333, .3333, .3333, 1.},
            new double[]{.3333, .3333, .3333, 1.},
            new double[]{.3333, .3333, .3333, 1.}));
  }

  @Test
  public void testGreyscale() {
    this.loadImages("red4x4.ppm", "green4x4.ppm", "blue4x4.ppm");
    this.model.greyscale("0", "0");
    this.model.greyscale("1", "1");
    this.model.greyscale("2", "2");
    assertTrue(this.checkAllSamePixels(4, 4,
            new double[]{.2126, .2126, .2126, 1.},
            new double[]{.7152, .7152, .7152, 1.},
            new double[]{.0722, .0722, .0722, 1.}));
  }

  @Test
  public void testSepia() {
    this.loadImages("red4x4.ppm", "green4x4.ppm", "blue4x4.ppm");
    this.model.sepia("0", "0");
    this.model.sepia("1", "1");
    this.model.sepia("2", "2");
    assertTrue(this.checkAllSamePixels(4, 4,
            new double[]{.393, .349, .272, 1.},
            new double[]{.769, .686, .534, 1.},
            new double[]{.189, .168, .131, 1.}));
  }

  @Test
  public void brighten() {
    this.loadImages("red4x4.ppm", "green4x4.ppm", "blue4x4.ppm");
    this.model.brighten(100, "0", "0");
    this.model.brighten(-100, "1", "1");
    this.model.brighten(0, "2", "2");
    assertTrue(this.checkAllSamePixels(4, 4,
            new double[]{1., .392, .392, 1.},
            new double[]{0., .607, 0., 1.},
            new double[]{0., 0., 1., 1.}));
  }

  @Test
  public void testGaussianBlur1() {
    this.loadImages("2x2.ppm");
    assertArrayEquals(new double[]{1., 0., 0., 1.},
            this.model.getImage("0").getPixel(0, 0), 0.001);
    assertArrayEquals(new double[]{0., 1., 0., 1.},
            this.model.getImage("0").getPixel(0, 1), 0.001);
    assertArrayEquals(new double[]{0., 0., 1., 1.},
            this.model.getImage("0").getPixel(1, 0), 0.001);
    assertArrayEquals(new double[]{0., 0., 0., 1.},
            this.model.getImage("0").getPixel(1, 1), 0.001);
    this.model.gaussianBlur(1, "0", "0");
    assertArrayEquals(new double[]{0.387, 0.235, 0.235, 1.},
            this.model.getImage("0").getPixel(0, 0), 0.001);
    assertArrayEquals(new double[]{0.235, 0.387, 0.142, 1.},
            this.model.getImage("0").getPixel(0, 1), 0.001);
    assertArrayEquals(new double[]{0.235, 0.142, 0.387, 1.},
            this.model.getImage("0").getPixel(1, 0), 0.001);
    assertArrayEquals(new double[]{0.142, 0.235, 0.235, 1.},
            this.model.getImage("0").getPixel(1, 1), 0.001);
  }

  @Test
  public void testGaussianBlur5() {
    this.loadImages("2x2.ppm");
    assertArrayEquals(new double[]{1., 0., 0., 1.},
            this.model.getImage("0").getPixel(0, 0), 0.001);
    assertArrayEquals(new double[]{0., 1., 0., 1.},
            this.model.getImage("0").getPixel(0, 1), 0.001);
    assertArrayEquals(new double[]{0., 0., 1., 1.},
            this.model.getImage("0").getPixel(1, 0), 0.001);
    assertArrayEquals(new double[]{0., 0., 0., 1.},
            this.model.getImage("0").getPixel(1, 1), 0.001);
    this.model.gaussianBlur(5, "0", "0");
    assertArrayEquals(new double[]{0.255, 0.249, 0.249, 1.},
            this.model.getImage("0").getPixel(0, 0), 0.001);
    assertArrayEquals(new double[]{0.249, 0.255, 0.245, 1.},
            this.model.getImage("0").getPixel(0, 1), 0.001);
    assertArrayEquals(new double[]{0.249, 0.245, 0.255, 1.},
            this.model.getImage("0").getPixel(1, 0), 0.001);
    assertArrayEquals(new double[]{0.245, 0.249, 0.249, 1.},
            this.model.getImage("0").getPixel(1, 1), 0.001);
  }

  @Test
  public void testSharpen() {
    this.loadImages("2x2.ppm");
    this.model.sharpen("0", "0");
    assertArrayEquals(new double[]{.571, .142, .142, 1.},
            this.model.getImage("0").getPixel(0, 0), 0.001);
    assertArrayEquals(new double[]{.142, .571, .142, 1.},
            this.model.getImage("0").getPixel(0, 1), 0.001);
    assertArrayEquals(new double[]{.142, .142, .571, 1.},
            this.model.getImage("0").getPixel(1, 0), 0.001);
    assertArrayEquals(new double[]{.142, .142, .142, 1.},
            this.model.getImage("0").getPixel(1, 1), 0.001);
    this.model.gaussianBlur(5, "0", "0");
    assertArrayEquals(new double[]{.252, .249, .249, 1.},
            this.model.getImage("0").getPixel(0, 0), 0.001);
    assertArrayEquals(new double[]{.249, .252, .247, 1.},
            this.model.getImage("0").getPixel(0, 1), 0.001);
    assertArrayEquals(new double[]{.249, .247, .252, 1.},
            this.model.getImage("0").getPixel(1, 0), 0.001);
    assertArrayEquals(new double[]{.247, .249, .249, 1.},
            this.model.getImage("0").getPixel(1, 1), 0.001);
  }

  @Test
  public void horizontalFlip() {
    this.loadImages("vbars4x4.ppm");
    this.model.horizontalFlip("0", "vbarsFlip");
    for (int i = 0; i < 4; i++) {
      assertArrayEquals(new double[][]{
          new double[]{1., 0., 1., 1.},
          new double[]{0., 0., 1., 1.},
          new double[]{0., 1., 0., 1.},
          new double[]{1., 0., 0., 1.}},
              new double[][]{this.model.getImage("vbarsFlip").getPixel(i, 0),
                      this.model.getImage("vbarsFlip").getPixel(i, 1),
                      this.model.getImage("vbarsFlip").getPixel(i, 2),
                      this.model.getImage("vbarsFlip").getPixel(i, 3)});
    }
  }

  @Test
  public void verticalFlip() {
    this.loadImages("hbars4x4.ppm");
    this.model.verticalFlip("0", "hbarsFlip");
    for (int i = 0; i < 4; i++) {
      double[] magenta = {1., 0., 1., 1.};
      assertArrayEquals(magenta,
              this.model.getImage("hbarsFlip").getPixel(0, i), 0.001);
    }
    for (int i = 0; i < 4; i++) {
      double[] blue = {0., 0., 1., 1.};
      assertArrayEquals(blue,
              this.model.getImage("hbarsFlip").getPixel(1, i), 0.001);
    }
    for (int i = 0; i < 4; i++) {
      double[] green = {0., 1., 0., 1.};
      assertArrayEquals(green,
              this.model.getImage("hbarsFlip").getPixel(2, i), 0.001);
    }
    for (int i = 0; i < 4; i++) {
      double[] red = {1., 0., 0., 1.};
      assertArrayEquals(red,
              this.model.getImage("hbarsFlip").getPixel(3, i), 0.001);
    }
  }

  @Test
  public void getImageException() {
    try {
      this.model.getImage(null);
      fail("This method should have thrown an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Image with the provided name 'null' does not exist", e.getMessage());
    }
  }

  @Test
  public void redComponentException() {
    try {
      this.model.redComponent("null", "oops");
    } catch (IllegalArgumentException e) {
      assertEquals("Image with the provided name 'null' does not exist", e.getMessage());
    }
  }

  @Test
  public void greenComponentException() {
    try {
      this.model.greenComponent("null", "oops");
    } catch (IllegalArgumentException e) {
      assertEquals("Image with the provided name 'null' does not exist", e.getMessage());
    }
  }

  @Test
  public void blueComponentException() {
    try {
      this.model.blueComponent("null", "oops");
    } catch (IllegalArgumentException e) {
      assertEquals("Image with the provided name 'null' does not exist", e.getMessage());
    }
  }

  @Test
  public void valueComponentException() {
    try {
      this.model.valueComponent("null", "oops");
    } catch (IllegalArgumentException e) {
      assertEquals("Image with the provided name 'null' does not exist", e.getMessage());
    }
  }

  @Test
  public void lumaComponentException() {
    try {
      this.model.lumaComponent("null", "oops");
    } catch (IllegalArgumentException e) {
      assertEquals("Image with the provided name 'null' does not exist", e.getMessage());
    }
  }

  @Test
  public void intensityComponentException() {
    try {
      this.model.intensityComponent("null", "oops");
    } catch (IllegalArgumentException e) {
      assertEquals("Image with the provided name 'null' does not exist", e.getMessage());
    }
  }

  @Test
  public void brightenException() {
    try {
      this.model.brighten(10, "null", "oops");
    } catch (IllegalArgumentException e) {
      assertEquals("Image with the provided name 'null' does not exist", e.getMessage());
    }
  }

  @Test
  public void horizontalFlipException() {
    try {
      this.model.horizontalFlip("null", "oops");
    } catch (IllegalArgumentException e) {
      assertEquals("Image with the provided name 'null' does not exist", e.getMessage());
    }
  }

  @Test
  public void verticalFlipException() {
    try {
      this.model.verticalFlip("null", "oops");
    } catch (IllegalArgumentException e) {
      assertEquals("Image with the provided name 'null' does not exist", e.getMessage());
    }
  }

  @Test
  public void rectangularImage() {
    this.loadImages("sky.ppm", "sky.ppm");
    this.testIdentical(this.model.getImage("0"), this.model.getImage("1"));
    this.model.horizontalFlip("1", "1");
    this.model.verticalFlip("1", "1");
    this.model.horizontalFlip("1", "1");
    this.model.verticalFlip("1", "1");
    assertTrue(this.testIdentical(this.model.getImage("0"), this.model.getImage("1")));
  }
}
