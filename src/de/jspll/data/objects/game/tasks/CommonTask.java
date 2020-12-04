package de.jspll.data.objects.game.tasks;

import com.google.gson.annotations.Expose;
import de.jspll.Main;
import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.game.stats.StatManager;
import de.jspll.graphics.Camera;
import de.jspll.graphics.ResourceHandler;
import de.jspll.util.Collision;
import de.jspll.util.PaintingUtil;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Laura Schmidt, Lukas Becker, Samuel Assmann
 * @version 1.0
 */

public class CommonTask implements Task {
    @Expose(deserialize = false, serialize = false)

    // general properties
    private TaskHolder holder;
    private boolean active = false;
    private float countDown = 0;
    private boolean countDownStarted = false;
    private iTaskReaction onSelect;
    private Point draggablePos;
    private final Color maskColor = new Color(0, 0, 0, 172);

    //texture properties
    private float draggableSize = 1.6f;
    private Dimension draggableDim;
    private String[] textureKeys;
    private BufferedImage[] textures;

    @Expose(deserialize = false, serialize = false)
    private boolean texturesLoaded = false;

    // mouse properties
    private boolean mousedown;
    private int[] mousePos = new int[2];
    private AtomicBoolean mouseClicked;

    // button properties
    private int btnStartY = 0;
    private int btnGoodX = 0;
    private int btnBadX = 0;
    int[] buttonSize = new int[]{80, 30};
    private boolean buttonLock = false;
    private boolean buttonGoodClicked;

    // screen properties
    private int screenWidth;
    private int screenHeight;
    private int boundingX;
    private int boundingY;
    private int boundingWidth;
    private int boundingHeight;

    // headings
    private final String goodHeading;
    private final String badHeading;

    private StatManager statManager;


    public CommonTask(String goodHeading, String badHeading, iTaskReaction onSelect, StatManager statManager) {
        this.goodHeading = goodHeading;
        this.badHeading = badHeading;
        this.onSelect = onSelect;
        this.statManager = statManager;
        //channels = new ChannelID[]{ChannelID.PLAYER, ChannelID.LOGIC};
    }

    public CommonTask(String goodHeading, String badHeading, iTaskReaction onSelect, StatManager statManager, String[] textureKeys) {
        this.goodHeading = goodHeading;
        this.badHeading = badHeading;
        this.onSelect = onSelect;
        this.statManager = statManager;
        //channels = new ChannelID[]{ChannelID.PLAYER, ChannelID.LOGIC};
        this.textureKeys = textureKeys;
    }


    private void textBasedRender(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        initTaskScreen(g, camera);

        if (buttonLock) {
            onButtonClicked(g, camera);
        } else {
            setUpButton(g, true);
            setUpButton(g, false);
        }


        if (!buttonLock) {
            checkClick();
        }
    }

    private boolean startedInHitbox = false;
    private void resetDraggablePos(){
        draggablePos = new Point(boundingX + screenWidth / 4, boundingY + screenHeight / 4);
    }

    private void textureBasedRender(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        Graphics2D g2d = (Graphics2D) g;

        if (draggablePos == null || draggableDim == null) {
            draggableDim = new Dimension(Math.round(textures[1].getWidth() * draggableSize), Math.round(textures[1].getHeight() * draggableSize));
            resetDraggablePos();
        }
        g2d.drawImage(textures[0], boundingX, boundingY, boundingWidth, boundingHeight, null);
        Rectangle btnGoodHitbox = new Rectangle(btnGoodX,btnStartY,buttonSize[0],buttonSize[1]);
        Rectangle btnBadHitbox = new Rectangle(btnBadX,btnStartY,buttonSize[0],buttonSize[1]);
        //g2d.setColor(Color.GREEN);
        //g2d.fill(btnGoodHitbox);
        //g2d.setColor(Color.RED);
        //g2d.fill(btnBadHitbox);

        if (buttonLock) {
            onButtonClicked(g, camera);
            return;
        } else {
            setUpButton(g, true);
            setUpButton(g, false);
        }


        Rectangle draggableHitbox = new Rectangle(draggablePos.x-draggableDim.width/2,
                draggablePos.y-draggableDim.height/2,
                draggableDim.width,
                draggableDim.height);
        getMousePos();
        Point mousePoint = new Point(mousePos[0],mousePos[1]);
        if(mouseClicked.get()){
            if(startedInHitbox){
                draggablePos = mousePoint;
            } else if(Collision.posInRect(mousePoint,draggableHitbox)){
                startedInHitbox = true;
            } else {
                startedInHitbox = false;
            }
        } else {
            if(startedInHitbox){
                startedInHitbox = false;
                if(Collision.posInRect(mousePoint,btnGoodHitbox)){
                    buttonLock = true;
                    buttonGoodClicked = true;
                    countDownStarted = true;
                } else if(Collision.posInRect(mousePoint,btnBadHitbox)){
                    buttonLock = true;
                    buttonGoodClicked = false;
                    countDownStarted = true;
                }
            }
        }


        PaintingUtil.drawPictureFromCenter(draggablePos, textures[1], g2d, draggableDim);
        if (Main.DEBUG) {
            Stroke s = g2d.getStroke();
            g2d.setStroke(new BasicStroke(2));
            g.setColor(Color.RED);
            g.drawRect(boundingX, boundingY, boundingWidth, boundingHeight);
            PaintingUtil.drawRectFromCenter(draggablePos, g2d, draggableDim);
            g2d.setStroke(s);
        }
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        //g.setFont(Font.getFont(Font.SANS_SERIF));
        // close task window when countdown is lower than 0
        if(!active) return;
        countTimerDown(elapsedTime);
        if (countDown < 0) {
            closeTask();
            active = false;
            return;
        }
        initVars(g);
        g.setColor(maskColor);
        g.fillRect(0, 0, camera.getWidth(), camera.getHeight());

        if (textures != null) {
            if (texturesLoaded) {
                textureBasedRender(g, elapsedTime, camera, currStage);
            } else {
                texturesLoaded = isLoaded();
                textBasedRender(g, elapsedTime, camera, currStage);
            }
        } else {
            textBasedRender(g, elapsedTime, camera, currStage);
        }


    }

    private void initVars(Graphics g) {
        if (textures != null) {
            buttonSize[0] = 200;
            buttonSize[1] = 100;
        }
        Graphics2D g2d = (Graphics2D) g;
        screenWidth = (int) g2d.getClipBounds().getWidth();
        screenHeight = (int) g2d.getClipBounds().getHeight();
        boundingX = screenWidth / 4;
        boundingY = screenHeight / 4;
        boundingWidth = screenWidth / 2;
        boundingHeight = screenHeight / 2;

        if(textures != null){
            btnGoodX = boundingX;
            btnBadX = boundingX + boundingWidth - buttonSize[0];
            btnStartY = boundingY+ boundingHeight;
        } else {
            btnGoodX = (boundingX + (screenWidth / 2) / 4) + 85;
            btnStartY = (int) (boundingY + (screenHeight / 2) * 0.8);
            btnBadX = (boundingX + (screenWidth / 2) / 2) + 85;
        }

    }

    /**
     * Initializes the basic task screen. Background gets darker. White rectangle gets drawn.
     *
     * @param g      Graphics
     * @param camera Camera
     */
    private void initTaskScreen(Graphics g, Camera camera) {
        g.setFont(new Font("Serif", Font.PLAIN, 14));

        //Bounding Box
        g.setColor(Color.WHITE);
        g.fillRect(boundingX, boundingY, screenWidth / 2, screenHeight / 2);

        if (!buttonLock) {
            g.setColor(Color.BLACK);
            g.drawString(goodHeading + " oder " + badHeading, (screenWidth / 2) - 110, screenHeight / 2);
        }
    }

    /**
     * Sets up one button. When goodButton is true a green button gets drawn which triggers an action
     * that when clicked increases the karma score. When goodButton is false a red button gets drawn which
     * triggers an action that when clicked decreases the karma score.
     *
     * @param g          Graphics
     * @param goodButton decides whether a button for good karma or bad karma gets drawn
     */
    private void setUpButton(Graphics g, boolean goodButton) {

        int xCoord = goodButton ? btnGoodX : btnBadX;
        String heading = goodButton ? goodHeading.split(" ")[1] : badHeading.split(" ")[1];

        g.setColor(goodButton ? Color.GREEN : Color.RED);
        if (checkHover(xCoord, btnStartY, buttonSize[0], buttonSize[1])) {
            g.fillRect(xCoord, btnStartY, buttonSize[0], buttonSize[1]);
            g.setColor(Color.YELLOW);
            g.drawString(heading, xCoord + 5, btnStartY + 15);
        } else {
            g.drawRect(xCoord, btnStartY, buttonSize[0], buttonSize[1]);
            g.setColor(Color.YELLOW);
            g.drawString(heading, xCoord + 5, btnStartY + 15);
        }

        if (buttonLock) {
            g.setColor(Color.YELLOW);
            g.drawString(heading, xCoord + 5, btnStartY + 15);
        }
    }

    private Color highlight_color= new Color(255,255,255,100);

    /**
     * Sets the remaining time to finish the task and draws the task name at the screen.
     *
     * @param g      Graphics
     * @param camera Camera
     */
    private void onButtonClicked(Graphics g, Camera camera) {
        Color txtColor = buttonGoodClicked ? new Color(48, 170, 0, 255) : new Color(196, 0, 0, 255);
        String correctHeading = buttonGoodClicked ? goodHeading : badHeading;
        Rectangle2D headingBounds = g.getFont().getStringBounds(correctHeading,new FontRenderContext(null,true,false));
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(highlight_color);
        g2d.fillRect((screenWidth / 2) - 110,screenHeight / 2 -(int) headingBounds.getHeight(),(int)headingBounds.getWidth(),(int)headingBounds.getHeight());
        g.setColor(txtColor);
        g.drawString(correctHeading, (screenWidth / 2) - 110, screenHeight / 2);
        g.drawString("Verbleibende Zeit: " + String.format("%2.2f", countDown), camera.getWidth() / 4 + 10, camera.getHeight() / 4 + 20);
    }

    /**
     * Checks if the mouse position is over one of the buttons.
     *
     * @param startX x-coordinate of top left corner of the button
     * @param startY y-coordinate of top left corner of the button
     * @param width  width of the button
     * @param height height of the button
     * @return if mouse is over button
     */
    protected boolean checkHover(int startX, int startY, int width, int height) {
        getMousePos();
        return mousePos[0] > startX && mousePos[0] < startX + width
                && mousePos[1] > startY && mousePos[1] < startY + height;
    }

    /**
     * Checks if a button got clicked and triggers the action that needs to be performed after
     * clicking the button.
     *
     * @see iTaskReaction
     */
    private void checkClick() {
        if (getMousePressed()) {
            if (checkHover(btnGoodX, btnStartY, buttonSize[0], buttonSize[1])) {
                countDown = onSelect.goodSelection(getHolder().getParent());
                buttonGoodClicked = true;
                buttonLock = true;
                countDownStarted = true;
            }

            if (checkHover(btnBadX, btnStartY, buttonSize[0], buttonSize[1])) {
                countDown = onSelect.badSelection(getHolder().getParent());
                buttonGoodClicked = false;
                buttonLock = true;
                countDownStarted = true;
            }
        }
    }

    @Override
    public boolean isLoaded() {
        if (textureKeys == null)
            return true;
        if (textures == null)
            return false;
        for (BufferedImage i : textures) {
            if (i == null)
                return false;
        }
        return true;
    }

    @Override
    public void loadTextures() {
        if (textureKeys == null)
            return;
        if (textures == null)
            textures = new BufferedImage[textureKeys.length];
        for (int i = 0; i < textureKeys.length; i++) {
            if (textures[i] == null)
                textures[i] = getHolder().getParent().getResourceHandler().getTexture(textureKeys[i], ResourceHandler.FileType.PNG);
        }
    }

    public void requestTexture() {
        if (textureKeys == null)
            return;
        for (String s : textureKeys) {
            getHolder().getParent().getResourceHandler().requestTexture(s, ResourceHandler.FileType.PNG);
        }
    }

    /**
     * Decreases the remaining time.
     *
     * @param elapsedTime time that passed by
     */
    private void countTimerDown(float elapsedTime) {
        if (countDownStarted) {
            countDown -= elapsedTime;
        }
    }

    /**
     * Updates Karma and Roundscore and unregisteres the task
     */
    private void closeTask() {
        onSelect.taskFinished(statManager, buttonGoodClicked);
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
                ", buttonGoodClicked=" + buttonGoodClicked +
                ", goodHeading='" + goodHeading + '\'' +
                ", badHeading='" + badHeading + '\'' +
                ", statManager=" + statManager +
                "} " + super.toString();
    }

    /**
     * Determines the mouse position.
     */
    private void getMousePos() {
        // TODO: get proper mouse position if possible, siehe ExampleTask (null-check)
        if (getHolder().getParent().getMousePos() != null) {
            mousePos[0] = (int) getHolder().getParent().getMousePos().getX();
            mousePos[1] = (int) getHolder().getParent().getMousePos().getY();
        }
    }

    public void activate() {

        buttonLock = false;
        mouseClicked = getHolder().getParent().getLogicHandler().getInputHandler().getMouse1();
        countDown = 10;
        this.active = true;
    }

    private boolean getMousePressed() {
        return mouseClicked.get();
    }

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

    @Override
    public char update(float elapsedTime) {
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
}
