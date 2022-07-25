package view;

import java.io.IOException;

/**
 * Interface represents a view for an image processor displayed to the user.
 */
public interface ImageProcessingTextView {
  /**
   * Render the provided image to the data destination.
   *
   * @param name name of the images as saved in the model
   * @throws IOException if transmission of the image to the data destination fails
   */
  void renderImage(String name) throws IOException;

  /**
   * Render a specific message to the data destination.
   *
   * @param message the message to be transmitted
   * @throws IOException if transmission of the image to the data destination fails
   */
  void renderMessage(String message) throws IOException;
}
