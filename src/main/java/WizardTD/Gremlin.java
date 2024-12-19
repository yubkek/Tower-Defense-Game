package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

public class Gremlin extends Enemy{
    /**
     * Calls constructor of parent Enemy class.
     * @param x float indicating x position.
     * @param y float indicating y position.
     * @param app a reference of PApplet passed to allow this class to call relevant drawing functions.
     * @param enemyImg PImage of the sprite.
     * @param speed float indicating movement speed of the enemy.
     * @param health float indicating total health of the enemy.
     * @param mana_gained_on_kill float indicating how much mana player gains when this enemy dies.
     * @param armor float indicating damage reduction.
     * @param pathToGo arraylist of float arrays giving the coordinates of the points the enemy will move to.
     * @param wiz Tile object that indicates the end of the path.
     * @param mana reference to the mana bar to allow increasing or decreasing of manabar depending on enemy state.
     */
    public Gremlin(float x, float y, PApplet app, PImage enemyImg, float speed, float health, float mana_gained_on_kill, float armor, ArrayList<float[]> pathToGo, Tile wiz, Mana mana) {
        super(x,y,app,enemyImg,speed,health,mana_gained_on_kill,armor,pathToGo,wiz,mana);
    }
}
