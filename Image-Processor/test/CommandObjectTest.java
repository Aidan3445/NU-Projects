import org.junit.Test;

import java.io.IOException;

import controller.textcommands.BlueComp;
import controller.textcommands.Brighten;
import controller.textcommands.GaussianBlur;
import controller.textcommands.GreenComp;
import controller.textcommands.Greyscale;
import controller.textcommands.HorizontalFlip;
import controller.textcommands.ImageProcessingTextCommand;
import controller.textcommands.IntensityComp;
import controller.textcommands.LumaComp;
import controller.textcommands.RedComp;
import controller.textcommands.Sepia;
import controller.textcommands.Sharpen;
import controller.textcommands.ValueComp;
import controller.textcommands.VerticalFlip;
import model.ImageProcessorImpl;
import view.ImageProcessingTextViewImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * JUnit test for the command objects.
 */
public class CommandObjectTest {
  private final String n = "Name or destination name cannot be null.";
  private final  String m = "Model cannot be null.";
  private final String v = "View cannot be null.";

  @Test
  public void modelViewExceptions() throws IOException {
    ImageProcessingTextCommand r = new RedComp("name", "dest");
    try {
      r.execute(new ImageProcessorImpl(), null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(v, e.getMessage());
    }
    try {
      r.execute(null, new ImageProcessingTextViewImpl(new ImageProcessorImpl()));
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(m, e.getMessage());
    }
  }

  @Test
  public void redComp() {
    try {
      new RedComp(null, "");
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new RedComp("", null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new RedComp(null, null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
  }

  @Test
  public void greenComp() {
    try {
      new GreenComp(null, "");
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new GreenComp("", null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new GreenComp(null, null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
  }

  @Test
  public void blueComp() {
    try {
      new BlueComp(null, "");
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new BlueComp("", null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new BlueComp(null, null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
  }

  @Test
  public void intensityComp() {
    try {
      new IntensityComp(null, "");
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new IntensityComp("", null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new IntensityComp(null, null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
  }

  @Test
  public void lumaComp() {
    try {
      new LumaComp(null, "");
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new LumaComp("", null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new LumaComp(null, null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
  }

  @Test
  public void valueComp() {
    try {
      new ValueComp(null, "");
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new ValueComp("", null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new ValueComp(null, null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
  }

  @Test
  public void greyscale() {
    try {
      new Greyscale(null, "");
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new Greyscale("", null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new Greyscale(null, null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
  }

  @Test
  public void sepia() {
    try {
      new Sepia(null, "");
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new Sepia("", null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new Sepia(null, null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
  }

  @Test
  public void horizFlip() {
    try {
      new HorizontalFlip(null, "");
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new HorizontalFlip("", null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new HorizontalFlip(null, null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
  }

  @Test
  public void vertFlip() {
    try {
      new VerticalFlip(null, "");
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new VerticalFlip("", null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new VerticalFlip(null, null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
  }

  @Test
  public void sharpen() {
    try {
      new Sharpen(null, "");
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new Sharpen("", null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new Sharpen(null, null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
  }

  @Test
  public void brighten() {
    try {
      new Brighten(0, null, "");
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new Brighten(1, "", null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new Brighten(10, null, null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
  }

  @Test
  public void gaussianBlur() {
    try {
      new GaussianBlur(0, null, "");
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new GaussianBlur(1, "", null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
    try {
      new GaussianBlur(10, null, null);
      fail("Should have thrown error.");
    } catch (IllegalArgumentException e) {
      assertEquals(n, e.getMessage());
    }
  }
}
