package cs3500.marblesolitaire.model.hw04;

import cs3500.marblesolitaire.model.hw02.EnglishSolitaireModel;

/**
 * Represents a European style marble solitaire game with octagonal board shape.
 */
public class EuropeanSolitaireModel extends EnglishSolitaireModel {

  /**
   * Construct a European style Marble Solitaire game with
   * given size and coordinates of removed starting peg.
   *
   * @param size the size of the board
   * @param sRow the row of the initially removed peg
   * @param sCol the column of the initially removed peg
   * @throws IllegalArgumentException if the size less than 3,
   *                                  or if the empty slot is off the playable board
   */
  public EuropeanSolitaireModel(int size, int sRow, int sCol) throws IllegalArgumentException {
    super(size, sRow, sCol);
  }

  /**
   * Construct a European style Marble Solitaire game with
   * size 3 and given coordinates of removed starting peg.
   *
   * @param sRow the row of the initially removed peg
   * @param sCol the column of the initially removed peg
   * @throws IllegalArgumentException if the empty slot is off the playable board
   */
  public EuropeanSolitaireModel(int sRow, int sCol) throws IllegalArgumentException {
    super(sRow, sCol);
  }

  /**
   * Construct a European style Marble Solitaire game with
   * a given size and the center peg removed.
   *
   * @param size the size of the board
   * @throws IllegalArgumentException if the size less than 3
   */
  public EuropeanSolitaireModel(int size) throws IllegalArgumentException {
    super(size);
  }

  /**
   * Construct a European style Marble Solitaire game with
   * size of 3 and the center peg removed.
   */
  public EuropeanSolitaireModel() {
    super();
  }

  /**
   * Is a slot is on the playable board.
   *
   * @param row row of slot
   * @param col column of slot
   * @return true if slot is on the board
   */
  @Override
  protected boolean offBoard(int row, int col) {
    int boarder = this.size - 1;
    return (row < boarder // top third
            && (row < col - boarder * 2 // top right corner
            || row + col < boarder)) // top left corner
            || (row > boarder * 2 // bottom third
            && (col < row - boarder * 2 // bottom left corner
            || row + col > boarder * 5)) // bottom right corner
            || this.outOfRange(row, col); // negative or beyond array size
  }
}
