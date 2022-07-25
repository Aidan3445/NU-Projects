package cs3500.marblesolitaire.view;

import java.io.IOException;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelState;

/**
 * Class represents an abstract marble solitaire text view of any shape.
 */
public class AbstractMarbleSolitaireTextView implements MarbleSolitaireView {
  protected final MarbleSolitaireModelState model;
  protected final Appendable ap;

  /**
   * Construct a Marble Solitaire view from a given model state.
   * The default output appendable is system.out.
   *
   * @param model the model state
   * @throws IllegalArgumentException if the model is null
   */
  public AbstractMarbleSolitaireTextView(MarbleSolitaireModelState model)
          throws IllegalArgumentException {
    this(model, System.out);
  }

  /**
   * Construct a Marble Solitaire view from a given model state and appendable output.
   *
   * @param model the model state
   * @param ap the appendable output
   * @throws IllegalArgumentException if model or ap are null
   */
  public AbstractMarbleSolitaireTextView(MarbleSolitaireModelState model, Appendable ap)
          throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }
    if (ap == null) {
      throw new IllegalArgumentException("Appendable output cannot be null.");
    }
    this.model = model;
    this.ap = ap;
  }

  /**
   * Return a string that represents the current state of the board. The
   * string should have one line per row of the game board. Each slot on the
   * game board is a single character (O, _ or space for a marble, empty and
   * invalid position respectively). Slots in a row should be separated by a
   * space. Each row has no space before the first slot and after the last slot.
   *
   * @return the game state as a string
   */
  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();
    int size = this.model.getBoardSize();
    for (int i = 0; i < size; i++) {
      output.append(this.spacing(i));
      for (int j = 0; j < size; j++) {
        MarbleSolitaireModelState.SlotState slot = this.model.getSlotAt(i, j);
        switch (slot) {
          case Empty:
            output.append("_ ");
            break;
          case Marble:
            output.append("O ");
            break;
          case Invalid:
            if (j > 0 && this.model.getSlotAt(i, j - 1) != slot) {
              j = size;
            } else {
              output.append("  ");
            }
            break;
          default:
            throw new IllegalStateException("Unexpected value: " + slot);
        }
      }
      output.append("\b").append(System.lineSeparator());
    }
    output = new StringBuilder(output.toString().replaceAll(" \b", ""));
    output = new StringBuilder(output.substring(0, output.length() - 1));
    return output.toString();
  }

  /**
   * Render the board to the provided data destination. The board should be rendered exactly
   * in the format produced by the toString method above.
   *
   * @throws IOException if transmission of the board to the provided data destination fails
   */
  @Override
  public void renderBoard() throws IOException {
    this.ap.append(this.toString());
  }

  /**
   * Render a specific message to the provided data destination.
   *
   * @param message the message to be transmitted
   * @throws IOException if transmission of the board to the provided data destination fails
   */
  @Override
  public void renderMessage(String message) throws IOException {
    this.ap.append(message);
  }

  /**
   * Add spaces before the first character based on row number.
   * Default is no spaces, override in child class to add extra spaces on each line.
   *
   * @param row the row number to add spaces too
   * @return empty string, no spaces.
   */
  protected String spacing(int row) {
    return "";
  }
}
