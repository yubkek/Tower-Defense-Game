package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;

public class Wizard extends Tile {
    /**
     * Calls constructor of parent Tile class.
     * @param x int of x position.
     * @param y int of y position.
     * @param app a reference of PApplet passed to allow this class to call relevant drawing functions.
     * @param tileImg PImage of the tile.
     * @param towerPlaceable boolean whether tower is allowed to be placed on the tile.
     * @param isPath boolean of whether this tile is a path.
     */
    public Wizard(int x, int y, PApplet app, PImage tileImg, boolean towerPlaceable, boolean isPath) {
        super(x,y,app,tileImg,towerPlaceable,isPath);
    }
    /**
     * Different display method as wizard house needs to be slightly adjusted to make sure it is centered.
     */
    public void display() {
        app.image(this.tileImg,this.x*32-8,this.y*32+40-8);
    }
    /**
     * Allows for object to be printed as a string. Mostly used to test.
     * @return string of type of tile.
     */
    public String toString() {
        return "Wizard ";
    }
}
