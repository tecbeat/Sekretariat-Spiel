package de.jspll.data.objects.game.tasks;

import com.google.gson.annotations.Expose;
import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.game.stats.StatManager;
import de.jspll.graphics.Camera;
import de.jspll.graphics.ResourceHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @version 1.0
 */

public class NPCTask extends GameObject implements Task {
    @Expose(deserialize = false, serialize = false)

    // general properties
    private TaskHolder holder;
    private boolean active = false;
    private float countDown = 0;
    private boolean countDownStarted = false;
    private iTaskReaction onSelect;
    private boolean isStudent;

    // mouse properties
    private boolean mousedown;
    private int[] mousePos = new int[2];
    private AtomicBoolean mouseClicked;

    // button properties
    private int btnStartY = 0;
    private int btnGoodX = 0;
    private int btnBadX = 0;
    int[] buttonSize = new int[]{70,30};
    private boolean buttonLock = false;
    private boolean goodInteraction;

    // screen properties
    private int screenWidth;
    private int screenHeight;
    private int boundingX;
    private int boundingY;

    // headings
    private final String goodHeading;
    private final String badHeading;

    private StatManager statManager;

    // files
    private String[] files;
    private BufferedImage[] textures;


    public NPCTask(String goodHeading, String badHeading, iTaskReaction onSelect, StatManager statManager, boolean student){
        this.goodHeading = goodHeading;
        this.badHeading = badHeading;
        this.onSelect = onSelect;
        this.statManager = statManager;
        channels = new ChannelID[]{ChannelID.PLAYER, ChannelID.LOGIC};
        isStudent = student;
    }

    public NPCTask(String goodHeading, String badHeading, iTaskReaction onSelect, StatManager statManager,boolean student, String[] files){
        this.goodHeading = goodHeading;
        this.badHeading = badHeading;
        this.onSelect = onSelect;
        this.statManager = statManager;
        channels = new ChannelID[]{ChannelID.PLAYER, ChannelID.LOGIC};
        this.files = files;
        isStudent = student;
    }

    /**
     * Display the Task if {@code countDown} is not 0.
     *
     * @param g           Graphics to draw
     * @param elapsedTime delta time between frames
     * @param camera      selected Camera
     * @param currStage   current active ChannelID
     */

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        initTaskScreen(g, camera);

        if(buttonLock) {
            paintInteraction(g, camera);
        }

        countTimerDown(elapsedTime);

        // close task window when countdown is lower than 0
        if(countDown < 0){
            closeTask();
            active = false;
            return;
        }
    }

    /**
     * Initializes the basic task screen. Background gets darker. White rectangle gets drawn.
     *
     * @param g Graphics
     * @param camera Camera
     */
    private void initTaskScreen(Graphics g, Camera camera) {
        g.setFont(new Font("Serif", Font.PLAIN, 14));
        Graphics2D g2d = (Graphics2D) g;
        screenWidth = (int) g2d.getClipBounds().getWidth();
        screenHeight = (int) g2d.getClipBounds().getHeight();
        boundingX = screenWidth / 4;
        boundingY = screenHeight / 4;
        btnGoodX = (boundingX + (screenWidth / 2) / 4) + 85;
        btnStartY = (int) (boundingY + (screenHeight / 2) * 0.8);

        g.setColor(new Color(0, 0, 0, 172));
        g.fillRect(0, 0, camera.getWidth(), camera.getHeight());

        //Bounding Box
        g.setColor(Color.WHITE);
        g.fillRect(boundingX, boundingY, screenWidth / 2, screenHeight / 2);
        if (files != null)
            if (isLoaded())
                g.drawImage(textures[0],boundingX, boundingY, screenWidth / 2, screenHeight / 2,null);

        if(!buttonLock) {
            g.setColor(Color.BLACK);
            g.drawString(goodHeading + " oder " + badHeading, (screenWidth / 2) - 110, screenHeight / 2);
        }
    }

    /**
     * Sets the remaining time to finish the task and draws the task name at the screen.
     *
     * @param g Graphics
     * @param camera Camera
     */
    private void paintInteraction(Graphics g, Camera camera) {
        g.setColor(goodInteraction ? new Color(48, 170, 0, 255) : new Color(196, 0, 0, 255));
        String correctHeading = goodInteraction ? goodHeading : badHeading;
        g.drawString("Du hast einen " + (isStudent ? "Studenten" : "Professor") + " getroffen!", (screenWidth / 2) - 110, screenHeight / 2 - 30);
        g.drawString(correctHeading, (screenWidth / 2) - 110, screenHeight / 2);
        g.drawString("Verbleibende Zeit: " + String.format("%2.2f",countDown), camera.getWidth() / 4 + 10, camera.getHeight() / 4 + 20);
    }

    /**
     * Check if all {@code textures} and {@code files} are loaded.
     *
     * @return true if {@code files} and all {@code textures} are loaded, else false
     */
    @Override
    public boolean isLoaded() {
        if(files == null)
            return true;
        if(textures == null)
            return false;
        for (BufferedImage i:textures) {
            if (i == null)
                return false;
        }
        return true;
    }

    /**
     * Load all textures from {@code files} and save them in {@code textures}.<br>
     * {@code Textures} are images based on what task it is.
     */
    @Override
    public void loadTextures() {
        if(files == null)
            return;
        if(textures == null)
            textures = new BufferedImage[files.length];
        for (int i = 0; i < files.length; i++){
            if (textures[i] == null)
                textures[i] = getHolder().getParent().getResourceHandler().getTexture(files[i], ResourceHandler.FileType.PNG);
        }
    }

    /**
     * Get the {@code ResourceHandler} and request all {@code Textures} needed for the images.
     *
     * @see ResourceHandler
     */
    public void requestTexture(){
        if (files == null)
            return;
        for (String s: files){
            getHolder().getParent().getResourceHandler().requestTexture(s, ResourceHandler.FileType.PNG);
        }
    }

    /**
     * Decreases the remaining time.
     *
     * @param elapsedTime time that passed by
     */
    private void countTimerDown(float elapsedTime) {
        if(countDownStarted) {
            countDown -= elapsedTime;
        }
    }

    /**
     * Updates Karma and Roundscore and unregisteres the task
     *
     */
    private void closeTask() {
        onSelect.taskFinished(statManager, goodInteraction);
        getHolder().getParent().getGameManager().taskCompleted();
        getHolder().getParent().unsubscribe(getHolder());
    }

    @Override
    public String toString() {
        return "CommonTask{" +
                "active=" + active +
                ", countDown=" + countDown +
                ", countDownStarted=" + countDownStarted +
                ", buttonLock=" + buttonLock +
                ", buttonGoodClicked=" + goodInteraction +
                ", goodHeading='" + goodHeading + '\'' +
                ", badHeading='" + badHeading + '\'' +
                ", statManager=" + statManager +
                "} " + super.toString();
    }

    /**
     * Task will be activated and is displayed on the next frame.
     * The type of interaction is based on the {@code KarmaScore} and what type of npc (student, professor)
     */
    public void activate(){
        buttonLock = false;
        mouseClicked = getHolder().getParent().getLogicHandler().getInputHandler().getMouse1();
        countDown = 10;
        this.active = true;




        if((isStudent && statManager.getKarmaScore() > 0) || (!isStudent && statManager.getKarmaScore() < 0)){
            goodInteraction = true;
        } else {
            goodInteraction = false;
        }

        buttonLock = true;
        countDownStarted = true;


    }

    /**
     * Task will be deactivated
     */
    public void deactivate(){
        active = false;
    }

    /**
     * Implement how to response when {@code CommonTask} is getting called. <br>
     * 1. The current {@code mousePos} and {@code mousedown} are transmitted to {@code CommonTask}.
     *
     * @param input Array of Objects
     * @return exit code - similar to program exit codes in Java/C
     */
    @Override
    public char call(Object[] input) {
        if (input[0] instanceof String) {
            if (((String) input[0]).contentEquals("input")) {
                mousedown = (boolean) input[1];
                int[] pos = (int[]) input[5];
                mousePos[0] = pos[0];
                mousePos[1] = pos[1];
            }
        }
        return 0;
    }

    public boolean isActive() {
        return active;
    }

    public void setHolder(TaskHolder holder) {
        this.holder = holder;
    }

    public TaskHolder getHolder() {
        return holder;
    }

    public String getName(){
        String holderID = getHolder().getID();
        return holderID.substring(holderID.indexOf(":")+1);
    }
}
