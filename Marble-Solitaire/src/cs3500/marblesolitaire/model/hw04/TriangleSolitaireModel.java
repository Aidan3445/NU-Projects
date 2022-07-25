package cs3500.marblesolitaire.model.hw04;

/**
 * Represents a marble solitaire game with a triangular board shape.
 */
public class TriangleSolitaireModel extends AbstractMarbleSolitaireModel {
  /**
   * Construct a Triangle Marble Solitaire game with
   * given size and coordinates of removed starting peg.
   *
   * @param size the size of the board
   * @param sRow the row of the initially removed peg
   * @param sCol the column of the initially removed peg
   * @throws IllegalArgumentException if the thickness isn't odd and greater than 3,
   *                                  or if the empty slot is off the playable board
   */
  public TriangleSolitaireModel(int size, int sRow, int sCol) throws IllegalArgumentException {
    super(size, sRow, sCol);
  }

  /**
   * Construct a Triangle Marble Solitaire game with
   * given coordinates of removed starting peg.
   *
   * @param sRow the row of the initially removed peg
   * @param sCol the column of the initially removed peg
   * @throws IllegalArgumentException if the thickness isn't odd and greater than 3,
   *                                  or if the empty slot is off the playable board
   */
  public TriangleSolitaireModel(int sRow, int sCol) throws IllegalArgumentException {
    super(5, sRow, sCol);
  }

  /**
   * Construct a Triangle Marble Solitaire game with
   * given size.
   *
   * @param size the size of the board
   * @throws IllegalArgumentException if the size is less than 4
   */
  public TriangleSolitaireModel(int size) throws IllegalArgumentException {
    super(size, 0, 0);
  }

  /**
   * Construct a default Triangle Marble Solitaire game with size 5 and the top marble removed.
   */
  public TriangleSolitaireModel() throws IllegalArgumentException {
    super(5, 0, 0);
  }

  /**
   * Throws correct exceptions if an invalid game is attempted to be created.
   *
   * @param size the size of the board
   * @param sRow the row of the initially removed peg
   * @param sCol the column of the initially removed peg
   * @throws IllegalArgumentException if the thickness isn't odd and greater than 3,
   *                                  or if the empty slot is off the playable board
   */
  @Override
  protected void constructorExceptions(int size, int sRow, int sCol)
          throws IllegalArgumentException {
    if (size < 2) {
      throw new IllegalArgumentException(
              "Size must positive.");
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
    return col > row // within triangle/pyramid
            || this.outOfRange(row, col); // negative or beyond array size
  }

  /**
   * Determine if a jump distance is valid for this board.
   * Allow jumps in 4 directions, super allows 8.
   *
   * @param fromRow row of slot making the jump
   * @param fromCol column of slot making the jump
   * @param toRow row of slot jumping to
   * @param toCol column of slot jumping to
   * @return if the distance is valid: exactly 2 spaces away.
   */
  @Override
  protected boolean validJumpDistance(int fromRow, int fromCol, int toRow, int toCol) {
    return Math.abs(fromRow - toRow) == 2 && Math.abs(fromCol - toCol) == 2
            || super.validJumpDistance(fromRow, fromCol, toRow, toCol);
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
  protected void invalidMoveExceptions(int fromRow, int fromCol, int toRow, int toCol)
          throws IllegalArgumentException {
    int vertDist = Math.abs(fromRow - toRow);
    int horizDist = Math.abs(fromCol - toCol);
    super.invalidMoveExceptions(fromRow, fromCol, toRow, toCol);
    if (!(vertDist == 0 || horizDist == 0 || (vertDist == 2 && horizDist == 2))) {
      throw new IllegalArgumentException("Jump must be parallel to one side of the triangle.");
    }
  }

  /**
   * Determine if a marble can move.
   *
   * @param row row of marble
   * @param col column of marble
   * @return true if it has any valid remaining moves
   */
  protected boolean isMovable(int row, int col) {
    boolean movable = super.isMovable(row, col);
    if (movable) {
      return true;
    }
    if (row < size - 2 && col < size - 2) {
      movable = this.validMove(row, col, row + 2, col + 2);
    }
    if (row > 1 && col > 1) {
      movable |= this.validMove(row, col, row - 2, col - 2);
    }
    if (row > 1 && col < size - 2) {
      movable |= this.validMove(row, col, row - 2, col + 2);
    }
    if (row < size - 2 && col > 1) {
      movable |= this.validMove(row, col, row + 2, col - 2);
    }
    return movable;
  }

  /**
   * Return the size of this board. The size is roughly the longest dimension of a board
   *
   * @return the size as an integer
   */
  @Override
  public int getBoardSize() {
    return this.size;
  }
}
