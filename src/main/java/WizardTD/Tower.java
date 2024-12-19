package WizardTD;

import processing.core.PImage;
import processing.core.PApplet;

import java.util.*;

public class Tower {
    private float x;
    private float y;
    private PImage tower;
    private PApplet app;
    private float range;
    private float firespeed;
    private int initRangeUpgrades;
    private int initSpeedUpgrades;
    private int initDamageUpgrades;
    private float dmg;
    private float initdmg;
    private ArrayList<Enemy> enemies;
    private ArrayList<Fireball> fireballs;
    private float fireballcd;
    private float initfireballcd;
    private boolean paused = false;
    private boolean ffed = false;
    private float upgradeCost = 20;
    private Mana mana;

    private boolean upgradesOn = false;

    /**
     * Constructor called whenever Tower object is created.
     * @param x float indicating x position.
     * @param y float indicating y position.
     * @param app a reference of PApplet passed to allow this class to call relevant drawing functions.
     * @param tower PImage of the sprite.
     * @param range float indicating how far tower can detect enemies.
     * @param firespeed float indicating how often tower shoots.
     * @param dmg float indicating how much to decrease enemy health by if hit.
     * @param enemies reference to enemies Arraylist to interact with enemy health and positions.
     * @param mana reference to mana bar to decrease amount.
     */
    public Tower(float x, float y, PImage tower, PApplet app, float range, float firespeed, float dmg, ArrayList<Enemy> enemies, Mana mana) {
        this.x = x;
        this.y = y;
        this.tower = tower;
        this.app = app;
        this.range = range;
        this.firespeed = firespeed;
        this.dmg = dmg;
        this.initdmg = dmg;
        this.initRangeUpgrades = 0;
        this.initSpeedUpgrades = 0;
        this.initDamageUpgrades = 0;
        this.enemies = enemies;
        this.fireballs = new ArrayList<>();
        this.fireballcd = 0;
        this.initfireballcd = 60/firespeed;
        this.mana = mana;
    }
    /**
     * Displays the tower using x and y positions.
     * Continuously checks if enemy in range using checkEnemyInRange() function.
     * Continously draws upgrades if any using drawUpgrades() function.
     * Checks if paused or ffed, changes the tower fire speed accordingly.
     */
    public void display() {
        app.image(this.tower, this.x*32, this.y*32+8);
        checkEnemyInRange();
        if (paused == true) {
            this.fireballcd += 0;
        } else if (ffed = true) {
            this.fireballcd += 2;
        } else {
            this.fireballcd++;
        }
        drawUpgrades();
    }
    /**
     * This function increases the upgrade based on the number given. For all upgrades, it first checks if the cost is greater than the current mana. 
     * If 1, increases range by 32 pixels or 1 tile. Increments the range upgrades counter. 
     * If 2, increases speed by half a second by dividing 60 by the desired firing speed, then setting a counter that increments 1/result. Increments the speed upgrade counter.
     * if 3, increases damage by half of the initial damage. Increments the damaage upgrade counter.
     * @param option int indicating which upgrade option.
     */
    public void upgrade(int option) {
        switch (option) {
            case 1: //range
                if (upgradeCost + 20f*initRangeUpgrades <= mana.getCurrent()) {
                    mana.decrease(upgradeCost + 10f*initRangeUpgrades);
                    this.initRangeUpgrades++;
                    this.range += 32;
                }
                break;
            case 2: //speed
                if (upgradeCost + 20f*initSpeedUpgrades <= mana.getCurrent()) {
                    mana.decrease(upgradeCost + 10f*initSpeedUpgrades);
                    this.initSpeedUpgrades++;
                    this.firespeed += 0.5;
                    if (initRangeUpgrades < 2){
                        this.initfireballcd = 60/firespeed;
                    }
                    this.fireballcd = this.initfireballcd;
                }
                break;
            case 3: //damage
                if (upgradeCost + 20f*initDamageUpgrades <= mana.getCurrent()) {
                    mana.decrease(upgradeCost + 10f*initDamageUpgrades);
                    this.initDamageUpgrades++;
                    this.dmg += this.initdmg*0.5;
                    
                }
                break;
        }
    }
    /**
     * Creates a yellow circle centerd in the middle of the tower to indicate the range the tower can detect and shoot at enemies.
     */
    public void showRange() {
        app.noFill(); 
        app.stroke(255, 255, 0); 
        app.strokeWeight(2); 
        app.ellipse(this.x*32+16, this.y*32+32-8, range, range);
        app.strokeWeight(1); 

    }
    /**
     * Returns value of the range attribute.
     * @return float of the range value.
     */
    public float getRange() {
        return this.range;
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
     * Checks whether enemy is in range by calculating the enemy x and y position distance from the tower and comparing against the range, if in range, create a fireball object and add to arraylist of fireballs.
     * <p>
     * Iterates through fireball list and if fireball is active, display() and shoot() functions are continuously run.
     */
    public void checkEnemyInRange() {
        if (paused == false) {
            boolean hasTarget = false;
            for (Enemy i : enemies) {
                if (!(i.isDead())) {
                    float distance = app.dist(this.x*32, this.y*32, i.getX()*32+16, i.getY()*32+16);
                    while (distance <= range*2/3 && fireballcd%initfireballcd==0 && !hasTarget) {
                        fireballs.add(new Fireball(this.x, this.y-1, app, app.loadImage("src/main/resources/WizardTD/fireball.png"), 5, i, this.dmg));
                        hasTarget = true; 
                    }
                }
                
            }
            for (Fireball j : fireballs) {
                if (j.getActive()) {
                    j.display();
                    j.shoot();
                }
            }
        }
        
    }
    /**
     * Sets the paused value to true, and calls pause function in every fireball.
     */
    public void pause() {
        paused = true;
        for (Fireball i : fireballs) {
            i.pause();
        }
    }
    /**
     * Sets the paused value to false, and calls unpause function in every fireball.
     */
    public void unpause() {
        paused = false;
        for (Fireball i : fireballs) {
            i.unpause();
        }
    }
    /**
     * Sets the ffed value to true, and calls ff function in every fireball.
     */
    public void ff() {
        for (Fireball i : fireballs) {
            i.ff();
        }
    }
    /**
     * Sets the ffed value to false, and calls unff function in every fireball.
     */
    public void unff() {
        for (Fireball i : fireballs) {
            i.unff();
        }
    }
    /**
     * Sets upgradesOn attribute to true.
     */
    public void turnOnUpgrade() {
        this.upgradesOn = true;
    }
    /**
     * Sets upgradesOn attribute to false.
     */
    public void turnOffUpgrade() {
        this.upgradesOn = false;
    }
    /**
     * Returns value of the upgradesOn attribute.
     * @return boolean of upgradesOn value.
     */
    public boolean upgradeable() {
        return this.upgradesOn;
    }
    /**
     * Checks amount of upgrades of range, speed, damage.
     * Draws circles on the top for every range upgrade, blue square which thickens per speed upgrade, and x for every damage upgrade at the bottom.
     * If all are greater than 0, sprite is upgraded to lvl 2 tower, and amount of circles,border,x drawn are subtracted by 1.
     * If all are greater than 1, sprite is upgraded to lvl 3 tower, and amount of circles,border,x drawn are subtracted by 2.
     */
    public void drawUpgrades() {
        if (initRangeUpgrades > 1 && initSpeedUpgrades > 1 && initDamageUpgrades > 1) {
            this.tower = app.loadImage("src/main/resources/WizardTD/tower2.png");
            for (int i = 0; i < initRangeUpgrades-2; i++) {
                circle(this.x, this.y, i*8f);
            }
            for (int i = 0; i < initSpeedUpgrades-2; i++) {
                blueBorder(this.x, this.y, i+1);
            }
            for (int i = 0; i < initDamageUpgrades-2; i++) {
                xmark(this.x, this.y, i*8f);
            }
        } else if (initRangeUpgrades > 0 && initSpeedUpgrades > 0 && initDamageUpgrades > 0) {
            this.tower = app.loadImage("src/main/resources/WizardTD/tower1.png");
            for (int i = 0; i < initRangeUpgrades-1; i++) {
                circle(this.x,this.y, i*8f);
            }
            for (int i = 0; i < initSpeedUpgrades-1; i++) {
                blueBorder(this.x, this.y, i+1);
            }
            for (int i = 0; i < initDamageUpgrades-1; i++) {
                xmark(this.x, this.y, i*8f);
            }
        } else {
            for (int i = 0; i < initRangeUpgrades; i++) {
                circle(this.x,this.y, i*8f);
            }
            for (int i = 0; i < initSpeedUpgrades; i++) {
                blueBorder(this.x, this.y, i+1);
            }
            for (int i = 0; i < initDamageUpgrades; i++) {
                xmark(this.x, this.y, i*8f);
            }
        }
    }
    /**
     * Draws the circles for the upgrades on the tower. Making each circle next to each other by using the offset.
     * @param x float indicating x position.
     * @param y float indicating y position.
     * @param offset float indicating how much to move right.
     */
    public void circle(float x, float y, float offset) {
        app.noFill();
        app.stroke(200, 0, 200);
        app.ellipse(x*32 + offset, y*32+10, 6, 6);
    }
    /**
     * Draws the blue squares for the upgrades on the tower. Making each upgrade have a thicker blue square by the thickness.
     * @param x float indicating x position.
     * @param y float indicating y position.
     * @param thick int indicating how thick the stroke should be.
     */
    public void blueBorder(float x, float y, int thick) {
        app.noFill();
        app.stroke(173, 216, 230);
        app.strokeWeight(thick);
        app.rect(x*32 + 6, y*32 + 15, 19, 19);
    }
    /**
     * Draws the x for the upgrades on the tower. Making each x next to each other by using the offset.
     * @param x float indicating x position.
     * @param y float indicating y position.
     * @param offset float indicating how much to move right.
     */
    public void xmark(float x, float y, float offset) {
        app.noFill();
        app.stroke(200, 0, 200);
        app.strokeWeight(2);
        app.line(x*32-2 + offset, y*32+37-2, x*32+2 + offset, y*32+37+2);
        app.line(x*32+2 + offset, y*32+37-2, x*32-2 + offset, y*32+37+2);
    }
    /**
     * Returns the value of the initRangeUpgrades attribute.
     * @return amount of times range has been upgraded.
     */
    public int getRangeUpgrades() {
        return this.initRangeUpgrades;
    }
    /**
     * Returns the value of the initSpeedUpgrades attribute.
     * @return amount of times speed has been upgraded.
     */
    public int getSpeedUpgrades() {
        return this.initSpeedUpgrades;
    }
    /**
     * Returns the value of the initDamageUpgrades attribute.
     * @return amount of times damage has been upgraded.
     */
    public int getDamageUpgrades() {
        return this.initDamageUpgrades;
    }
}

