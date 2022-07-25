import java.util.concurrent.CancellationException;
import java.util.function.Function;

import javax.swing.JFileChooser;

import controller.GUIControllerFeatures;
import view.ImageProcessingGUIView;

/**
 * Empty GUI View as a mock for controller testing.
 */
public class ImageProcessorGUIViewMock implements ImageProcessingGUIView {
  private StringBuilder log;

  /**
   * Creates an empty GUI view.
   * @param log the StringBuilder object to use
   */
  public ImageProcessorGUIViewMock(StringBuilder log) {
    this.log = log;
  }

  /**
   * Refresh the screen. This is called when the something on the
   * screen is updated and therefore it must be redrawn.
   */
  @Override
  public void refresh() {
    this.log.append("Refreshed.").append(System.lineSeparator());
  }

  /**
   * Display a message in a suitable area of the GUI.
   *
   * @param message the message to be displayed
   */
  @Override
  public void renderMessage(String message) {
    this.log.append(String.format("Message: %s", message)).append(System.lineSeparator());
  }

  /**
   * Prompts an input. Re-prompts until cancelled or an integer is entered.
   *
   * @return integer that was entered
   * @throws CancellationException when the action is cancelled
   */
  @Override
  public int getIntInput(String info) throws CancellationException {
    this.log.append(info);
    return 10;
  }

  /**
   * Add features to the view.
   *
   * @param features the object holding the features
   */
  @Override
  public void addFeatures(GUIControllerFeatures features) {
    this.log.append("Features added.").append(System.lineSeparator());
  }

  /**
   * Prompts the file system to load and save images.
   * A filter may be null; if so, that is used for save a file.
   *
   * @param loadSave function object to determine whether the view
   *                 should prompt to load or save a file.
   * @return the absolute path to this file
   */
  @Override
  public String filePrompt(Function<JFileChooser, Integer> loadSave) {
    this.log.append("File System Prompted");
    return "file.png";
  }
}
