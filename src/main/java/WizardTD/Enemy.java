package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

public abstract class Enemy {
    protected float x;
    protected float y;
    protected PApplet app;
    protected PImage enemyImg;
    protected float speed;
    protected float pausespeed = 0;
    protected float ffspeed;
    protected float realspeed;
    protected float maxHealth;
    protected float health;
    protected float mana_gained_on_kill;
    protected float armor;
    protected int pathIndex;
    protected float startX;
    protected float startY;
    protected int count;
    protected boolean paused;
    protected boolean ffed;
    protected boolean dead;
    protected ArrayList<float[]> path;
    protected Tile end;
    protected Mana mana;

    /**
     * Constructor called whenever Enemy object is created.
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
    public Enemy(float x, float y, PApplet app, PImage enemyImg, float speed, float health, float mana_gained_on_kill, float armor, ArrayList<float[]> pathToGo, Tile wiz, Mana mana) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.app = app;
        this.enemyImg = enemyImg;
        this.speed = speed;
        this.realspeed = speed;
        this.ffspeed = speed*2;
        this.maxHealth = health;
        this.health = health;
        this.mana_gained_on_kill = mana_gained_on_kill;
        this.armor = armor;
        this.pathIndex = 0;
        this.count = -1;
        this.paused = false;
        this.ffed = false;
        this.path = pathToGo;
        this.end = wiz;
        this.mana = mana;
    }
    /**
     * If the enemy position is at the end tile position, then reset the position to the original x and y positions and reset pathIndex to 0.
     * <p>
     * Starting at a pathIndex of 0, iterate through path arraylist, making the current index of the arraylist the current target, calculates distance between enemy and current target and moves according 
     * to the speed towards the target until it reaches. If the current target is reached, pathIndex is incremented and repeats until enemy x and y position matches the end tile x and y positions.
     */
    public void movement() {
        if (this.x == this.end.getX() && this.y == this.end.getY()) {
            this.x = this.startX;
            this.y = this.startY;
            this.pathIndex = 0;
        } else {
            float[] target = path.get(pathIndex); 
    
            float targetX = target[0];
            float targetY = target[1];
    
            float dx = targetX - x;
            float dy = targetY - y;
    
            float distance = app.dist(x, y, targetX, targetY);

            float dxNormalized = dx / distance;
            float dyNormalized = dy / distance;

            x += dxNormalized * this.speed;
            y += dyNormalized * this.speed;

            if (distance < this.speed) {
                x = targetX;
                y = targetY;
    
                pathIndex++; 
            } 
        } 
    }
    /**
     * Displays the enemy if health is not equal to 0.
     * If the health is equal to 0, the enemy stops moving by setting speed to 0, and if not dead, calls die() function. Otherwise, stops displaying.
     */
    public void display() {
        if (health != 0) {
            app.image(this.enemyImg,this.x*32,this.y*32+40);
            drawHealth();
        } else {
            this.speed = 0;
            if (!(isDead())) {
                die();
            }
        }
    }
    /**
     * Checks if current x and y positions are equal to the end tile x and y positions.
     * @return boolean of whether x and y positions match.
     */
    public boolean checkReachHouse() {
        if (this.x == end.getX() && this.y == end.getY()) {
            return true;
        }
        return false;
    }
    /**
     * Draws 2 health bars on top of the enemy sprite to indicate health bar, health mar scales depending on the current health / maximum health.
     */
    public void drawHealth() {
        app.fill(255,0,0);
        app.rect(getX()*32-3,getY()*32+40-7,25,5);
        app.fill(124,252,0);
        app.rect(getX()*32-3,getY()*32+40-7,(getHealth()/getMaxHealth())*25,5);
    }
    /**
     * Returns value of the current health of the enemy.
     * @return float of current health.
     */
    public float getHealth() {
        return this.health;
    }
    /**
     * Returns value of the maximum health of the enemy.
     * @return float of max health.
     */
    public float getMaxHealth() {
        return this.maxHealth;
    }
    /**
     * Returns value of dead attribute.
     * @return boolean if dead or not.
     */
    public boolean isDead() {
        return this.dead;
    }
    /** 
     * Returns the value of the x attribute.
     * @return float of the x value
     */
    public float getX() {
        return this.x;
    }
    /** 
     * Returns the value of the y attribute.
     * @return float of the y value
     */
    public float getY() {
        return this.y;
    }
    /**
     * Returns value of the armor attribute.
     * @return float of the armor value.
     */
    public float getArmour() {
        return this.armor;
    }
    /**
     * Decreases the current health according to value passed. If the current health - value is less than 0, the health is set to 0 instead.
     * @param dmg float value indicating how much to decrease health by.
     */
    public void takeDamage(float dmg) {
        if (health - dmg < 0) {
            health = 0;
            return;
        }
        this.health -= dmg;
    }
    /**
     * Returns true if health is 0, if health is 0, die() function is run and return true. Otherwise returns false.
     * @return true if health equals 0, otherwise false.
     */
    public boolean checkDead() {
        if (health == 0) {
            die();
            return true;
        }
        return false;
    }
    /**
     * Updates the sprite image every 4 frames to display the death animation.
     * Once the death animation is complete, change the dead attribute value to true, and add mana_gained_on_kill attribute to mana reference.
     */
    public void die() {
        if (count >= 0) {
            this.enemyImg = app.loadImage("src/main/resources/WizardTD/gremlin1.png");
        }
        if (count >= 3) {
            this.enemyImg = app.loadImage("src/main/resources/WizardTD/gremlin2.png");
        }
        if (count >= 7) {
            this.enemyImg = app.loadImage("src/main/resources/WizardTD/gremlin3.png");
        }
        if (count >= 11 && count < 15) {
            this.enemyImg = app.loadImage("src/main/resources/WizardTD/gremlin4.png");
        }
        if (count == 15) {
            count = -1;
            this.dead = true;
            mana.add(mana_gained_on_kill);
            return;
        }
        app.image(this.enemyImg,this.x*32,this.y*32+40);
        count++;
    }
    /**
     * Sets the paused value to true, and sets speed to the pausespeed.
     */
    public void pause() {
        this.paused = true;
        this.speed = pausespeed;
    }
    /**
     * Sets the paused value to false, and sets speed to the normal speed.
     */
    public void unpause() {
        this.paused = false;
        this.speed = realspeed;
    }
    /**
     * Sets the ffed value to true, and sets speed to the fast forwarded speed.
     */
    public void ff() {
        this.ffed = true;
        this.speed = ffspeed;

    }
    /**
     * Sets the ffed value to false, and sets speed to the normal speed.
     */
    public void unff() {
        this.ffed = false;
        this.speed = realspeed;
    }
}
