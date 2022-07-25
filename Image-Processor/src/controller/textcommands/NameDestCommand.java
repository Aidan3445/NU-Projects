package controller.textcommands;

import model.ImageProcessingModel;
import view.ImageProcessingTextView;

/**
 * Abstract class that represents commands that need both an image name and destination name.
 */
public abstract class NameDestCommand implements ImageProcessingTextCommand {
  protected final String name;
  protected final String dest;

  /**
   * Initialize the name of the image to use for the given command.
   *
   * @param name name to use
   * @param dest destination name to use
   * @throws IllegalArgumentException if name or dest is null
   */
  public NameDestCommand(String name, String dest) {
    if (name == null || dest == null) {
      throw new IllegalArgumentException("Name or destination name cannot be null.");
    }
    this.name = name;
    this.dest = dest;
  }

  /**
   * Make sure the model and view are not null.
   *
   * @param m model to check
   * @param v view to check
   * @throws IllegalArgumentException if either the model or view is null
   */
  protected void modelViewException(ImageProcessingModel m, ImageProcessingTextView v)
          throws IllegalArgumentException {
    if (m == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }
    if (v == null) {
      throw new IllegalArgumentException("View cannot be null.");
    }
  }
}
