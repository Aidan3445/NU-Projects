package cs3500.marblesolitaire.model.hw04;

import org.junit.Before;
import org.junit.Test;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelState;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * JUnit tests for triangle marble solitaire model class.
 */
public class TriangleSolitaireModelTest {

  private MarbleSolitaireModel e0;
  private MarbleSolitaireModel e1;
  private MarbleSolitaireModel e2;
  private MarbleSolitaireModel e3;


  @Before
  public void setup() {
    this.e0 = new TriangleSolitaireModel();
    this.e1 = new TriangleSolitaireModel(2);
    this.e2 = new TriangleSolitaireModel(3, 0);
    this.e3 = new TriangleSolitaireModel(7, 5, 4);
  }

  //make moves to win game with thickness 3 and starting center marble missing.
  private void playGame() {
    this.e0.move(2, 2, 0, 0);
    this.e0.move(2, 0, 2, 2);
    this.e0.move(0, 0, 2, 0);
    this.e0.move(3, 0, 1, 0);
    this.e0.move(4, 2, 2, 0);
    this.e0.move(3, 3, 3, 1);
    this.e0.move(1, 0, 3, 0);
    this.e0.move(3, 0, 3, 2);
    this.e0.move(4, 4, 4, 2);
    this.e0.move(4, 1, 4, 3);
    this.e0.move(2, 2, 4, 2);
    this.e0.move(4, 3, 4, 1);
    this.e0.move(4, 0, 4, 2);
  }

  @Test
  public void constructorExceptions1() {
    for (int size : new int[]{1, -5}) {
      try {
        new TriangleSolitaireModel(size);
        fail(String.format(
                "Error should have been thrown: TriangleSolitaireModel(%d)", size));
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
        new TriangleSolitaireModel(pos[0], pos[1]);
        fail(String.format(
                "Error should have been thrown: TriangleSolitaireModel(%d, %d)", pos[0], pos[1]));
        return;
      } catch (IllegalArgumentException e) {
        //error was properly thrown and caught
      }
    }
  }

  @Test
  public void constructorExceptions3() {
    for (int[] params : new int[][]{new int[]{6, 7, 7}, new int[]{1, 0, 0}, new int[]{5, 0, 3}}) {
      try {
        new TriangleSolitaireModel(params[0], params[1], params[2]);
        fail(String.format(
                "Error should have been thrown: TriangleSolitaireModel(%d, %d, %d)",
                params[0], params[1], params[2]));
        return;
      } catch (IllegalArgumentException e) {
        //error was properly thrown and caught
      }
    }
  }

  @org.junit.Test
  public void move() {
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, this.e0.getSlotAt(2, 0));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, this.e0.getSlotAt(1, 0));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, this.e0.getSlotAt(0, 0));

    this.e0.move(2, 0, 0, 0);

    assertEquals(MarbleSolitaireModelState.SlotState.Empty, this.e0.getSlotAt(2, 0));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, this.e0.getSlotAt(1, 0));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, this.e0.getSlotAt(0, 0));
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
    assertTrue(this.e1.isGameOver());
    assertFalse(this.e2.isGameOver());

    this.playGame();

    assertTrue(this.e0.isGameOver());
  }

  @org.junit.Test
  public void getBoardSize() {
    assertEquals(5, this.e0.getBoardSize());
    assertEquals(2, this.e1.getBoardSize());
    assertEquals(5, this.e2.getBoardSize());
    assertEquals(7, this.e3.getBoardSize());
  }

  @org.junit.Test
  public void getSlotAt() {
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, this.e0.getSlotAt(0, 0));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, this.e1.getSlotAt(0, 0));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, this.e2.getSlotAt(3, 0));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, this.e3.getSlotAt(5, 4));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, this.e3.getSlotAt(6, 6));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, this.e0.getSlotAt(4, 0));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, this.e2.getSlotAt(2, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, this.e1.getSlotAt(1, 0));
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid, this.e0.getSlotAt(0, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid, this.e1.getSlotAt(0, 1));
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid, this.e2.getSlotAt(3, 4));
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid, this.e3.getSlotAt(2, 6));
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
    assertEquals(14, this.e0.getScore());
    assertEquals(2, this.e1.getScore());
    assertEquals(14, this.e2.getScore());
    assertEquals(27, this.e3.getScore());

    this.playGame();

    assertEquals(1, this.e0.getScore());
  }
}