package cs3500.marblesolitaire.view;

import cs3500.marblesolitaire.model.hw02.EnglishSolitaireModel;

/**
 * JUnit test for MarbleSolitaireTextView class.
 */

public class MarbleSolitaireTextViewTest extends AbstractTextViewTest {
  public MarbleSolitaireTextViewTest() {
    super(5, 0, 6);
  }

  /**
   * Initialize a view-model pair with given model size and starting slot.
   *
   * @param size size of model
   * @param sRow row of starting slot
   * @param sCol column of starting slot
   */
  @Override
  protected void createViews(int size, int sRow, int sCol) {
    this.defaultModel = new EnglishSolitaireModel();
    this.defaultView = new MarbleSolitaireTextView(this.defaultModel);
    this.defaultLogView = new MarbleSolitaireTextView(this.defaultModel, this.log);
    this.badLogView = new MarbleSolitaireTextView(this.defaultModel, this.badLog);
    this.otherModel = new EnglishSolitaireModel(size, sRow, sCol);
    this.otherView = new MarbleSolitaireTextView(this.otherModel);
  }

  /**
   * Attempt to create a view with a null model.
   *
   * @throws IllegalArgumentException model cannot be null
   */
  @Override
  protected void createViewNullModel() throws IllegalArgumentException {
    new MarbleSolitaireTextView(null);
  }

  /**
   * Attempt to create a view with a null appendable.
   *
   * @throws IllegalArgumentException appendable cannot be null
   */
  @Override
  protected void createViewNullAP() throws IllegalArgumentException {
    new MarbleSolitaireTextView(this.defaultModel, null);
  }

  /**
   * Get string of default board with no moves.
   *
   * @return the string of the board
   */
  @Override
  protected String defaultNoMove() {
    return "    O O O"
            + System.lineSeparator() + "    O O O"
            + System.lineSeparator() + "O O O O O O O"
            + System.lineSeparator() + "O O O _ O O O"
            + System.lineSeparator() + "O O O O O O O"
            + System.lineSeparator() + "    O O O"
            + System.lineSeparator() + "    O O O";
  }

  /**
   * Get string of default board with {@link #defaultValidMove() move}  made.
   *
   * @return the string of the board
   */
  @Override
  protected String defaultValidMove() {
    return "    O O O"
            + System.lineSeparator() + "    O _ O"
            + System.lineSeparator() + "O O O _ O O O"
            + System.lineSeparator() + "O O O O O O O"
            + System.lineSeparator() + "O O O O O O O"
            + System.lineSeparator() + "    O O O"
            + System.lineSeparator() + "    O O O";
  }

  /**
   * Get string of {@link #otherModel board} with no moves.
   *
   * @return the string of the board
   */
  @Override
  protected String otherNoMove() {
    return "        O O _ O O"
            + System.lineSeparator() + "        O O O O O"
            + System.lineSeparator() + "        O O O O O"
            + System.lineSeparator() + "        O O O O O"
            + System.lineSeparator() + "O O O O O O O O O O O O O"
            + System.lineSeparator() + "O O O O O O O O O O O O O"
            + System.lineSeparator() + "O O O O O O O O O O O O O"
            + System.lineSeparator() + "O O O O O O O O O O O O O"
            + System.lineSeparator() + "O O O O O O O O O O O O O"
            + System.lineSeparator() + "        O O O O O"
            + System.lineSeparator() + "        O O O O O"
            + System.lineSeparator() + "        O O O O O"
            + System.lineSeparator() + "        O O O O O";
  }

  /**
   * Make a valid first move on the default board size.
   */
  @Override
  protected void defaultValidFirstMove() {
    this.defaultModel.move(1, 3, 3, 3);
  }

  /**
   * Make an invalid move on the default board size.
   *
   * @throws IllegalArgumentException this is an invalid move by definition
   */
  @Override
  protected void defaultInvalidMove() throws IllegalArgumentException {
    this.defaultModel.move(0, 0, 3, 3);
  }
}