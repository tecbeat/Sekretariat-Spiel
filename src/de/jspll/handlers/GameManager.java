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
import de.jspll.data.objects.game.tasks.reactions.*;
import java.util.Random;

import java.awt.*;
import java.util.ArrayList;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Lukas Becker
 *
 * @version 1.0
 */

public class GameManager extends GameObject {
    private StatManager statManager;
    private int level= 0;
    private ArrayList<TexturedObject> tasks;
    Random randomGenerator = new Random(System.currentTimeMillis());
    private GameObjectHandler gameObjectHandler;

    private float time = 0f;

    private boolean gameRunning = false;
    private int taskCount = 0;


    public GameManager (GameObjectHandler gameObjectHandler, StatManager statManager){
        this.gameObjectHandler = gameObjectHandler;
        this.statManager = statManager;
        tasks = this.tempTaskContainer();
        channels = new ChannelID[]{ChannelID.LOGIC};
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
        }
        return 0;
    }

    public StatManager getStatManager() {
        return statManager;
    }

    private int getTaskCountForCurrentLevel(){
        return 2 + (level * 4);
    }

    public float getTimeTillNextTask(){
        if(taskCount == getTaskCountForCurrentLevel()) return 0;
        return (30/level) - time;
    }

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

    private TexturedObject getRandomTask(){
        return tasks.get(randomGenerator.nextInt(tasks.size()));
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

    private void addTaskToCurrentLevel(){
        ChannelID scene = gameObjectHandler.getActiveScene();
        TexturedObject task = getRandomTask();
        gameObjectHandler.loadTask(scene, task);
        System.out.println("Added: " + task.toString() + " Task!");
    }

    private void addAllTasksToCurrentLevel(){
        ChannelID scene = gameObjectHandler.getActiveScene();
        for(TexturedObject object : getTasksforCurrentLevel()){
            gameObjectHandler.loadTask(scene, object);
        }
    }

    public int getLevel(){
        return this.level;
    }

    //Increments leve, starts task addition
    public void startGame(){
        this.level++;
        this.gameRunning = true;
        //Twice, because there shall be two basis tasks
        //Could be done in a loop, but honestly this was easier
        addTaskToCurrentLevel();
        addTaskToCurrentLevel();
    }
}
