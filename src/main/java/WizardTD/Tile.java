package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;

public abstract class Tile {
    protected int x;
    protected int y;
    protected PApplet app;
    protected PImage tileImg;
    protected boolean towerPlaceable;
    protected boolean isPath;
    protected boolean visited;

    /**
     * Constructor called whenever Tile object is created.
     * @param x int of x position.
     * @param y int of y position.
     * @param app a reference of PApplet passed to allow this class to call relevant drawing functions.
     * @param tileImg PImage of the tile.
     * @param towerPlaceable boolean whether tower is allowed to be placed on the tile.
     * @param isPath boolean of whether this tile is a path.
     */
    public Tile(int x, int y, PApplet app, PImage tileImg, boolean towerPlaceable, boolean isPath) {
        this.x = x;
        this.y = y;
        this.app = app;
        this.tileImg = tileImg;
        this.towerPlaceable = towerPlaceable;
        this.isPath = isPath;
        this.visited = false;
    }
    /**
     * Displays the tile object at its x and y positions, multiplying x by 32 and y by 32 then adding 40 to adjust it to the correct position as each is 32 pixels long and wide.
     */
    public void display() {
        app.image(this.tileImg,this.x*32,this.y*32+40);
    }
    /** 
     * Returns the value of the x attribute.
     * @return int of the x value
     */
    public int getX() {
        return this.x;
    }
    /** 
     * Returns the value of the y attribute.
     * @return int of the y value
     */
    public int getY() {
        return this.y;
    }
    /**
     * Returns value of the towerPlaceable attribute. 
     * @return boolean if tower can be placed or not.
     */
    public boolean towerAble() {
        return this.towerPlaceable;
    }
    /**
     * Sets the value of towerPlaceable to false as tower has already been placed. 
     */
    public void placed() {
        this.towerPlaceable = false;
    }
    /**
     * Returns value of isPath attribute.
     * @return boolean if this tile is a path or not.
     */
    public boolean path() {
        return this.isPath;
    }
}
