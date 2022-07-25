package controller.textcommands;

import java.io.IOException;

import model.ImageProcessingModel;
import view.ImageProcessingTextView;

/**
 * Class represents a gaussian blur command.
 */
public class GaussianBlur extends AmountCommand {
  /**
   * Initialize the name of the image to use for the given command.
   *
   * @param name name to use
   * @param dest destination name to use
   */
  public GaussianBlur(int amount, String name, String dest) {
    super(amount, name, dest);

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
    m.gaussianBlur(this.amount, this.name, this.dest);
    v.renderMessage(
            String.format("%s was saved as a gaussian blurred image by %d called %s",
                    this.name, this.amount, this.dest));

  }
}
