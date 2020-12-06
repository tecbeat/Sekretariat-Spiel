package de.jspll.handlers;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.TexturedObject;
import de.jspll.data.objects.game.player.ColorScheme;
import de.jspll.data.objects.game.player.NPC;
import de.jspll.data.objects.game.player.Player;
import de.jspll.data.objects.game.stats.StatManager;
import de.jspll.data.objects.game.tasks.*;
import de.jspll.data.objects.game.tasks.reactions.*;
import de.jspll.graphics.Camera;
import de.jspll.graphics.ResourceHandler;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Random;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The Game Manager handles basic functions such as difficulty, play and pause
 *
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Lukas Becker, Laura Schmidt
 *
 * @version 1.0
 */
public class GameManager extends TexturedObject {
    //Balancing
    private final float ROUND_TIME = 10f;
    private final int NEXT_TASK_TRESHOLD = 30;
    private final int BASE_TASKS = 2;
    private final int TASKS_PER_LEVEL = 4;
    private final float LEVEL_COMPLETION_TRESHOLD = 0.7f;

    //Game interruptions
    private boolean resultScreen = false;
    private boolean pauseScreen = false;
    private boolean pauseForbiddenScreen = false;
    private float pauseForbiddenTime = 0;
    private boolean gameRunning = false;

    //references
    private GameObjectHandler gameObjectHandler;
    private StatManager statManager = new StatManager(this);
    private Random randomGenerator = new Random(System.currentTimeMillis());
    private HashMap<String, AtomicBoolean> keyMap;
    private Player player;


    private int level = 0;
    private float time = 0f;
    private ArrayList<Integer> activeTaskIdentifiers = new ArrayList<>();
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
    int[] buttonSize = new int[]{120,30};

    // screen graphics
    // 0-2: path to textures for pause-screen, 3-15: path textures for round-screen
    private String[] textureKeys;
    private BufferedImage[] textures;
    private boolean texturesLoaded = false;


    public GameManager(GameObjectHandler gameObjectHandler){
        this.gameObjectHandler = gameObjectHandler;
        setTextureKeys();
        channels = new ChannelID[]{ChannelID.LOGIC, ChannelID.UI};
    }

    /**
     * update overrides game object update
     * Adds tasks and stops game if time over
     *
     * @param elapsedTime elapsed time
     * @return
     */
    @Override
    public char update(float elapsedTime) {
        if(gameRunning) {

            if(player == null){
                gameObjectHandler.dispatch(ChannelID.PLAYER, new Object[]{"playerObject", ChannelID.LOGIC});
            }

            try {
                if(keyMap == null){
                    keyMap = gameObjectHandler.getLogicHandler().getInputHandler().getKeyMap();
                }
            } catch (Exception e){
                e.printStackTrace();
            }

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

            if(keyMap != null && keyMap.get("p").get()){
                pauseGame();
            }

            if(keyMap != null && keyMap.get("ESC").get()){
                pauseForbiddenStart();
            }

            if(pauseForbiddenScreen && pauseForbiddenTime <= time){
                pauseForbiddenEnd();
            }

            statManager.update(elapsedTime);

        } else {
            if(keyMap != null && keyMap.get("r").get()){
                resumeGame();
            }
        }
        return 0;
    }

    private void pauseForbiddenEnd() {
        pauseForbiddenScreen = false;
        gameObjectHandler.subscribe(player, ChannelID.LOGIC);
        //TODO: WHY do I need this??
        gameObjectHandler.loadStatManager(statManager);
    }

    private void pauseForbiddenStart() {
        pauseForbiddenScreen = true;
        pauseForbiddenTime = time + 2;
        gameObjectHandler.unsubscribe(player, ChannelID.LOGIC);
        gameObjectHandler.loadObject(this);
    }

    @Override
    public char call(Object[] input) {
        if (input == null || input.length < 1) {
            return 0;
        } else if (input[0] instanceof String) {
            String cmd = (String) input[0];
            if (cmd.contentEquals("playerObj") && input[1] instanceof Player) {
                player = (Player) input[1];
            }
        }
        return 0;
    }

    /**
     * only paints something if the level is over or paused
     * @param g Graphics
     * @param elapsedTime elapsed time
     * @param camera camera
     * @param currStage current stage
     */
    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        if(!texturesLoaded) {
            loadTextures();
        }

        if(resultScreen) {
            initResultScreen(g, camera);
            setUpButton(g);
            checkClick();
        }

        if(pauseScreen) {
            pauseScreen(g,camera, true);
        }

        if(pauseForbiddenScreen){
            pauseScreen(g,camera,false);
        }
    }

    public StatManager getStatManager() {
        return statManager;
    }

    /**
     * @return TaskCount calculated by balancing constants
     */
    private int getTaskCountForCurrentLevel(){
        return BASE_TASKS + (level * TASKS_PER_LEVEL);
    }


    /**
     * @return Completion Percentage
     */
    private float getTaskCompletionPercentage(){
        return (float)completedTasks/getTaskCountForCurrentLevel();
    }

    /**
     * @return When to add new task
     */
    public float getTimeTillNextTask(){
        if(taskCount == getTaskCountForCurrentLevel()) return 0;
        return (NEXT_TASK_TRESHOLD / level) - time;
    }

    /**
     * Gets task and adds it through gameObjectHandler
     */
    private void addTaskToCurrentLevel(){
        ChannelID scene = ChannelID.SCENE_GAME;
        TexturedObject task = getRandomTask();
        gameObjectHandler.loadTask(scene, task);
    }

    /**
     * Increase counter to keep track for when the game ends
     */
    public void taskCompleted(){
        this.completedTasks++;
    }


    /**
     * Increments level, starts task addition, resets time, task count and karma
     */
    public void startGame(){
        this.level++;
        this.gameRunning = true;
        this.remainingTime = ROUND_TIME;
        this.taskCount = 0;
        this.completedTasks = 0;
        statManager.resetKarma();
        for(int i = 0; i< BASE_TASKS; i++)
            addTaskToCurrentLevel();
        //Level Loading resets the ui channel, so the statManager needs to get loaded again
        gameObjectHandler.loadStatManager(statManager);
    }

    /**
     * Stops Countdowns, disables input
     */
    private void stopLevel(){
        this.gameRunning = false;
        gameObjectHandler.clearScene(ChannelID.LOGIC);
        gameObjectHandler.loadObject(this);
        this.resultScreen = true;
    }

    private void pauseGame(){
        gameObjectHandler.unsubscribe(player, ChannelID.LOGIC);
        gameRunning = false;
        pauseScreen = true;
        gameObjectHandler.loadObject(this);
    }

    private void resumeGame(){
        gameObjectHandler.subscribe(player, ChannelID.LOGIC);
        gameRunning = true;
        pauseScreen = false;
        //TODO I have no idea why this is needed:
        gameObjectHandler.loadStatManager(statManager);
    }

    /**
     * Initializes the basic task screen. Background gets darker. White rectangle gets drawn.
     *
     * @param g Graphics
     * @param camera Camera
     */
    public void initResultScreen(Graphics g, Camera camera) {
        if(!texturesLoaded){
            loadTextures();
        }
        g.setFont(new Font("Kristen ITC", Font.PLAIN, 42));
        Graphics2D g2d = (Graphics2D) g;
        initScreenProperties(g2d);

        g.setColor(new Color(46, 49, 49,200));
        g.fillRect(0, 0, camera.getWidth(), camera.getHeight());

        //Bounding Box
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, screenWidth, screenHeight);

        g2d.drawImage(getRightPentagram(), 100,
                (screenHeight / 2) - ((getRightPentagram().getHeight() / 2) / 2),
                getRightPentagram().getWidth() / 2,
                getRightPentagram().getWidth() / 2,
                null);

        // TODO: add round information to round-screen
        g.setColor(Color.BLACK);
        g.setFont(new Font("Kristen ITC", Font.BOLD, 42));
        g.drawString(getTaskCompletionPercentage() > LEVEL_COMPLETION_TRESHOLD ? "Level erfolgreich" : "Level gescheitert",
                (boundingX + (screenWidth / 2) / 4) + 85,
                (screenHeight / 2) - ((getRightPentagram().getHeight() / 2) / 2) + 20);
        g.setFont(new Font("Kristen ITC", Font.PLAIN, 42));
        g.drawString("Karma-Punkte: " + statManager.getKarmaScore(),
                (boundingX + (screenWidth / 2) / 4) + 85,
                (screenHeight / 2) - ((getRightPentagram().getHeight() / 2) / 2) + 100);
        g.drawString("Spiel-Punkte: " + statManager.getGameScore(),
                (boundingX + (screenWidth / 2) / 4) + 85,
                (screenHeight / 2) - ((getRightPentagram().getHeight() / 2) / 2) + 180);
        g.drawString("Abgeschlossene Aufgaben: " + completedTasks,
                (boundingX + (screenWidth / 2) / 4) + 85,
                (screenHeight / 2) - ((getRightPentagram().getHeight() / 2) / 2) + 260);
    }

    /**
     * Initializes the basic pause screen. Background gets darker. White rectangle gets drawn.
     *
     * @param g Graphics
     * @param camera Camera
     */
    private void pauseScreen(Graphics g, Camera camera, boolean actualPause) {
        g.setFont(new Font("Kristen ITC", Font.PLAIN, 30));
        Graphics2D g2d = (Graphics2D) g;
        initScreenProperties(g2d);

        if(actualPause) {
            g2d.drawImage(textures[2], 0, 0, screenWidth, screenHeight, null);
            g.setColor(Color.BLACK);
            g.drawString("Spiel pausiert", (screenWidth / 2), screenHeight / 2);
            g.drawString("Weiter gehts mit der Taste \"R\"!", (screenWidth / 2), (screenHeight / 2) + 50);
        } else {
            g2d.drawImage(textures[0], 0, 0, screenWidth, screenHeight, null);
        }
    }

    private void initScreenProperties(Graphics2D g2d) {
        screenWidth = (int) g2d.getClipBounds().getWidth();
        screenHeight = (int) g2d.getClipBounds().getHeight();
        boundingX = screenWidth / 4;
        boundingY = screenHeight / 4;
        btnStartX = (boundingX + (screenWidth / 2) / 4) + 85;
        btnStartY = (int) (boundingY + (screenHeight / 2) * 0.8);
    }

    /**
     * Sets up game ended button
     *
     * @param g Graphics
     */
    private void setUpButton(Graphics g) {
        g.setFont(new Font("Kristen ITC", Font.PLAIN, 14));
        String heading = getTaskCompletionPercentage() > LEVEL_COMPLETION_TRESHOLD ? "Nächstes Level" : "Hauptmenu";

        g.setColor(getTaskCompletionPercentage() > LEVEL_COMPLETION_TRESHOLD ? Color.GREEN : Color.RED);
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
        if(gameObjectHandler.getMousePos() != null) {
            mousePos[0] = (int) gameObjectHandler.getMousePos().getX();
            mousePos[1] = (int) gameObjectHandler.getMousePos().getY();
        }
    }

    /**
     * Returns the right pentagram-image according to the karma-score.
     *
     * @return pentagram-image
     */
    private BufferedImage getRightPentagram() {
        int karmaScore = statManager.getKarmaScore();
        int taskCompleted = getTaskCountForCurrentLevel();
        float karmaPerTask = (float) karmaScore / taskCompleted;
        float percentage = (karmaPerTask / 30) * 100;

        if(percentage >= -100 && percentage < -90) {
            return textures[3];
        } else if (percentage > -90 && percentage < -70) {
            return textures[4];
        } else if (percentage > -70 && percentage < -50) {
            return textures[5];
        } else if (percentage > -50 && percentage < -30) {
            return textures[6];
        } else if (percentage > -30 && percentage < -15) {
            return textures[7];
        } else if (percentage > -15 && percentage < 0) {
            return textures[8];
        } else if (percentage == 0) {
            return textures[9];
        } else if (percentage > 0 && percentage < 15) {
            return textures[10];
        } else if (percentage > 15 && percentage < 30) {
            return textures[11];
        } else if (percentage > 30 && percentage < 50) {
            return textures[12];
        } else if (percentage > 50 && percentage < 70) {
            return textures[13];
        } else if (percentage > 70 && percentage < 90) {
            return textures[14];
        } else if (percentage > 90 && percentage <= 100){
            return textures[15];
        } else {
            return textures[9];
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

    private void setTextureKeys() {
        textureKeys = new String[]{
                "/assets/screen/pause/KeinePause",
                "/assets/screen/pause/Pause1",
                "/assets/screen/pause/Pause2",
                "/assets/screen/round/penta0",
                "/assets/screen/round/penta1",
                "/assets/screen/round/penta2",
                "/assets/screen/round/penta3",
                "/assets/screen/round/penta4",
                "/assets/screen/round/penta5",
                "/assets/screen/round/penta6",
                "/assets/screen/round/penta7",
                "/assets/screen/round/penta8",
                "/assets/screen/round/penta9",
                "/assets/screen/round/penta10",
                "/assets/screen/round/penta11",
                "/assets/screen/round/penta12",
        };
    }

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

    public void loadTextures() {
        if (textureKeys == null)
            return;
        if (textures == null)
            textures = new BufferedImage[textureKeys.length];
        for (int i = 0; i < textureKeys.length; i++) {
            if (textures[i] == null)
                textures[i] = getParent().getResourceHandler().getTexture(textureKeys[i], ResourceHandler.FileType.PNG);
        }
        for(BufferedImage currImage : textures) {
           if(currImage == null) {
               texturesLoaded = false;
               return;
           }
        }
        texturesLoaded = true;
    }

    @Override
    public void requestTexture() {
        if (textureKeys == null)
            return;
        for (String s : textureKeys) {
            getParent().getResourceHandler().requestTexture(s, ResourceHandler.FileType.PNG);
        }
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

    public void removeTaskFromActiveList(){
        activeTaskIdentifiers.remove(0);
    }

    int instanceCount = 0;

    /**
     * @return one random task
     */
    private TexturedObject getRandomTask(){
        final int NPC_ID = 3;

        int attemptCount = 0; //if there are no tasks available add an npc

        int id;
        do {
            id = randomGenerator.nextInt(10);

            if (getParent().isInternetTaskDone()) {
                id = randomGenerator.nextInt(6);
            }
            attemptCount++;
            if(attemptCount > 30)
                id = NPC_ID;
        } while (!(!(activeTaskIdentifiers.contains(id)) || id == NPC_ID));
        if(id != NPC_ID)
            activeTaskIdentifiers.add(id);
        instanceCount++;

        switch (id){
            case 0:
                TaskHolder thMail = new TaskHolder("mail" + instanceCount, "g.dflt.TaskHolder",
                        new Point(622,2090),
                        new Dimension(32,16),
                        new CommonTask("Post sortieren", "Post schreddern", new MailReaction(), statManager,
                                new String[]{"/assets/task/image/mail_pic","/assets/task/image/mail_drag"}), 65);
                thMail.setListener(gameObjectHandler);
                return thMail;
            case 1:
                TaskHolder thGrades = new TaskHolder("grades" + instanceCount, "g.dflt.TaskHolder",
                        new Point(1638, 2295),
                        new Dimension(32, 16),
                        new CommonTask("Noten eintragen", "Noten verwerfen", new GradesReaction(), statManager,
                                new String[]{"/assets/task/image/grades_pic","/assets/task/image/grades_drag"}), 65);
                thGrades.setListener(gameObjectHandler);
                return thGrades;
            case 2:
                TaskHolder thPhone = new TaskHolder("phone" + instanceCount, "g.dflt.TaskHolder",
                        new Point(3105, 440),
                        new Dimension(32, 16),
                        new CommonTask("Telefonat annehmen", "Telefonat ablehnen", new PhoneReaction(),
                                statManager, new String[]{"/assets/task/image/phone_pic","/assets/task/image/phone_drag"}), 65);
                thPhone.setListener(gameObjectHandler);
                return thPhone;
            case NPC_ID:
                NPC thNPCTask = new NPC("TaskNPC" + instanceCount, "g.ntt.NPC", ColorScheme.PURPLE_MAN,
                        new TaskHolder("NPC " + instanceCount, "g.dflt.TaskHolder",
                        new Point(1280, 1120),
                        new Dimension(32, 16),
                        new NPCTask("friendly interaction","unfriendly interaction", new NPCReaction(),
                                statManager, instanceCount % 2 == 0), 65));
                thNPCTask.setListener(gameObjectHandler);
                thNPCTask.requestTexture();
                return thNPCTask;

            case 4:
                TaskHolder thStudentCard = new TaskHolder("studentcard" + instanceCount, "g.dflt.TaskHolder",
                        new Point(1280, 1760),
                        new Dimension(32, 16),
                        new CommonTask("Studierendenausweise austeilen", "Studierendenausweise schreddern",
                                new StudentCardReaction(), statManager), 65);
                thStudentCard.setListener(gameObjectHandler);
                return thStudentCard;
            case 5:
                TaskHolder thEOB = new TaskHolder("eob" + instanceCount, "g.dflt.TaskHolder",
                        new Point(2430, 2335),
                        new Dimension(32, 16),
                        new CommonTask("Feierabend machen", new EOBReaction(), statManager), 65);
                thEOB.setListener(gameObjectHandler);
                return thEOB;
            case 6:
                TaskHolder thInternet = new TaskHolder("internet" + instanceCount, "g.dflt.TaskHolder",
                        new Point(750, 656),
                        new Dimension(32, 16),
                        new CommonTask("Internet löschen", new InternetReaction(), statManager), 65);
                thInternet.setListener(gameObjectHandler);
                return thInternet;
            case 7:
                TaskHolder thCoursePlan = new TaskHolder("courseplan" + instanceCount, "g.dflt.TaskHolder",
                        new Point(1818, 455),
                        new Dimension(32, 16),
                        new CommonTask("Kursplan eintragen", "Kursplan verwerfen", new CoursePlanReaction(), statManager,
                                new String[]{"/assets/task/image/courseplan_pic","/assets/task/image/courseplan_drag"}), 65);
                thCoursePlan.setListener(gameObjectHandler);
                return thCoursePlan;
            case 8:
                TaskHolder thEMail = new TaskHolder("email" + instanceCount, "g.dflt.TaskHolder",
                        new Point(2750, 1536),
                        new Dimension(32, 16),
                        new CommonTask("Mails löschen", new EMailReaction(), statManager), 65);
                thEMail.setListener(gameObjectHandler);
                return thEMail;
            case 9:
                TaskHolder thCourses = new TaskHolder("courses" + instanceCount, "g.dflt.TaskHolder",
                        new Point(2320, 1778),
                        new Dimension(32, 16),
                        new CommonTask("Kurse zuordnen", "Kurse löschen", new CoursesReaction(), statManager,
                                new String[]{"/assets/task/image/course_pic","/assets/task/image/course_drag"}), 65);
                thCourses.setListener(gameObjectHandler);
                return thCourses;
            default:
                return null;
        }
    }

    public void setRemainingTime(float remainingTime) {
        this.remainingTime = remainingTime;
    }
}
