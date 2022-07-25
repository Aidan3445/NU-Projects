package cs3500.marblesolitaire.model.hw02;

import cs3500.marblesolitaire.model.hw04.AbstractMarbleSolitaireModel;

/**
 * Represents an English style Marble Solitaire game.
 */
public class EnglishSolitaireModel extends AbstractMarbleSolitaireModel {
  /**
   * Construct an English style Marble Solitaire game with
   * given size and coordinates of removed starting peg.
   *
   * @param size the size of the board
   * @param sRow the row of the initially removed peg
   * @param sCol the column of the initially removed peg
   * @throws IllegalArgumentException if the size less than 3,
   *                                  or if the empty slot is off the playable board
   */
  public EnglishSolitaireModel(int size, int sRow, int sCol) throws IllegalArgumentException {
    super(size, sRow, sCol);
  }

  /**
   * Construct an English style Marble Solitaire game with
   * size 3 and given coordinates of removed starting peg.
   *
   * @param sRow the row of the initially removed peg
   * @param sCol the column of the initially removed peg
   * @throws IllegalArgumentException if the empty slot is off the playable board
   */
  public EnglishSolitaireModel(int sRow, int sCol) throws IllegalArgumentException {
    super(3, sRow, sCol);
  }

  /**
   * Construct an English style Marble Solitaire game with
   * a given size and the center peg removed.
   *
   * @param size the size of the board
   * @throws IllegalArgumentException if the size less than 3
   */
  public EnglishSolitaireModel(int size) throws IllegalArgumentException {
    super(size,
            EnglishSolitaireModel.getMiddle(size),
            EnglishSolitaireModel.getMiddle(size));
  }

  /**
   * Construct an English style Marble Solitaire game with
   * size of 3 and the center peg removed.
   */
  public EnglishSolitaireModel() {
    super(3, 3, 3);
  }

  /**
   * Throws correct exceptions if an invalid game is attempted to be created.
   *
   * @param size the size of the board
   * @param sRow the row of the initially removed peg
   * @param sCol the column of the initially removed peg
   * @throws IllegalArgumentException if the size less than 3,
   *                                  or if the empty slot is off the playable board
   */
  @Override
  protected void constructorExceptions(int size, int sRow, int sCol)
          throws IllegalArgumentException {
    if (size % 2 == 0) {
      throw new IllegalArgumentException(
              "size must be odd.");
    }
    if (size < 3) {
      throw new IllegalArgumentException(
              "size must be larger than 3.");
    }
    if (this.offBoard(sRow, sCol)) {
      throw new IllegalArgumentException(
              "Invalid empty cell position (" + sRow + ", " + sCol + ")");
    }
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
    return (row < boarder && col < boarder) // top left corner
            || (row < boarder && col > boarder * 2) // bottom left corner
            || (row > boarder * 2 && col < boarder) // top right corner
            || (row > boarder * 2 && col > boarder * 2) // bottom right corner
            || this.outOfRange(row, col); // negative or beyond array size
  }

  /**
   * Get the middle of the board.
   *
   * @param size the size to calculate middle of
   * @return the index of the middle starting at 0
   */
  protected static int getMiddle(int size) {
    return 3 * (size - 1) / 2;
  }

  /**
   * Throws exceptions for invalid moves with specified message.
   *
   * @param fromRow the row number of the position to be moved from
   * @param fromCol the column number of the position to be moved from
   * @param toRow   the row number of the position to be moved to
   * @param toCol   the column number of the position to be moved to
   * @throws IllegalArgumentException if the move is not possible or invalid
   */
  @Override
  protected void invalidMoveExceptions(int fromRow, int fromCol, int toRow, int toCol)
          throws IllegalArgumentException {
    if (!(Math.abs(fromRow - toRow) == 0 || Math.abs(fromCol - toCol) == 0)) {
      throw new IllegalArgumentException("Must jump orthogonally.");
    }
    super.invalidMoveExceptions(fromRow, fromCol, toRow, toCol);
  }

  /**
   * Return the size of this board. The size is roughly the longest dimension of a board.
   *
   * @return the size as an integer
   */
  @Override
  public int getBoardSize() {
    return 3 * this.size - 2;
  }
}
