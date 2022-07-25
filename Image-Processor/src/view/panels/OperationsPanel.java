package view.panels;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import controller.GUIControllerFeatures;

/**
 * Class represents a panel for the image processing operations.
 */
public class OperationsPanel extends JPanel implements InteractivePanel {
  private final JButton red;
  private final JButton blue;
  private final JButton green;
  private final JButton value;
  private final JButton luma;
  private final JButton intensity;
  private final JButton greyscale;
  private final JButton sepia;
  private final JButton blur;
  private final JButton sharpen;
  private final JButton brighten;
  private final JButton hFlip;
  private final JButton vFlip;
  private final JButton downscale;

  /**
   * Constructs an OperationsPanelImpl.
   */
  public OperationsPanel() {
    super();
    this.setLayout(new GridLayout(14, 0)); // update to match number of buttons
    this.setBorder(new EmptyBorder(0, 10, 0, 10));
    this.setBackground(Color.white);
    this.red = new JButton("Red Component");
    this.green = new JButton("Green Component");
    this.blue = new JButton("Blue Component");
    this.value = new JButton("Value Component");
    this.luma = new JButton("Luma Component");
    this.intensity = new JButton("Intensity Component");
    this.greyscale = new JButton("Greyscale");
    this.sepia = new JButton("Sepia");
    this.blur = new JButton("Blur");
    this.sharpen = new JButton("Sharpen");
    this.brighten = new JButton("Brighten");
    this.hFlip = new JButton("Horizontal Flip");
    this.vFlip = new JButton("Vertical Flip");
    downscale = new JButton("Downscale");
    this.addButtons();
  }

  /**
   * Adds the buttons to this panel.
   */
  private void addButtons() {
    this.add(this.red);
    this.add(this.green);
    this.add(this.blue);
    this.add(this.value);
    this.add(this.luma);
    this.add(this.intensity);
    this.add(this.greyscale);
    this.add(this.sepia);
    this.add(this.blur);
    this.add(this.sharpen);
    this.add(this.brighten);
    this.add(this.hFlip);
    this.add(this.vFlip);
    this.add(this.downscale);
  }

  /**
   * Links the buttons with the intended image processing features.
   *
   * @param features the features to refer to
   */
  @Override
  public void operations(GUIControllerFeatures features) {
    this.red.addActionListener(evt -> features.redComp());
    this.green.addActionListener(evt -> features.greenComp());
    this.blue.addActionListener(evt -> features.blueComp());
    this.value.addActionListener(evt -> features.valueComp());
    this.luma.addActionListener(evt -> features.lumaComp());
    this.intensity.addActionListener(evt -> features.intensityComp());
    this.sepia.addActionListener(evt -> features.sepia());
    this.greyscale.addActionListener(evt -> features.greyscale());
    this.blur.addActionListener(evt -> features.gaussianBlur());
    this.sharpen.addActionListener(evt -> features.sharpen());
    this.brighten.addActionListener(evt -> features.brighten());
    this.hFlip.addActionListener(evt -> features.horizontalFlip());
    this.vFlip.addActionListener(evt -> features.verticalFlip());
    this.downscale.addActionListener(evt -> features.downscale());
  }
}
