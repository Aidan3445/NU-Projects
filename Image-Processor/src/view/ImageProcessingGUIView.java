package view;

import java.util.concurrent.CancellationException;
import java.util.function.Function;

import javax.swing.JFileChooser;

import controller.GUIControllerFeatures;

/**
 * Interface represents a view for a GUI program.
 */
public interface ImageProcessingGUIView {
  /**
   * Refresh the screen. This is called when the something on the
   * screen is updated, and therefore it must be redrawn.
   */
  void refresh();

  /**
   * Display a message in a suitable area of the GUI.
   * @param message the message to be displayed
   */
  void renderMessage(String message);

  /**
   * Prompts an input. Re-prompts until cancelled or an integer is entered.
   *
   * @param info additional details to direct what the input is for
   * @return integer that was entered
   * @throws CancellationException when the action is cancelled
   */
  int getIntInput(String info) throws CancellationException;

  /**
   * Add features to the view.
   *
   * @param features the object holding the features
   */
  void addFeatures(GUIControllerFeatures features);

  /**
   * Prompts the file system to load and save images.
   * A filter may be null; if so, that is used for save a file.
   *
   * @param loadSave function object to determine whether the view
   *                 should prompt to load or save a file.
   * @return the absolute path to this file
   */
  String filePrompt(Function<JFileChooser, Integer> loadSave);
}
