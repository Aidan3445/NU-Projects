import org.junit.Test;

import model.ImageProcessingDownscaling;
import model.ImageProcessingModel;
import model.ImageProcessorDownscalingModel;
import model.ImageProcessorImpl;

import static org.junit.Assert.assertTrue;

/**
 * JUnit tests for an image processor with downscaling.
 */
public class ImageProcessorDownscalingModelTest extends AbstractImageProcessorTest {

  /**
   * Initialize the path for the images to be pulled from and saved to.
   */
  public ImageProcessorDownscalingModelTest() {
    super("./res/");
  }

  @Test
  public void downscaleEven() {
    this.loadImages("red4x4.ppm", "2x2.ppm");
    ((ImageProcessingDownscaling)this.model).downscale(50, 50, "0", "0");
    assertTrue(this.checkAllSamePixels(1, 1,
        new double[]{1.,0.,0.,1.}));
  }

  @Test
  public void downscaleUneven1() {
    this.loadImages("red4x4.ppm");
    ((ImageProcessingDownscaling)this.model).downscale(25, 100, "0", "0");
    assertTrue(this.checkAllSamePixels(1, 4,
            new double[]{1.,0.,0.,1.}));
  }

  @Test
  public void downscaleUneven2() {
    this.loadImages("red4x4.ppm");
    ((ImageProcessingDownscaling)this.model).downscale(100, 76, "0", "0");
    assertTrue(this.checkAllSamePixels(4, 3,
            new double[]{1.,0.,0.,1.}));
  }

  @Test
  public void downscaleUneven3() {
    this.loadImages("red4x4.ppm");
    ((ImageProcessingDownscaling)this.model).downscale(50, 76, "0", "0");
    assertTrue(this.checkAllSamePixels(2, 3,
            new double[]{1.,0.,0.,1.}));
  }

  @Test
  public void downscaleMixedPixels() {
    this.loadImages("hBars4x4.ppm");
    ((ImageProcessingDownscaling)this.model).downscale(6, 6, "0", "0");
    assertTrue(this.checkAllSamePixels(1, 1,
            new double[]{1.,0.,1.,1.}));
  }

  /**
   * Create a model of a type specific to the child test class.
   *
   * @return the model that was created
   */
  @Override
  protected ImageProcessingModel createModel() {
    return new ImageProcessorDownscalingModel(new ImageProcessorImpl());
  }
}