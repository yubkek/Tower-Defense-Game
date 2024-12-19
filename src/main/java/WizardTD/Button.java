package WizardTD;

import processing.core.PApplet;

public class Button {
    private final float x = 650;
    private float y;
    private PApplet app;
    private String text;
    private String desc;
    private boolean pressed = false;
    private Mana mana;
    /**
     * Constructor called whenever Button object is created.
     * @param y a float indicating y position of button.
     * @param app a reference of PApplet passed to allow this class to call relevant drawing functions such as rect().
     * @param text a string of the text to be put inside the button.
     * @param desc a string of the text to be put outside the button.
     */
    public Button(float y, PApplet app, String text, String desc) {
        this.y = y;
        this.app = app;
        this.text = text;
        this.desc = desc;
    }
    /**
     * Constructor called whenever Button object for manaspell button is created.
     * @param y a float indicating y position of button.
     * @param app a reference of PApplet passed to allow this class to call relevant drawing functions such as rect().
     * @param text a string of the text to be put inside the button.
     * @param mana a Mana object reference of the manabar to allow manaspell button to access some methods in Mana class.
     */
    public Button(float y, PApplet app, String text, Mana mana) {
        this.y = y;
        this.app = app;
        this.text = text;
        this.desc = desc;
        this.mana = mana;
    }
    /**
     * Checks if the button is pressed, if true, the button is displayed as the highlighted version instead using the highlighted() function.
     * <p>
     * Otherwise, checks if the button is the manaspell button by seeing if the mana attribute is null. If null, that means the button is not a manaspell button. 
     * This displays the button using rect() to create border, the object x and y positions, and places text inside the button and the description outside. 
     * If it is the manaspell button, it displays the text, but the outside description uses a getCost() method from the Mana class to update the cost of the spell in the display.
     */
    public void display() {
        if (pressed) {
            highlighted();
        } else if (this.mana == null) {
            app.noFill();
            app.textSize(35);
            app.strokeWeight(2);
            app.rect(this.x,this.y,50,50); //box
            app.text(text, this.x + 3, this.y + 40); //text in box
            app.textSize(11);
            app.text(desc, this.x + 53, this.y + 25); //text outside box
            app.strokeWeight(1);
        } else {
            app.noFill();
            app.textSize(35);
            app.strokeWeight(2);
            app.rect(this.x,this.y,50,50); //box
            app.text(text, this.x + 3, this.y + 40); //text in box
            app.textSize(11);
            app.text("Mana Pool\nCost: " + (int)mana.getCost(), this.x + 53, this.y + 25); //text outside box
            app.strokeWeight(1);
        }
    }
    /**
     * Is run if the button is pressed or hovered over.
     * <p>
     * Functions similar to display, only creates another yellow box with same dimensions to indicate a highlighted state.
     */
    public void highlighted() {
        if (this.mana == null) {
            app.fill(255, 255, 0);
            app.rect(this.x,this.y,50,50);
            app.fill(0);
            app.noFill();
            app.textSize(35);
            app.strokeWeight(2);
            app.rect(this.x,this.y,50,50); //box
            app.text(text, this.x + 3, this.y + 40); //text in box
            app.textSize(11);
            app.text(desc, this.x + 53, this.y + 25); //text outside box
            app.strokeWeight(1);
        } else {
            app.fill(255, 255, 0);
            app.rect(this.x,this.y,50,50);
            app.fill(0);
            app.noFill();
            app.textSize(35);
            app.strokeWeight(2);
            app.rect(this.x,this.y,50,50); //box
            app.text(text, this.x + 3, this.y + 40); //text in box
            app.textSize(11);
            app.text("Mana Pool\nCost: " + (int)mana.getCost(), this.x + 53, this.y + 25); //text outside box
            app.strokeWeight(1);
        }
        

    }
    /**
     * Returns the value of the type attribute.
     * @return string of the button type.
     */
    public String getType() {
        return this.text;
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
     * Sets the pressed attribute to true.
     */
    public void press() {
        this.pressed = true;
    }
    /**
     * Sets the pressed attribute to false.
     */
    public void unpress() {
        this.pressed = false;
    }
    /**
     * Returns the value of the pressed attribute.
     * @return boolean indicating whether button is pressed.
     */
    public boolean isPressed() {
        return this.pressed;
    }
    
}
