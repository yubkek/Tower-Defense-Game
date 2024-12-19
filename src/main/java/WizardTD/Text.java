package WizardTD;

import processing.core.PApplet;

public class Text {
    private int x = 5;
    private int y = 30;
    private int waveNum;
    private float cd;
    private PApplet app;

    /**
     * Constructor called whenever Text object is created.
     * @param waveNum int indicating the wave number.
     * @param cd float of the countdown time.
     * @param app a reference of PApplet passed to allow this class to call relevant drawing functions.
     */
    public Text(int waveNum, float cd, PApplet app) {
        this.waveNum = waveNum;
        this.cd = cd;
        this.app = app;
    }
    /**
     * Displays the wave number and countdown, and countdown is decreased by 1/60 every frame so every second shows a decrease of 1.
     */
    public void display() {
        if (cd > 0) {
            cd -= 1/60f;
        }
        app.textSize(25);
        app.stroke(2);
        app.text("Wave " + waveNum + " starts: " + (int)cd, 5,29);
    }
}
