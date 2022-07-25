package view.panels;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import controller.GUIControllerFeatures;

/**
 * Class represents a panel for file related operations.
 */
public class FilePanel extends JPanel implements InteractivePanel {
  private final JButton load;
  private final JButton save;

  /**
   * Constructs a FilePanel with the provided dimensions.
   */
  public FilePanel() {
    super();
    this.setLayout(new GridLayout(2, 1));
    this.setBorder(new EmptyBorder(0, 10, 0, 10));
    this.setBackground(Color.white);
    this.load = new JButton("Load Image");
    this.save = new JButton("Save Image");
    this.addButtons();
  }

  /**
   * Adds the buttons to this panel.
   */
  private void addButtons() {
    this.add(load);
    this.add(save);
  }

  /**
   * Links the buttons with the intended image processing features.
   *
   * @param features the features to refer to
   */
  @Override
  public void operations(GUIControllerFeatures features) {
    this.load.addActionListener(evt -> features.load());
    this.save.addActionListener(evt -> features.save());
  }
}
