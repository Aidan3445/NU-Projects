import org.junit.Before;
import org.junit.Test;

import model.Image;
import model.ImageImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;


/**
 * A JUnit class to test the ImageImpl class.
 */
public class ImageImplTest {
  Image i;

  @Before
  public void setup() {
    this.i = new ImageImpl(new double[][][]{{{0.,0.,0.,0.},
            {1.,1.,1.,0.}}}, 255);
  }

  @Test
  public void constructorAndExceptions() {
    try {
      this.i = new ImageImpl(null, 0);
      fail("Constructor should have thrown error: Null pixels");
    } catch (IllegalArgumentException e) {
      assertEquals("The provided pixels array cannot be null.", e.getMessage());
    }
    try {
      this.i = new ImageImpl(new double[][][]{{{0}}}, 0);
      fail("Constructor should have thrown error: Non RGB/RGBA");
    } catch (IllegalArgumentException e) {
      assertEquals("All provided pixels must be in RGBA format.",
              e.getMessage());
    }
    try {
      this.i = new ImageImpl(new double[][][]{{{0.,0.,0.,0.}}}, 0);
    } catch (IllegalArgumentException e) {
      fail("This should npt have thrown an exception.");
    }
  }

  @Test
  public void getPixel() {
    assertArrayEquals(new double[] {0.,0.,0.,0.}, this.i.getPixel(0,0), .001);
    try {
      this.i.getPixel(2,4);
      fail("Should not have allowed row and/or column to be more than the pixel width and height");
    } catch (IllegalArgumentException e) {
      assertEquals("Row 2 and column 4 is out of bounds", e.getMessage());
    }
  }

  @Test
  public void getMaxValue() {
    assertEquals(255, this.i.getMaxValue());
  }

  @Test
  public void getWidth() {
    assertEquals(2, this.i.getWidth());
  }

  @Test
  public void getHeight() {
    assertEquals(1, this.i.getHeight());
  }
}