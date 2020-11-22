package de.jspll.data.objects.game.tasks;

import com.google.gson.annotations.Expose;
import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.graphics.Camera;

import java.awt.*;

public class CommonTask extends GameObject implements Task {
    @Expose(deserialize = false, serialize = false)

    // general properties
    private TaskHolder holder;
    private boolean active = false;
    private float countDown = 0;
    private boolean countDownStarted = false;
    private iTaskReaction onSelect;

    // mouse properties
    private boolean mousedown;
    private int[] mousePos = new int[2];

    // button properties
    private int btnStartY = 0;
    private int btnGoodX = 0;
    private int btnBadX = 0;
    int[] buttonSize = new int[]{70,30};
    private boolean buttonLock = false;
    private boolean buttonGoodClicked;

    // screen properties
    private int screenWidth;
    private int screenHeight;
    private int boundingX;
    private int boundingY;

    // headings
    private final String goodHeading;
    private final String badHeading;


    public CommonTask(String goodHeading, String badHeading, iTaskReaction onSelect){
        this.goodHeading = goodHeading;
        this.badHeading = badHeading;
        this.onSelect = onSelect;
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        initTaskScreen(g, camera);

        if(buttonLock) {
            onButtonClicked(g, camera);
        } else {
            setUpButton(g, true);
            setUpButton(g, false);
        }

        countTimerDown(elapsedTime);

        // close task window when countdown is lower than 0
        if(countDown < 0){
            active = false;
            return;
        }

        if(!buttonLock){
            checkClick();
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
        btnGoodX = boundingX + (screenWidth / 2) / 4;
        btnStartY = (int) (boundingY + (screenHeight / 2) * 0.8);

        g.setColor(new Color(0, 0, 0, 172));
        g.fillRect(0, 0, camera.getWidth(), camera.getHeight());

        //Bounding Box
        g.setColor(Color.WHITE);
        g.fillRect(boundingX, boundingY, screenWidth / 2, screenHeight / 2);
    }

    /**
     * Sets up one button. When goodButton is true a green button gets drawn which triggers an action
     * that when clicked increases the karma score. When goodButton is false a red button gets drawn which
     * triggers an action that when clicked decreases the karma score.
     *
     * @param g Graphics
     * @param goodButton decides whether a button for good karma or bad karma gets drawn
     */
    private void setUpButton(Graphics g, boolean goodButton) {
        if(!goodButton) {
            btnBadX = boundingX + (screenWidth / 2) / 2;
        }
        int xCoord = goodButton ? btnGoodX : btnBadX;
        String heading = goodButton ? goodHeading.split(" ")[1] : badHeading.split(" ")[1];

        g.setColor(goodButton ? Color.GREEN : Color.RED);
        if (checkHover(xCoord, btnStartY, buttonSize[0], buttonSize[1])) {
            g.fillRect(xCoord, btnStartY, buttonSize[0], buttonSize[1]);
        } else {
            g.drawRect(xCoord, btnStartY, buttonSize[0], buttonSize[1]);
        }

        g.setColor(Color.black);
        g.drawString(heading, xCoord + 5, btnStartY + 15);
    }

    /**
     * Sets the remaining time to finish the task and draws the task name at the screen.
     *
     * @param g Graphics
     * @param camera Camera
     */
    private void onButtonClicked(Graphics g, Camera camera) {
        g.setColor(buttonGoodClicked ? new Color(48, 170, 0, 255) : new Color(196, 0, 0, 255));
        String correctHeading = buttonGoodClicked ? goodHeading : badHeading;
        g.drawString(correctHeading, camera.getWidth() / 2, camera.getHeight() / 2);
        g.drawString("Verbleibende Zeit: " + countDown, camera.getWidth() / 4 + 10, camera.getHeight() / 4 + 20);
    }

    /**
     * Checks if the mouse position is over one of the buttons.
     *
     * @param startX x-coordinate of top left corner of the button
     * @param startY y-coordinate of top left corner of the button
     * @param width width of the button
     * @param height height of the button
     * @return if mouse is over button
     */
    protected boolean checkHover(int startX, int startY, int width, int height){
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
    private void checkClick(){
        if(getMousePressed()) {
            if(checkHover(btnGoodX, btnStartY, buttonSize[0], buttonSize[1])){
                System.out.println("positive Karma");
                countDown = onSelect.goodSelection(getHolder().getParent());
                buttonGoodClicked = true;
                buttonLock = true;
                countDownStarted = true;
            }

            if(checkHover(btnBadX, btnStartY, buttonSize[0], buttonSize[1])){
                System.out.println("negative Karma");
                countDown = onSelect.badSelection(getHolder().getParent());
                buttonGoodClicked = false;
                buttonLock = true;
                countDownStarted = true;
            }
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
     * Determines the mouse position.
     */
    private void getMousePos(){
        // TODO: get proper mouse position if possible
        mousePos[0] = (int) getHolder().getParent().getMousePos().getX();
        mousePos[1] = (int) getHolder().getParent().getMousePos().getY();
    }

    public void activate(){
        countDown = 10;
        this.active = true;
    }

    private boolean getMousePressed(){
        // TODO: check if there is better/easier way to get if mouse is pressed
        return (boolean) getHolder().getParent().getLogicHandler().getInputHandler().getInputInfo()[1];
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