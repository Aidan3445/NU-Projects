package controller.textcommands;

import java.io.IOException;

import model.ImageProcessingModel;
import view.ImageProcessingTextView;

/**
 * Interface that represents and image processing command available to a text view.
 */
public interface ImageProcessingTextCommand {
  /**
   * Executes this command on the provided model and view objects.
   * @param m the model object to use
   * @param v the view object to use
   * @throws IllegalArgumentException if either of the model or view object is null
   * @throws IOException if transmission to the view fails
   */
  void execute(ImageProcessingModel m, ImageProcessingTextView v)
          throws IllegalArgumentException, IOException;
}
