package controller.textcommands;

import java.io.IOException;

import model.ImageProcessingModel;
import view.ImageProcessingTextView;

/**
 * Class represents a sharpen command.
 */
public class Sharpen extends NameDestCommand {
  /**
   * Initialize the name of the image to use for the given command.
   *
   * @param name name to use
   * @param dest destination name to use
   */
  public Sharpen(String name, String dest) {
    super(name, dest);
  }

  /**
   * Executes this command on the provided model and view objects.
   *
   * @param m the model object to use
   * @param v the view object to use
   * @throws IllegalArgumentException if either of the model or view object is null
   * @throws IOException              if transmission to the view fails
   */
  @Override
  public void execute(ImageProcessingModel m, ImageProcessingTextView v)
          throws IllegalArgumentException, IOException {
    this.modelViewException(m, v);
    m.sharpen(name, dest);
    v.renderMessage(
            String.format("%s was saved as a sharpened image called %s",
                    name, dest));
  }
}
