package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;

public class Mana {
    private float amount;
    private float max;
    private boolean empty;
    private PApplet app;
    private float cost;
    private float increasePerUse;
    private float capMultiplier;

    private int upgradeCt = 0;

    /**
     * Constructor called whenever Mana object is created.
     * @param amount float indicating current mana.
     * @param cap float indicating max mana.
     * @param app a reference of PApplet passed to allow this class to call relevant drawing functions.
     * @param cost float indicating cost of manaspell.
     * @param increasePerUse float indicating how much to increase cost by according to how many times upgraded.
     * @param capMultiplier float to multiply cap by whenever manaspell used.
     */
    public Mana(float amount, float cap, PApplet app, float cost, float increasePerUse, float capMultiplier) {
        this.amount = amount;
        this.max = cap;
        this.empty = false;
        this.app = app;
        this.cost = cost;
        this.increasePerUse = increasePerUse;
        this.capMultiplier = capMultiplier;
    }
    /**
     * Displays mana bar using 2 rectangles on top of each other, scaled using current amount / maximum amount.
     * Text on the left that says mana and the number displayed in the middle of the bar.
     */
    public void display() {
        app.textSize(20);
        app.text("MANA:", 255,30);
        app.stroke(0);
        app.fill(240,248,255);
        app.rect(330,10,400,20);
        app.fill(0,191,255);
        app.rect(330,10,(getCurrent()/max)*400,20);//changing one
        app.textSize(15);
        app.fill(0);
        app.text((int)getCurrent() + " / " + (int)max, 480,26);
    }
    /**
     * Returns value of the current amount of mana.
     * @return float of amount value.
     */
    public float getCurrent() {
        return this.amount;
    }
    /**
     * Adds to the current amount depending on the float value passed. If the current value + value to add is greater than the cap, the current value is set to the cap.
     * @param amt float amount to add to current.
     */
    public void add(float amt) {
        if (this.amount+amt > max) {
            this.amount = max;
            return;
        } 
        this.amount += amt;
    }
    /**
     * Decreases the current amount depending on the float value passed. If the current value - value to add is less than 0, the current value is set to 0.
     * @param amt float amount to decrease current.
     */
    public void decrease(float amt) {
        if ((this.amount-amt) < 0.0) {
            this.amount = 0;
            this.empty = true;
            return;
        }
        this.amount -= amt;
    }
    /**
     * If the current amount - the cost of the spell is less than 0, function does nothing. Otherwise, subtracts cost from the current amount, increases cap by the multiplier and increments the upgrade counter.
     */
    public void manaSpell() {
        if (this.amount - (this.cost+this.upgradeCt*this.increasePerUse) < 0) {
            return;
        }
        this.amount -= this.cost + this.upgradeCt * this.increasePerUse;
        this.max = this.max*this.capMultiplier;
        this.upgradeCt++;
    }
    /**
     * Returns the value of the upgrade count.
     * @return int how many times upgraded.
     */
    public int getUpgradeCt() {
        return this.upgradeCt;
    }
    /**
     * Returns the value of the current cost calculated by adding cost and upgrade count multiplied by increase per use amount.
     * @return float value of current cost.
     */
    public float getCost() {
        return this.cost+this.upgradeCt*this.increasePerUse;
    }
}