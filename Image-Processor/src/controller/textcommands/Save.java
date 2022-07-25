package controller.textcommands;

import java.io.IOException;

import model.ImageProcessingModel;
import model.ImageUtil;
import view.ImageProcessingTextView;

/**
 * Class represents the save command.
 */
public class Save extends NameDestCommand {
  private final String resPath;

  /**
   * Initialize the name and path of the image to use for the given command.
   *
   * @param name name of image to save
   * @param path destination to use
   * @param resPath the source folder set
   */
  public Save(String name, String path, String resPath) {
    super(name, path);
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
    ImageUtil.saveImage(m.getImage(this.name), this.resPath + this.dest);
    v.renderMessage(String.format("%s was saved as %s", this.name, this.resPath + this.dest));
  }
}
