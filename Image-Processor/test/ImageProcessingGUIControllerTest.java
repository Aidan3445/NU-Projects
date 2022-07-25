import org.junit.Before;
import org.junit.Test;


import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;

import controller.ImageProcessingGUIController;
import model.Image;
import model.ImageProcessingModel;
import model.ImageProcessorImpl;
import model.ImageUtil;
import view.ImageProcessingGUIView;
import view.ImageProcessingGUIViewImpl;
import view.panels.HistogramPanel;
import view.panels.HistogramPanelImpl;
import view.panels.ImagePanel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * JUnit tests for an image processing gui controller.
 */
public class ImageProcessingGUIControllerTest {
  private ImageProcessingGUIController controller;
  private StringBuilder mLog;
  private StringBuilder vLog;


  private ImageProcessingModel mMock;
  private ImageProcessingGUIView vMock;

  @Before
  public void setupMock() {
    this.mLog = new StringBuilder();
    this.vLog = new StringBuilder();
    this.mMock = new ImageProcessorMock(this.mLog);
    this.vMock = new ImageProcessorGUIViewMock(this.vLog);
    this.controller = new ImageProcessingGUIController(this.mMock, this.vMock);
  }

  @Test
  public void constructorExceptions() {
    try {
      new ImageProcessingGUIController(null, this.vMock);
      fail("Constructor should have failed.");
    } catch (IllegalArgumentException e) {
      assertEquals("The provided model is null.", e.getMessage());
    }
    try {
      new ImageProcessingGUIController(this.mMock, null);
      fail("Constructor should have failed.");
    } catch (IllegalArgumentException e) {
      assertEquals("The provided view is null.", e.getMessage());
    }
  }

  @Test
  public void load() {
    this.controller.load();
    assertEquals("load: Name: current", this.mLog.toString());
    File f1 = new File("file.png");
    if (f1.delete()) {
      System.out.println("Deleted the file: " + f1.getName());
    } else {
      System.out.println("Failed to delete the files.");
    }
  }

  @Test
  public void save() {
    this.controller.save();
    assertEquals("get-image: Name of image to get: current", this.mLog.toString());

  }

  @Test
  public void testMessages() {
    assertEquals("Features added." + System.lineSeparator()
                    + "Refreshed." + System.lineSeparator(),
            this.vLog.toString());
  }

  @Test
  public void redComponent() {
    this.controller.redComp();
    assertEquals("red-component: Name of image to use: current New name: current",
            this.mLog.toString());
  }

  @Test
  public void greenComponent() {
    this.controller.greenComp();
    assertEquals("green-component: Name of image to use: current New name: current",
            this.mLog.toString());
  }

  @Test
  public void blueComponent() {
    this.controller.blueComp();
    assertEquals("blue-component: Name of image to use: current New name: current",
            this.mLog.toString());
  }

  @Test
  public void valueComponent() {
    this.controller.valueComp();
    assertEquals("value-component: Name of image to use: current New name: current",
            this.mLog.toString());
  }

  @Test
  public void lumaComponent() {
    this.controller.lumaComp();
    assertEquals("luma-component: Name of image to use: current New name: current",
            this.mLog.toString());
  }

  @Test
  public void intensityComponent() {
    this.controller.intensityComp();
    assertEquals("intensity-component: Name of image to use: current New name: current",
            this.mLog.toString());
  }

  @Test
  public void greyscale() {
    this.controller.greyscale();
    assertEquals("greyscale: Name of image to use: current New name: current",
            this.mLog.toString());
  }

  @Test
  public void sepia() {
    this.controller.sepia();
    assertEquals("sepia: Name of image to use: current New name: current",
            this.mLog.toString());
  }

  @Test
  public void brighten() {
    this.controller.brighten();
    assertEquals("get-image: Name of image to get: current"
                    + "brighten: Increment: 10 Name of image to use: current New name: current",
            this.mLog.toString());
  }

  @Test
  public void blur() {
    this.controller.gaussianBlur();
    assertEquals("get-image: Name of image to get: current"
            + "gaussian-blur: Amount: 10 Name of image to use: current New name: current",
            this.mLog.toString());
  }

  @Test
  public void sharpen() {
    this.controller.sharpen();
    assertEquals("sharpen: Name of image to use: current New name: current",
            this.mLog.toString());
  }

  @Test
  public void horizontalFlip() {
    this.controller.horizontalFlip();
    assertEquals("horizontal-flip: Name of image to use: current New name: current",
            this.mLog.toString());
  }

  @Test
  public void verticalFlip() {
    this.controller.verticalFlip();
    assertEquals("vertical-flip: Name of image to use: current New name: current",
            this.mLog.toString());
  }

  @Test
  public void downscale() {
    this.controller.downscale();
    assertEquals("get-image: Name of image to get: current"
            + "get-image: Name of image to get: current"
            + "load: Name: current", mLog.toString());
  }

  /**
   * JUnit test class for the GUI view panels.
   */
  public static class ImageProcessingGUIViewImplTest {

    @Test
    public void viewConstructorExceptions() {
      try {
        ImageProcessingGUIView v = new ImageProcessingGUIViewImpl(null);
        fail("Constructor should have thrown an exception");
      } catch (IllegalArgumentException e) {
        assertEquals("Model cannot be null.", e.getMessage());
      }
    }

    @Test
    public void imagePanelConstructorExceptions() {
      try {
        ImagePanel p = new ImagePanel(null, new Dimension(1, 1));
      } catch (IllegalArgumentException e) {
        assertEquals("Model cannot be null.", e.getMessage());
      }
      try {
        ImagePanel p = new ImagePanel(new ImageProcessorImpl(), null);
      } catch (IllegalArgumentException e) {
        assertEquals("Dimension cannot be null.", e.getMessage());
      }
    }

    @Test
    public void imagePanelUpdate() {
      HistogramPanel hGram = new HistogramPanelImpl();
      try {
        hGram.update(null);
        fail("Should have thrown exception.");
      } catch (IllegalArgumentException e) {
        assertEquals("Image cannot be null.", e.getMessage());
      }
      model.Image h;
      try {
        Image img = ImageUtil.readImage("./res/2x2.ppm");
        h = ImageUtil.createHistogram(img);
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      }
      for (int i = 0; i < 200; i++) {
        for (int j = 0; j < 256; j++) {
          double[] rgb = h.getPixel(i, j);
          int r = (int) (rgb[0] * 255);
          int g = (int) (rgb[1] * 255);
          int b = (int) (rgb[2] * 255);
          if (j != 0 && j != 255 && j != 85) {
            assertEquals(255, r);
            assertEquals(255, g);
            assertEquals(255, b);
          } else if (j == 85) {
            assertEquals(127, r);
            assertEquals(127, g);
            assertEquals(127, b);
          } else if (i > 133) {
            if (j == 0) {
              assertEquals(47, r);
              assertEquals(79, g);
              assertEquals(143, b);
            } else {
              assertEquals(63, r);
              assertEquals(95, g);
              assertEquals(159, b);
            }
          } else {
            if (j == 0) {
              assertEquals(63, r);
              assertEquals(95, g);
              assertEquals(159, b);
            } else {
              assertEquals(255, r);
              assertEquals(255, g);
              assertEquals(255, b);
            }
          }
        }
      }
    }
  }
}