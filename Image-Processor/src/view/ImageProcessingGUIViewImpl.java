package view;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.io.File;
import java.util.concurrent.CancellationException;
import java.util.function.Function;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.border.EmptyBorder;

import controller.GUIControllerFeatures;
import model.ImageProcessingViewModel;
import view.panels.FilePanel;
import view.panels.HistogramPanel;
import view.panels.HistogramPanelImpl;
import view.panels.ImagePanel;
import view.panels.InteractivePanel;
import view.panels.OperationsPanel;

/**
 * Class that displays user interaction with an image via a GUI user interface.
 */
public class ImageProcessingGUIViewImpl extends JFrame implements ImageProcessingGUIView {
  private final ImageProcessingViewModel model;
  private final HistogramPanel hGramPanel;
  private final InteractivePanel opsPanel;
  private final InteractivePanel filePanel;

  /**
   * Construct a text view of the image from the provided model object.
   *
   * @param model the model to use
   * @throws IllegalArgumentException if the provided model is null
   */
  public ImageProcessingGUIViewImpl(ImageProcessingViewModel model)
          throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }
    this.model = model;
    Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    Dimension d = new Dimension(bounds.width, bounds.height);

    // right side
    JPanel side = new JPanel();
    side.setLayout(new GridLayout(3, 0, 0, 5));
    side.setBorder(new EmptyBorder(0, 10, 0, 0));
    side.setPreferredSize(new Dimension(d.width * 2 / 10, d.height));

    // histogram
    this.hGramPanel = new HistogramPanelImpl();
    JScrollPane scrollHGram = new JScrollPane((JPanel) this.hGramPanel);
    scrollHGram.setBorder(null);
    side.add(scrollHGram);

    // ops
    this.opsPanel = new OperationsPanel();
    JScrollPane scrollOps = new JScrollPane((JPanel) this.opsPanel);
    scrollOps.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrollOps.setBorder(null);
    side.add(scrollOps);

    // files
    this.filePanel = new FilePanel();
    side.add((JPanel) this.filePanel);

    this.add(side, BorderLayout.EAST);

    // image
    JPanel imagePanel = new ImagePanel(this.model, d);
    JScrollPane scrollImage = new JScrollPane(imagePanel);
    scrollImage.setBorder(null);
    scrollImage.setPreferredSize(new Dimension(d.width * 8 / 10, d.height));
    scrollImage.getVerticalScrollBar().setUnitIncrement(16);
    scrollImage.getHorizontalScrollBar().setUnitIncrement(16);
    scrollImage.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
    this.add(scrollImage, BorderLayout.WEST);

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.pack();
    this.setResizable(false);
    this.setVisible(true);
    this.setFocusable(true);
  }

  /**
   * Refresh the screen. This is called when the something on the
   * screen is updated, and therefore it must be redrawn.
   */
  @Override
  public void refresh() {
    try {
      this.hGramPanel.update(this.model.getImage("current"));
    } catch (IllegalArgumentException e) {
      // No image loaded, no histogram to draw
    }
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.requestFocus();
    this.repaint();
  }

  /**
   * Display a message in a suitable area of the GUI.
   *
   * @param message the message to be displayed
   */
  @Override
  public void renderMessage(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

  /**
   * Prompts an input. Re-prompts until cancelled or an integer is entered.
   *
   * @return integer that was entered
   * @throws CancellationException when the action is cancelled
   */
  @Override
  public int getIntInput(String info) throws CancellationException {
    int amount;
    while (true) {
      String prompt = JOptionPane.showInputDialog("Enter an integer: " + info);
      if (prompt == null) {
        throw new CancellationException("Cancelled input request.");
      }
      try {
        amount = Integer.parseInt(prompt);
        break;
      } catch (NumberFormatException e) {
        this.renderMessage("");
        JOptionPane.showMessageDialog(this, "Please enter an integer.");
      }
    }
    return amount;
  }

  /**
   * Add features to the view.
   *
   * @param features the object holding the features
   */
  @Override
  public void addFeatures(GUIControllerFeatures features) {
    this.opsPanel.operations(features);
    this.filePanel.operations(features);
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
    final JFileChooser fChooser = new JFileChooser(".");
    int retValue = loadSave.apply(fChooser);
    File f;
    if (retValue == JFileChooser.APPROVE_OPTION) {
      f = fChooser.getSelectedFile();
    } else {
      throw new CancellationException("File path not selected.");
    }
    return f.getAbsolutePath();
  }
}