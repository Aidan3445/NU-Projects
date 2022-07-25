package view.panels;

import controller.GUIControllerFeatures;

/**
 * Interface for the interactive side panel.
 */
public interface InteractivePanel {
  /**
   * Links the buttons with the intended image processing features.
   *
   * @param features the features to refer to
   */
  void operations(GUIControllerFeatures features);
}
