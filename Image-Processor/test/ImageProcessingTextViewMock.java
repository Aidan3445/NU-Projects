import java.io.IOException;

import view.ImageProcessingTextView;

/**
 * Empty text view as a mock for controller testing.
 */
public class ImageProcessingTextViewMock implements ImageProcessingTextView {
  private StringBuilder log;

  /**
   * Creates an empty text view.
   * @param log the StringBuilder object to use
   */
  public ImageProcessingTextViewMock(StringBuilder log) {
    this.log = log;
  }

  /**
   * Render the provided image to the data destination.
   *
   * @param name name of the images as saved in the model
   * @throws IOException if transmission of the board to the provided data destination fails
   */
  @Override
  public void renderImage(String name) throws IOException {
    this.log.append(String.format("Name: %s", name)).append(System.lineSeparator());
  }

  /**
   * Render a specific message to the data destination.
   *
   * @param message the message to be transmitted
   * @throws IOException if transmission of the board to the provided data destination fails
   */
  @Override
  public void renderMessage(String message) throws IOException {
    this.log.append(String.format("Message: %s", message)).append(System.lineSeparator());
  }
}
