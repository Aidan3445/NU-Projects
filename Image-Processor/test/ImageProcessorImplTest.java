import model.ImageProcessingModel;
import model.ImageProcessorImpl;

/**
 * JUnit tests for an image processor.
 */
public class ImageProcessorImplTest extends AbstractImageProcessorTest {
  /**
   * Initialize the path for the images to be pulled from and saved to.
   */
  public ImageProcessorImplTest() {
    super("./res/"); // change per your file management.
  }

  /**
   * Create a model of a type specific to the child test class.
   *
   * @return the model that was created
   */
  @Override
  protected ImageProcessingModel createModel() {
    return new ImageProcessorImpl();
  }
}