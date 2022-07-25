package cs3500.marblesolitaire.model.hw02;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * JUnit test for EnglishSolitaireModel class.
 */
public class EnglishSolitaireModelTest {
  private MarbleSolitaireModel e0;
  private MarbleSolitaireModel e1;
  private MarbleSolitaireModel e2;
  private MarbleSolitaireModel e3;


  @Before
  public void setup() {
    this.e0 = new EnglishSolitaireModel();
    this.e1 = new EnglishSolitaireModel(5);
    this.e2 = new EnglishSolitaireModel(0, 3);
    this.e3 = new EnglishSolitaireModel(7, 8, 9);
  }

  //make moves to win game with thickness 3 and starting center marble missing.
  private void winGame() {
    this.e0.move(3, 5, 3, 3);
    this.e0.move(1, 4, 3, 4);
    this.e0.move(2, 6, 2, 4);
    this.e0.move(4, 6, 2, 6);
    this.e0.move(2, 3, 2, 5);
    this.e0.move(2, 6, 2, 4);

    this.e0.move(2, 1, 2, 3);
    this.e0.move(0, 2, 2, 2);
    this.e0.move(0, 4, 0, 2);
    this.e0.move(3, 2, 1, 2);
    this.e0.move(0, 2, 2, 2);

    this.e0.move(5, 2, 3, 2);
    this.e0.move(4, 0, 4, 2);
    this.e0.move(2, 0, 4, 0);
    this.e0.move(4, 3, 4, 1);
    this.e0.move(4, 0, 4, 2);

    this.e0.move(4, 5, 4, 3);
    this.e0.move(6, 4, 4, 4);
    this.e0.move(6, 2, 6, 4);
    this.e0.move(3, 4, 5, 4);
    this.e0.move(6, 4, 4, 4);

    this.e0.move(3, 2, 1, 2);
    this.e0.move(1, 2, 1, 4);
    this.e0.move(1, 4, 3, 4);
    this.e0.move(3, 4, 5, 4);
    this.e0.move(5, 4, 5, 2);
    this.e0.move(5, 2, 3, 2);

    this.e0.move(3, 3, 1, 3);
    this.e0.move(3, 1, 3, 3);
    this.e0.move(4, 3, 2, 3);
    this.e0.move(1, 3, 3, 3);
  }

  // make moves to end game with thickness 3 and starting (0, 3) marble missing without winning.
  private void loseGame() {
    this.e2.move(2, 3, 0, 3);
    this.e2.move(4, 3, 2, 3);
    this.e2.move(6, 3, 4, 3);
    this.e2.move(3, 5, 3, 3);
    this.e2.move(3, 2, 3, 4);
    this.e2.move(3, 0, 3, 2);
  }

  @Test
  public void constructorExceptions1() {
    for (int thickness : new int[]{6, 1, -5}) {
      try {
        new EnglishSolitaireModel(thickness);
        fail("Error should have been thrown.");
        return;
      } catch (IllegalArgumentException e) {
        //error was properly thrown and caught
      }
    }
  }

  @Test
  public void constructorExceptions2() {
    for (int[] pos : new int[][]{
        new int[]{5, 1}, new int[]{10, 100}, new int[]{8, 4}, new int[]{-8, 4}
    }) {
      try {
        new EnglishSolitaireModel(pos[0], pos[1]);
        fail("Error should have been thrown.");
        return;
      } catch (IllegalArgumentException e) {
        //error was properly thrown and caught
      }
    }
  }

  @Test
  public void constructorExceptions3() {
    for (int[] params : new int[][]{
        new int[]{6, 7, 7}, new int[]{1, 0, 0}, new int[]{5, 0, 3}, new int[]{3, 100, 10}
    }) {
      try {
        new EnglishSolitaireModel(params[0], params[1], params[2]);
        fail("Error should have been thrown.");
        return;
      } catch (IllegalArgumentException e) {
        //error was properly thrown and caught
      }
    }
  }

  @org.junit.Test
  public void move() {
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, this.e0.getSlotAt(1, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, this.e0.getSlotAt(2, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, this.e0.getSlotAt(3, 3));

    this.e0.move(1, 3, 3, 3);

    assertEquals(MarbleSolitaireModelState.SlotState.Empty, this.e0.getSlotAt(1, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, this.e0.getSlotAt(2, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, this.e0.getSlotAt(3, 3));
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void moveToMarble() {
    //jumping to a marble
    this.e3.move(0, 3, 2, 3);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void moveFromEmpty() {
    //jumping from an empty slot
    this.e2.move(5, 5, 5, 7);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void moveFromInvalid() {
    //jumping from an invalid slot
    this.e3.move(13, 13, 13, 11);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void moveToInvalid() {
    //jumping to an invalid slot
    this.e1.move(9, 7, 9, 9);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void moveOverEmpty() {
    //jumping over an empty slot
    this.e0.move(3, 5, 3, 3);
    this.e0.move(3, 3, 3, 5);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void moveTooBig() {
    //jumping 2 spaces
    this.e0.move(0, 3, 3, 3);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void moveDiagonal() {
    //jumping on a diagonal
    this.e0.move(0, 2, 2, 4);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void moveToSelf() {
    //jumping to yourself
    this.e1.move(0, 4, 0, 4);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void moveToSelfEmpty() {
    //jumping to yourself
    this.e0.move(3, 3, 3, 3);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void moveToOutOfBounds() {
    //jumping out of bounds
    this.e0.move(3, 6, 3, 8);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void moveFromOutOfBounds() {
    //jumping out of bounds
    this.e0.move(-1, 3, 1, 3);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void moveToAbove() {
    //jumping out of bounds
    this.e0.move(1, 3, -1, 3);
  }

  @org.junit.Test
  public void isGameOver() {
    assertFalse(this.e0.isGameOver());
    assertFalse(this.e2.isGameOver());
    assertFalse(this.e1.isGameOver());

    this.winGame();
    this.loseGame();

    assertTrue(this.e0.isGameOver());
    assertTrue(this.e2.isGameOver());
  }

  @org.junit.Test
  public void getBoardSize() {
    assertEquals(7, this.e0.getBoardSize());
    assertEquals(13, this.e1.getBoardSize());
    assertEquals(7, this.e2.getBoardSize());
    assertEquals(19, this.e3.getBoardSize());
  }

  @org.junit.Test
  public void getSlotAt() {
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, this.e0.getSlotAt(3, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, this.e1.getSlotAt(6, 6));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, this.e2.getSlotAt(0, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, this.e3.getSlotAt(8, 9));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, this.e3.getSlotAt(0, 7));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, this.e0.getSlotAt(6, 4));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, this.e2.getSlotAt(2, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, this.e1.getSlotAt(7, 12));
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid, this.e0.getSlotAt(1, 1));
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid, this.e1.getSlotAt(3, 9));
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid, this.e2.getSlotAt(5, 1));
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid, this.e3.getSlotAt(13, 13));
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void getSlotAbove() {
    this.e0.getSlotAt(-1, 3);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void getSlotRight() {
    this.e1.getSlotAt(3, 14);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void getSlotBottom() {
    this.e2.getSlotAt(10, 3);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void getSlotLeft() {
    this.e3.getSlotAt(9, -8);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void getSlotAboveLeft() {
    this.e0.getSlotAt(-10, -10);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void getSlotAboveRight() {
    this.e1.getSlotAt(-2, 27);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void getSlotBottomRight() {
    this.e2.getSlotAt(100, 100);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void getSlotBottomLeft() {
    this.e3.getSlotAt(19, -10);
  }

  @org.junit.Test
  public void getScore() {
    assertEquals(32, this.e0.getScore());
    assertEquals(104, this.e1.getScore());
    assertEquals(32, this.e2.getScore());
    assertEquals(216, this.e3.getScore());

    this.winGame();
    this.loseGame();

    assertEquals(1, this.e0.getScore());
    assertEquals(26, this.e2.getScore());
  }
}