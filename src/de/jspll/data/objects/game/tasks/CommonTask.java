package de.jspll.data.objects.game.tasks;

import com.google.gson.annotations.Expose;
import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.graphics.Camera;

import java.awt.*;

public class CommonTask extends GameObject implements Task {
    private Color maskColor = new Color(0, 0, 0, 172);
    @Expose(deserialize = false, serialize = false)
    private TaskHolder holder;
    private boolean active = false;

    private boolean mousedown;
    private int[] mousePos = new int[2];


    //for testing
    private float countDown = 0;
    private boolean countDownStarted = false;

    private boolean buttonsSet = true;

    private String heading;
    private String good;
    private String bad;

    private int btnStartY = 0;
    private int btnGoodX = 0;
    private int btnBadX = 0;
    int[] buttonSize = new int[]{70,30};

    private boolean buttonLock = false;

    private iTaskReaction onSelect;

    public CommonTask(String heading, String optionGood, String optionBad, iTaskReaction onSelect){
        this.heading = heading;
        this.good = optionGood;
        this.bad = optionBad;
        this.onSelect = onSelect;
    }

    public boolean isActive() {
        return active;
    }

    public void activate(){
        countDown = 10;
        this.active = true;
    }

    public void setHolder(TaskHolder holder) {
        this.holder = holder;
    }

    public TaskHolder getHolder() {
        return holder;
    }

    public Color getMaskColor() {
        return maskColor;
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        Graphics2D g2d = (Graphics2D) g;
        int screenWidth = (int) g2d.getClipBounds().getWidth();
        int screenHeight = (int) g2d.getClipBounds().getHeight();
        int boundingX = screenWidth / 4;
        int boundingY = screenHeight / 4;
        btnGoodX = boundingX + (screenWidth / 2) / 4;
        btnStartY = (int) (boundingY + (screenHeight / 2) * 0.8);

        g.setColor(maskColor);
        g.fillRect(0, 0, camera.getWidth(), camera.getHeight());


        //Bounding Box
        g.setColor(Color.WHITE);
        g.fillRect(boundingX, boundingY, screenWidth / 2, screenHeight / 2);

        if(buttonLock) {
            g.setColor(Color.BLUE);
            g.drawString(heading, camera.getWidth() / 2, camera.getHeight() / 2);
            g.drawString("Verbleibende Zeit: " + countDown, camera.getWidth() / 4 + 10, camera.getHeight() / 4 + 20);
        } else {
            g.setColor(Color.GREEN);
            if (checkHover(btnGoodX, btnStartY, buttonSize[0], buttonSize[1])) {
                g.fillRect(btnGoodX, btnStartY, buttonSize[0], buttonSize[1]);
            } else {
                g.drawRect(btnGoodX, btnStartY, buttonSize[0], buttonSize[1]);
            }

            g.setColor(Color.black);
            g.drawString(good, btnGoodX + 5, btnStartY + 15);

            btnBadX = boundingX + (screenWidth / 2) / 2;
            g.setColor(Color.RED);
            if (checkHover(btnBadX, btnStartY, buttonSize[0], buttonSize[1])) {
                g.fillRect(btnBadX, btnStartY, buttonSize[0], buttonSize[1]);
            } else {
                g.drawRect(btnBadX, btnStartY, buttonSize[0], buttonSize[1]);
            }

            g.setColor(Color.black);
            g.drawString(bad, btnBadX + 5, btnStartY + 15);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Serif", Font.PLAIN, 14));
        g.drawString("test!", camera.applyXTransform(x+5), camera.applyYTransform(y+13));

        //for testing, remove when developing Tasks
        if(countDownStarted)
            countDown -= elapsedTime;
        if(countDown < 0){
            active = false;
            return;
        }

        if(!buttonLock){
            checkClick();
        }
    }

    protected boolean checkHover(int startX, int startY, int width, int height){
        getMousePos();
        boolean hover =mousePos[0] > startX && mousePos[0] < startX + width && mousePos[1] > startY && mousePos[1] < startY + height;
        return hover;
    }

    private void getMousePos(){
        mousePos[0] = (int)getHolder().getParent().getMousePos().getX();
        mousePos[1] = (int)getHolder().getParent().getMousePos().getY();
    }

    private boolean getMousePressed(){
        return (boolean) getHolder().getParent().getLogicHandler().getInputHandler().getInputInfo()[1];
        //return mousedown;
    }

    private void checkClick(){
        if(getMousePressed()){
            if(checkHover(btnGoodX, btnStartY, buttonSize[0], buttonSize[1])){
                System.out.println("positive Karma");
                countDown = onSelect.goodSelection(getHolder().getParent());
                buttonLock = true;
                countDownStarted = true;
            }

            if(checkHover(btnBadX, btnStartY, buttonSize[0], buttonSize[1])){
                System.out.println("negative Karma");
                countDown = onSelect.badSelection(getHolder().getParent());
                buttonLock = true;
                countDownStarted = true;
            }
        }

    }


    @Override
    public char call(Object[] input) { // input[0] always = "toTask"
        if (input[0] instanceof String) {
            if (((String) input[0]).contentEquals("input")) {
                if ((boolean) input[1]) {
                    mousedown = true;
                } else {
                    mousedown = false;
                }
                int[] pos = (int[]) input[5];
                mousePos[0] = pos[0];
                mousePos[1] = pos[1];
            }

        }

        return 0;
    }
}
