package controller.textcommands;

import java.io.IOException;
import java.util.List;

import model.ImageProcessingModel;
import view.ImageProcessingTextView;

/**
 * Class represents the available images command.
 */
public class AvailableImages implements ImageProcessingTextCommand {

  /**
   * Construct a new command to list the images available.
   */
  public AvailableImages() {
    // No fields to initialize
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
    StringBuilder out = new StringBuilder();
    List<String> images = m.getAll();
    int size = images.size();
    if (size == 0) {
      out.append("No images loaded.").append(System.lineSeparator());
    } else if (size == 1) {
      out.append("There is 1 image available:").append(System.lineSeparator());
      out.append(images.get(0)).append(System.lineSeparator());
    } else {
      out.append(
              String.format("There are %d images available:", size)).append(System.lineSeparator());
      for (String i : images) {
        out.append(i).append(System.lineSeparator());
      }
    }
    v.renderMessage(out.toString());
  }
}
