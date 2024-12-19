package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.MouseEvent;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.*;


public class App extends PApplet {

    public static final int CELLSIZE = 32;
    public static final int SIDEBAR = 120;
    public static final int TOPBAR = 40;
    public static final int BOARD_WIDTH = 20;

    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE+TOPBAR;

    public static final int FPS = 60;

    public String configPath;

    public Random random = new Random();
	
	// Feel free to add any additional methods or attributes you want. Please put classes in different files.
    private boolean paused;
    private boolean ffed;
    private char[][] level;
    private ArrayList<Tile> tiles;
    private ArrayList<Tower> towers;
    private ArrayList<Enemy> enemies;
    private ArrayList<Fireball> fireballs;
    private ArrayList<Button> buttons;
    private Mana mana;

    private boolean buildTower = false;
    private boolean towerbuilt = false;
    private boolean buildTowerWithButton = false;
    private boolean canRelease = false;

    private String levelfile;
    private int countDown; 
    private int waveNumber;

    private PathMake pm;
    private ArrayList<float[]> pathToGo;

    private ArrayList<float[]> pathToGo21;
    private ArrayList<float[]> pathToGo22;

    private ArrayList<float[]> pathToGo31;
    private ArrayList<float[]> pathToGo32;
    private ArrayList<float[]> pathToGo33;
    private ArrayList<float[]> pathToGo34;

    private ArrayList<float[]> pathToGo41;
    private ArrayList<float[]> pathToGo42;
    private ArrayList<float[]> pathToGo43;

    private Wizard wzLocation;

    //starting positions
    private int[] l1 = new int[] {-3,1};

    private int[] l2x1 = new int[] {-3,1};
    private int[] l2x2 = new int[] {9,-1};

    private int[] l3x1 = new int[] {16,-1};
    private int[] l3x2 = new int[] {20,5};
    private int[] l3x3 = new int[] {6,20};
    private int[] l3x4 = new int[] {14,20};

    private int[] l4x1 = new int[] {-1,8};
    private int[] l4x2 = new int[] {9,-1};
    private int[] l4x3 = new int[] {20,5};

    private Button pauseButton;
    private Button fastForwardButton;
    private Button buildTowerButton;
    private Button upgradeSpeedButton;
    private Button upgradeRangeButton;
    private Button upgradeDamageButton;
    private Button manaSpellButton;

    private Text toptext;

    // from json
    private JSONObject json;

    private String leveltext;
    private float initial_tower_range;
    private float initial_tower_firing_speed;
    private float initial_tower_damage;
    private float initial_mana;
    private float initial_mana_cap;
    private float initial_mana_gained_per_second;
    private float tower_cost;
    private float mana_pool_spell_initial_cost;
    private float mana_pool_spell_cost_increase_per_use;
    private float mana_pool_spell_cap_multiplier;
    private float mana_pool_spell_mana_gained_multiplier;

    private String enemy_type;
    private float monster_hp;
    private float monster_speed;
    private float monster_armour;
    private float mana_gained_on_kill;
    private int toSpawn;

    private boolean won;
    private boolean lost;

    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
     */
	@Override
    public void setup() {
        frameRate(FPS);

        //readJSON("config.json");
        json = loadJSONObject("config.json");
        readJSON(); 

        paused = false;
        ffed = false;
        level = new char[20][20];
        tiles = new ArrayList<>();
        towers = new ArrayList<>();
        enemies = new ArrayList<>();
        fireballs = new ArrayList<>();
        buttons = new ArrayList<>();
        mana = new Mana(this.initial_mana, this.initial_mana_cap, this, this.mana_pool_spell_initial_cost, this.mana_pool_spell_cost_increase_per_use, this.mana_pool_spell_cap_multiplier);
        won = false;
        lost = false;

        //making map 
        levelfile = leveltext;
        readFile(levelfile);
        makeMap();
        makeWizard();
        
        //title cd 
        toptext = new Text(1,5,this);
        
        // Load images during setup
		// Eg:
        // loadImage("src/main/resources/WizardTD/tower0.png");
        // loadImage("src/main/resources/WizardTD/tower1.png");
        // loadImage("src/main/resources/WizardTD/tower2.png");

        pm = new PathMake();
        pathToGo = pm.level1path();
        
        pathToGo21 = pm.level2path1();
        pathToGo22 = pm.level2path2();

        pathToGo31 = pm.level3path1();
        pathToGo32 = pm.level3path2();
        pathToGo33 = pm.level3path3();
        pathToGo34 = pm.level3path4();

        pathToGo41 = pm.level4path1();
        pathToGo42 = pm.level4path2();
        pathToGo43 = pm.level4path3();

        this.wzLocation = findWiz(tiles);

        //make buttons
        this.fastForwardButton = new Button(40, this, "FF", "2x speed");
        this.pauseButton = new Button(100, this, "P", "pause");
        this.buildTowerButton = new Button(160, this, "T", "build\ntower");
        this.upgradeRangeButton = new Button(220, this, "U1", "upgrade\nrange");
        this.upgradeSpeedButton = new Button(280, this, "U2", "upgrade\nspeed");
        this.upgradeDamageButton = new Button(340, this, "U3", "upgrade\ndamage");
        this.manaSpellButton = new Button(400, this, "M", mana);
        buttons.add(pauseButton);
        buttons.add(fastForwardButton);
        buttons.add(buildTowerButton);
        buttons.add(upgradeRangeButton);
        buttons.add(upgradeSpeedButton);
        buttons.add(upgradeDamageButton);
        buttons.add(manaSpellButton);
        
        spawn(this.toSpawn);
    }
    /**
     * This method is used to find the wizard house to indicate the last tile of the path. 
     * 
     * @param list an arraylist of Tile that contains all tiles present on the board.
     * @return A wizard object that extends from Tile.
     */
    public Wizard findWiz(ArrayList<Tile> list) {
        for (Tile i : list) {
            if (i instanceof Wizard) {
                Wizard wiz = (Wizard) i;
                return wiz;
            }
        }
        return null;
    }


    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(){
        //for test only 
        if (key == 'L' || key == 'l') {
            mana.add(1000);
            System.out.println(initial_tower_range);
        }
        if (key == 'T' || key == 't') {
            if (buildTowerButton.isPressed()) {
                buildTowerButton.unpress();
                buildTower = false;
            } else {
                buildTowerButton.press();
                buildTower = true;
            }
        }
        if (key == 'M' || key == 'm') {
            manaSpellButton.press();
            mana.manaSpell();
        }
        if (key == 'P' || key == 'p') {
            paused = !paused;
            if (pauseButton.isPressed()) {
                pauseButton.unpress();
            } else {
                pauseButton.press();
            }
            checkPaused();
        }
        if (key == 'F' || key == 'f') {
            ffed = !ffed;
            if (fastForwardButton.isPressed()) {
                fastForwardButton.unpress();
            } else {
                fastForwardButton.press();
            }
            checkffed();
        }
        if (key == '1') {
            if (upgradeRangeButton.isPressed()) {
                upgradeRangeButton.unpress();
            } else {
                upgradeRangeButton.press();
            }
        }
        if (key == '2') {
            if (upgradeSpeedButton.isPressed()) {
                upgradeSpeedButton.unpress();
            } else {
                upgradeSpeedButton.press();
            }
        }
        if (key == '3') {
            if (upgradeDamageButton.isPressed()) {
                upgradeDamageButton.unpress();
            } else {
                upgradeDamageButton.press();
            }
        }
        
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){
        if (manaSpellButton.isPressed()) {
            manaSpellButton.unpress();
        }
    }
    /**
     * This method is called whenever a mouse button is pressed.
     * <p>
     * Handles all button functionality as well as tower placing by comparing the target X and Y positions with the mouse x and y positions.
     * 
     * @param e a MouseEvent object that has getters for X and Y of the mouse position.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        //making tower
        if (buildTower == true) {
            for (Tile i : tiles) {
                if (e.getX()/32 == i.getX() && (e.getY()-40)/32 == i.getY()) {
                    if (i.towerAble() && mana.getCurrent() >= 100) {
                        towers.add(new Tower((e.getX()/32),(e.getY()/32),loadImage("src/main/resources/WizardTD/tower0.png"),this, this.initial_tower_range, this.initial_tower_firing_speed, this.initial_tower_damage, enemies, mana));
                        i.placed();
                        towerbuilt = true;
                        buildTowerButton.unpress();
                    }
                }
            }
        } 
        //press tower button
        if (e.getX() >= 650 && e.getX() <= 700 && e.getY() >= 160 && e.getY() <= 210) {
            buildTowerWithButton = true;
            if (buildTowerButton.isPressed()) {
                buildTowerButton.unpress();
            } else {
                buildTowerButton.press();
            }
        }
        if (buildTowerWithButton == true && buildTowerButton.isPressed()) {
            for (Tile i : tiles) {
                if (e.getX()/32 == i.getX() && (e.getY()-40)/32 == i.getY()) {
                    if (i.towerAble() && mana.getCurrent() >= 100) {
                        towers.add(new Tower((e.getX()/32),(e.getY()/32),loadImage("src/main/resources/WizardTD/tower0.png"),this, this.initial_tower_range, this.initial_tower_firing_speed, this.initial_tower_damage, enemies, mana));
                        i.placed();
                        towerbuilt = true;
                        canRelease = true;
                        buildTowerButton.unpress();
                    }
                }
            }
        }
        
        //press pause button
        if (e.getX() >= 650 && e.getX() <= 700 && e.getY() >= 100 && e.getY() <= 150) {
            paused = !paused;
            checkPaused();
            if (pauseButton.isPressed()) {
                pauseButton.unpress();
            } else {
                pauseButton.press();
            }
        }
        //press ff button
        if (e.getX() >= 650 && e.getX() <= 700 && e.getY() >= 40 && e.getY() <= 90) {
            ffed = !ffed;
            checkffed();
            if (fastForwardButton.isPressed()) {
                fastForwardButton.unpress();
            } else {
                fastForwardButton.press();
            }
        }
        //press mana spell button
        if (e.getX() >= 650 && e.getX() <= 700 && e.getY() >= 400 && e.getY() <= 450) {
            mana.manaSpell();
        }

        //real uppgarde range
        if (e.getX() >= 650 && e.getX() <= 700 && e.getY() >= 220 && e.getY() <= 270) {
            if (upgradeRangeButton.isPressed()) {
                upgradeRangeButton.unpress();
            } else {
                upgradeRangeButton.press();
            }
        } 
        //real upgrade speed
        if (e.getX() >= 650 && e.getX() <= 700 && e.getY() >= 280 && e.getY() <= 330) {
            if (upgradeSpeedButton.isPressed()) {
                upgradeSpeedButton.unpress();
            } else {
                upgradeSpeedButton.press();
            }
        } 
        //real upgrade dmg
        if (e.getX() >= 650 && e.getX() <= 700 && e.getY() >= 340 && e.getY() <= 390) {
            if (upgradeDamageButton.isPressed()) {
                upgradeDamageButton.unpress();
            } else {
                upgradeDamageButton.press();
            }
        } 
        //upgrades
        for (Tower i : towers) {
            if (e.getX() >= i.getX()*32 && e.getX() <= i.getX()*32+32 && e.getY() >= i.getY()*32 && e.getY() <= i.getY()*32+32) {
                if (upgradeRangeButton.isPressed()) {
                    i.upgrade(1);
                    upgradeRangeButton.unpress();
                }
                if (upgradeSpeedButton.isPressed()) {
                    i.upgrade(2);
                    upgradeSpeedButton.unpress();
                }
                if (upgradeDamageButton.isPressed()) {
                    i.upgrade(3);
                    upgradeDamageButton.unpress();
                }
            }
        }

    }

    /**
     * This method is called whenever a mouse button is released.
     * <p>
     * Makes sure that when functionality completed by mousePressed() is complete and does not run again unless button is pressed again.
     * 
     * @param e a MouseEvent object that has getters for X and Y of the mouse position
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        //tower built by key 
        if (buildTower == true) {
            if (towerbuilt) {
                mana.decrease(tower_cost);
                towerbuilt = false;
            }
            buildTower = false;
        }
        //tower built by button
        if (buildTowerWithButton == true && canRelease == true) {
            if (towerbuilt) {
                mana.decrease(tower_cost);
                towerbuilt = false;
            }
            buildTowerWithButton = false;
            canRelease = false;
        }
    }

    /*@Override
    public void mouseDragged(MouseEvent e) {

    }*/
   
    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {
        background(150, 105, 25);
        drawMap();
        fixPaths();

        //checkWL
        endText();
        
        //enemies
        enemyDisplay();

        //towers
        towerDisplay();
        endText();
        //manabar 
        manaDisplay();

        //wizard on top
        drawWizard();

        //buttons
        drawButtons();

        //wave + cd
        toptext.display();

    }
    /**
     * Checks the paused variable if true or not, if true, calls the pause function in both tower and enemy lists. Setting all movement speeds to 0.
     * <p> 
     * If paused variable is false, calls the unpause function in both tower and enemy lists. Resumes normal movement.
     */
    public void checkPaused() {
        for (Enemy i : enemies) {
            if (paused) {
                i.pause();
            } else {
                i.unpause();
            }
        }
        for (Tower j : towers) {
            if (paused) {
                j.pause();
            } else {
                j.unpause();
            }
        }
    }
    /**
     * Checks the ffed variable if true or not, if true, calls the ff function in both tower and enemy lists. Setting all movement speeds to double.
     * <p> 
     * If paused ffed is false, calls the unff function in both tower and enemy lists. Resumes normal movement.
     */
    public void checkffed() {
        for (Enemy i : enemies) {
            if (ffed) {
                i.ff();
            } else {
                i.unff();
            }
        }
        for (Tower j : towers) {
            if (paused) {
                j.ff();
            } else {
                j.unff();
            }
        }
    }
    /**
     * Displays the manabar at the top right corner of the screen. Handles the constant mana gain by constantly checking whether game is paused or fast forwarded, where it will either double or pause gain amount. 
     * Otherwise maintains normal mana gain rate.
     */
    public void manaDisplay() {
        noFill();
        noStroke();
        fill(150, 105, 25);
        rect(-1,0,24*32,40);
        fill(0,0,0);
        mana.display();
        float cd = 60/initial_mana_gained_per_second;
        if (!won && !lost) {
            if (mana.getUpgradeCt() > 0) {
            cd = 60/(initial_mana_gained_per_second*mana_pool_spell_mana_gained_multiplier*mana.getUpgradeCt());
            }
            if (!paused && !ffed) {
                mana.add(1/cd);
            } else if (ffed) {
                mana.add(2/cd);
            } else if (paused) {
                mana.add(0/60f);
            }
        }
        
       
    }
    /**
     * Displays the towers on the map, as well as shows range indicator whenever mouse is hovered over the tower location.
     * <p>
     * If upgrades are selected, a tooltip on the bottom right of the screen will also be displayed, showing each cost of the upgrades and the total cost.
     */
    public void towerDisplay() {
        fill(150, 105, 25);
        noStroke();
        rect(640, 0, 300, 500);
        for (Tower i : towers) {
            int totalCost = 0;
            i.display();
            if (mouseX/32 == i.getX() && (mouseY)/32 == i.getY()) {
                i.showRange();
                if (upgradeRangeButton.isPressed() || upgradeSpeedButton.isPressed() || upgradeDamageButton.isPressed()) {
                    textSize(17);
                    stroke(0);
                    fill(255,255,255);
                    rect(650, 500, 90, 140);
                    fill(0,0,0);
                    text("Upgrades", 655, 520);
                    textSize(13);
                    if (upgradeRangeButton.isPressed()) {
                        text("Range: " + (int)(20+(i.getRangeUpgrades()*10)), 655, 545);
                        totalCost+=(int)(20+(i.getRangeUpgrades()*10));
                    } 
                    if (upgradeSpeedButton.isPressed()) {
                        text("Speed: " + (int)(20+(i.getSpeedUpgrades()*10)), 655, 570);
                        totalCost+=(int)(20+(i.getSpeedUpgrades()*10));
                    } 
                    if (upgradeDamageButton.isPressed()) {
                        text("Damage: " + (int)(20+(i.getDamageUpgrades()*10)), 655, 595);
                        totalCost+=(int)(20+(i.getDamageUpgrades()*10));
                    }
                    textSize(17);
                    text("Total: "+totalCost, 655, 630);
                }
            }
        }
    }
    /**
     * Displays the enemies on the screen by iterating through enemies arraylist and constantly calling the movement() and display() functions within the Enemy object.
     * <P>
     * If the Enemy object reaches the target location, also decreases the mana bar by the monster current health, obtained with getter getHealth.
     */
    public void enemyDisplay() {
        noFill();
        noStroke();
        for (Enemy i : enemies) {
            i.display();
            i.movement();
            if (i.checkReachHouse()) {
                mana.decrease(i.getHealth());
            }
        }
    }
    /**
     * Runs the program when gradle run is entered.
     * @param args String array of command line.
     */
    public static void main(String[] args) {
        PApplet.main("WizardTD.App");
    }

    /**
     * Source: https://stackoverflow.com/questions/37758061/rotate-a-buffered-image-in-java
     * @param pimg The image to be rotated
     * @param angle between 0 and 360 degrees
     * @return the new rotated image
     */
    public PImage rotateImageByDegrees(PImage pimg, double angle) {
        BufferedImage img = (BufferedImage) pimg.getNative();
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        PImage result = this.createImage(newWidth, newHeight, ARGB);
        //BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        BufferedImage rotated = (BufferedImage) result.getNative();
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                result.set(i, j, rotated.getRGB(i, j));
            }
        }

        return result;
    }
    /**
     * Reads the text file and edits the 2d array "level" which stores the different characters of the text file indicating the different tiles on the map to be displayed.
     * @param file a string containing the name of the text file to be read
     */
    public void readFile(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
    
            ArrayList<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
    
            for (int i = 0; i < 20; i++) {
                String currentLine = lines.get(i);
                for (int j = 0; j < 20 && j < currentLine.length(); j++) {
                    level[i][j] = currentLine.charAt(j);
                }
            }

            reader.close();
        } catch (Exception e) {
            System.out.println("WHERE");
        }
    }
    /**
     * Reads through the 2d array "level" and creates and adds to an arraylist tiles of type Tile. Different characters add a different type of tile as well as store the x and y positions of the tile as found in the array.
     */
    public void makeMap() {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                char temp = level[j][i];
                
                switch(temp) {
                    case 'S':
                        tiles.add(new Shrub(i,j,this,loadImage("src/main/resources/WizardTD/shrub.png"),false,false));
                        break;

                    case 'X':            
                        tiles.add(new Path(i,j,this,loadImage("src/main/resources/WizardTD/path0.png"),false,true));
                        break;

                    case 'W':
                        tiles.add(new Wizard(i,j,this,loadImage("src/main/resources/WizardTD/wizard_house.png"),false,false));
                        break;

                    default:
                        tiles.add(new Grass(i,j,this,loadImage("src/main/resources/WizardTD/grass.png"),true,false));
                        break;
                }
            }
        }
    }
    /** 
     * Reads through 2d array "level" and creates and adds a Wizard object to the tiles arraylist. 
     */
    public void makeWizard() {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                char temp = level[j][i];
                
                switch(temp) {
                    case 'W':
                        tiles.add(new Wizard(i,j,this,loadImage("src/main/resources/WizardTD/wizard_house.png"),false,false));
                        break;
                }

            }
        }
    }
    /**
     * Iterates through the Tile arraylist tiles and loads all the tiles except wizard's image onto the screen using the tiles x and y attributes.
     */
    public void drawMap() {
        for (Tile i : tiles) {
            if (!(i instanceof Wizard)) {
                i.display();
            }
        }
    }
    /**
     * Iterates through the Tile arraylist tiles and loads only the wizard image onto the screen using the tiles x and y attributes.
     * <p>
     * Allows for the wizard tile to able be loaded on top of enemies.
     */
    public void drawWizard() {
        for (Tile i : tiles) {
            if (i instanceof Wizard) {
                i.display();
            }
        }
    }
    /**
     * Creates and adds the enemies to be spawned into the arraylist enemies of type Enemy. Depending on the level loaded, creates a random amount to be loaded into each starting position. 
     * @param amt the amount that needs to be spawned in total.
     */
    public void spawn(int amt) {
        float startX = 0;
        float startY = 0;
        if (levelfile.equals("level1.txt")) {
            startX = l1[0];
            startY = l1[1];
            for (int i = 0; i < amt; i++) {
                enemies.add(new Gremlin(startX,startY,this,loadImage("src/main/resources/WizardTD/gremlin.png"),this.monster_speed,this.monster_hp,this.mana_gained_on_kill,this.monster_armour, pathToGo, wzLocation, mana));
                startX-=1;
            }
        }
        int target = amt; 
        int maxRandomNumber = target - 1;
        int number1 = random.nextInt(maxRandomNumber) + 1;
        int number2 = target - number1;
        if (levelfile.equals("level2.txt")) {
            startX = l2x1[0];
            startY = l2x1[1];
            for (int i = 0; i < number1; i++) {
                enemies.add(new Gremlin(startX,startY,this,loadImage("src/main/resources/WizardTD/gremlin.png"),this.monster_speed,this.monster_hp,this.mana_gained_on_kill,this.monster_armour, pathToGo21, wzLocation, mana));
                startX-=1;
            }
        }
        if (levelfile.equals("level2.txt")) {
            startX = l2x2[0];
            startY = l2x2[1];
            for (int i = 0; i < number2; i++) {
                enemies.add(new Gremlin(startX,startY,this,loadImage("src/main/resources/WizardTD/gremlin.png"),this.monster_speed,this.monster_hp,this.mana_gained_on_kill,this.monster_armour, pathToGo22, wzLocation, mana));
                startY-=1;
            }
        }
        target = amt;
        maxRandomNumber = target - 1;
        number1 = random.nextInt(maxRandomNumber) + 1;
        int remaining1 = target - number1;
        
        number2 = random.nextInt(remaining1 - 2) + 1;
        int remaining2 = remaining1 - number2;
        
        int number3 = random.nextInt(remaining2 - 1) + 1;
        int number4 = remaining2 - number3;
        if (levelfile.equals("level3.txt")) {
            startX = l3x1[0];
            startY = l3x1[1];
            for (int i = 0; i < number1; i++) {
                enemies.add(new Gremlin(startX,startY,this,loadImage("src/main/resources/WizardTD/gremlin.png"),this.monster_speed,this.monster_hp,this.mana_gained_on_kill,this.monster_armour, pathToGo31, wzLocation, mana));
                startY-=1;
            }
        }
        if (levelfile.equals("level3.txt")) {
            startX = l3x2[0];
            startY = l3x2[1];
            for (int i = 0; i < number2; i++) {
                enemies.add(new Gremlin(startX,startY,this,loadImage("src/main/resources/WizardTD/gremlin.png"),this.monster_speed,this.monster_hp,this.mana_gained_on_kill,this.monster_armour, pathToGo32, wzLocation, mana));
                startX+=1;
            }
        }
        if (levelfile.equals("level3.txt")) {
            startX = l3x3[0];
            startY = l3x3[1];
            for (int i = 0; i < number3; i++) {
                enemies.add(new Gremlin(startX,startY,this,loadImage("src/main/resources/WizardTD/gremlin.png"),this.monster_speed,this.monster_hp,this.mana_gained_on_kill,this.monster_armour, pathToGo33, wzLocation, mana));
                startY+=1;
            }
        }
        if (levelfile.equals("level3.txt")) {
            startX = l3x4[0];
            startY = l3x4[1];
            for (int i = 0; i < number4; i++) {
                enemies.add(new Gremlin(startX,startY,this,loadImage("src/main/resources/WizardTD/gremlin.png"),this.monster_speed,this.monster_hp,this.mana_gained_on_kill,this.monster_armour, pathToGo34, wzLocation, mana));
                startY+=1;
            }
        }
        target = amt; 
        maxRandomNumber = target - 2;
        
        number1 = random.nextInt(maxRandomNumber) + 1;
        remaining1 = target - number1;
        
        number2 = random.nextInt(remaining1 - 1) + 1;
        number3 = remaining1 - number2;
        if (levelfile.equals("level4.txt")) {
            startX = l4x1[0];
            startY = l4x1[1];
            for (int i = 0; i < number1; i++) {
                enemies.add(new Gremlin(startX,startY,this,loadImage("src/main/resources/WizardTD/gremlin.png"),this.monster_speed,this.monster_hp,this.mana_gained_on_kill,this.monster_armour, pathToGo41, wzLocation, mana));
                startX-=1;
            }
        }
        if (levelfile.equals("level4.txt")) {
            startX = l4x2[0];
            startY = l4x2[1];
            for (int i = 0; i < number2; i++) {
                enemies.add(new Gremlin(startX,startY,this,loadImage("src/main/resources/WizardTD/gremlin.png"),this.monster_speed,this.monster_hp,this.mana_gained_on_kill,this.monster_armour, pathToGo42, wzLocation, mana));
                startY-=1;
            }
        }
        if (levelfile.equals("level4.txt")) {
            startX = l4x3[0];
            startY = l4x3[1];
            for (int i = 0; i < number3; i++) {
                enemies.add(new Gremlin(startX,startY,this,loadImage("src/main/resources/WizardTD/gremlin.png"),this.monster_speed,this.monster_hp,this.mana_gained_on_kill,this.monster_armour, pathToGo43, wzLocation, mana));
                startX+=1;
            }
        }
    }
    /**
     * Fixes the path images on the screen by checking the surrounding tiles and rotating or loading another path image according to surroundings.
     */
    public void fixPaths() {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                char temp = level[j][i];
                
                switch(temp) {
                    case 'X': 

                        char onTop = 'O';
                        if (j > 0) {
                            onTop = level[j-1][i];
                        }
                        char onBot = 'O';
                        if (j < 19) {
                            onBot = level[j+1][i];
                        }
                        char onRight = 'O';
                        if (i > 0) {
                            onRight = level[j][i-1];
                        }
                        char onLeft = 'O';
                        if (i < 19) {
                            onLeft = level[j][i+1];
                        }

                        //path0
                        if (onTop == temp) {
                            image(rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path0.png"),90),i*32,j*32+40);
                        }

                        if (onBot == temp) {
                            image(rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path0.png"),90),i*32,j*32+40);
                        }

                        if (onTop == temp && onBot == temp) {
                            image(rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path0.png"),90),i*32,j*32+40);
                        }

                        //path1 
                        if (onTop != temp && onBot == temp && onRight != temp && onLeft == temp) {
                            image(rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path1.png"),-90),i*32,j*32+40);//XX
                                                                                                                             //X
                        }

                        if (onTop != temp && onBot == temp && onRight == temp && onLeft != temp) {
                            image(rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path1.png"),0),i*32,j*32+40); //XX
                                                                                                                            // X
                        }

                        if (onTop == temp && onBot != temp && onRight == temp && onLeft != temp) {
                            image(rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path1.png"),90),i*32,j*32+40);// X
                                                                                                                            //XX
                        }

                        if (onTop == temp && onBot != temp && onRight != temp && onLeft == temp) {
                            image(rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path1.png"),180),i*32,j*32+40);//X
                                                                                                                           //XX
                        }

                        //path2 
                        if (onTop != temp && onBot == temp && onRight == temp && onLeft == temp) {
                            image(rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path2.png"),0),i*32,j*32+40);
                        }

                        if (onTop == temp && onBot != temp && onRight == temp && onLeft == temp) {
                            image(rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path2.png"),180),i*32,j*32+40);
                        }

                        if (onTop == temp && onBot == temp && onRight != temp && onLeft == temp) {
                            image(rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path2.png"),270),i*32,j*32+40);
                        }

                        if (onTop == temp && onBot == temp && onRight == temp && onLeft != temp) {
                            image(rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path2.png"),90),i*32,j*32+40);
                        }

                        //path3
                        if (onTop == temp && onBot == temp && onRight == temp && onLeft == temp) {
                            image(loadImage("src/main/resources/WizardTD/path3.png"),i*32,j*32+40);
                        }

                        
                        
                }
            } 
        }
    }
    /**
     * Iterates through buttons arraylist of type Button and displays on the screen using display() function of the button.
     * <p>
     * Also highlights the button whenever the mouse position is within the button.
     */
    public void drawButtons() {
        for (Button i : buttons) {
            i.display();
            if (mouseX >= i.getX() && mouseX <= i.getX()+50 && mouseY >= i.getY() && mouseY <= i.getY()+50) {
                i.highlighted();
            }
        }
    }
    /**
     * Reads json file and sets the values to the corresponding variables.
     */
    public void readJSON() {
        this.leveltext = json.getString("layout");

        //intial variables
        this.initial_tower_range = json.getFloat("initial_tower_range");
        this.initial_tower_firing_speed = json.getFloat("initial_tower_firing_speed");
        this.initial_tower_damage = json.getFloat("initial_tower_damage");
        this.initial_mana = json.getFloat("initial_mana");
        this.initial_mana_cap = json.getFloat("initial_mana_cap");
        this.initial_mana_gained_per_second = json.getFloat("initial_mana_gained_per_second");
        this.tower_cost = json.getFloat("tower_cost");
        this.mana_pool_spell_initial_cost = json.getFloat("mana_pool_spell_initial_cost");
        this.mana_pool_spell_cost_increase_per_use = json.getFloat("mana_pool_spell_cost_increase_per_use");
        this.mana_pool_spell_cap_multiplier = json.getFloat("mana_pool_spell_cap_multiplier");
        this.mana_pool_spell_mana_gained_multiplier = json.getFloat("mana_pool_spell_mana_gained_multiplier");

        //waves
        this.monster_hp = 100f;
        this.monster_speed = 0.05f;
        this.monster_armour = 0.5f;
        this.mana_gained_on_kill = 1f;
        this.toSpawn = 15;
    }
    /**
     * Checks whether player has won by checking if all enemies are dead.
     * @return boolean whether player has won.
     */
    public boolean checkW() {
        boolean enemyAllDead = true;
        for (Enemy i : enemies) {
            if (!i.isDead()) {
                enemyAllDead = false;
                break;
            }
        }
        return enemyAllDead;
    }
    /**
     * Checks whether player has lost by checking if mana is 0.
     * @return boolean whether player has lost.
     */
    public boolean checkL() {
        if (mana.getCurrent() == 0) {
            return true;
        }
        return false;
    }
    /**
     * Checks the checkL() and checkW() functions, if any are true, game pauses and the appropriate text is displayed.
     */
    public void endText() {
        if (checkL()) {
            lost = true;
            paused = true;
            checkPaused();
            fill(0);
            textSize(100);
            text("YOU LOSE", 100, 300);
        } else if (checkW()) {
            won = true;
            paused = true;
            checkPaused();
            fill(0);
            textSize(100);
            text("YOU WIN", 110, 300);
        }

    }
}
