package controller.textcommands;

import java.io.FileNotFoundException;
import java.io.IOException;

import model.Image;
import model.ImageProcessingModel;
import model.ImageUtil;
import view.ImageProcessingTextView;

/**
 * Class represents the load command.
 */
public class Load extends NameDestCommand {
  private final String resPath;

  /**
   * Initialize the name of the image to use for the given command.
   *
   * @param path path to get image from
   * @param name destination name to hold image as in the model
   * @param resPath the source folder set
   */
  public Load(String path, String name, String resPath) {
    super(path, name);
    this.resPath = resPath;
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
    String path = this.resPath + this.name;
    try {
      Image image = ImageUtil.readImage(path);
      m.load(this.dest, image);

    } catch (FileNotFoundException e) {
      v.renderMessage(
              String.format("Error: Image with the provided path '%s' does not exist", path));
      return;
    }
    v.renderMessage(String.format("Image %s was loaded and called %s",
            this.resPath + this.name, this.dest));

  }
}
