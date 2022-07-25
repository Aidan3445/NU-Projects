import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import controller.ImageProcessingTextController;
import controller.ImageProcessingTextControllerImpl;
import model.ImageProcessingModel;
import model.ImageProcessorImpl;
import view.ImageProcessingTextView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * JUnit tests for an image processing text controller.
 */
public class ImageProcessingTextControllerTest {
  private StringBuilder mLog;
  private StringBuilder vLog;

  private Readable rd;
  private ImageProcessingModel mMock;
  private ImageProcessingTextView vMock;
  private ImageProcessingTextController cMock;

  private String menu;

  /**
   * Set up a mock model and view, and real controller for testing.
   *
   * @param inputs the command line arguments to use
   */
  private void setupMock(Readable inputs) {
    this.mLog = new StringBuilder();
    this.vLog = new StringBuilder();
    this.rd = inputs;

    this.mMock = new ImageProcessorMock(this.mLog);
    this.vMock = new ImageProcessingTextViewMock(this.vLog);
    this.cMock = new ImageProcessingTextControllerImpl(this.mMock, this.vMock, this.rd);

    this.cMock.run();

    this.menu = "Message: Operation | Description" + System.lineSeparator();
    menu += "load image-path image-name | "
            + "Loads an image from the image-path and names it image-name"
            + System.lineSeparator();
    menu += "save image-name image-path | "
            + "Saves the image named image-name to the image-path which should also "
            + "include the image-name at the end"
            + System.lineSeparator();
    menu += "available-images | "
            + "Get a list of all images available to you for image processing"
            + System.lineSeparator();
    menu += "set-source | "
            + "Set the source folder to load and save images to"
            + System.lineSeparator();
    menu += "red-component image-name destination-image-name | "
            + "Creates a greyscale image with the red component of the image-name and "
            + "names it destination-image-name"
            + System.lineSeparator();
    menu += "green-component image-name destination-image-name | "
            + "Creates a greyscale image with the green component of the image-name and "
            + "names it destination-image-name"
            + System.lineSeparator();
    menu += "blue-component image-name destination-image-name | "
            + "Creates a greyscale image with the blue component of the image-name and "
            + "names it destination-image-name"
            + System.lineSeparator();
    menu += "value-component image-name destination-image-name | "
            + "Creates a greyscale image with the maximum RGB value for each pixel of"
            + " the image-name and names it destination-image-name"
            + System.lineSeparator();
    menu += "luma-component image-name destination-image-name | "
            + "Creates a greyscale image with the average RGB value for each pixel of"
            + " the image-name and names it destination-image-name"
            + System.lineSeparator();
    menu += "intensity-component image-name destination-image-name | "
            + "Creates a greyscale image with the weighted sum of 0.126R + 0.7152G + 0.0722B "
            + "for each pixel of the image-name and names it destination-image-name"
            + System.lineSeparator();
    menu += "greyscale image-name destination-image-name | "
            + "Creates a greyscale image for image-name and names it destination-image-name"
            + System.lineSeparator();
    menu += "sepia image-name destination-image-name | "
            + "Creates a sepia-toned image for image-name and names it destination-image-name"
            + System.lineSeparator();
    menu += "brighten increment image-name destination-image-name | "
            + "Creates a new image by brightening the image-name by the increment and names it "
            + "destination-image-name"
            + System.lineSeparator();
    menu += "gaussian-blur amount image-name destination-image-name | "
            + "Blurs the image by a given amount"
            + System.lineSeparator();
    menu += "sharpen image-name destination-image-name | "
            + "Sharpens the image once"
            + System.lineSeparator();
    menu += "horizontal-flip image-name destination-image-name | "
            + "Flips image-name horizontally and names it destination-image-name"
            + System.lineSeparator();
    menu += "vertical-flip image-name destination-image-name | "
            + "Flips image-name vertically and names it destination-image-name"
            + System.lineSeparator();
    menu += "q or quit| Quits the program"
            + System.lineSeparator();
    menu += System.lineSeparator()
            + "Type instruction:" + System.lineSeparator();
  }

  /**
   * Set up a real model and controller, and mock view for testing.
   *
   * @param inputs the command line arguments to use
   */
  private void setupReal(String inputs) {
    this.vLog = new StringBuilder();
    this.rd = new StringReader(inputs);

    this.mMock = new ImageProcessorImpl();
    this.vMock = new ImageProcessingTextViewMock(this.vLog);
    this.cMock = new ImageProcessingTextControllerImpl(this.mMock, this.vMock, this.rd);

    this.cMock.run();
  }

  @Test
  public void constructorExceptions() {
    this.setupReal("q");
    try {
      new ImageProcessingTextControllerImpl(null, this.vMock, this.rd);
      fail("Constructor should have failed.");
    } catch (IllegalArgumentException e) {
      assertEquals("The provided model is null.", e.getMessage());
    }
    try {
      new ImageProcessingTextControllerImpl(this.mMock, null, this.rd);
      fail("Constructor should have failed.");
    } catch (IllegalArgumentException e) {
      assertEquals("The provided view is null.", e.getMessage());
    }
    try {
      new ImageProcessingTextControllerImpl(this.mMock, this.vMock, null);
      fail("Constructor should have failed.");
    } catch (IllegalArgumentException e) {
      assertEquals("The provided readable object is null.", e.getMessage());
    }
  }

  /**
   * Gets the specified line from the output string.
   *
   * @param line line number to get starting at line 1
   * @throws IllegalArgumentException if the line number is negative or too big
   */
  private String getLine(int line) throws IllegalArgumentException {
    String[] output = vLog.toString().split(System.lineSeparator());
    if (line < 0) {
      throw new IllegalArgumentException("Start line cannot be negative.");
    } else if (line > output.length) {
      throw new IllegalArgumentException(String.format(
              "There are only %d lines in the string, %d is too big.", output.length, line));
    }
    return output[line - 1];
  }

  @Test
  public void testQuit() {
    for (String q : new String[]{"q", "Q", "bad-operation q", "save bad-name path.ppm q",
        "load bad-path name q", "red-component bad-name name q", "brighten bad-increment q"}) {
      this.setupMock(new StringReader(q));
      assertEquals(this.menu, this.getLine(1) + System.lineSeparator()
              + this.getLine(2) + System.lineSeparator()
              + this.getLine(3) + System.lineSeparator()
              + this.getLine(4) + System.lineSeparator()
              + this.getLine(5) + System.lineSeparator()
              + this.getLine(6) + System.lineSeparator()
              + this.getLine(7) + System.lineSeparator()
              + this.getLine(8) + System.lineSeparator()
              + this.getLine(9) + System.lineSeparator()
              + this.getLine(10) + System.lineSeparator()
              + this.getLine(11) + System.lineSeparator()
              + this.getLine(12) + System.lineSeparator()
              + this.getLine(13) + System.lineSeparator()
              + this.getLine(14) + System.lineSeparator()
              + this.getLine(15) + System.lineSeparator()
              + this.getLine(16) + System.lineSeparator()
              + this.getLine(17) + System.lineSeparator()
              + this.getLine(18) + System.lineSeparator()
              + this.getLine(19) + System.lineSeparator()
              + this.getLine(20) + System.lineSeparator()
              + this.getLine(21) + System.lineSeparator());
    }
    // deletes the files so test can be run multiple times.
    File f1 = new File("path.ppm");
    if (f1.delete()) {
      System.out.println("Deleted the file: " + f1.getName());
    } else {
      System.out.println("Failed to delete the files.");
    }
  }

  @Test
  public void sendErrors() {
    this.setupReal("bad-operation q");
    assertEquals("Message: The provided operation is not supported",
            this.getLine(22));

    this.setupReal("save a none.ppm q");
    assertEquals("Message: Error: Image with the provided name 'a' does not exist",
            this.getLine(22));

    this.setupReal("load bad-path.ppm bad q");
    assertEquals("Message: Error: Image with the provided path 'bad-path.ppm' does not exist",
            this.getLine(22));

    this.setupReal("brighten a bn sn q");
    assertEquals("Message: Invalid inputs try again, re-read the menu descriptions.",
            this.getLine(22));
  }

  @Test
  public void menu() {
    this.setupMock(new StringReader("q"));
    assertEquals(this.menu, this.vLog.toString());
  }

  @Test
  public void load() {
    this.setupMock(new StringReader("load ./res/penguin.png p q"));
    assertEquals("load: Name: p",
            this.mLog.toString());
  }

  @Test
  public void save() {
    this.setupMock(new StringReader("save ab ab.ppm q"));
    assertEquals("get-image: Name of image to get: ab", this.mLog.toString());
  }

  @Test
  public void setSource() {
    this.setupMock(new StringReader("set-source HW5/res/"));
    assertEquals("Message: Source folder has been set to 'HW5/res/'", this.getLine(22));
  }

  @Test
  public void availableImages() {
    this.setupMock(new StringReader("available-images"));
    assertEquals("Message: There is 1 image available:" + System.lineSeparator()
                    + "TEST.notimg",
            this.getLine(22) + System.lineSeparator() + this.getLine(23));
  }

  @Test
  public void redComponent() {
    this.setupMock(new StringReader("red-component ab abR q"));
    assertEquals("red-component: Name of image to use: ab New name: abR",
            this.mLog.toString());
  }

  @Test
  public void greenComponent() {
    this.setupMock(new StringReader("green-component ab abG q"));
    assertEquals("green-component: Name of image to use: ab New name: abG",
            this.mLog.toString());
  }

  @Test
  public void blueComponent() {
    this.setupMock(new StringReader("blue-component ab abB q"));
    assertEquals("blue-component: Name of image to use: ab New name: abB",
            this.mLog.toString());
  }

  @Test
  public void valueComponent() {
    this.setupMock(new StringReader("value-component ab abV q"));
    assertEquals("value-component: Name of image to use: ab New name: abV",
            this.mLog.toString());
  }

  @Test
  public void lumaComponent() {
    this.setupMock(new StringReader("luma-component ab abL q"));
    assertEquals("luma-component: Name of image to use: ab New name: abL",
            this.mLog.toString());
  }

  @Test
  public void intensityComponent() {
    this.setupMock(new StringReader("intensity-component ab abI q"));
    assertEquals("intensity-component: Name of image to use: ab New name: abI",
            this.mLog.toString());
  }

  @Test
  public void greyscale() {
    this.setupMock(new StringReader("greyscale ab abG q"));
    assertEquals("greyscale: Name of image to use: ab New name: abG",
            this.mLog.toString());
  }

  @Test
  public void sepia() {
    this.setupMock(new StringReader("sepia ab abS q"));
    assertEquals("sepia: Name of image to use: ab New name: abS",
            this.mLog.toString());
  }

  @Test
  public void brighten() {
    this.setupMock(new StringReader("brighten 10 ab abB q"));
    assertEquals("brighten: Increment: 10 Name of image to use: ab New name: abB",
            this.mLog.toString());
  }

  @Test
  public void blur() {
    this.setupMock(new StringReader("gaussian-blur 1 ab abB q"));
    assertEquals("gaussian-blur: Amount: 1 Name of image to use: ab New name: abB",
            this.mLog.toString());
  }

  @Test
  public void sharpen() {
    this.setupMock(new StringReader("sharpen ab abS q"));
    assertEquals("sharpen: Name of image to use: ab New name: abS",
            this.mLog.toString());
  }

  @Test
  public void horizontalFlip() {
    this.setupMock(new StringReader("horizontal-flip ab abH q"));
    assertEquals("horizontal-flip: Name of image to use: ab New name: abH",
            this.mLog.toString());
  }

  @Test
  public void verticalFlip() {
    this.setupMock(new StringReader("vertical-flip ab abV q"));
    assertEquals("vertical-flip: Name of image to use: ab New name: abV",
            this.mLog.toString());
  }

  @Test
  public void scriptReading() {
    File script = new File("temp.txt");
    try {
      FileWriter f = new FileWriter(script);
      f.write("set-source ./res/ "
              + "load 2x2.ppm t "
              + "sepia t ts "
              + "q");
      f.close();
      this.setupMock(new FileReader("temp.txt"));
      File f1 = new File("temp.txt");
      if (f1.delete()) {
        System.out.println("Deleted the file: " + f1.getName());
      } else {
        System.out.println("Failed to delete the files.");
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals("load: Name: t"
            + "sepia: Name of image to use: t New name: ts", this.mLog.toString());
    assertEquals("Message: Source folder has been set to './res/'"
                + System.lineSeparator() + "Message: Type instruction: "
                + System.lineSeparator() + "Message: Image ./res/2x2.ppm was loaded and called t"
                + System.lineSeparator() + "Message: Type instruction: "
                + System.lineSeparator() + "Message: t was saved as a sepia-toned image called ts"
                + System.lineSeparator() + "Message: Type instruction: ",
        this.getLine(22) + System.lineSeparator()
                + this.getLine(23) + System.lineSeparator()
                + this.getLine(24) + System.lineSeparator()
                + this.getLine(25) + System.lineSeparator()
                + this.getLine(26) + System.lineSeparator()
                + this.getLine(27));
  }
}