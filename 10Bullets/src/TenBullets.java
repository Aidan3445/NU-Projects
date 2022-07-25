package HW5.src;

import tester.*;
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;
import java.util.Random;

// represents a game world
class TenBullets extends World {

    int width;
    int height;
    int currentTick;
    int bulletsLeft;
    int shipsDestroyed;
    ILoBS bulletsShips;

    TenBullets(int bulletsLeft) {
        this.width = 750; //  Middle: 750/2 = 375
        this.height = 500; // Middle: 500/2 = 250
        this.currentTick = 0;
        this.bulletsLeft = bulletsLeft;
        this.shipsDestroyed = 0;
        this.bulletsShips = new MtLoBS();
    }

    TenBullets(int currentTick, int bulletsLeft, int shipsDestroyed, ILoBS bulletsShips) {
        this.width = 750;
        this.height = 500;
        this.currentTick = currentTick;
        this.bulletsLeft = bulletsLeft;
        this.shipsDestroyed = shipsDestroyed;
        this.bulletsShips = bulletsShips;
    }

    //testing cases
    TenBullets(boolean test) {
        this.width = 750;
        this.height = 500;
        this.currentTick = 0;
        this.bulletsLeft = 10;
        this.shipsDestroyed = 0;
        this.bulletsShips = new ConsLoBS(new Ship(150, 100, 1),
                new ConsLoBS(new Ship(600, 100, 0),
                        new ConsLoBS(new Bullet(375, 500, -90, 3),
                                new ConsLoBS(new Bullet(375, 500, -90, 3), new MtLoBS()))));
    }

    //calls draw() method on list of bullets and ships to draw them
    WorldScene draw(WorldScene scene) {
        return this.bulletsShips.draw(scene);
    }

    //draws the stats on the top of the screen
    WorldScene addInfo(WorldScene scene) {
        return scene.placeImageXY(
                new TextImage("Bullets Left: " + Integer.toString(this.bulletsLeft),
                        Color.black), 85, 10).placeImageXY(
                new TextImage("Ships destroyed: " + Integer.toString(this.shipsDestroyed),
                        Color.black), 650, 10);
    }

    //returns true if you can shoot
    boolean canShoot() {
        return this.bulletsLeft > 0;
    }

    //returns true if game is over
    boolean gameEnd() {
        return !canShoot() && this.bulletsShips.numBullets() == 0;
    }

    //calls move() method on list of bullets and ships to move them
    TenBullets move() {
        return new TenBullets(this.currentTick, this.bulletsLeft,
                this.shipsDestroyed, this.bulletsShips.move());
    }

    //call offScreen() method on list of Bullets and ships to remove ones that are off screen
    TenBullets onScreen() {
        return new TenBullets(this.currentTick, this.bulletsLeft,
                this.shipsDestroyed, this.bulletsShips.onScreen(this.width, this.height));
    }

    //calls collide() method on list of bullets and ships to check and act on collisions
    TenBullets collisions() {
        return new TenBullets(this.currentTick, this.bulletsLeft,
                this.shipsDestroyed, this.bulletsShips.collide());
    }

    //calls numHit() method on list of bullets and ships to see how many are hit
    TenBullets shipsDestroyed() {
        return new TenBullets(this.currentTick, this.bulletsLeft,
                this.shipsDestroyed + this.bulletsShips.numHit(this.bulletsShips), this.bulletsShips);
    }

    //generates a list of new ships with random height and direction (horizontal only)
    public ILoBS generateShipsHelper(int count) {
        if (count == 0) {
            return new MtLoBS();
        } else {
            // .nextInt(int, int) doesnt work?
            int leftRight = new Random().nextInt(2);
            int range = (int)(this.height * 5 / 7);
            int rangeDelta = (int)(this.height * 1 / 7);
            return new ConsLoBS(new Ship(
                    leftRight * (this.width + 40) - 20, //100 units variance for x spawn
                    new Random().nextInt(range) + rangeDelta, leftRight - 1),
                    generateShipsHelper(count - 1)); //left start
        }
    }

    public TenBullets generateShips() {
        if (this.currentTick % 100 == 0) {
            return new TenBullets(this.currentTick, this.bulletsLeft, this.shipsDestroyed,
                    this.bulletsShips.addList(generateShipsHelper(new  Random().nextInt(3) + 1)));
        } else {
            return this;
        }
    }

    @Override //creates world scene
    public WorldScene makeScene() {
        //Make a new scene.
        WorldScene scene = new WorldScene(this.width, this.height);

        if (!gameEnd()) {
            scene = this.draw(scene);

            scene = this.addInfo(scene);
        }
        return scene;
    }

    @Override //tests for world end state
    public WorldEnd worldEnds() {
        if (this.gameEnd()) {
            return new WorldEnd(true, this.makeEndScene());
        } else {
            return new WorldEnd(false, this.makeScene());
        }
    }

    //defines end scene
    public WorldScene makeEndScene() {
        WorldScene endScene = new WorldScene(this.width, this.height);
        return endScene.placeImageXY(
                new TextImage("Ships Destroyed: " + Integer.toString(
                        this.shipsDestroyed), Color.red), this.width / 2, this.height / 2);
    }

    //increments currentTick by 1
    TenBullets nextTick() {
        return new TenBullets(this.currentTick + 1, this.bulletsLeft,
                this.shipsDestroyed, this.bulletsShips);
    }

    //runs each tick of bigBang
    @Override
    public TenBullets onTick() {
        return this.onScreen().move().generateShips().shipsDestroyed().collisions().nextTick(); //
    }

    @Override //tests for key events and creates newly shot bullet
    public TenBullets onKeyEvent(String key) {
        if (key.equals(" ") && canShoot()) {
            return new TenBullets(this.currentTick, this.bulletsLeft - 1,
                    this.shipsDestroyed, new ConsLoBS(
                    new Bullet(this.width / 2, this.height, -90, 1), this.bulletsShips));
        } else {
            return this;
        }
    }
}

// represents game pieces in TenBullets
interface IBS {

    //creates and places image for ship or bullet
    WorldScene draw(WorldScene scene);

    //moves the ship or bullet based on angle
    ABS move();

    //checks if two ABS are of opposite types (one bullet one ship)
    boolean diffType(ABS other);

    //checks if a bullet and ship are in contact with each other
    boolean contact(ABS other);

    //either explodes bullet or removes ship
    ILoBS explode();

    //is this bullet or ship off screen?
    boolean offScreen(int width, int height);
}

// abstracts some operations that Bullet and Ship share
abstract class ABS implements IBS {
    int xPos;
    int yPos;
    double angle;
    int size;
    int speed;
    Color color;

    ABS(int xPos, int yPos, double angle, int size, Color color) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.angle = angle;
        this.size = Math.min(20, size);
        this.color = color;
        this.speed = 2; //to change speed w/o changing tick rate
    }

    //draws circle of ship or bullet indicated by color, bullets can be different sizes
    public WorldScene draw(WorldScene scene) {
        return scene.placeImageXY(
                new CircleImage(this.size, OutlineMode.SOLID, this.color), this.xPos, this.yPos);
    }

    //returns true if the distance between the centers of the
    //bullets and/or ships is less than the sum of their sizes
    public boolean contact(ABS other) {
        return Math.hypot(this.xPos - other.xPos, this.yPos - other.yPos) <= this.size + other.size;
    }

    //returns false, this is not necessarily a ship
    boolean isShip() {
        return false;
    }

    //returns false, this is not necessarily a bullet
    boolean isBullet() {
        return false;
    }

    //returns true if off screen
    public boolean offScreen(int width, int height) {
        return this.xPos > width + this.size || this.xPos < -this.size
                || this.yPos > height + this.size || this.yPos < -this.size;
    }
}

// extends the abstract class ABS
// represents a ship in TenBullets
class Ship extends ABS {

    int leftRight;

    Ship(int xPos, int yPos, int leftRight) { //leftRight: 1 = left, 0 = right
        super(xPos, yPos, 180 * (leftRight + 1), 20, Color.red);
        this.leftRight = leftRight;
    }

    //moves ship horizontally, angle should always be 180 or 360 for full horizontal speed
    public ABS move() {
        return new Ship((int) (this.xPos + this.speed * Math.cos(Math.toRadians(this.angle))),
                this.yPos, this.leftRight); //change speed to 1 maybe?
    }

    //returns true, this is a ship
    boolean isShip() {
        return true;
    }

    //returns true if other is a bullet
    public boolean diffType(ABS other) {
        return other.isBullet();
    }

    //returns empty list to effectively delete this ship
    public ILoBS explode() {
        return new MtLoBS();
    }
}

// extends the abstract class ABS
// represents a bullet in TenBullets
class Bullet extends ABS {
    int explodeNum;

    Bullet(int xPos, int yPos, double angle, int explodeNum) {
        super(xPos, yPos, angle, 5 + explodeNum, Color.blue);
        this.explodeNum = explodeNum;
    }

    //moves bullet based on angle, should be consistent speed regardless of angle
    //bullets always move twice the speed of ships
    public ABS move() {
        return new Bullet((int) (this.xPos + 2 * this.speed * Math.cos(Math.toRadians(this.angle))),
                (int) (this.yPos + 2 * this.speed * Math.sin(Math.toRadians(this.angle))),
                this.angle, this.explodeNum);
    }

    //returns true, this is a bullet
    boolean isBullet() {
        return true;
    }

    //returns true if other is a ship
    public boolean diffType(ABS other) {
        return other.isShip();
    }

    //returns list of bullets that follow the explosion rules
    //based on number of previous explosions
    public ILoBS explode() {
        return this.explodeHelper(this.explodeNum + 1);
    }

    //creates list of new bullets  according to explosion rules
    ILoBS explodeHelper(int count) {
        if (count == 0) {
            return new MtLoBS();
        } else {
            return new ConsLoBS(new Bullet(this.xPos, this.yPos,
                    count * (360 / (this.explodeNum + 1)), this.explodeNum + 1),
                    this.explodeHelper(count - 1));
        }
    }
}

// represents a list of bullets of ships
interface ILoBS {

    //adds other list to the end of this list
    ILoBS addList(ILoBS other);

    //creates and places images for list of ships or bullets
    WorldScene draw(WorldScene scene);

    //moves bullets and ships in list
    ILoBS move();

    //checks if any bullets collide with any ships
    //removes ships and 'explodes' bullets
    ILoBS collide();

    //explodes or removes objects of opposite types that collided
    ILoBS explodeCollisions(ABS other);

    //checks if a bullet and ship are in contact with each other
    boolean contact(ABS other);

    //counts how many bullets are in the list
    int numBullets();

    //gets list of bullets and ships that are on screen
    ILoBS onScreen(int width, int height);

    //counts the number of ships that were hit by a bullet
    int numHit(ILoBS bulletsShips);
}

// represents an empty bullet
class MtLoBS implements ILoBS {
    MtLoBS() { }

    //returns other list, nothing to add to
    public ILoBS addList(ILoBS other) {
        return other;
    }

    //returns given scene, nothing to add
    public WorldScene draw(WorldScene scene) {
        return scene;
    }

    //returns empty list, nothing to move
    public ILoBS move() {
        return this;
    }

    //returns empty list, nothing to collide with
    public ILoBS collide() {
        return this;
    }

    //returns empty list, nothing to remove
    public ILoBS explodeCollisions(ABS other) {
        return this;
    }

    //returns false, nothing to be in contact
    public boolean contact(ABS other) {
        return false;
    }

    //returns 0, no bullets in empty list
    public int numBullets() {
        return 0;
    }

    //returns empty list, nothing to be on screen
    public ILoBS onScreen(int width, int height) {
        return this;
    }

    //returns 0, no ships to hit
    public int numHit(ILoBS bulletsShips) {
        return 0;
    }
}

// represents a list of bullets and ships
class ConsLoBS implements ILoBS {
    ABS first;
    ILoBS rest;

    ConsLoBS(ABS first, ILoBS rest) {
        this.first = first;
        this.rest = rest;
    }

    //creates new list with first item of this list list produces by recursive call
    //eventually reaches end of this list and adds the other list
    public ILoBS addList(ILoBS other) {
        return new ConsLoBS(this.first, this.rest.addList(other));
    }

    //calls draw() method on first in list, calls recursively
    public WorldScene draw(WorldScene scene) {
        return this.rest.draw(this.first.draw(scene));
    }

    //calls move() method on first in list, calls recursively
    public ILoBS move() {
        return new ConsLoBS(this.first.move(), this.rest.move());
    }

    //feeds rest of list into contact(other) method on first in list
    //if true, removes or 'explodes' based on type
    //if false, calls recursively
    public ILoBS collide() {
        if (this.rest.contact(this.first)) {
            return this.first.explode().addList(this.rest.explodeCollisions(this.first)).collide();
        } else {
            return new ConsLoBS(this.first, this.rest.collide());
        }
    }

    //removes all valid collisions from list
    public ILoBS explodeCollisions(ABS other) {
        if (this.first.contact(other)) {
            return this.rest.explodeCollisions(other).addList(this.first.explode());
        } else {
            return new ConsLoBS(this.first, this.rest.explodeCollisions(other));
        }
    }

    //returns true if this.first and other are different types
    //and if the contact(other) method run on ABS returns true
    public boolean contact(ABS other) {
        return (this.first.diffType(other) && this.first.contact(other))
                || (this.rest.contact(other));
    }

    //returns a list of only on screen bullets and ships
    public ILoBS onScreen(int width, int height) {
        if (this.first.offScreen(width, height)) {
            return this.rest.onScreen(width, height);
        } else {
            return new ConsLoBS(this.first, this.rest.onScreen(width, height));
        }
    }

    //returns call recursively, adds one if isBullet() method returns true on first
    public int numBullets() {
        if (this.first.isBullet()) {
            return 1 + this.rest.numBullets();
        } else {
            return this.rest.numBullets();
        }
    }

    //returns call recursively, adds one if first is ship and was hit
    public int numHit(ILoBS bulletsShips) {
        if (this.first.isShip() && bulletsShips.contact(this.first)) {
            return 1 + this.rest.numHit(bulletsShips);
        } else {
            return this.rest.numHit(bulletsShips);
        }
    }
}

class ExamplesTenBullets {

    // examples
    TenBullets exampleGame = new TenBullets(500, 500, 0, new MtLoBS());
    TenBullets exampleGameNoBullets = new TenBullets(0);
    TenBullets exampleGameNoBulletsLeftSomeOnScreen = new TenBullets(0, 0, 5,
            new ConsLoBS(new Bullet(4, 4, 4, 4), new MtLoBS()));
    TenBullets exampleGameCollision = new TenBullets(0, 0, 5, new ConsLoBS(new Bullet(4, 4, 4, 1),
            new ConsLoBS(new Ship(4, 4, 1), new MtLoBS())));
    TenBullets exampleTwoCollision = new TenBullets(0, 0, 0, new ConsLoBS(new Ship(100, 100, 0),
            new ConsLoBS(new Ship(100, 100, 0),
                    new ConsLoBS(new Bullet(100, 110, Math.toRadians(90), 1), new MtLoBS()))));
    TenBullets exampleGameEnd = new TenBullets(0, 0, 0, new MtLoBS());
    CircleImage circle = new CircleImage(5, OutlineMode.OUTLINE, Color.black);
    WorldScene blankScene = new WorldScene(500, 500);
    WorldScene exampleScene = new WorldScene(500, 500)
            .placeImageXY(this.circle, 200, 200);
    ABS testShip = new Ship(100, 100, 100);
    ABS testBullet = new Bullet(100, 100, 100, 1);
    ILoBS testEmpty = new MtLoBS();
    ILoBS testList = new ConsLoBS(this.testShip, new ConsLoBS(this.testBullet, new MtLoBS()));

    // runs the TenBullets game
    boolean testBigBang(Tester t) {
        TenBullets world = new TenBullets(10);
        //width, height, tickrate = 0.5 means every 0.5 seconds the onTick method will get called.
        return world.bigBang(750, 500, 0.01);
    }

    boolean testDraw(Tester t) {
        return t.checkExpect(this.exampleGame.draw(this.blankScene), this.blankScene)
                && t.checkExpect(this.exampleGame.draw(this.exampleScene), this.exampleScene);
    }

    boolean testAddInfo(Tester t) {
        return t.checkExpect(this.exampleGame.addInfo(exampleScene), exampleScene.placeImageXY(
                new TextImage("Bullets Left: " + Integer.toString(500),
                        Color.black), 85, 10).placeImageXY(
                new TextImage("Ships destroyed: " + Integer.toString(0),
                        Color.black), 650, 10))
                && t.checkExpect(this.exampleGame.addInfo(blankScene), blankScene.placeImageXY(
                new TextImage("Bullets Left: " + Integer.toString(500),
                        Color.black), 85, 10).placeImageXY(
                new TextImage("Ships destroyed: " + Integer.toString(0),
                        Color.black), 650, 10));
    }

    boolean testCanShoot(Tester t) {
        return t.checkExpect(this.exampleGame.canShoot(), true)
                && t.checkExpect(this.exampleGameNoBullets.canShoot(), false);
    }

    boolean testGameEnd(Tester t) {
        return t.checkExpect(this.exampleGame.gameEnd(), false)
                && t.checkExpect(this.exampleGameNoBullets.gameEnd(), true)
                && t.checkExpect(this.exampleGameNoBulletsLeftSomeOnScreen.gameEnd(), false);
    }

    boolean testMove(Tester t) {
        return t.checkExpect(this.exampleGameNoBulletsLeftSomeOnScreen.move(), new TenBullets(0, 0, 5,
                new ConsLoBS(new Bullet(7, 4, 4, 4), new MtLoBS())))
                && t.checkExpect(this.exampleGame.move(), this.exampleGame)
                && t.checkExpect(this.exampleGameNoBullets.move(), this.exampleGameNoBullets);
    }

    boolean testOnScreen(Tester t) {
        return t.checkExpect(this.exampleGame.onScreen(), this.exampleGame)
                && t.checkExpect(this.exampleGameNoBullets.onScreen(), this.exampleGameNoBullets)
                && t.checkExpect(this.exampleGameNoBulletsLeftSomeOnScreen.onScreen(),
                this.exampleGameNoBulletsLeftSomeOnScreen);
    }

    boolean testCollisions(Tester t) {
        return t.checkExpect(this.exampleGame.collisions(), this.exampleGame)
                && t.checkExpect(this.exampleGameNoBullets.collisions(), this.exampleGameNoBullets)
                && t.checkExpect(this.exampleGameNoBulletsLeftSomeOnScreen.collisions(),
                this.exampleGameNoBulletsLeftSomeOnScreen)
                && t.checkExpect(this.exampleGameCollision.collisions(), new TenBullets(0, 0, 5,
                new ConsLoBS(new Bullet(4, 4, 360, 2), new ConsLoBS(new Bullet(4, 4, 180, 2),
                        new MtLoBS()))));
    }

    boolean testShipsDestroyed(Tester t) {
        return t.checkExpect(this.exampleGame.shipsDestroyed(), this.exampleGame)
                && t.checkExpect(this.exampleGameCollision.shipsDestroyed(),
                new TenBullets(0, 0, 6,
                        new ConsLoBS(new Bullet(4, 4, 4, 1), new ConsLoBS(new Ship(4, 4, 1),
                                new MtLoBS()))))
                && t.checkExpect(this.exampleGameNoBullets.shipsDestroyed(),
                this.exampleGameNoBullets)
                && t.checkExpect(this.exampleGameNoBulletsLeftSomeOnScreen.shipsDestroyed(),
                this.exampleGameNoBulletsLeftSomeOnScreen);
    }

    boolean testMakeScene(Tester t) {
        return t.checkExpect(this.exampleGame.makeScene(), new WorldScene(750, 500)
                .placeImageXY(new TextImage("Bullets Left: 500", 13, FontStyle.REGULAR,
                        Color.black), 85, 10)
                .placeImageXY(new TextImage("Ships destroyed: 0", 13, FontStyle.REGULAR,
                        Color.black), 650, 10))
                && t.checkExpect(this.exampleGame.makeScene(), new WorldScene(750, 500)
                .placeImageXY(new TextImage("Bullets Left: 500", 13, FontStyle.REGULAR,
                        Color.black), 85, 10)
                .placeImageXY(new TextImage("Ships destroyed: 0", 13, FontStyle.REGULAR,
                        Color.black), 650, 10));
    }

    boolean testWorldEnds(Tester t) {
        return t.checkExpect(this.exampleGame.worldEnds(),
                new WorldEnd(false, this.exampleGame.makeScene()))
                && t.checkExpect(this.exampleGameEnd.worldEnds(),
                new WorldEnd(true, this.exampleGameEnd.makeEndScene()));
    }

    boolean testMakeEndScene(Tester t) {
        return t.checkExpect(this.exampleGame.makeEndScene(), new WorldScene(750, 500)
                .placeImageXY(new TextImage("Ships Destroyed: 0", 13, FontStyle.REGULAR,
                        Color.red), 375, 250))
                && t.checkExpect(this.exampleTwoCollision.makeEndScene(), new WorldScene(750, 500)
                .placeImageXY(new TextImage("Ships Destroyed: 0", 13, FontStyle.REGULAR,
                        Color.red), 375, 250));
    }

    boolean testNextTick(Tester t) {
        return t.checkExpect(this.exampleGame.nextTick(), new TenBullets(501, 500, 0,
                new MtLoBS()))
                && t.checkExpect(this.exampleGameCollision.nextTick(),
                new TenBullets(1, 0, 5, new ConsLoBS(new Bullet(4, 4, 4, 1),
                        new ConsLoBS(new Ship(4, 4, 1), new MtLoBS()))));
    }

    boolean testOnKeyEvent(Tester t) {
        return t.checkExpect(this.exampleGame.onKeyEvent(" "),
                new TenBullets(500, 499, 0,
                        new ConsLoBS( new Bullet(375, 500, -90, 1), new MtLoBS())))
                && t.checkExpect(this.exampleGame.onKeyEvent("a"), this.exampleGame);
    }

    boolean testDiffType(Tester t) {
        return t.checkExpect(this.testShip.diffType(this.testBullet), true)
                && t.checkExpect(this.testBullet.diffType(testShip), true)
                && t.checkExpect(this.testBullet.diffType(testBullet), false)
                && t.checkExpect(this.testShip.diffType(testShip), false);
    }

    boolean testContact(Tester t) {
        return t.checkExpect(this.testBullet.contact(testBullet), true)
                && t.checkExpect(this.testShip.contact(this.testShip), true)
                && t.checkExpect(this.testShip.contact(this.testBullet), true)
                && t.checkExpect(this.testBullet.contact(new Bullet(300, 300, 300, 300)),
                false);
    }

    boolean testExplode(Tester t) {
        return t.checkExpect(this.testBullet.explode(),
                new ConsLoBS(new Bullet(100, 100, 360, 2),
                        new ConsLoBS(new Bullet(100, 100, 180, 2), new MtLoBS())))
                && t.checkExpect(this.testShip.explode(), new MtLoBS());
    }

    boolean testoffScreen(Tester t) {
        return t.checkExpect(this.testBullet.offScreen(100, 100), false)
                && t.checkExpect(this.testBullet.offScreen(50, 50), true)
                && t.checkExpect(this.testShip.offScreen(100, 100), false)
                && t.checkExpect(this.testShip.offScreen(50, 50), true);
    }

    boolean testIsShip(Tester t) {
        return t.checkExpect(this.testBullet.isShip(), false)
                && t.checkExpect(this.testShip.isShip(), true);
    }

    boolean testIsBullet(Tester t) {
        return t.checkExpect(this.testBullet.isBullet(), true)
                && t.checkExpect(this.testShip.isBullet(), false);
    }

    boolean testAddList(Tester t) {
        return t.checkExpect(this.testList.addList(this.testList),
                new ConsLoBS(this.testShip,
                        new ConsLoBS(this.testBullet, new ConsLoBS(this.testShip,
                                new ConsLoBS(this.testBullet, new MtLoBS())))))
                && t.checkExpect(this.testList.addList(new MtLoBS()), this.testList)
                && t.checkExpect(this.testEmpty.addList(this.testList), this.testList);
    }

    boolean testExplodeCollisions(Tester t) {
        return t.checkExpect(this.testList.explodeCollisions(this.testBullet),
                new ConsLoBS(new Bullet(100, 100, 360, 2),
                        new ConsLoBS(new Bullet(100, 100, 180, 2), new MtLoBS())))
                && t.checkExpect(this.testEmpty.explodeCollisions(this.testBullet),
                new MtLoBS());
    }

    boolean testNumBullets(Tester t) {
        return t.checkExpect(this.testList.numBullets(), 1)
                && t.checkExpect(this.testEmpty.numBullets(), 0);
    }

    boolean testOnScreenILoBS(Tester t) {
        return t.checkExpect(this.testList.onScreen(50, 50), new MtLoBS())
                && t.checkExpect(this.testEmpty.onScreen(50, 50), new MtLoBS())
                && t.checkExpect(this.testList.onScreen(100, 100), this.testList);
    }

    boolean testNumHit(Tester t) {
        return t.checkExpect(this.testList.numHit(testList), 1)
                && t.checkExpect(this.testEmpty.numHit(this.testList), 0)
                && t.checkExpect(this.testList.numHit(testEmpty), 0);
    }
}









