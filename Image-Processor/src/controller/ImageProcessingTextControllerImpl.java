package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import controller.textcommands.BlueComp;
import controller.textcommands.Brighten;
import controller.textcommands.GaussianBlur;
import controller.textcommands.GreenComp;
import controller.textcommands.Greyscale;
import controller.textcommands.HorizontalFlip;
import controller.textcommands.ImageProcessingTextCommand;
import controller.textcommands.IntensityComp;
import controller.textcommands.Load;
import controller.textcommands.AvailableImages;
import controller.textcommands.LumaComp;
import controller.textcommands.RedComp;
import controller.textcommands.Save;
import controller.textcommands.Sepia;
import controller.textcommands.Sharpen;
import controller.textcommands.ValueComp;
import controller.textcommands.VerticalFlip;
import model.ImageProcessingModel;
import view.ImageProcessingTextView;

/**
 * Class represents the controller to allow user to perform
 * image processing effects via text interface.
 */
public class ImageProcessingTextControllerImpl
        implements ImageProcessingTextController {
  private final ImageProcessingModel model;
  private final ImageProcessingTextView view;
  private final Scanner sc;
  private final Map<String, Function<Scanner, ImageProcessingTextCommand>> commandMap;
  private String resPath = "";

  /**
   * Create a controller for a text view to process an image.
   *
   * @param model the model object to use
   * @param view  the view object to use
   * @param rd    the readable object to use
   * @throws IllegalArgumentException if any of the parameters are null
   */
  public ImageProcessingTextControllerImpl(
          ImageProcessingModel model, ImageProcessingTextView view, Readable rd)
          throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("The provided model is null.");
    }
    if (view == null) {
      throw new IllegalArgumentException("The provided view is null.");
    }
    if (rd == null) {
      throw new IllegalArgumentException("The provided readable object is null.");
    }
    this.model = model;
    this.view = view;
    this.sc = new Scanner(rd);
    this.sc.useDelimiter(",|\\s+");
    this.commandMap = this.setupCommands();
  }

  /**
   * Sets up the commands available to this program.
   * Make sure to add all command objects that should be available to the controller.
   *
   * @return the map that was created
   */
  @Override
  public Map<String, Function<Scanner, ImageProcessingTextCommand>> setupCommands() {
    Map<String, Function<Scanner, ImageProcessingTextCommand>> map =
            new HashMap<String, Function<Scanner, ImageProcessingTextCommand>>();
    map.put("load", s -> new Load(s.next(), s.next(), this.resPath));
    map.put("save", s -> new Save(s.next(), s.next(), this.resPath));
    map.put("available-images", s -> new AvailableImages());
    map.put("red-component", s -> new RedComp(s.next(), s.next()));
    map.put("green-component", s -> new GreenComp(s.next(), s.next()));
    map.put("blue-component", s -> new BlueComp(s.next(), s.next()));
    map.put("value-component", s -> new ValueComp(s.next(), s.next()));
    map.put("luma-component", s -> new LumaComp(s.next(), s.next()));
    map.put("intensity-component", s -> new IntensityComp(s.next(), s.next()));
    map.put("greyscale", s -> new Greyscale(s.next(), s.next()));
    map.put("sepia", s -> new Sepia(s.next(), s.next()));
    map.put("brighten", s -> new Brighten(s.nextInt(), s.next(), s.next()));
    map.put("gaussian-blur", s -> new GaussianBlur(s.nextInt(), s.next(), s.next()));
    map.put("sharpen", s -> new Sharpen(s.next(), s.next()));
    map.put("horizontal-flip", s -> new HorizontalFlip(s.next(), s.next()));
    map.put("vertical-flip", s -> new VerticalFlip(s.next(), s.next()));
    return map;
  }

  /**
   * Runs the model and begins taking inputs.
   *
   * @throws IllegalStateException if the controller cannot properly read or write
   */
  @Override
  public void run() throws IllegalStateException {
    boolean quit = false;
    String operation;
    try {
      this.menu();
      while (!quit && sc.hasNext()) {
        operation = sc.next().toLowerCase();
        Function<Scanner, ImageProcessingTextCommand> cmd = this.commandMap.get(operation);
        if (cmd == null) {
          switch (operation) {
            case "menu":
              this.menu();
              break;
            case "set-source":
              this.setSourcePath();
              break;
            case "q":
            case "quit":
              quit = true;
              break;
            default:
              this.view.renderMessage("The provided operation is not supported");
          }
        } else {
          try {
            ImageProcessingTextCommand c = cmd.apply(this.sc);
            try {
              c.execute(this.model, this.view);
              this.view.renderMessage("Type instruction: ");
            } catch (IllegalArgumentException e) {
              this.view.renderMessage("Error: " + e.getMessage());
            }
          } catch (InputMismatchException e) {
            this.view.renderMessage("Invalid inputs try again, re-read the menu descriptions.");
          }
        }
      }
    } catch (IOException e) {
      throw new IllegalStateException("Inputs or outputs failed." + System.lineSeparator());
    }
  }

  /**
   * The operations available to this program.
   *
   * @throws IOException if transmission to the view fails
   */
  @Override
  public void menu() throws IOException {
    String menu = "Operation | Description" + System.lineSeparator();
    menu += "load image-path image-name | "
            + "Loads an image from the image-path and names it image-name"
            + System.lineSeparator();
    menu += "save image-name image-path | "
            + "Saves the image named image-name to the image-path which should also "
            + "include the image-name at the end"
            + System.lineSeparator();
    menu += "available-images | "
            + "Get a list of all images available to you for image processing"
            + System.lineSeparator();
    menu += "set-source | "
            + "Set the source folder to load and save images to"
            + System.lineSeparator();
    menu += "red-component image-name destination-image-name | "
            + "Creates a greyscale image with the red component of the image-name and "
            + "names it destination-image-name"
            + System.lineSeparator();
    menu += "green-component image-name destination-image-name | "
            + "Creates a greyscale image with the green component of the image-name and "
            + "names it destination-image-name"
            + System.lineSeparator();
    menu += "blue-component image-name destination-image-name | "
            + "Creates a greyscale image with the blue component of the image-name and "
            + "names it destination-image-name"
            + System.lineSeparator();
    menu += "value-component image-name destination-image-name | "
            + "Creates a greyscale image with the maximum RGB value for each pixel of"
            + " the image-name and names it destination-image-name"
            + System.lineSeparator();
    menu += "luma-component image-name destination-image-name | "
            + "Creates a greyscale image with the average RGB value for each pixel of"
            + " the image-name and names it destination-image-name"
            + System.lineSeparator();
    menu += "intensity-component image-name destination-image-name | "
            + "Creates a greyscale image with the weighted sum of 0.126R + 0.7152G + 0.0722B "
            + "for each pixel of the image-name and names it destination-image-name"
            + System.lineSeparator();
    menu += "greyscale image-name destination-image-name | "
            + "Creates a greyscale image for image-name and names it destination-image-name"
            + System.lineSeparator();
    menu += "sepia image-name destination-image-name | "
            + "Creates a sepia-toned image for image-name and names it destination-image-name"
            + System.lineSeparator();
    menu += "brighten increment image-name destination-image-name | "
            + "Creates a new image by brightening the image-name by the increment and names it "
            + "destination-image-name"
            + System.lineSeparator();
    menu += "gaussian-blur amount image-name destination-image-name | "
            + "Blurs the image by a given amount"
            + System.lineSeparator();
    menu += "sharpen image-name destination-image-name | "
            + "Sharpens the image once"
            + System.lineSeparator();
    menu += "horizontal-flip image-name destination-image-name | "
            + "Flips image-name horizontally and names it destination-image-name"
            + System.lineSeparator();
    menu += "vertical-flip image-name destination-image-name | "
            + "Flips image-name vertically and names it destination-image-name"
            + System.lineSeparator();
    menu += "q or quit| Quits the program"
            + System.lineSeparator();
    menu += System.lineSeparator()
            + "Type instruction:";
    this.view.renderMessage(menu);
  }

  /**
   * Set the source path for folder to load and save images.
   *
   * @throws IOException if transmission to the view fails
   */
  @Override
  public void setSourcePath() throws IOException {
    String path = sc.next();
    this.resPath = path;
    this.view.renderMessage(String.format("Source folder has been set to '%s'", path));
    this.view.renderMessage("Type instruction: ");
  }
}
