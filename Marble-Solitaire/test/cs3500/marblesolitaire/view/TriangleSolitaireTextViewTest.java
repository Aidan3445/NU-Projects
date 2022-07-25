package cs3500.marblesolitaire.view;

import cs3500.marblesolitaire.model.hw04.TriangleSolitaireModel;

/**
 * JUnit test class for testing triangle shape board of marble solitaire.
 */
public class TriangleSolitaireTextViewTest extends AbstractTextViewTest {

  /**
   * Initialize test class with one default view and model
   * and another model and view with a different size and starting slot.
   */
  public TriangleSolitaireTextViewTest() {
    super(7, 2, 2);
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
    this.defaultModel = new TriangleSolitaireModel();
    this.defaultView = new TriangleSolitaireTextView(this.defaultModel);
    this.defaultLogView = new TriangleSolitaireTextView(this.defaultModel, this.log);
    this.badLogView = new MarbleSolitaireTextView(this.defaultModel, this.badLog);
    this.otherModel = new TriangleSolitaireModel(size, sRow, sCol);
    this.otherView = new TriangleSolitaireTextView(this.otherModel);
  }

  /**
   * Attempt to create a view with a null model.
   *
   * @throws IllegalArgumentException model cannot be null
   */
  @Override
  protected void createViewNullModel() throws IllegalArgumentException {
    new TriangleSolitaireTextView(null);
  }

  /**
   * Attempt to create a view with a null appendable.
   *
   * @throws IllegalArgumentException appendable cannot be null
   */
  @Override
  protected void createViewNullAP() throws IllegalArgumentException {
    new TriangleSolitaireTextView(this.defaultModel, null);
  }

  /**
   * Get string of default board with no moves.
   *
   * @return the string of the board
   */
  @Override
  protected String defaultNoMove() {
    return "    _"
            + System.lineSeparator() + "   O O"
            + System.lineSeparator() + "  O O O"
            + System.lineSeparator() + " O O O O"
            + System.lineSeparator() + "O O O O O";
  }

  /**
   * Get string of default board with {@link #defaultValidMove() move}  made.
   *
   * @return the string of the board
   */
  @Override
  protected String defaultValidMove() {
    return "    O"
            + System.lineSeparator() + "   _ O"
            + System.lineSeparator() + "  _ O O"
            + System.lineSeparator() + " O O O O"
            + System.lineSeparator() + "O O O O O";
  }

  /**
   * Get string of {@link #otherModel board} with no moves.
   *
   * @return the string of the board
   */
  @Override
  protected String otherNoMove() {
    return "      O"
            + System.lineSeparator() + "     O O"
            + System.lineSeparator() + "    O O _"
            + System.lineSeparator() + "   O O O O"
            + System.lineSeparator() + "  O O O O O"
            + System.lineSeparator() + " O O O O O O"
            + System.lineSeparator() + "O O O O O O O";
  }

  /**
   * Make a valid first move on the default board size.
   */
  @Override
  protected void defaultValidFirstMove() {
    this.defaultModel.move(2, 0, 0, 0);
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