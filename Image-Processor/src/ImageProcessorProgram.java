import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

import controller.ImageProcessingGUIController;
import controller.ImageProcessingTextController;
import controller.ImageProcessingTextControllerImpl;
import model.ImageProcessingModel;
import model.ImageProcessorImpl;
import view.ImageProcessingGUIView;
import view.ImageProcessingTextView;
import view.ImageProcessingTextViewImpl;
import view.ImageProcessingGUIViewImpl;

/**
 * Main class for the image processor program by Aidan and Cilly.
 */
public class ImageProcessorProgram {
  /**
   * Runs the program.
   *
   * @param args command line arguments, not yet supported
   */
  public static void main(String[] args) {
    Readable r;
    ImageProcessingModel m = new ImageProcessorImpl();
    if (args.length > 0) {
      String runType = args[0];
      if (runType.equalsIgnoreCase("-file")) {
        try {
          r = new FileReader(args[1]);
        } catch (FileNotFoundException e) {
          System.out.println("Provided file path was invalid.");
          return;
        }
      } else if (runType.equalsIgnoreCase("-text")) {
        r = new InputStreamReader(System.in);
      } else {
        System.out.println("-file [file path] and -text are valid command-line arguments.");
        return;
      }
      ImageProcessingTextView v = new ImageProcessingTextViewImpl(m);
      ImageProcessingTextController c = new ImageProcessingTextControllerImpl(m, v, r);
      c.run();
    } else {
      ImageProcessingGUIView v = new ImageProcessingGUIViewImpl(m);
      ImageProcessingGUIController c = new ImageProcessingGUIController(m, v);
    }
  }
}
