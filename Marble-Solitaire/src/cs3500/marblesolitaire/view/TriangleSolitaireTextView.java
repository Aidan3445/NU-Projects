package cs3500.marblesolitaire.view;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelState;

/**
 * Class representing a text view for a triangular marble solitaire board.
 */
public class TriangleSolitaireTextView extends AbstractMarbleSolitaireTextView {
  /**
   * Construct a Marble Solitaire view from a given model state.
   * The default output appendable is system.out.
   *
   * @param model the model state
   * @throws IllegalArgumentException if the model is null
   */
  public TriangleSolitaireTextView(MarbleSolitaireModelState model)
          throws IllegalArgumentException {
    super(model);
  }

  /**
   * Construct a Marble Solitaire view from a given model state and appendable output.
   *
   * @param model the model state
   * @param ap the appendable output
   * @throws IllegalArgumentException if model or ap are null
   */
  public TriangleSolitaireTextView(MarbleSolitaireModelState model, Appendable ap)
          throws IllegalArgumentException {
    super(model, ap);
  }

  /**
   * Add spaces before the first character based on row number to build pyramid shape.
   *
   * @param row the row number to add spaces too
   * @return the number of spaces at the start of the row
   */
  @Override
  protected String spacing(int row) {
    return " ".repeat(Math.max(0, this.model.getBoardSize()) - 1 - row);
  }
}
