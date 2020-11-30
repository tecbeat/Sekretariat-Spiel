package de.jspll.handlers;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.TexturedObject;
import de.jspll.data.objects.game.player.ColorScheme;
import de.jspll.data.objects.game.player.NPC;
import de.jspll.data.objects.game.player.Player;
import de.jspll.data.objects.game.stats.StatManager;
import de.jspll.data.objects.game.tasks.CommonTask;
import de.jspll.data.objects.game.tasks.TaskHolder;
import de.jspll.data.objects.game.tasks.iTaskReaction;
import de.jspll.data.objects.game.tasks.reactions.*;
import de.jspll.graphics.Camera;

import java.util.Random;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Lukas Becker
 *
 * @version 1.0
 */

public class GameManager extends GameObject {
    //Balancing
    private static float ROUND_TIME = 300f;
    private static int NEXT_TASK_TRESHOLD = 30;
    private static int BASE_TASKS = 2;
    private static int TASKS_PER_LEVEL = 4;
    private static float LEVEL_COMPLETION_TRESHOLD = 0.7f;

    private boolean resultScreen = false;
    private boolean pauseScreen = false;


    private int level = 0;
    Random randomGenerator = new Random(System.currentTimeMillis());
    private GameObjectHandler gameObjectHandler;
    private StatManager statManager = new StatManager(this);

    private float time = 0f;

    private boolean gameRunning = false;
    private int taskCount = 0;
    private int completedTasks = 0;
    private float remainingTime;

    // mouse properties
    private boolean mousedown;
    private int[] mousePos = new int[2];
    private AtomicBoolean mouseClicked;

    // screen properties
    private int screenWidth;
    private int screenHeight;
    private int boundingX;
    private int boundingY;

    // button properties
    private int btnStartY = 0;
    private int btnStartX = 0;
    int[] buttonSize = new int[]{70,30};


    public GameManager (GameObjectHandler gameObjectHandler){
        this.gameObjectHandler = gameObjectHandler;
        remainingTime = ROUND_TIME;
        channels = new ChannelID[]{ChannelID.LOGIC, ChannelID.UI};
    }

    @Override
    public char update(float elapsedTime) {
        if(gameRunning) {
            time += elapsedTime;
            if (getTimeTillNextTask() <= 0 && taskCount < getTaskCountForCurrentLevel()) {
                time = 0;
                taskCount++;
                addTaskToCurrentLevel();
            }

            remainingTime -= elapsedTime;

            if(remainingTime < 0){
                stopLevel();
                resultScreen = true;
                mouseClicked = gameObjectHandler.getLogicHandler().getInputHandler().getMouse1();
            }
        }
        return 0;
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        if(resultScreen) {
            initResultScreen(g, camera);
            setUpButton(g);
            checkClick();
        }

    }

    public StatManager getStatManager() {
        return statManager;
    }

    private int getTaskCountForCurrentLevel(){
        return BASE_TASKS + (level * TASKS_PER_LEVEL);
    }

    private float getTaskCompletionPercentage(){
        return (float)completedTasks/getTaskCountForCurrentLevel();
    }

    public float getTimeTillNextTask(){
        if(taskCount == getTaskCountForCurrentLevel()) return 0;
        return (NEXT_TASK_TRESHOLD/level) - time;
    }

    private void addTaskToCurrentLevel(){
        ChannelID scene = ChannelID.SCENE_GAME;
        TexturedObject task = getRandomTask();
        gameObjectHandler.loadTask(scene, task);
        System.out.println("Added: " + task.toString() + " Task!");
    }

    public void taskCompleted(){
        this.completedTasks++;
    }

    //Increments level, starts task addition
    public void startGame(){
        this.level++;
        this.gameRunning = true;
        this.remainingTime = ROUND_TIME;
        statManager.resetKarma();
        for(int i = 0; i< BASE_TASKS; i++)
            addTaskToCurrentLevel();
    }

    //Stops Countdowns, clears input
    private void stopLevel(){
        this.gameRunning = false;
        gameObjectHandler.clearScene(ChannelID.LOGIC);
        gameObjectHandler.loadObject(this);
    }

    /**
     * Initializes the basic task screen. Background gets darker. White rectangle gets drawn.
     *
     * @param g Graphics
     * @param camera Camera
     */
    private void initResultScreen(Graphics g, Camera camera) {
        g.setFont(new Font("Serif", Font.PLAIN, 14));
        Graphics2D g2d = (Graphics2D) g;
        screenWidth = (int) g2d.getClipBounds().getWidth();
        screenHeight = (int) g2d.getClipBounds().getHeight();
        boundingX = screenWidth / 4;
        boundingY = screenHeight / 4;
        btnStartX = (boundingX + (screenWidth / 2) / 4) + 85;
        btnStartY = (int) (boundingY + (screenHeight / 2) * 0.8);

        g.setColor(new Color(46, 49, 49,200));
        g.fillRect(0, 0, camera.getWidth(), camera.getHeight());

        //Bounding Box
        g.setColor(Color.WHITE);
        g.fillRect(boundingX, boundingY, screenWidth / 2, screenHeight / 2);


        g.setColor(Color.BLACK);
        g.drawString("Level Complete", (screenWidth / 2) - 110, screenHeight / 2);
        g.drawString(getTaskCompletionPercentage() > LEVEL_COMPLETION_TRESHOLD ? "Level complete" : "You Failed", (screenWidth / 2) - 110, screenHeight / 2 + 25);

    }

    private void setUpButton(Graphics g) {
        String heading = getTaskCompletionPercentage()>LEVEL_COMPLETION_TRESHOLD ? "Next Level" : "Main Menu";

        g.setColor(getTaskCompletionPercentage()>LEVEL_COMPLETION_TRESHOLD ? Color.GREEN : Color.RED);
        if (checkHover(btnStartX, btnStartY, buttonSize[0], buttonSize[1])) {
            g.fillRect(btnStartX, btnStartY, buttonSize[0], buttonSize[1]);
            g.setColor(Color.BLACK);
            g.drawString(heading, btnStartX + 5, btnStartY + 15);
        } else {
            g.drawRect(btnStartX, btnStartY, buttonSize[0], buttonSize[1]);
            g.setColor(Color.BLACK);
            g.drawString(heading, btnStartX + 5, btnStartY + 15);
        }
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
     * Determines the mouse position.
     */
    private void getMousePos(){
        // TODO: get proper mouse position if possible, siehe ExampleTask (null-check)
        if(gameObjectHandler.getMousePos() != null) {
            mousePos[0] = (int) gameObjectHandler.getMousePos().getX();
            mousePos[1] = (int) gameObjectHandler.getMousePos().getY();
        }
    }

    /**
     * Checks if a button got clicked and triggers the action that needs to be performed after
     * clicking the button
     */
    private void checkClick(){
        if(getMousePressed()) {
            if(checkHover(btnStartX, btnStartY, buttonSize[0], buttonSize[1])){
                if(getTaskCompletionPercentage() > LEVEL_COMPLETION_TRESHOLD){
                    gameObjectHandler.loadNextLevel();
                } else {
                    gameObjectHandler.loadScene(ChannelID.SCENE_1, "/scenes/MainMenu.json");
                }
                resultScreen = false;
            }
        }
    }

    private boolean getMousePressed(){
        return mouseClicked.get();
    }

    public int getLevel(){
        return this.level;
    }

    public float getRoundTime(){
        return this.ROUND_TIME;
    }
    public float getRemainingTime(){
        return this.remainingTime;
    }


    //TODO: Not sure if we need these two
    public ArrayList<TexturedObject> getTasksforCurrentLevel(){
        int taskCount = getTaskCountForCurrentLevel();
        ArrayList<TexturedObject> res = new ArrayList<>();
        int id;
        for (int i = 0; i < taskCount ; i++){
            TexturedObject task = getRandomTask();
            if(!res.contains(task)){
                res.add(task);
            } else {
                i--;
            }
        }

        return res;
    }

    private void addAllTasksToCurrentLevel(){
        ChannelID scene = gameObjectHandler.getActiveScene();
        for(TexturedObject object : getTasksforCurrentLevel()){
            gameObjectHandler.loadTask(scene, object);
        }
    }
    //End T O D O

    private TexturedObject getRandomTask(){
        int id = randomGenerator.nextInt(5);

        switch (id){
            case 0:
                TaskHolder thMail = new TaskHolder("mail", "g.dflt.TaskHolder",
                        new Point(622,2090),
                        new Dimension(32,16),
                        new CommonTask("Post sortieren", "Post schreddern", new MailReaction(), statManager), 65);
                thMail.setListener(gameObjectHandler);
                return thMail;
            case 1:
                TaskHolder thGrades = new TaskHolder("grades", "g.dflt.TaskHolder",
                        new Point(1638, 2295),
                        new Dimension(32, 16),
                        new CommonTask("Noten eintragen", "Noten verwerfen", new GradesReaction(), statManager), 65);
                thGrades.setListener(gameObjectHandler);
                return thGrades;
            case 2:
                TaskHolder thPhone = new TaskHolder("phone", "g.dflt.TaskHolder",
                        new Point(3105, 440),
                        new Dimension(32, 16),
                        new CommonTask("Telefonat annehmen", "Telefonat ablehnen", new PhoneReaction(), statManager), 65);
                thPhone.setListener(gameObjectHandler);
                return thPhone;
            case 3:
                TaskHolder thCourses = new TaskHolder("courses", "g.dflt.TaskHolder",
                        new Point(2320, 1778),
                        new Dimension(32, 16),
                        new CommonTask("Kurse zuordnen", "Kurse löschen", new CoursesReaction(), statManager), 65);
                thCourses.setListener(gameObjectHandler);
                return thCourses;
            case 4:
                TaskHolder thCoursePlan = new TaskHolder("courseplan", "g.dflt.TaskHolder",
                        new Point(1818, 455),
                        new Dimension(32, 16),
                        new CommonTask("Kursplan eintragen", "Kursplan verwerfen", new CoursePlanReaction(), statManager), 65);
                thCoursePlan.setListener(gameObjectHandler);
                return thCoursePlan;
            case 5:
                Player testNPC = new NPC("NPC",  ColorScheme.BLUE);
                testNPC.setListener(gameObjectHandler);
                return testNPC;
            default:
                return null;
        }
    }

    private ArrayList<TexturedObject> tempTaskContainer(){

        ArrayList<TexturedObject> result = new ArrayList<>();

        TaskHolder thMail = new TaskHolder("mail", "g.dflt.TaskHolder",
                new Point(622,2090),
                new Dimension(32,16),
                new CommonTask("Post sortieren", "Post schreddern", new MailReaction(), statManager), 65);
        thMail.setListener(gameObjectHandler);
        result.add(thMail);

        TaskHolder thGrades = new TaskHolder("grades", "g.dflt.TaskHolder",
                new Point(1638, 2295),
                new Dimension(32, 16),
                new CommonTask("Noten eintragen", "Noten verwerfen", new GradesReaction(), statManager), 65);
        thGrades.setListener(gameObjectHandler);
        result.add(thGrades);

        TaskHolder thPhone = new TaskHolder("phone", "g.dflt.TaskHolder",
                new Point(3105, 440),
                new Dimension(32, 16),
                new CommonTask("Telefonat annehmen", "Telefonat ablehnen", new PhoneReaction(), statManager), 65);
        thPhone.setListener(gameObjectHandler);
        result.add(thPhone);

        TaskHolder thCourses = new TaskHolder("courses", "g.dflt.TaskHolder",
                new Point(2320, 1778),
                new Dimension(32, 16),
                new CommonTask("Kurse zuordnen", "Kurse löschen", new CoursesReaction(), statManager), 65);
        thCourses.setListener(gameObjectHandler);
        result.add(thCourses);

        TaskHolder thCoursePlan = new TaskHolder("courseplan", "g.dflt.TaskHolder",
                new Point(1818, 455),
                new Dimension(32, 16),
                new CommonTask("Kursplan eintragen", "Kursplan verwerfen", new CoursePlanReaction(), statManager), 65);
        thCoursePlan.setListener(gameObjectHandler);
        result.add(thCoursePlan);

        Player testNPC = new NPC("NPC",  ColorScheme.BLUE);
        testNPC.setListener(gameObjectHandler);

        result.add(testNPC);

        return result;
    }
}
