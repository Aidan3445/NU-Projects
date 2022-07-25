package cs3500.marblesolitaire.view;

import org.junit.Test;

import java.io.IOException;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;

import static org.junit.Assert.assertEquals;

/**
 * JUnit tests for abstracted marble solitaire methods.
 */
public abstract class AbstractTextViewTest {
  protected final Appendable log = new StringBuilder();
  protected final Appendable badLog = new CorruptAppendable();
  protected MarbleSolitaireModel defaultModel;
  protected  MarbleSolitaireView defaultView;
  protected MarbleSolitaireView defaultLogView;
  protected MarbleSolitaireView badLogView;
  protected MarbleSolitaireModel otherModel;
  protected  MarbleSolitaireView otherView;

  public AbstractTextViewTest(int size, int sRow, int sCol) {
    this.createViews(size, sRow, sCol);
  }

  /**
   * Initialize a view-model pair with given model size and starting slot.
   *
   * @param size size of model
   * @param sRow row of starting slot
   * @param sCol column of starting slot
   */
  protected abstract void createViews(int size, int sRow, int sCol);

  /**
   * Attempt to create a view with a null model.
   *
   * @throws IllegalArgumentException model cannot be null
   */
  protected abstract void createViewNullModel() throws IllegalArgumentException;

  /**
   * Attempt to create a view with a null appendable.
   *
   * @throws IllegalArgumentException appendable cannot be null
   */
  protected abstract void createViewNullAP() throws IllegalArgumentException;

  /**
   * Get string of default board with no moves.
   *
   * @return the string of the board
   */
  protected abstract String defaultNoMove();

  /**
   * Get string of default board with {@link #defaultValidMove() move}  made.
   *
   * @return the string of the board
   */
  protected abstract String defaultValidMove();

  /**
   * Get string of {@link #otherModel board} with no moves.
   *
   * @return the string of the board
   */
  protected abstract String otherNoMove();

  /**
   * Make a valid first move on the default board size.
   */
  protected abstract void defaultValidFirstMove();

  /**
   * Make an invalid move on the default board size.
   *
   * @throws IllegalArgumentException this is an invalid move by definition
   */
  protected abstract void defaultInvalidMove() throws IllegalArgumentException;


  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    this.createViewNullModel();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullAp() {
    this.createViewNullAP();
  }

  @Test
  public void testToString() {
    assertEquals(this.defaultNoMove(), this.defaultView.toString());

    this.defaultValidFirstMove();

    assertEquals(this.defaultValidMove(), this.defaultView.toString());

    try {
      this.defaultInvalidMove();
    } catch (IllegalArgumentException e) {
      System.out.println("Tried to move but failed. No change should have been made.");
    }

    assertEquals(this.defaultValidMove(), this.defaultView.toString());

    assertEquals(this.otherNoMove(), this.otherView.toString());
  }

  @Test
  public void testRenderBoard() {
    try {
      this.defaultLogView.renderBoard();
      this.defaultLogView.renderMessage(System.lineSeparator());
      this.defaultValidFirstMove();
      this.defaultLogView.renderBoard();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    assertEquals(this.defaultNoMove() + System.lineSeparator()
            + this.defaultValidMove(), log.toString());
  }

  @Test(expected = IOException.class)
  public void testRenderBoardBadOutput() throws IOException {
    this.badLogView.renderBoard();
    assertEquals("", this.badLog.toString());
  }

  @Test
  public void testRenderMessage() {
    try {
      this.defaultLogView.renderMessage("HELLOOOOOOOOOOOOOOOOOOOOOOOOO");
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    assertEquals("HELLOOOOOOOOOOOOOOOOOOOOOOOOO", this.log.toString());
  }

  @Test(expected = IOException.class)
  public void testRenderMessageBadOutput() throws IOException {
    this.badLogView.renderMessage("HELLOOOOOOOOOOOOOOOOOOOOOOOOO");
    assertEquals("HELLOOOOOOOOOOOOOOOOOOOOOOOOO", this.badLog.toString());
  }
}
