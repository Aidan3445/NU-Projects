package cs3500.marblesolitaire.controller;

/**
 * Interface that represents a controller to take inputs and pass them along to a marble
 * solitaire model and view.
 */
public interface MarbleSolitaireController {
  /**
   * Runs the model and begins taking inputs.
   *
   * @throws IllegalStateException if the controller cannot properly read or write
   */
  void playGame() throws IllegalStateException;
}
