import tester.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

class FloodItWorld extends World {
  int height = 600; //height of screen
  int width = 720; //width of screen 
  int boardSize; //number of tiles on one side of square board
  static int tileSize; //size of each square tile
  ArrayList<ArrayList<Cell>> board; //2D array to hold data for cells and their neighbors
  int numColors; //number of colors
  Color currentColor; //current flooded color
  double seconds; //seconds since new board was created
  int clicks; //number if clicks/floods
  int maxClicks; //max number of clicks/floods

  //creates new floodit board with given board size (n by n) and given number of colors (3 - 8)
  FloodItWorld(int boardSize, int numColors) {
    if (numColors > 8 || numColors < 3) { //colors must be from 3-8
      throw new IllegalArgumentException("3 - 8 Colors Only.");
    }
    this.boardSize = boardSize;
    this.numColors = numColors;
    FloodItWorld.tileSize = this.height / boardSize;
    this.maxClicks = boardSize + 2 * numColors; //arbitrary method of finding max number of clicks
    this.generateBoard();
  }

  Random rand = new Random();

  // creates a new instance of FifteenGame with the given seeded random
  FloodItWorld(int boardSize, int numColors, Random rand) {
    if (numColors > 8 || numColors < 3) { //colors must be from 3-8
      throw new IllegalArgumentException("3 - 8 Colors Only.");
    }
    this.rand = rand;
    this.boardSize = boardSize;
    this.numColors = numColors;
    FloodItWorld.tileSize = this.height / boardSize;
    this.generateBoard();
    this.setMaxClicks();
  }

  //resets the game board
  void generateBoard() {
    ArrayList<Color> colors = new ArrayList<Color>(Arrays.asList(
        Color.red, Color.green, Color.blue, Color.orange, Color.magenta, Color.yellow,
        Color.cyan, Color.pink));
    this.board = new ArrayList<ArrayList<Cell>>();
    //add cells to 2D array
    for (int i = 0; i < this.boardSize; i++) {
      this.board.add(new ArrayList<Cell>());
      for (int j = 0; j < this.boardSize; j++) {
        this.board.get(i).add(new Cell(colors.get(rand.nextInt(this.numColors))));
      }
    }
    //set each cell's neighbors
    for (int i = 0; i < this.boardSize; i++) {
      for (int j = 0; j < this.boardSize; j++) {
        this.board.get(i).get(j).setNeighbors(this.board, i, j);
      }
    }
    //set other variables to starting state
    Cell start = this.board.get(0).get(0);
    start.flooded = true;
    this.currentColor = start.color;
    this.clicks = 0;
    this.seconds = 0;
  }
  
  //sets max clicks
  void setMaxClicks() {
    if (this.boardSize > 12) {
      this.maxClicks = this.boardSize + (this.numColors * 5 / 2) + 10;
    }
    else if (this.boardSize < 4) {
      this.maxClicks = this.boardSize + this.numColors / 2;
    }
    else {
      this.maxClicks = boardSize * 4 / 3 + this.numColors;
    }
  }

  //adds to color region
  void flood() {
    //get list of re-colored flooded cells
    ArrayList<Cell> floodStep = new ArrayList<Cell>();
    for (ArrayList<Cell> column : this.board) {
      for (Cell cell : column) {
        if (cell.flooded && cell.color == this.currentColor) {
          floodStep.add(cell);
        }
      }
    }
    //flood neighbors of each cell in list from above
    for (Cell cell : floodStep) {
      for (Cell n : cell.neighbors) {
        if (n.flooded || n.color == this.currentColor) {
          n.flooded = true;
          n.color = this.currentColor;
        }
      }
    }
  }

  //converts seconds to timer text image
  WorldImage timer() {
    int sec = (int) Math.floor(this.seconds);
    String secTxt;
    String minTxt = Integer.toString(sec / 60);
    if (sec % 60 < 10) {
      secTxt = "0" + Integer.toString(sec % 60);
    } else {
      secTxt = Integer.toString(sec % 60);
    }
    String timerTxt = minTxt + ":" + secTxt;
    double scale = 1;
    if (seconds > 600) {
      scale -= 0.2 * Math.floor(Math.log10(sec / 60));
    }
    return new ScaleImage(
        new TextImage(timerTxt, 40, Color.red).movePinhole(10, -20), scale);
  }

  //checks if entire board is the same color (to ensure all flooding was finished)
  boolean checkWin() {
    boolean win = true;
    for (int i = 0; i < this.boardSize; i++) {
      for (int j = 0; j < this.boardSize; j++) {
        win = win && this.board.get(i).get(j).color == this.currentColor;
      }
    }
    return win;
  }

  //tests for end scene or game scene each tick
  @Override
  public WorldEnd worldEnds() {
    if (this.checkWin()) {
      return new WorldEnd(true, this.endScene());
    } else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  //draws game board scene each tick
  @Override
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(this.width, this.height);
    //draw cells
    for (int i = 0; i < this.boardSize; i++) {
      for (int j = 0; j < this.boardSize; j++) {
        scene.placeImageXY(board.get(i).get(j).draw(), 
            FloodItWorld.tileSize * i + FloodItWorld.tileSize / 2, 
            FloodItWorld.tileSize * j + FloodItWorld.tileSize / 2);
      }
    }
    //draw timer
    scene.placeImageXY(this.timer(), this.width * 11 / 12, this.height / 5);
    //draw click counter
    scene.placeImageXY(new TextImage(
        Integer.toString(this.clicks) + "/" + Integer.toString(this.maxClicks), 30, Color.red), 
        this.width * 11 / 12, this.height * 2 / 5);
    return scene;
  }

  //display win or loss
  public WorldScene endScene() {
    WorldScene scene = this.makeScene();
    String endStr = "YOU WIN!";
    if (this.clicks > this.maxClicks) {
      endStr = "YOU LOSE";
    }
    scene.placeImageXY(new TextImage(endStr, 100, Color.black), 
        this.width * 5 / 12, this.height / 2);
    return scene;
  }

  //changes current color to be color that was clicked
  @Override
  public void onMouseClicked(Posn pos) {
    if (pos.x < this.width * 5 / 6) {
      Cell clickedTile = this.board.get(
          Math.floorDiv(pos.x, FloodItWorld.tileSize)).get(
              Math.floorDiv(pos.y, FloodItWorld.tileSize));
      if (this.currentColor != clickedTile.color) {
        this.currentColor = clickedTile.color;
        this.board.get(0).get(0).color = clickedTile.color;
        this.clicks++;
      }
    }
  } 

  //resets new board when 'r' key is clicked
  @Override
  public void onKeyEvent(String key) {
    if (key.equalsIgnoreCase("r")) {
      this.generateBoard();
    }
  }

  //each tick runs flood animation
  //each tick time goes up my tick speed (to keep seconds accurate)
  @Override
  public void onTick() {
    this.flood();
    this.seconds += 0.01;  
  }
}

class Cell {
  Color color; //color of the cell
  boolean flooded; //is the cell a part of the current flood?
  ArrayList<Cell> neighbors; //list of the cell's neighbors

  //creates new, unflooded cell with given color
  Cell(Color color) {
    this.color = color;
    this.flooded = false;
    this.neighbors = new ArrayList<Cell>();
  }

  // more specific constructor for testing purposes
  Cell(Color color, boolean flooded, ArrayList<Cell> neighbors) {
    this.color = color;
    this.flooded = flooded;
    this.neighbors = neighbors;
  }

  //draws solid square of this cell's colors
  WorldImage draw() {
    return 
        new RectangleImage(FloodItWorld.tileSize, FloodItWorld.tileSize, "solid", this.color);
  }

  //adds neighbors to this cell - checks for edges to avoid range errors
  void setNeighbors(ArrayList<ArrayList<Cell>> grid, int col, int row) {
    if (row < grid.get(0).size() - 1) {
      this.neighbors.add(grid.get(col).get(row + 1));
    }
    if (row > 0 ) {
      this.neighbors.add(grid.get(col).get(row - 1));
    }
    if (col < grid.size() - 1) {
      this.neighbors.add(grid.get(col + 1).get(row));
    }
    if (col > 0) {
      this.neighbors.add(grid.get(col - 1).get(row));
    }
  }
}

class ExamplesFloodIt {
  FloodItWorld world;

  FloodItWorld test;
  FloodItWorld test1;
  ArrayList<ArrayList<Cell>> testCell;

  ArrayList<Cell> c1;
  ArrayList<Cell> c2;
  ArrayList<Cell> c3;
  ArrayList<ArrayList<Cell>> board;

  void testBigBang(Tester t) {
    
    this.reset();

    this.world.bigBang(world.width, world.height, 0.01);
  }

  //this is the playable world
  void reset() {
    this.world = new FloodItWorld(20, 6);
  }

  // creates the game and modifies other examples
  void setup() {

    // test an instance of FloodItWorld with the given random
    this.test = new FloodItWorld(3, 6, new Random(12345));
    this.test1 = new FloodItWorld(3, 6, new Random(12345));

    this.c1 = new ArrayList<Cell>(Arrays.asList(new Cell(Color.green), new Cell(Color.magenta),
        new Cell(Color.orange)));
    this.c2 = new ArrayList<Cell>(Arrays.asList(new Cell(Color.red), new Cell(Color.green),
        new Cell(Color.magenta)));
    this.c3 = new ArrayList<Cell>(Arrays.asList(new Cell(Color.green), new Cell(Color.red),
        new Cell(Color.green)));

    this.board = new ArrayList<ArrayList<Cell>>(Arrays.asList(this.c1, this.c2, this.c3));

    this.board.get(0).get(0).flooded = true;

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        this.board.get(i).get(j).setNeighbors(this.board, i, j);
      }
    }
  }
  
  void testNumColors(Tester t) {
    
    this.setup();
    
    t.checkConstructorException(new IllegalArgumentException("3 - 8 Colors Only."), "FloodItWorld",
        3, 2);
    
    t.checkConstructorException(new IllegalArgumentException("3 - 8 Colors Only."), "FloodItWorld",
        3, 1000000);
    
    this.reset();
  }

  void testGenerateBoard(Tester t) {

    t.checkExpect(new FloodItWorld(3, 3, new Random(12345)).board, new FloodItWorld(3, 3, 
        new Random(12345)).board);

    this.setup();

    t.checkExpect(this.test.board, this.board);

    this.reset();
  }
  
  void testSetMaxClicks(Tester t) {
    
    FloodItWorld size3 = new FloodItWorld(3, 6);
    
    t.checkExpect(size3.maxClicks, 15);
    
    FloodItWorld size8 = new FloodItWorld(8, 6);
    
    t.checkExpect(size8.maxClicks, 20);

    
    FloodItWorld size15 = new FloodItWorld(15, 6);
    
    t.checkExpect(size15.maxClicks, 27);

    
    this.reset();
  }

  void testFlood(Tester t) {

    this.setup();
    
    t.checkExpect(this.test.board.get(0).get(1).flooded, false);
    
    t.checkExpect(this.test.board.get(1).get(0).flooded, false);
    
    this.test.onMouseClicked(new Posn(10, 300));
    
    this.test.flood();
    
    t.checkExpect(this.test.board.get(0).get(1).flooded, true);
    
    t.checkExpect(this.test.board.get(1).get(0).flooded, false);
    
    this.reset();
  }
  
  void testMakeScene(Tester t) {
    
    FloodItWorld testMakeScene = new FloodItWorld(1, 3, new Random(12345));

    WorldScene initialScene = new WorldScene(720, 600);

    initialScene.placeImageXY(new RectangleImage(600, 600, OutlineMode.SOLID, Color.green), 
        300, 300);

    initialScene.placeImageXY(new ScaleImage(new TextImage("0:00", 40, Color.red).movePinhole(10,
        -20), 1), 660, 120);

    initialScene.placeImageXY(new TextImage("0/2", 30, Color.red), 660, 240);

    t.checkExpect(testMakeScene.makeScene(), initialScene);

    this.reset();
  }

  void testEndScene(Tester t) {

    FloodItWorld testEndScene = new FloodItWorld(1, 3, new Random(12345));

    WorldScene winTest = testEndScene.makeScene();
    winTest.placeImageXY(new TextImage("YOU WIN!", 100, Color.black), 
        300, 300);

    t.checkExpect(testEndScene.endScene(), winTest);

    testEndScene.clicks = 100;

    WorldScene loseTest = testEndScene.makeScene();
    loseTest.placeImageXY(new TextImage("YOU LOSE", 100, Color.black), 
        300, 300);

    t.checkExpect(testEndScene.endScene(), loseTest);

    this.reset();
  }

  void testTimer(Tester t) {

    this.setup();

    t.checkExpect(this.test.timer(), new ScaleImage(
        new TextImage("0:00", 40, Color.red).movePinhole(10, -20), 1));

    this.test.seconds = 120;

    t.checkExpect(this.test.timer(), new ScaleImage(
        new TextImage("2:00", 40, Color.red).movePinhole(10, -20), 1));

    this.test.seconds = 800;

    t.checkExpect(this.test.timer(), new ScaleImage(
        new TextImage("13:20", 40, Color.red).movePinhole(10, -20), 0.8));

    this.reset();
  }

  void testCheckWin(Tester t) {

    this.setup();

    t.checkExpect(this.test.checkWin(), false);

    FloodItWorld testWin = new FloodItWorld(1, 3);

    t.checkExpect(testWin.checkWin(), true);

    this.reset();
  }

  void testWorldEnd(Tester t) {

    this.setup();

    t.checkExpect(this.test.worldEnds(), new WorldEnd(false, this.test.makeScene()));

    FloodItWorld testEnd = new FloodItWorld(1, 3);

    t.checkExpect(testEnd.worldEnds(), new WorldEnd(true, testEnd.endScene()));

    this.reset();
  }

  //   * onMouseClicked
  void testMouseClick(Tester t) {
    
    this.setup();
    
    t.checkExpect(this.test.board.get(0).get(1).flooded, false);
    
    t.checkExpect(this.test.board.get(1).get(0).flooded, false);
    
    this.test.onMouseClicked(new Posn(10, 300));
    
    this.test.flood();
    
    t.checkExpect(this.test.board.get(0).get(1).flooded, true);
    
    t.checkExpect(this.test.board.get(1).get(0).flooded, false);
    
    this.setup();
    
    this.test.onMouseClicked(new Posn(1000, 1000));
    
    t.checkExpect(this.test.board.get(0).get(1).flooded, false);
    
    t.checkExpect(this.test.board.get(1).get(0).flooded, false);
    
    this.reset();
  }
  
  void testOnKeyEvent(Tester t) {
    
    FloodItWorld testRandom = new FloodItWorld(3, 3, new Random(12345));
    
    FloodItWorld testRandom2 = new FloodItWorld(3, 3, new Random(12345));

    t.checkExpect(testRandom.board.get(0).get(0).color, Color.green);
    
    testRandom.rand = new Random(23456);
    
    testRandom.onKeyEvent(" ");
    
    t.checkExpect(testRandom.board, testRandom2.board);
    
    testRandom.onKeyEvent("r");
    
    t.checkExpect(testRandom.board.get(0).get(0).color, Color.red);
    
    this.reset();

  }
  
  void testOnTick(Tester t) {
    
    this.setup();
    
    t.checkInexact(this.test.seconds, 0.0, 0.01);
    
    for (int i = 0; i < 200; i++) {
      this.test.onTick();
    }
    
    t.checkInexact(this.test.seconds, 2.0, 0.01);
    
    this.reset();
  }

  void testDraw(Tester t) {

    this.setup();

    // assumes global size constant computes to 200
    t.checkExpect(new Cell(Color.magenta).draw(), new RectangleImage(200, 200, "solid",
        Color.magenta));
    t.checkExpect(new Cell(Color.orange).draw(), new RectangleImage(200, 200, "solid",
        Color.orange));
    
    this.reset();
  }

  void testSetNeighbors(Tester t) {

    this.setup();

    //start (corner)
    t.checkExpect(this.test.board.get(0).get(0).neighbors, this.board.get(0).get(0).neighbors); 

    //edge
    t.checkExpect(this.test.board.get(1).get(0).neighbors, this.board.get(1).get(0).neighbors);

    //middle
    t.checkExpect(this.test.board.get(1).get(1).neighbors, this.board.get(1).get(1).neighbors); 
    
    this.reset();
  }
}











