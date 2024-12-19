package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;

public class Grass extends Tile {
    /**
     * Calls constructor of parent Tile class.
     * @param x int of x position.
     * @param y int of y position.
     * @param app a reference of PApplet passed to allow this class to call relevant drawing functions.
     * @param tileImg PImage of the tile.
     * @param towerPlaceable boolean whether tower is allowed to be placed on the tile.
     * @param isPath boolean of whether this tile is a path.
     */
    public Grass(int x, int y, PApplet app, PImage tileImg, boolean towerPlaceable, boolean isPath) {
        super(x,y,app,tileImg,towerPlaceable,isPath);
    }
    /**
     * Allows for object to be printed as a string. Mostly used to test.
     * @return string of type of tile.
     */
    public String toString() {
        return "Grass ";
    }
}
