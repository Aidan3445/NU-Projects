package cs3500.marblesolitaire.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Scanner;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.model.hw04.TriangleSolitaireModel;
import cs3500.marblesolitaire.view.MarbleSolitaireView;
import cs3500.marblesolitaire.view.TriangleSolitaireTextView;

/**
 * Class that represents a controller for a marble solitaire game.
 */
public class MarbleSolitaireControllerImpl implements MarbleSolitaireController {
  private final MarbleSolitaireModel model;
  private final MarbleSolitaireView view;
  private final Readable rd;

  /**
   * Construct a controller and initialize the model, input, and output (view).
   *
   * @param model the model to play the game on
   * @param view the interface to see the game
   * @param rd the input source
   * @throws IllegalArgumentException if any of the parameters are null
   */
  public MarbleSolitaireControllerImpl(
          MarbleSolitaireModel model, MarbleSolitaireView view, Readable rd)
          throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }
    if (view == null) {
      throw new IllegalArgumentException("View cannot be null.");
    }
    if (rd == null) {
      throw new IllegalArgumentException("Readable cannot be null.");
    }
    this.model = model;
    this.view = view;
    this.rd = rd;
  }

  /**
   * Runs the model and begins taking inputs.
   *
   * @throws IllegalStateException if the controller cannot properly read or write
   */
  @Override
  public void playGame() throws IllegalStateException {
    Scanner sc = new Scanner(this.rd);
    int[] moveInputs = new int[4];
    try {
      while (!this.model.isGameOver()) {
        this.view.renderBoard();
        this.renderScore();
        for (int i = 0; i < 4;) {
          try {
            if (sc.hasNextInt()) {
              moveInputs[i] = sc.nextInt() - 1;
              i++;
            } else if (sc.next().equalsIgnoreCase("q")) {
              this.view.renderMessage("Game quit!" + System.lineSeparator());
              this.view.renderMessage("State of game when quit:" + System.lineSeparator());
              this.view.renderBoard();
              this.renderScore();
              return;
            } else {
              this.view.renderMessage("Invalid move. Play again." + System.lineSeparator());
            }
          } catch (NoSuchElementException e) {
            throw new IllegalStateException("Readable ran out of inputs.");
          }
        }
        try {
          this.model.move(moveInputs[0], moveInputs[1], moveInputs[2], moveInputs[3]);
        } catch (IllegalArgumentException e) {
          this.view.renderMessage("Error: " + e.getMessage() + System.lineSeparator());
        }
      }
      this.view.renderMessage("Game over!" + System.lineSeparator());
      this.view.renderBoard();
      this.renderScore();
    } catch (IOException e) {
      throw new IllegalStateException("Inputs or outputs failed." + System.lineSeparator());
    }
  }

  /**
   * Render score the te view output.
   *
   * @throws IllegalStateException if the appendable is closed.
   */
  private void renderScore() throws IllegalStateException {
    try {
      this.view.renderMessage(String.format(
              System.lineSeparator() + "Score: %d" + System.lineSeparator(),
              this.model.getScore()));
    } catch (IOException e) {
      throw new IllegalStateException("Inputs or outputs failed.");
    }
  }

  public static void main(String[] args) {
    MarbleSolitaireModel m = new TriangleSolitaireModel();
    MarbleSolitaireView v = new TriangleSolitaireTextView(m);
    MarbleSolitaireController c =
            new MarbleSolitaireControllerImpl(m, v, new InputStreamReader(System.in));
    c.playGame();
  }
}
