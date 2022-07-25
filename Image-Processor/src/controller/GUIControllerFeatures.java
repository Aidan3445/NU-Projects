package controller;

/**
 * Interface represents the features available to a GUI controller.
 */
public interface GUIControllerFeatures {
  /**
   * Call the red component command on the model and update the view.
   */
  void redComp();

  /**
   * Call the green component command on the model and update the view.
   */
  void greenComp();

  /**
   * Call the blue component command on the model and update the view.
   */
  void blueComp();

  /**
   * Call the value component command on the model and update the view.
   */
  void valueComp();

  /**
   * Call the luma component command on the model and update the view.
   */
  void lumaComp();

  /**
   * Call the intensity component command on the model and update the view.
   */
  void intensityComp();

  /**
   * Call the greyscale command on the model and update the view.
   */
  void greyscale();

  /**
   * Call the sepia command on the model and update the view.
   */
  void sepia();

  /**
   * Call the brighten command on the model and update the view.
   * Prompts for an amount to brighten or darken.
   */
  void brighten();

  /**
   * Call the gaussian blur command on the model and update the view.
   * Prompts for an amount to blur.
   */
  void gaussianBlur();

  /**
   * Call the sharpen command on the model and update the view.
   */
  void sharpen();

  /**
   * Call the horizontal flip command on the model and update the view.
   */
  void horizontalFlip();

  /**
   * Call the vertical flip command on the model and update the view.
   */
  void verticalFlip();

  /**
   * Call the downscale command on the model and update the view.
   * Prompts for an amount to downscale in both the x and y direction.
   */
  void downscale();

  /**
   * Call the load command on the model and update the view.
   */
  void load();

  /**
   * Call the save command on the model and update the view.
   */
  void save();
}
