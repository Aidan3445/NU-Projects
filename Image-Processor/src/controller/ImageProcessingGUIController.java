package controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.CancellationException;

import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.Image;
import model.ImageProcessingDownscaling;
import model.ImageProcessingModel;
import model.ImageProcessorDownscalingModel;
import model.ImageUtil;
import view.ImageProcessingGUIView;

/**
 * Class represents a gui controller for an image processing program.
 */
public class ImageProcessingGUIController implements GUIControllerFeatures {
  private final ImageProcessingModel model;
  private final ImageProcessingGUIView view;

  /**
   * Create a controller for a text view to process an image.
   *
   * @param model the model object to use
   * @param view  the view object to use
   * @throws IllegalArgumentException if any of the parameters are null
   */
  public ImageProcessingGUIController(
          ImageProcessingModel model, ImageProcessingGUIView view)
          throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("The provided model is null.");
    }
    if (view == null) {
      throw new IllegalArgumentException("The provided view is null.");
    }
    this.model = model;
    this.view = view;
    this.view.addFeatures(this);
    this.view.refresh();
  }

  /**
   * Call the red component command on the model and update the view.
   */
  @Override
  public void redComp() {
    try {
      this.model.redComponent("current", "current");
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("No image is loaded.");
    }
    this.view.refresh();
  }

  /**
   * Call the green component command on the model and update the view.
   */
  @Override
  public void greenComp() {
    try {
      this.model.greenComponent("current", "current");
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("No image is loaded.");
    }
    this.view.refresh();
  }

  /**
   * Call the blue component command on the model and update the view.
   */
  @Override
  public void blueComp() {
    try {
      this.model.blueComponent("current", "current");
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("No image is loaded.");
    }
    this.view.refresh();
  }

  /**
   * Call the value component command on the model and update the view.
   */
  @Override
  public void valueComp() {
    try {
      this.model.valueComponent("current", "current");
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("No image is loaded.");
    }
    this.view.refresh();
    this.view.refresh();
  }

  /**
   * Call the luma component command on the model and update the view.
   */
  @Override
  public void lumaComp() {
    try {
      this.model.lumaComponent("current", "current");
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("No image is loaded.");
    }
    this.view.refresh();
    this.view.refresh();
  }

  /**
   * Call the intensity component command on the model and update the view.
   */
  @Override
  public void intensityComp() {
    try {
      this.model.intensityComponent("current", "current");
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("No image is loaded.");
    }
    this.view.refresh();
    this.view.refresh();
  }

  /**
   * Call the greyscale command on the model and update the view.
   */
  @Override
  public void greyscale() {
    try {
      this.model.greyscale("current", "current");
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("No image is loaded.");
    }
    this.view.refresh();
    this.view.refresh();
  }

  /**
   * Call the sepia command on the model and update the view.
   */
  @Override
  public void sepia() {
    try {
      this.model.sepia("current", "current");
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("No image is loaded.");
    }
    this.view.refresh();
    this.view.refresh();
  }

  /**
   * Call the brighten command on the model and update the view.
   * Prompts for an amount to brighten or darken.
   */
  @Override
  public void brighten() {
    int amount;
    try {
      this.model.getImage("current");
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("No image is loaded.");
      return;
    }
    try {
      amount = this.view.getIntInput("Brighten amount (negative values will darken)");
    } catch (CancellationException e) {
      return;
    }
    this.model.brighten(amount, "current", "current");
    this.view.refresh();
  }

  /**
   * Call the gaussian blur command on the model and update the view.
   * Prompts for an amount to blur.
   */
  @Override
  public void gaussianBlur() {
    int amount;
    try {
      this.model.getImage("current");
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("No image is loaded.");
      return;
    }
    try {
      amount = this.view.getIntInput("Blur amount (0-25)");
      if (amount > 25) {
        // This is for minimizing gui loading times,
        // functionality should not be limited within the processor itself.
        this.view.renderMessage("Maximum blur is 25.");
        return;
      }
    } catch (CancellationException e) {
      return;
    }
    try {
      this.model.gaussianBlur(amount, "current", "current");
    } catch (IllegalArgumentException e) {
      this.view.renderMessage(e.getMessage());
    }
    this.view.refresh();
  }

  /**
   * Call the sharpen command on the model and update the view.
   */
  @Override
  public void sharpen() {
    try {
      this.model.sharpen("current", "current");
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("No image is loaded.");
      return;
    }
    this.view.refresh();
  }

  /**
   * Call the horizontal flip command on the model and update the view.
   */
  @Override
  public void horizontalFlip() {
    try {
      this.model.horizontalFlip("current", "current");
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("No image is loaded.");
      return;
    }
    this.view.refresh();
  }

  /**
   * Call the vertical flip command on the model and update the view.
   */
  @Override
  public void verticalFlip() {
    try {
      this.model.verticalFlip("current", "current");
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("No image is loaded.");
      return;
    }
    this.view.refresh();
  }

  /**
   * Call the downscale command on the model and update the view.
   * Prompts for an amount to downscale in both the x and y direction.
   */
  @Override
  public void downscale() {
    int pctX;
    int pctY;
    try {
      this.model.getImage("current");
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("No image is loaded.");
      return;
    }
    try {
      pctX = this.view.getIntInput("X downscale size (0-100% of original)");
      pctY = this.view.getIntInput("Y downscale size (0-100% of original)");
    } catch (CancellationException e) {
      return;
    }
    ImageProcessingDownscaling down = new ImageProcessorDownscalingModel(this.model);
    try {
      down.downscale(pctX, pctY, "current", "current");
    } catch (IllegalArgumentException e) {
      this.view.renderMessage(e.getMessage());
    }
    this.view.refresh();
  }

  /**
   * Call the load command on the model and update the view.
   */
  @Override
  public void load() {
    String f;
    try {
      f = this.view.filePrompt(fChooser -> {
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "PPM, PNG, BMP, JPG, & JPEG Images",
                "jpg", "jpeg", "png", "ppm", "bmp");
        fChooser.setFileFilter(filter);
        return fChooser.showOpenDialog(new JPopupMenu());
      });
    } catch (CancellationException e) {
      return;
    }
    try {
      this.model.load("current", ImageUtil.readImage(f));
    } catch (FileNotFoundException e) {
      this.view.renderMessage(
              "The image you tried to load either doesn't exist or is not compatible.");
    }
    this.view.refresh();
  }

  /**
   * Call the save command on the model and update the view.
   */
  @Override
  public void save() {
    String f;
    Image i;
    try {
      i = this.model.getImage("current");
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("No image is loaded.");
      return;
    }
    try {
      f = this.view.filePrompt(fChooser -> fChooser.showSaveDialog(new JPopupMenu()));
    } catch (CancellationException e) {
      return;
    }
    try {
      ImageUtil.saveImage(i, f);

    } catch (IOException e) {
      this.view.renderMessage("Image failed to save.");
      return;
    }
    this.view.refresh();
  }
}
