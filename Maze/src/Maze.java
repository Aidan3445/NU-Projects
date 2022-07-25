package HW10.src;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

class Maze extends World {
  int width = 850; //width of the window, fixed
  int height = 700; //height of the window
  int tileSize; //width and height of square tiles
  int cols; //number of tiles that fit in the window horizontally
  int rows; //number of tiles that fit in the window vertically
  ArrayList<ArrayList<Node>> field; //2D array list of Nodes in the maze
  Node start; //starting node of maze
  Node end; //ending node of maze\
  //search
  Node current; //the current node of the search
  ICollection<Node> worklist; //available unchecked nodes
  ArrayList<Node> checked; //already checked nodes
  HashMap<Node, Node> prev; //hashmap to connect path that was ultimately taken
  boolean running; //if true, maze is in searching process
  boolean test;
  int bScore;
  int dScore;

  Random rand;

  Maze(int cols, int rows) {
    if (cols > 70 || rows > 70 || cols < 2 || rows < 2) {
      throw new IllegalArgumentException("Invalid maze dimensions"); //max 100x70, min 2x2
    }
    this.rand = new Random();
    this.cols = cols;
    this.rows = rows;
    this.tileSize = 700 / Math.max(this.cols, this.rows); //playable board is 700
    this.generateMaze(0, 0, this.cols - 1, this.rows - 1);
    this.bScore = 0;
    this.dScore = 0;
    this.test = true;

  }

  Maze(int cols, int rows, Random rand) {
    if (cols > 70 || rows > 70 || cols < 2 || rows < 2) {
      throw new IllegalArgumentException("Invalid maze dimensions"); //max 100x70, min 2x2
    }
    this.rand = rand;
    this.cols = cols;
    this.rows = rows;
    this.tileSize = 700 / Math.max(this.cols, this.rows); //playable board is 700
    this.generateMaze(0, 0, this.cols - 1, this.rows - 1);
    this.bScore = 0;
    this.dScore = 0;
    this.test = true;
  }

  //create 2D array field of nodes with no edges, use kruskal to create a minimum spanning tree
  void generateMaze(int startCol, int startRow, int endCol, int endRow) {
    this.field = new ArrayList<ArrayList<Node>>();
    for (int i = 0; i < this.cols; i++) {
      this.field.add(new ArrayList<Node>());
      for (int j = 0; j < this.rows; j++) {
        this.field.get(i).add(new Node(i, j, this.rand));
      }
    }
    this.kruskal();
    this.reset(this.field.get(startCol).get(startRow), this.field.get(endCol).get(endRow));
  }

  //create list of all edges and heapsort weights into priotiry queue 
  //delete use kruskal alg to remove until loops are removed
  void kruskal() {
    HashMap<Node, Node> rep = new HashMap<Node, Node>();
    ArrayList<Edge> edges = new ArrayList<Edge>();
    ArrayList<Edge> tree = new ArrayList<Edge>();
    for (ArrayList<Node> col : this.field) {
      for (Node node : col) {
        rep.put(node, node);
        if (node.row < field.get(0).size() - 1) {
          edges.add(new Edge(node, field.get(node.col).get(node.row + 1)));
        }
        if (node.row > 0 ) {
          edges.add(new Edge(node, field.get(node.col).get(node.row - 1)));
        }
        if (node.col < field.size() - 1) {
          edges.add(new Edge(node, field.get(node.col + 1).get(node.row)));
        }
        if (node.col > 0) {
          edges.add(new Edge(node, field.get(node.col - 1).get(node.row)));
        }
      }
    }
    Edge.heapSort(edges, edges.size());
    while (edges.size() > 0) {
      Edge current = edges.get(0);
      if (current.n1.find(rep).equals(current.n2.find(rep))) {
        edges.remove(current);
      } else {
        tree.add(current);
        rep.put(current.n1.find(rep), current.n2.find(rep));
      }
    }
    for (Edge e : tree) {
      e.addEdge();
    }
  }

  //set start and end, and reset maze walls, hashmap, and checked list
  void reset(Node start, Node end) {
    this.start = start;
    this.end = end;
    this.current = this.start;
    this.prev = new HashMap<Node, Node>();
    this.checked = new ArrayList<Node>();
    this.running = false;
    for (ArrayList<Node> col : this.field) {
      for (Node node : col) {
        node.color = Color.gray;
      }
    }
  }

  //set color of this step in recreation and call on previous using hashmap (starting with current)
  void recreate(Node step) {
    step.color = this.colorFromDist(step);
    if (step.equals(this.start)) {
      return;
    }
    this.recreate(this.prev.get(step));
  }

  //find distance to end and use HSB to get rainbow gradient value for that distance
  Color colorFromDist(Node node) {
    double hue = Math.hypot(this.end.col - node.col, this.end.row - node.row) 
        / Math.hypot(this.end.col - this.start.col, this.end.row - this.start.row) * 335 / 360;
    return Color.getHSBColor((float) hue, 1, 1);
  }

  //draw nodes and build walls, color start and end white
  @Override
  public WorldScene makeScene() {
    this.start.color = Color.white;
    this.end.color = Color.white;
    WorldScene scene = new WorldScene(this.width, this.height);
    for (ArrayList<Node> col : this.field) {
      for (Node node : col) {
        node.draw(scene, this.tileSize, this.running);
      }
    }
    for (ArrayList<Node> col : this.field) {
      for (Node node : col) {
        node.buildWalls(scene, this.field, this.tileSize);
      }
    }
    scene.placeImageXY(new TextImage("Breadth-First Moves", Color.black), 
        (this.width + this.cols * this.tileSize) / 2, this.height / 3);
    scene.placeImageXY(new TextImage("Depth-First Moves", Color.black), 
        (this.width + this.cols * this.tileSize) / 2, this.height * 2 / 3);
    scene.placeImageXY(new TextImage(Integer.toString(this.bScore), Color.red), 
        (this.width + this.cols * this.tileSize) / 2, this.height / 3 + 30);
    scene.placeImageXY(new TextImage(Integer.toString(this.dScore), Color.red), 
        (this.width + this.cols * this.tileSize) / 2, this.height * 2 / 3 + 30);
    return scene;
  }

  //'space bar' clears maze and generates new walls
  //'c' clears maze but preserves walls
  //'b' initializes BFS if maze is not already being solved
  //'d' initializes DFS if maze is not already being solved
  @Override
  public void onKeyEvent(String key) {
    if (key.equals(" ")) {
      this.generateMaze(this.start.col, this.start.row, this.end.col, this.end.row);
      this.bScore = 0;
      this.dScore = 0;
    }
    if (key.equalsIgnoreCase("c")) {
      this.reset(this.start, this.end);
    }
    if (!this.running) {
      if (key.equalsIgnoreCase("b")) {
        this.bScore = 0;
        this.reset(this.start, this.end);
        this.running = true;
        this.worklist = new Queue<Node>();
        this.worklist.add(this.current);
        this.test = true;
      }
      if (key.equalsIgnoreCase("d")) {
        this.dScore = 0;
        this.reset(this.start, this.end);
        this.running = true;
        this.worklist = new Stack<Node>();
        this.worklist.add(this.current);
        this.test = false;
      }
    }
  }

  //single loop of BFS or DFS depending on the data structure used for worklist if search is running
  @Override
  public void onTick() {
    if (this.running) {
      if (!this.worklist.isEmpty()) {
        this.current = this.worklist.pop();
        this.recreate(this.current);
        if (this.current.equals(this.end)) {
          this.running = false;
          return;
        } else if (!this.checked.contains(this.current)) {
          if (this.test) {
            this.bScore = bScore + 1;
          } else {
            this.dScore = dScore + 1;
          }
          for (Node n : this.current.neighbors) {
            if (!this.checked.contains(n)) {
              this.worklist.add(n);
              this.prev.put(n, this.current);
            }
          }
          this.checked.add(this.current);

        }
      }
    }
  }
}

class Node {
  int col; //column position of the node (x)
  int row; //row position of the node (y)
  double weight; //randomized weight of node
  ArrayList<Node> neighbors; //list of connected neighbors (max size 4, min size 1)
  Color color; //color of node

  Random rand; //for testing randomness

  Node(int col, int row) {
    this.col = col;
    this.row = row;
    this.weight = new Random().nextDouble();
    this.neighbors = new ArrayList<Node>();
    this.color = Color.gray;

    this.rand = new Random();
  }

  //for testing randomness
  Node(int col, int row, Random rand) {
    this(col, row);
    this.weight = rand.nextDouble();
  }

  //draws node and resets color to gray for next tick
  void draw(WorldScene scene, int tileSize, boolean running) {
    scene.placeImageXY(
        new RectangleImage(tileSize, tileSize , "solid", this.color),
        this.col * tileSize + tileSize / 2, this.row * tileSize + tileSize / 2);

    if (!this.color.equals(Color.gray) && running) {
      this.color = Color.darkGray;
    }
  }

  //draws walls for neighbors below and/or to the right
  //existing left and top walls would be drawn by neighbors
  void buildWalls(WorldScene scene, ArrayList<ArrayList<Node>> field, int tileSize) {
    if (this.col < field.size() - 1
        && !this.neighbors.contains(field.get(this.col + 1).get(this.row))) {
      scene.placeImageXY(new RectangleImage(
          tileSize / 10, tileSize + tileSize / 10, "solid", Color.black), 
          (this.col + 1) * tileSize, this.row * tileSize + tileSize / 2);
    }
    if (this.row < field.get(0).size() - 1
        && !this.neighbors.contains(field.get(this.col).get(this.row + 1))) {
      scene.placeImageXY(new RectangleImage(
          tileSize + tileSize / 10, tileSize / 10, "solid", Color.black), 
          this.col * tileSize + tileSize / 2, (this.row + 1) * tileSize);
    }
    if (this.col == field.size() - 1) {
      scene.placeImageXY(new RectangleImage(
          tileSize / 10, tileSize + tileSize / 10, "solid", Color.black), 
          (this.col + 1) * tileSize, this.row * tileSize + tileSize / 2);
    }
    if (this.row == field.get(0).size() - 1) {
      scene.placeImageXY(new RectangleImage(
          tileSize + tileSize / 10, tileSize / 10, "solid", Color.black), 
          this.col * tileSize + tileSize / 2, (this.row + 1) * tileSize);
    }
  }

  //find root node using hashmap
  Node find(HashMap<Node, Node> map) {
    if (map.get(this).equals(this)) {
      return this;
    } else {
      return map.get(this).find(map);
    }
  }

  //overwritten .equals() method for comparison 
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof Node) {
      Node n = (Node) obj;
      return n.col == this.col && n.row == this.row;
    } else {
      return false;
    }
  }

  //overwritten .hashCode() method for hashcode usage
  @Override
  public int hashCode() {
    return Objects.hash(this.col, this.row);
  }
}

class Edge {
  Node n1; //first node in edge
  Node n2; //second node in edge
  double weight; //sum of node's weights to randomize maze

  Edge(Node n1, Node n2) {
    this.n1 = n1;
    this.n2 = n2;
    this.weight = n1.weight + n2.weight;
  }

  //adds both nodes to eachother's neighbors array list
  void addEdge() {
    this.n1.neighbors.add(n2);
    this.n2.neighbors.add(n1);
  }

  //overwritten .equals() method for comparison 
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof Edge) {
      Edge e = (Edge) obj;
      return (e.n1.equals(this.n1) && e.n2.equals(this.n2)) 
          || (e.n1.equals(this.n2) && e.n2.equals(this.n1));
    } else {
      return false;
    }
  }

  //overwritten .hashCode() method for hashcode usage
  @Override
  public int hashCode() {
    return Objects.hash(this.n1, this.n2);
  }

  //edge heapsort aglorithm (lowest to highest weight)
  static void heapSort(ArrayList<Edge> heap, int size) {
    for (int i = size / 2 - 1; i >= 0; i--) {
      Edge.prepHeap(heap, size, i);
    }
    for (int i = size - 1; i >= 0; i--) {
      Edge hold = heap.get(0);
      heap.set(0, heap.get(i));
      heap.set(i, hold);
      Edge.prepHeap(heap, i, 0);
    }
  }


  //edge heap algorithm, lowest weight in heap at the top
  static void prepHeap(ArrayList<Edge> heap, int size, int maxIndex) {
    int max = maxIndex;
    int l = 2 * maxIndex + 1;
    int r = 2 * maxIndex + 2;
    if (l < size && heap.get(l).weight > heap.get(max).weight) {
      max = l; }
    if (r < size && heap.get(r).weight > heap.get(max).weight) {
      max = r;
    }
    if (maxIndex != max) {
      Edge hold = heap.get(maxIndex);
      heap.set(maxIndex, heap.get(max));
      heap.set(max, hold);
      Edge.prepHeap(heap, size, max);
    }
  }
}

interface ICollection<T> {

  boolean isEmpty(); //returns true if collection is empty

  void add(T item); //adds element following that collection's rules

  T pop(); //removes and returns the first element
}

class Queue<T> implements ICollection<T> {
  ArrayDeque<T> contents;

  Queue() {
    this.contents = new ArrayDeque<T>();
  }

  //calls isEmpty on ArrayDeque<T> contents
  public boolean isEmpty() {
    return this.contents.isEmpty();
  }

  //calls addLast on ArrayDeque<T> contents
  public void add(T item) {
    this.contents.addLast(item);
  }

  //calls pop on ArrayDeque<T> contents
  public T pop() {
    return this.contents.pop();
  }
}

class Stack<T> implements ICollection<T> {
  ArrayDeque<T> contents;

  Stack() {
    this.contents = new ArrayDeque<T>();
  }

  //calls isEmpty on ArrayDeque<T> contents
  public boolean isEmpty() {
    return this.contents.isEmpty();
  }

  //calls addFirst on ArrayDeque<T> contents
  public void add(T item) {
    this.contents.addFirst(item);
  }

  //calls pop on ArrayDeque<T> contents
  public T pop() {
    return this.contents.pop();
  }
}

class ExamplesMaze {
  Maze world;

  Node EN1;
  Node EN2;
  Node EN3;
  Node EN4;
  Edge testEdge;
  Edge testEdge2;
  Edge testEdge3;

  ArrayList<Edge> testArray;
  ArrayList<Edge> testHeapSort;

  Stack<Node> testStack;
  Queue<Node> testQueue;

  WorldScene scene;

  HashMap<Node, Node> testHash;

  WorldScene testScene;


  void testBigBang(Tester t) {

    this.reset();

    this.world.bigBang(world.width, world.height, 0.001);
  }
  
  void reset() {
    this.world = new Maze(70, 70);
  }

  void setup() {

    this.world = new Maze(2, 2, new Random(9));

    this.EN1 = new Node(0, 0, new Random(123));
    this.EN2 = new Node(1, 0, new Random(234));

    this.testEdge = new Edge(EN1, EN2);
    this.testArray = new ArrayList<Edge>(Arrays.asList(this.testEdge));

    this.EN3 = new Node(0, 1, new Random(1));
    this.EN4 = new Node(1, 1, new Random(2));

    this.testEdge2 = new Edge(this.EN3, this.EN4);
    this.testEdge2.weight = 1;
    this.testEdge3 = new Edge(this.EN3, this.EN4);
    this.testEdge3.weight = 2;

    this.testHeapSort = new ArrayList<Edge>(
        Arrays.asList(this.testEdge, this.testEdge2, this.testEdge3));

    this.testStack = new Stack<Node>();
    this.testQueue = new Queue<Node>();

    this.scene = new WorldScene(1000, 700);

    this.testHash = new HashMap<Node, Node>();
    testHash.put(EN1, EN2);
    testHash.put(EN2, EN2);
  }
  
  void testMazeConstructor(Tester t) {
    
    t.checkConstructorException(new IllegalArgumentException("Invalid maze dimensions"), 
        "Maze", 1, 1);

    t.checkConstructorException(new IllegalArgumentException("Invalid maze dimensions"), 
        "Maze", 71, 71);

    t.checkConstructorException(new IllegalArgumentException("Invalid maze dimensions"), 
        "Maze", 1, 48);

    t.checkConstructorException(new IllegalArgumentException("Invalid maze dimensions"), 
        "Maze", 39, 100);
    
    t.checkConstructorNoException("testMazeConstructor", "Maze", 2, 2);

    t.checkConstructorNoException("testMazeConstructor", "Maze", 70, 70);

  }

  void testGenerateMaze(Tester t) {

    this.setup(); //generateMaze is called in the constructor for this.world

    t.checkExpect(this.world.field.get(0).get(0).equals(this.EN1), true);

    t.checkExpect(this.world.field.get(0).get(1).equals(this.EN3), true);

    t.checkExpect(this.world.field.get(1).get(0).equals(this.EN2), true);

    t.checkExpect(this.world.field.get(1).get(1).equals(this.EN4), true);

    this.reset();
  }

  void testKruskal(Tester t) {

    this.setup(); //kruskal is called in generateMaze in the constructor for this.world

    t.checkExpect(this.world.field.get(0).get(0).neighbors.size(), 1);

    t.checkExpect(this.world.field.get(0).get(0).neighbors.get(0).equals(this.EN3), true);

    t.checkExpect(this.world.field.get(0).get(1).neighbors.size(), 2);

    t.checkExpect(this.world.field.get(0).get(1).neighbors.get(0).equals(this.EN4), true);

    t.checkExpect(this.world.field.get(0).get(1).neighbors.get(1).equals(this.EN1), true);

    t.checkExpect(this.world.field.get(1).get(0).neighbors.size(), 1);

    t.checkExpect(this.world.field.get(1).get(0).neighbors.get(0).equals(this.EN4), true);

    t.checkExpect(this.world.field.get(1).get(1).neighbors.size(), 2);

    t.checkExpect(this.world.field.get(1).get(1).neighbors.get(0).equals(this.EN3), true);

    t.checkExpect(this.world.field.get(1).get(1).neighbors.get(1).equals(this.EN2), true);

    this.reset();
  }

  void testResetMaze(Tester t) {

    this.setup(); //reset is called in generateMaze in the constructor for this.world

    t.checkExpect(this.world.prev.size(), 0);

    t.checkExpect(this.world.start.equals(this.EN1), true);

    t.checkExpect(this.world.end.equals(this.EN4), true);

    t.checkExpect(this.world.current.equals(this.EN1), true);

    t.checkExpect(this.world.checked.size(), 0);

    t.checkExpect(this.world.running, false);

    t.checkExpect(this.world.field.get(0).get(0).color, Color.gray);

    t.checkExpect(this.world.field.get(0).get(1).color, Color.gray);

    t.checkExpect(this.world.field.get(1).get(0).color, Color.gray);

    t.checkExpect(this.world.field.get(1).get(1).color, Color.gray);

    this.reset();
  }

  void testRecreate(Tester t) {

    this.setup();

    this.world.onKeyEvent("d");

    for (int i = 0; i < 4; i++) {
      this.world.onTick(); //recreate called within onTick
    }

    t.checkExpect(this.world.field.get(0).get(0).color, new Color(255, 0, 106));

    t.checkExpect(this.world.field.get(0).get(1).color, new Color(0, 13, 255));

    t.checkExpect(this.world.field.get(1).get(0).color, Color.gray);

    t.checkExpect(this.world.field.get(1).get(1).color, new Color(255, 0, 0));

    this.reset();
  }

  void testColorFromDist(Tester t) {

    Maze bigWorld = new Maze(35, 35);

    t.checkExpect(bigWorld.colorFromDist(bigWorld.field.get(10).get(10)), new Color(0, 15, 255));

    this.reset();
  }

  void testMakeSceneDrawBuildWalls(Tester t) { 
    //same test for:
    //* make scene in maze class
    //* draw and buildWalls in node class

    this.setup();

    WorldScene testWorldScene = this.world.makeScene();    

    this.testScene = new WorldScene(850, 700);

    this.testScene.placeImageXY(new RectangleImage(350, 350, "solid", Color.white), 175, 175);

    this.testScene.placeImageXY(new RectangleImage(350, 350, "solid", Color.gray), 175, 525);

    this.testScene.placeImageXY(new RectangleImage(350, 350, "solid", Color.gray), 525, 175);

    this.testScene.placeImageXY(new RectangleImage(350, 350, "solid", Color.white), 525, 525);

    this.testScene.placeImageXY(new RectangleImage(35, 385, "solid", Color.black), 350, 175);
    
    this.testScene.placeImageXY(new RectangleImage(35, 385, "solid", Color.black), 700, 175);
    
    this.testScene.placeImageXY(new RectangleImage(35, 385, "solid", Color.black), 700, 525);
    
    this.testScene.placeImageXY(new RectangleImage(385, 35, "solid", Color.black), 175, 700);
    
    this.testScene.placeImageXY(new RectangleImage(385, 35, "solid", Color.black), 525, 700);

    this.testScene.placeImageXY(new TextImage("Breadth-First Moves", Color.black), 775, 233);

    this.testScene.placeImageXY(new TextImage("Depth-First Moves", Color.black), 775, 466);

    this.testScene.placeImageXY(new TextImage("0", Color.red), 775, 263);

    this.testScene.placeImageXY(new TextImage("0", Color.red), 775, 496);

    t.checkExpect(testWorldScene, this.testScene);

    this.reset();
  }

  void testOnKey(Tester t) {

    this.setup(); //generateMaze is called in the constructor for this.world

    this.world.onKeyEvent(" ");

    t.checkExpect(this.world.field.get(0).get(0).equals(this.EN1), true);

    t.checkExpect(this.world.field.get(0).get(1).equals(this.EN3), true);

    t.checkExpect(this.world.field.get(1).get(0).equals(this.EN2), true);

    t.checkExpect(this.world.field.get(1).get(1).equals(this.EN4), true);

    this.world.onKeyEvent("c");

    this.setup(); //reset is called in generateMaze in the constructor for this.world

    t.checkExpect(this.world.prev.size(), 0);

    t.checkExpect(this.world.start.equals(this.EN1), true);

    t.checkExpect(this.world.end.equals(this.EN4), true);

    t.checkExpect(this.world.current.equals(this.EN1), true);

    t.checkExpect(this.world.checked.size(), 0);

    t.checkExpect(this.world.running, false);

    t.checkExpect(this.world.field.get(0).get(0).color, Color.gray);

    t.checkExpect(this.world.field.get(0).get(1).color, Color.gray);

    t.checkExpect(this.world.field.get(1).get(0).color, Color.gray);

    t.checkExpect(this.world.field.get(1).get(1).color, Color.gray);

    this.reset();
  }

  void testOnTick(Tester t) { //test BFS or DFS also tests onKey("d") and onKey("b")

    this.setup();

    this.world.onKeyEvent("d");

    this.world.onTick();

    t.checkExpect(this.world.current.equals(this.EN1), true);

    this.world.running = true;

    this.world.onTick();

    t.checkExpect(this.world.current.equals(this.EN3), true);

    this.world.onTick();

    t.checkExpect(this.world.current.equals(this.EN4), true);

    t.checkExpect(this.world.running, false);

    this.world.onKeyEvent("c");

    this.world.onKeyEvent("b");

    this.world.onTick();

    t.checkExpect(this.world.current.equals(this.EN1), true);

    this.world.running = true;

    this.world.onTick();

    t.checkExpect(this.world.current.equals(this.EN3), true);

    this.world.onTick();

    t.checkExpect(this.world.current.equals(this.EN4), true);

    t.checkExpect(this.world.running, false);

    this.reset();
  }

  void testFind(Tester t) {

    this.setup();

    t.checkExpect(this.EN1.find(testHash), this.EN2);
    t.checkExpect(this.EN2.find(testHash), this.EN2);

    this.reset();
  }

  void testEqualsNode(Tester t) {

    this.setup();

    t.checkExpect(this.EN1.equals(this.EN1), true);
    t.checkExpect(this.EN1.equals(this.EN2), false);
    t.checkExpect(this.EN1.equals(new Node(0, 0, new Random(123))), true);

    this.reset();
  }

  void testHashCode(Tester t) {

    this.setup();

    t.checkExpect(this.testHash.hashCode(), 33);

    this.reset();
  }

  void testAddEdge(Tester t) {

    this.setup();

    t.checkExpect(this.EN1.neighbors, new ArrayList<Node>());
    t.checkExpect(this.EN2.neighbors, new ArrayList<Node>());

    this.testEdge.addEdge();

    t.checkExpect(this.EN1.neighbors, new ArrayList<Node>(Arrays.asList(this.EN2)));
    t.checkExpect(this.EN2.neighbors, new ArrayList<Node>(Arrays.asList(this.EN1)));

    this.reset();
  }

  void testEquals(Tester t) {

    this.setup();

    t.checkExpect(this.EN1.equals(this.EN1), true);
    t.checkExpect(this.EN1.equals(this.EN2), false);
    t.checkExpect(this.EN1.equals(new Node(0, 0, new Random(123))), true);

    this.reset();
  }

  void testHeapSort(Tester t) {

    this.setup();

    Edge.heapSort(this.testHeapSort, 3);

    t.checkExpect(this.testHeapSort.get(0), this.testEdge2);
    t.checkExpect(this.testHeapSort.get(1), this.testEdge);
    t.checkExpect(this.testHeapSort.get(2), this.testEdge3);

    this.reset();
  }

  void testPrepHeap(Tester t) {

    this.setup();

    Edge.prepHeap(this.testHeapSort, 3, 0);

    t.checkExpect(this.testHeapSort, new ArrayList<Edge>(Arrays.asList(this.testEdge3, 
        this.testEdge2, this.testEdge)));
    t.checkExpect(this.testHeapSort.get(0), this.testEdge3);
    t.checkExpect(this.testHeapSort.get(1), this.testEdge2);
    t.checkExpect(this.testHeapSort.get(2), this.testEdge);

    this.reset();
  }

  void testIsEmptyQueue(Tester t) {

    this.setup();

    t.checkExpect(this.testQueue.isEmpty(), true);

    this.testQueue.add(EN1);

    t.checkExpect(this.testQueue.isEmpty(), false);

    this.reset();
  }

  void testAddQueue(Tester t) {

    this.setup();

    t.checkExpect(this.testQueue.isEmpty(), true);
    this.testQueue.add(EN1);

    t.checkExpect(this.testQueue.contents.getFirst(), this.EN1);

    this.testQueue.add(EN2);

    t.checkExpect(this.testQueue.contents.getFirst(), this.EN1);

    this.reset();
  }

  void testPopQueue(Tester t) {

    this.setup();

    this.testQueue.add(EN1);
    this.testQueue.add(EN2);

    t.checkExpect(this.testQueue.contents.pop(), this.EN1);
    t.checkExpect(this.testQueue.contents.getFirst(), this.EN2);

    t.checkExpect(this.testQueue.contents.pop(), this.EN2);
    t.checkExpect(this.testQueue.contents, new ArrayDeque<Node>());

    this.reset();
  }

  void testIsEmptyStack(Tester t) {

    this.setup();

    t.checkExpect(this.testStack.contents.isEmpty(), true);

    this.testStack.add(EN1);

    t.checkExpect(this.testStack.contents.isEmpty(), false);

    this.reset();
  }

  void testAddStack(Tester t) {

    this.setup();

    t.checkExpect(this.testStack.isEmpty(), true);

    this.testStack.add(EN1);

    t.checkExpect(this.testStack.contents.getFirst(), this.EN1);

    this.testStack.add(EN2);

    t.checkExpect(this.testStack.contents.getFirst(), this.EN2);

    this.reset();
  }

  void testPopStack(Tester t) {

    this.setup();

    this.testStack.add(EN1);
    this.testStack.add(EN2);

    t.checkExpect(this.testStack.contents.pop(), this.EN2);
    t.checkExpect(this.testStack.contents.getFirst(), this.EN1);


    t.checkExpect(this.testStack.contents.pop(), this.EN1);
    t.checkExpect(this.testStack, new Stack<Node>());

    this.reset();
  }
}