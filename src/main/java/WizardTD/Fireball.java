package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Fireball {
    private float x; 
    private float y;
    private PApplet app;
    private PImage fbImg;
    private int speed;
    private int pausedspeed = 0;
    private int ffspeed;
    private int realspeed;
    private boolean isActive;
    private Enemy toTrack;
    private float dmg;

    /**
     * Constructor called whenever Fireball object is created.
     * @param x float indicating x position.
     * @param y float indicating y position.
     * @param app a reference of PApplet passed to allow this class to call relevant drawing functions.
     * @param fbImg PImage of the sprite.
     * @param speed float indicating movement speed of the fireball.
     * @param toTrack reference of the Enemy object that this fireball is aiming at.
     * @param dmg float indicating how much to reduce enemy health by if hit.
     */
    public Fireball(float x, float y, PApplet app, PImage fbImg, int speed, Enemy toTrack, float dmg) {
        this.x = x;
        this.y = y;
        this.app = app;
        this.fbImg = fbImg;
        this.speed = speed;
        this.isActive = true;
        this.toTrack = toTrack;
        this.dmg = dmg;
        this.realspeed = speed;
        this.ffspeed = speed;

    }
    /**
     * First checks if the fireball has reached the target and hit, then sets target to the enemy x and y position using the Enemy class getter functions.
     * Then calculates the direction and then moves towards the target at the speed set by the speed attribute, by continuously changing the x and y positions.
     */
    public void shoot() {   
        reachTarget();
        checkHit();
        float targetX = toTrack.getX();
        float targetY = toTrack.getY();
        
        // Calculate the direction to the target
        float dx = targetX - x;
        float dy = targetY - y;
        float distance = app.dist(x*32, y*32, targetX*32, targetY*32);
        
        // Calculate the tracking velocity
        float vx = (dx / distance) * speed;
        float vy = (dy / distance) * speed;
        
        // Update the fireball's position
        x += vx;
        y += vy;
    }
    /**
     * Displays the fireball image at its x and y positions as long as the enemy it is tracking is not dead by checking using the isDead() function in the Enemy class.
     */
    public void display() { 
        if (!(toTrack.isDead())) {
            app.image(this.fbImg,this.x*32+8,this.y*32+48);
        }
    }
    /**
     * Checks whether the fireball x and y position has reached the enemy sprite position.
     * @return true if x and y positions match, otherwise false.
     */
    public boolean reachTarget() {
        if (this.x >= toTrack.getX() && this.x <= toTrack.getX()+32 && this.y >= toTrack.getY() && this.y <= toTrack.getY()+32) {
            this.isActive = false;
            return true;
        }
        return false;
    }
    /**
     * Calls reachTarget() function, if true, causes enemy to take dmg according to the fireball dmg multiplied by the enemy armor, obtained using Enemy class getArmour() function.
     * Otherwise, does nothing.
     */
    public void checkHit() {
        if (reachTarget()) {
            toTrack.takeDamage(dmg*toTrack.getArmour());
        }
    }
    /**
     * Returns the value of the isActive attribute.
     * @return boolean of fireball isActive value.
     */
    public boolean getActive() {
        return this.isActive;
    }
    /**
     * Sets the paused value to true, and sets speed to the pausespeed.
     */
    public void pause() {
        this.speed = pausedspeed;
    }
    /**
     * Sets the paused value to false, and sets speed to the normal speed.
     */
    public void unpause() {
        this.speed = realspeed;
    }
    /**
     * Sets the ffed value to true, and sets speed to the fast forwarded speed.
     */
    public void ff() {
        this.speed = ffspeed;
    }
    /**
     * Sets the ffed value to false, and sets speed to the normal speed.
     */
    public void unff() {
        this.speed = realspeed;
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
}
