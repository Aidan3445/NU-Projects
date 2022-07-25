import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import model.Image;
import model.ImageProcessingModel;
import model.ImageProcessingViewModel;
import model.ImageProcessorImpl;
import model.ImageUtil;
import view.ImageProcessingTextView;
import view.ImageProcessingTextViewImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * JUnit test for text view of image processor.
 */
public class ImageProcessingTextViewTest {
  ImageProcessingViewModel viewModel;
  Appendable ap;
  Appendable badAP;
  ImageProcessingTextView view;
  ImageProcessingTextView badView;

  /**
   * Creates a view model for the text view test.
   */
  public ImageProcessingTextViewTest() {
    ImageProcessingModel model = new ImageProcessorImpl();
    this.ap = new StringBuilder();
    this.badAP = new CorruptAppendable();

    try {
      Image image = ImageUtil.readImage("./res/penguin.ppm");
      model.load("p", image);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    this.viewModel = model;

    this.view = new ImageProcessingTextViewImpl(this.viewModel, this.ap);
    this.badView = new ImageProcessingTextViewImpl(this.viewModel, this.badAP);
  }

  @Test
  public void renderImage() {
    try {
      this.view.renderImage("p");
    } catch (IOException e) {
      fail("Appendable should not be corrupt.");
    }
    assertEquals("p:"
            + System.lineSeparator() + "Height: 128"
            + System.lineSeparator() + "Width: 128"
            + System.lineSeparator() + "Max Color Value (Usually 255): 255"
            + System.lineSeparator(), this.ap.toString());

    try {
      this.badView.renderImage("p");
      fail("Appendable is corrupt, error should have been thrown.");
    } catch (IOException e) {
      assertEquals("This is corrupt.", e.getMessage());
    }
  }

  @Test
  public void renderMessage() {
    try {
      this.view.renderMessage("HELLLLOOOOO");
    } catch (IOException e) {
      fail("Appendable should not be corrupt.");
    }
    assertEquals("HELLLLOOOOO" + System.lineSeparator(), this.ap.toString());

    try {
      this.badView.renderMessage("HELLLLOOOOO");
      fail("Appendable is corrupt, error should have been thrown.");
    } catch (IOException e) {
      assertEquals("This is corrupt.", e.getMessage());
    }
  }
}