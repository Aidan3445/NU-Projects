package cs3500.marblesolitaire.model.hw04;

import java.util.Arrays;
import java.util.Objects;

import cs3500.marblesolitaire.model.hw02.EnglishSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;

/**
 * Abstract class for marble solitaire models of any board shape.
 */
public abstract class AbstractMarbleSolitaireModel implements MarbleSolitaireModel {
  protected final int size;
  protected final SlotState[][] board;

  /**
   * Construct a Marble Solitaire game with
   * given size and coordinates of removed starting peg.
   *
   * @param size the size of the board
   * @param sRow the row of the initially removed peg
   * @param sCol the column of the initially removed peg
   * @throws IllegalArgumentException if {@link AbstractMarbleSolitaireModel#constructorExceptions}
   *                                  throws any exceptions
   */
  public AbstractMarbleSolitaireModel(int size, int sRow, int sCol) {
    this.size = size;
    this.constructorExceptions(size, sRow, sCol);
    this.board = this.generateBoard(sRow, sCol);
  }

  /**
   * Throws correct exceptions if an invalid game is attempted to be created.
   *
   * @param size the size of the board
   * @param sRow the row of the initially removed peg
   * @param sCol the column of the initially removed peg
   * @throws IllegalArgumentException if the parameters cannot be initialized
   */
  protected abstract void constructorExceptions(int size, int sRow, int sCol)
          throws IllegalArgumentException;

  /**
   * Make arrays that represent an english board with given slot empty.
   *
   * @param row row of empty slot
   * @param col column of empty slot
   * @return 2D array representing the board
   */
  private final SlotState[][] generateBoard(int row, int col) throws IllegalArgumentException {
    int size = this.getBoardSize();
    SlotState[][] board = new SlotState[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (this.offBoard(i, j)) {
          board[i][j] = SlotState.Invalid;
        } else if (i == row && j == col) {
          board[i][j] = SlotState.Empty;
        } else {
          board[i][j] = SlotState.Marble;
        }
      }
    }
    return board;
  }

  /**
   * Is a slot is on the playable board.
   *
   * @param row row of slot
   * @param col column of slot
   * @return true if slot is on the board
   */
  protected abstract boolean offBoard(int row, int col);

  /**
   * Is the row, col index negative or beyond the array size.
   *
   * @param row row of slot
   * @param col column of slot
   * @return true if either row or col are negative or beyond the array size
   */
  protected final boolean outOfRange(int row, int col) {
    return row >= this.getBoardSize() || col >= this.getBoardSize() // too big
            || row < 0 || col < 0; // negative
  }

  /**
   * Is a move valid.
   *
   * @param fromRow row of slot making the jump
   * @param fromCol column of slot making the jump
   * @param toRow   row of slot jumping to
   * @param toCol   column of slot jumping to
   * @return true if move is valid
   * @throws IllegalArgumentException if the from or to marble positions are
   *                                  beyond the board's rectangle
   */
  protected final boolean validMove(int fromRow, int fromCol, int toRow, int toCol)
          throws IllegalArgumentException {
    SlotState from;
    SlotState to;
    try {
      from = this.board[fromRow][fromCol];
      to = this.board[toRow][toCol];
    } catch (IndexOutOfBoundsException e) {
      if (this.offBoard(fromRow, fromCol) && this.offBoard(toRow, toCol)) {
        throw new IllegalArgumentException(
                "Space (" + fromRow + ", " + fromCol + ") and "
                        + "(" + toRow + ", " + toCol + ") are out of the board."
        );
      } else if (this.offBoard(fromRow, fromCol)) {
        throw new IllegalArgumentException(
                "Space (" + fromRow + ", " + fromCol + ") is out of the board.");
      } else {
        throw new IllegalArgumentException(
                "Space (" + toRow + ", " + toCol + ") is out of the board.");
      }
    }
    return from == SlotState.Marble
            && this.board[(fromRow + toRow) / 2][(fromCol + toCol) / 2] == SlotState.Marble
            && to == SlotState.Empty
            && this.validJumpDistance(fromRow, fromCol, toRow, toCol);
  }

  /**
   * Determine if a marble can move.
   *
   * @param row row of marble
   * @param col column of marble
   * @return true if it has any valid remaining moves
   */
  protected boolean isMovable(int row, int col) {
    int size = this.getBoardSize();
    boolean movable = false;
    if (row < size - 2) {
      movable = this.validMove(row, col, row + 2, col);
    }
    if (row > 1) {
      movable |= this.validMove(row, col, row - 2, col);
    }
    if (col < size - 2) {
      movable |= this.validMove(row, col, row, col + 2);
    }
    if (col > 1) {
      movable |= this.validMove(row, col, row, col - 2);
    }
    return movable;
  }

  /**
   * Determine if a jump distance is valid for this board.
   * Allow jumps in 4 directions.
   *
   * @param fromRow row of slot making the jump
   * @param fromCol column of slot making the jump
   * @param toRow   row of slot jumping to
   * @param toCol   column of slot jumping to
   * @return if the distance is valid: exactly 2 spaces away.
   */
  protected boolean validJumpDistance(int fromRow, int fromCol, int toRow, int toCol) {
    int vertDist = Math.abs(fromRow - toRow);
    int horizDist = Math.abs(fromCol - toCol);
    return vertDist == 2 && horizDist == 0 || vertDist == 0 && horizDist == 2;
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
    if (this.board[fromRow][fromCol] == SlotState.Empty) {
      throw new IllegalArgumentException("Cannot jump from an empty space.");
    } else if (this.board[fromRow][fromCol] == SlotState.Invalid) {
      throw new IllegalArgumentException("Cannot jump from an invalid space.");
    }
    if (this.board[toRow][toCol] == SlotState.Marble) {
      throw new IllegalArgumentException("Cannot jump to a space with a marble.");
    } else if (this.board[toRow][toCol] == SlotState.Invalid) {
      throw new IllegalArgumentException("Cannot jump to an invalid space.");
    }
    if (!(Math.abs(fromRow - toRow) == 2 || Math.abs(fromCol - toCol) == 2)) {
      throw new IllegalArgumentException("Must jump over exactly one filled space.");
    }
    if (this.board[(fromRow + toRow) / 2][(fromCol + toCol) / 2] == SlotState.Empty) {
      throw new IllegalArgumentException("Cannot jump over an empty space.");
    } else if (this.board[(fromRow + toRow) / 2][(fromCol + toCol) / 2] == SlotState.Invalid) {
      throw new IllegalArgumentException("Cannot jump over an invalid space.");
    }
  }

  /**
   * Move a single marble from a given position to another given position.
   * A move is valid only if the from and to positions are valid. Specific
   * implementations may place additional constraints on the validity of a move.
   *
   * @param fromRow the row number of the position to be moved from
   *                (starts at 0)
   * @param fromCol the column number of the position to be moved from
   *                (starts at 0)
   * @param toRow   the row number of the position to be moved to
   *                (starts at 0)
   * @param toCol   the column number of the position to be moved to
   *                (starts at 0)
   * @throws IllegalArgumentException if the move is not possible or invalid
   */
  @Override
  public final void move(int fromRow, int fromCol, int toRow, int toCol)
          throws IllegalArgumentException {
    if (!this.validMove(fromRow, fromCol, toRow, toCol)) {
      this.invalidMoveExceptions(fromRow, fromCol, toRow, toCol);
    }
    this.board[fromRow][fromCol] = SlotState.Empty;
    this.board[(fromRow + toRow) / 2][(fromCol + toCol) / 2] = SlotState.Empty;
    this.board[toRow][toCol] = SlotState.Marble;
  }

  /**
   * Get the state of the slot at a given position on the board.
   *
   * @param row the row of the position sought, starting at 0
   * @param col the column of the position sought, starting at 0
   * @return the state of the slot at the given row and column
   * @throws IllegalArgumentException if the row or the column are beyond
   *                                  the dimensions of the board
   */
  @Override
  public final SlotState getSlotAt(int row, int col) throws IllegalArgumentException {
    if (row >= this.getBoardSize() || col >= this.getBoardSize() || row < 0 || col < 0) {
      throw new IllegalArgumentException("Space (" + row + ", " + col + ") is out of the board.");
    }
    return this.board[row][col];
  }

  /**
   * Determine and return if the game is over or not. A game is over if no
   * more moves can be made.
   *
   * @return true if the game is over, false otherwise
   */
  @Override
  public final boolean isGameOver() {
    boolean movesLeft = false;
    int size = this.getBoardSize();
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        movesLeft |= this.isMovable(i, j);
      }
    }
    return !movesLeft;
  }

  /**
   * Return the number of marbles currently on the board.
   *
   * @return the number of marbles currently on the board
   */
  @Override
  public final int getScore() {
    int count = 0;
    int size = this.getBoardSize();
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (this.board[i][j] == SlotState.Marble) {
          count++;
        }
      }
    }
    return count;
  }

  /**
   * returns true if this SimpleFraction is the same as the other SimpleFraction.
   *
   * @param obj compared object.
   * @return whether they are the same.
   */
  @Override
  public final boolean equals(Object obj) {
    if (obj instanceof EnglishSolitaireModel) {
      EnglishSolitaireModel other = (EnglishSolitaireModel) obj;
      return other.size == this.size && Arrays.deepEquals(this.board, other.board);
    }
    return false;
  }

  /**
   * generates a hashcode for this SimpleFraction.
   *
   * @return the generated hashcode.
   */
  @Override
  public final int hashCode() {
    return Objects.hash(this.size, Arrays.deepHashCode(this.board));
  }
}
