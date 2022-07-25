package cs3500.marblesolitaire.view;


import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelState;

/**
 * Represents a way to view a Marble Solitaire game board.
 */
public class MarbleSolitaireTextView extends AbstractMarbleSolitaireTextView {

  /**
   * Construct a Marble Solitaire view from a given model state.
   * The default output appendable is system.out.
   *
   * @param model the model state
   * @throws IllegalArgumentException if the model is null
   */
  public MarbleSolitaireTextView(MarbleSolitaireModelState model) throws IllegalArgumentException {
    super(model);
  }

  /**
   * Construct a Marble Solitaire view from a given model state and appendable output.
   *
   * @param model the model state
   * @param ap the appendable output
   * @throws IllegalArgumentException if model or ap are null
   */
  public MarbleSolitaireTextView(MarbleSolitaireModelState model, Appendable ap)
          throws IllegalArgumentException {
    super(model, ap);
  }
}
