package view;

import java.io.IOException;

import model.Image;
import model.ImageProcessingViewModel;

/**
 * Class that displays user interaction with an image via a text user interface.
 */
public class ImageProcessingTextViewImpl implements ImageProcessingTextView {
  private final ImageProcessingViewModel model;
  private final Appendable ap;

  /**
   * Construct a text view of the image from the provided model object.
   * @param model the model to use
   * @throws IllegalArgumentException if the provided model is null
   */
  public ImageProcessingTextViewImpl(ImageProcessingViewModel model)
          throws IllegalArgumentException {
    this(model, System.out);
  }

  /**
   * Construct a text view for a given model and a given output destination.
   *
   * @param model the model to use
   * @param ap the appendable object to use
   * @throws IllegalArgumentException if the provided model or appendable is null
   */
  public ImageProcessingTextViewImpl(ImageProcessingViewModel model, Appendable ap)
          throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("The provided Model object cannot be null");
    }
    if (ap == null) {
      throw new IllegalArgumentException("The provided Appendable object cannot be null");
    }
    this.model = model;
    this.ap = ap;
  }

  /**
   * Render the provided image to the data destination.
   *
   * @param name name of the images as saved in the model
   * @throws IOException if transmission of the image to the data destination fails
   */
  @Override
  public void renderImage(String name) throws IOException {
    Image image = model.getImage(name);
    this.ap.append(name).append(":").append(System.lineSeparator());
    this.ap.append("Height: ").append(Integer.toString(image.getHeight()))
            .append(System.lineSeparator());
    this.ap.append("Width: ").append(Integer.toString(image.getWidth()))
            .append(System.lineSeparator());
    this.ap.append("Max Color Value (Usually 255): ").append(Integer.toString(image.getMaxValue()))
            .append(System.lineSeparator());
  }

  /**
   * Render a specific message to the data destination.
   *
   * @param message the message to be transmitted
   * @throws IOException if transmission of the image to the data destination fails
   */
  @Override
  public void renderMessage(String message) throws IOException {
    this.ap.append(message).append(System.lineSeparator());
  }
}
