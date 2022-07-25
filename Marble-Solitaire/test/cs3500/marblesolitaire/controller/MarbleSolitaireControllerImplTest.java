package cs3500.marblesolitaire.controller;

import org.junit.Test;

import java.io.StringReader;
import java.util.Random;

import cs3500.marblesolitaire.model.hw02.EnglishSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.view.MarbleSolitaireTextView;
import cs3500.marblesolitaire.view.MarbleSolitaireView;
import cs3500.marblesolitaire.model.hw02.MockModel;

import static org.junit.Assert.assertEquals;

/**
 * JUnit test for the marble solitaire controller.
 */
public class MarbleSolitaireControllerImplTest {
  MarbleSolitaireModel model;
  MarbleSolitaireView view;
  MarbleSolitaireController controller;
  StringBuilder log;
  Readable rd;

  Random r = new Random(2003);

  private void setupMockModel(String inputs) {
    this.log = new StringBuilder();
    this.model = new MockModel(this.log);
    this.rd = new StringReader(inputs);
    this.view = new MarbleSolitaireTextView(new EnglishSolitaireModel());
    this.controller = new MarbleSolitaireControllerImpl(this.model, this.view, this.rd);
    this.controller.playGame();
  }

  private void setup(String inputs) {
    this.log = new StringBuilder();
    this.model = new EnglishSolitaireModel();
    this.rd = new StringReader(inputs);
    this.view = new MarbleSolitaireTextView(this.model, this.log);
    this.controller = new MarbleSolitaireControllerImpl(this.model, this.view, this.rd);
    this.controller.playGame();
  }

  /**
   * Gets the specified line from the output string.
   *
   * @param line line number to get startingat line 1
   * @throws IllegalArgumentException if the line number is negative or too big
   */
  private String getLine(int line) throws IllegalArgumentException {
    String[] output = log.toString().split(System.lineSeparator());
    if (line < 0) {
      throw new IllegalArgumentException("Start line cannot be negative.");
    } else if (line > output.length) {
      throw new IllegalArgumentException(String.format(
              "There are only %d lines in the string, %d is too big.", output.length, line));
    }
    return output[line - 1];
  }

  @Test
  public void testQuit() {
    for (String q : new String[]{"q", "Q", "4 2 4 q", "4 2 Q 4", "4 q 4 4", "Q 2 4 4"}) {
      this.setup(q);
      assertEquals("    O O O"
              + System.lineSeparator() + "    O O O"
              + System.lineSeparator() + "O O O O O O O"
              + System.lineSeparator() + "O O O _ O O O"
              + System.lineSeparator() + "O O O O O O O"
              + System.lineSeparator() + "    O O O"
              + System.lineSeparator() + "    O O O"
              + System.lineSeparator() + "Score: 32"
              + System.lineSeparator() + "Game quit!"
              + System.lineSeparator() + "State of game when quit:"
              + System.lineSeparator() + "    O O O"
              + System.lineSeparator() + "    O O O"
              + System.lineSeparator() + "O O O O O O O"
              + System.lineSeparator() + "O O O _ O O O"
              + System.lineSeparator() + "O O O O O O O"
              + System.lineSeparator() + "    O O O"
              + System.lineSeparator() + "    O O O"
              + System.lineSeparator() + "Score: 32"
              + System.lineSeparator(), this.log.toString());
    }
  }

  @Test
  public void testReEnterInput() {
    String randChars = "piwe;mdbvusw,noespueo:slc{nun]ddmpemdi!mindf.nsd/";
    for (int i = 0; i < 10000; i++) {
      int start = this.r.nextInt(randChars.length() - 2);
      int end = start + 1 + this.r.nextInt(randChars.length() - start);
      String input = randChars.substring(start, end);
      this.setup(String.format("2 4 4 %s 4 q", input));
      assertEquals("Score: 32", this.getLine(8));
      assertEquals("Invalid move. Play again.", this.getLine(9));
      assertEquals("Score: 31", this.getLine(17));
    }
  }

  @Test
  public void testMoveInput() {
    for (int i = 0; i < 10000; i++) {
      int fromRow = this.r.nextInt();
      int fromCol = this.r.nextInt();
      int toRow = this.r.nextInt();
      int toCol = this.r.nextInt();
      this.setupMockModel(String.format("%d %d %d %d q", fromRow, fromCol, toRow, toCol));
      assertEquals(String.format("fromRow = %d, fromCol = %d, toRow = %d, toCol = %d",
              fromRow - 1, fromCol - 1, toRow - 1, toCol - 1), this.log.toString());
    }
  }

  @Test
  public void testInvalidMoveToInvalid() {
    String[] invalidMoves = {"4 1 2 1", "4 2 2 2", "1 4 1 6", "2 4 2 6",
        "4 6 6 6", "4 7 6 7", "6 4 6 2", "7 4 7 2"};
    for (String input : invalidMoves) {
      this.setup(String.format("%s q", input));
      assertEquals("Error: Cannot jump to an invalid space.", this.getLine(9));
    }
  }

  @Test
  public void testInvalidMoveFromInvalid() {
    String[] invalidMoves = {"2 4 4 4 2 2 2 4", "4 6 4 4 2 6 4 6", "6 4 4 4 6 6 6 4",
        "4 2 4 4 6 2 4 2"};
    for (String input : invalidMoves) {
      this.setup(String.format("%s q", input));
      assertEquals("Error: Cannot jump from an invalid space.",
              this.getLine(17));
    }
  }

  @Test
  public void testInvalidMoveOutsideBoard() {
    for (int i = 0; i < 10000; i++) {
      int fromRow = this.r.nextInt(20000) - 10000;
      int fromCol = this.r.nextInt(20000) - 10000;
      int toRow = this.r.nextInt(20000) - 10000;
      int toCol = this.r.nextInt(20000) - 10000;
      this.setup(String.format("%d %d %d %d q", fromRow, fromCol, toRow, toCol));
      if (fromRow > 7 || fromCol > 7 || toRow > 7 || toCol > 7
              || fromRow < 1 || fromCol < 1 || toRow < 1 || toCol < 1) {
        assertEquals(String.format("Error: Space (%d, %d) and (%d, %d) are out of the board.",
                        fromRow - 1, fromCol - 1, toRow - 1, toCol - 1),
                this.getLine(9));
      }
    }
  }

  @Test
  public void testInvalidFromMoveOutsideBoard() {
    for (int i = 0; i < 10000; i++) {
      int fromRow = this.r.nextInt(20000) - 10000;
      int fromCol = this.r.nextInt(20000) - 10000;
      this.setup(String.format("%d %d 4 4 q", fromRow, fromCol));
      if (fromRow > 7 || fromCol > 7 || fromRow < 1 || fromCol < 1) {
        assertEquals(String.format("Error: Space (%d, %d) is out of the board.",
                fromRow - 1, fromCol - 1), this.getLine(9));
      }
    }
  }

  @Test
  public void testInvalidToMoveOutsideBoard() {
    for (int i = 0; i < 10000; i++) {
      int toRow = this.r.nextInt(20000) - 10000;
      int toCol = this.r.nextInt(20000) - 10000;
      this.setup(String.format("4 3 %d %d q", toRow, toCol));
      if (toRow > 7 || toCol > 7 || toRow < 1 || toCol < 1) {
        assertEquals(String.format("Error: Space (%d, %d) is out of the board.",
                toRow - 1, toCol - 1), this.getLine(9));
      }
    }
  }

  @Test
  public void testInvalidMoveFromEmpty() {
    String[] invalidMoves = {"2 4", "4 2", "4 6", "6 4"};
    for (String input : invalidMoves) {
      this.setup(String.format("4 4 %s q", input));
      assertEquals("Error: Cannot jump from an empty space.", this.getLine(9));
    }
  }

  @Test
  public void testInvalidMoveToMarble() {
    String[] invalidMoves = {"3 3 3 5", "3 5 5 5", "5 5 5 3", "5 3 3 3"};
    for (String input : invalidMoves) {
      this.setup(String.format("%s q", input));
      assertEquals("Error: Cannot jump to a space with a marble.", this.getLine(9));
    }
  }

  @Test
  public void testInvalidMoveTooBig() {
    String[] invalidMoves = {"1 4", "4 7", "7 4", "4 1"};
    for (String input : invalidMoves) {
      this.setup(String.format("%s 4 4 q", input));
      assertEquals("Error: Must jump over exactly one filled space.", this.getLine(9));
    }
  }

  @Test
  public void testInvalidMoveDiagonally() {
    String[] invalidMoves = {"2 4 4 4 3 2 3 4 5 5 3 3", "2 4 4 4 3 6 3 4 5 3 3 5",
        "6 4 4 4 5 6 5 4 3 3 5 5", "6 4 4 4 5 2 5 4 3 5 5 3"};
    for (String input : invalidMoves) {
      this.setup(String.format("%s q", input));
      assertEquals("Error: Must jump orthogonally.",
              this.getLine(25));
    }
  }

  @Test
  public void testInvalidMoveOverEmpty() {
    String[] invalidMoves = {"2 4 4 4 4 4 2 4", "4 6 4 4 4 4 4 6", "6 4 4 4 4 4 6 4",
        "4 2 4 4 4 4 4 2"};
    for (String input : invalidMoves) {
      this.setup(String.format("%s q", input));
      assertEquals("Error: Cannot jump over an empty space.",
              this.getLine(17));
    }
  }

  @Test
  public void testWinGame() {
    this.setup("4 6 4 4"
            + System.lineSeparator() + "2 5 4 5"
            + System.lineSeparator() + "3 7 3 5"
            + System.lineSeparator() + "5 7 3 7 "
            + System.lineSeparator() + "3 4 3 6"
            + System.lineSeparator() + "3 7 3 5"
            + System.lineSeparator() + "3 2 3 4"
            + System.lineSeparator() + "1 3 3 3 "
            + System.lineSeparator() + "1 5 1 3"
            + System.lineSeparator() + "4 3 2 3"
            + System.lineSeparator() + "1 3 3 3"
            + System.lineSeparator() + "6 3 4 3"
            + System.lineSeparator() + "5 1 5 3"
            + System.lineSeparator() + "3 1 5 1"
            + System.lineSeparator() + "5 4 5 2"
            + System.lineSeparator() + "5 1 5 3"
            + System.lineSeparator() + "5 6 5 4"
            + System.lineSeparator() + "7 5 5 5 "
            + System.lineSeparator() + "7 3 7 5"
            + System.lineSeparator() + "4 5 6 5"
            + System.lineSeparator() + "7 5 5 5"
            + System.lineSeparator() + "4 3 2 3"
            + System.lineSeparator() + "2 3 2 5"
            + System.lineSeparator() + "2 5 4 5"
            + System.lineSeparator() + "4 5 6 5"
            + System.lineSeparator() + "6 5 6 3"
            + System.lineSeparator() + "6 3 4 3"
            + System.lineSeparator() + "4 4 2 4"
            + System.lineSeparator() + "4 2 4 4"
            + System.lineSeparator() + "5 4 3 4"
            + System.lineSeparator() + "2 4 4 4"
            + System.lineSeparator());
    assertEquals("Game over!", this.getLine(249));
  }

  @Test(expected = IllegalStateException.class)
  public void testCatchIOException() {
    this.setup("6 4 4 4");
    assertEquals("Readable ran out of inputs.", this.log.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    new MarbleSolitaireControllerImpl(
            null,
            new MarbleSolitaireTextView(new EnglishSolitaireModel()),
            new StringReader(""));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullView() {
    new MarbleSolitaireControllerImpl(
            new EnglishSolitaireModel(), null, new StringReader(""));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullReadable() {
    MarbleSolitaireModel english = new EnglishSolitaireModel();
    new MarbleSolitaireControllerImpl(english, new MarbleSolitaireTextView(english), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAllNull() {
    new MarbleSolitaireControllerImpl(null, null, null);
  }
}
