package de.jspll.data.objects.game.stats;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.TexturedObject;
import de.jspll.graphics.Camera;
import de.jspll.handlers.GameManager;

import java.awt.*;
import java.util.HashMap;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Laura Schmidt
 *
 * @version 1.0
 */
public class StatManager extends TexturedObject {

    private Integer gameScore;
    private Integer roundScore;
    private Integer karmaScore;
    private float roundTime;
    private float remainingTime;
    private GameManager gameManager;

    private float timeSinceUpdate = -1;
    private HashMap<String, Float> activeTasks;

    public StatManager(GameManager gm) {
        this.gameScore = 0;
        this.roundScore = 0;
        this.karmaScore = 0;
        this.gameManager = gm;
        roundTime = gm.getRoundTime();
        this.remainingTime = roundTime;

        channels = new ChannelID[]{ChannelID.INPUT, ChannelID.UI};
    }

    /**
     * <ul>
     *     <li> General format: "statmanager statcommand taskTime remainingTime scoredPoints positive scoredKarma" </li>
     *     <li> Finished task with positve karma: "statmanager 0 taskTime remainingTime scoredPoints 1 scoredKarma" </li>
     *     <li> Finished task with negative karma: "statmanager 0 taskTime remainingTime scoredPoints 0 scoredKarma" </li>
     *     <li> Failed task with negative karma: "statmanager 1 taskTime remainingTime scoredPoints 1 scoredKarma" </li>
     *     <li> Failed task with positive karma: "statmanager 1 taskTime remainingTime scoredPoints 1 scoredKarma" </li>
     * </ul>
     */
    @Override
    public char call(Object[] input) {
        if(input == null || input.length < 1) {
            return 0;
        } else if(input[0] instanceof String && ((String) input[0]).contentEquals("statmanager")) {
            if(input[1] instanceof Integer) {
                switch(StatCommand.getById((Integer) input[1])) {
                    case TASK_FINISHED:
                        callTaskFinished(input, true);
                        break;
                    case TASK_FAILED:
                        callTaskFailed(input, false);
                        break;
                    case UNKNOWN: default:
                        return 0;
                }
            }
        } else if(input[0] instanceof String && ((String)input[0]).equals("activeTask")) {
            activeTasks.put((String)input[1], (float)input[2]);
        }
        return 0;
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        int height = 180;
        if(activeTasks != null)
            height = 180 + activeTasks.size() * 25;

        g.setColor(new Color(255,255,255,200));
        g.fillRect(camera.getWidth() - 250, 0, camera.getWidth(), height);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Kristen ITC", Font.PLAIN, 18));

        // draw game score
        g.setColor(Color.BLACK);
        g.drawString("Spiel-Punkte: " + gameScore, camera.getWidth() - 240, 20);

        // draw Level
        int completionPercentage = Math.round((gameManager.getTaskCompletionPercentage()/gameManager.getLEVEL_COMPLETION_THRESHOLD())*100);
        g.drawString("Level " + gameManager.getLevel() + " - " + (Math.min(completionPercentage, 100)) + "%", camera.getWidth() - 240, 45);

        // draw karma score
        setColorForKarmaScore(g);
        g.drawString("Karma-Punkte: " + karmaScore, camera.getWidth() - 240, 70);

        // draw game score
        g.setColor(Color.BLACK);
        g.drawString("Runden-Punkte: " + roundScore, camera.getWidth() - 240, 95);

        g.drawString("Nächste Aufgabe in: " + Math.round(getParent().getGameManager().getTimeTillNextTask()), camera.getWidth() - 240, 120);

        // draw remaining time
        remainingTime = gameManager.getRemainingTime();
        setColorForRemainingTime(g);
        g.drawString("Verbleibende Zeit: " + (remainingTime >= 0 ? (int) remainingTime : 0),
                camera.getWidth() - 240, 145);

        int todoY = 170;

        g.setColor(Color.BLACK);
        g.drawString("Todo: ",camera.getWidth() - 240, todoY);

        if(activeTasks != null){
            for (String key : activeTasks.keySet()) {
                todoY += 25;
                if(key.equals("Studierendenausweise austeilen")) {
                    g.drawString("Ausweise austeilen" + ": " + Math.round(remainingTime - activeTasks.get(key)),
                            camera.getWidth() - 240, todoY);
                    continue;
                }
                g.drawString(key + ": " + Math.round(remainingTime - activeTasks.get(key)),
                        camera.getWidth() - 240, todoY);
            }
        }
    }

    @Override
    public char update(float elapsedTime) {
        timeSinceUpdate += elapsedTime;
        if(timeSinceUpdate > 1 || timeSinceUpdate == -1){
            timeSinceUpdate = 0;
            activeTasks = new HashMap<>();
            getParent().dispatch(ChannelID.LOGIC, new Object[]{"getTask"});
        }
        return 0;
    }

    /**
     * Sets color for the karma score. If karma is lower then 0 the
     * karma score gets painted in red. If karma is higher then 0 the score
     * gets painted in green. If the karma equals 0 the score gets
     * painted in black.
     *
     * @param g Graphics
     */
    private void setColorForKarmaScore(Graphics g) {
        if(karmaScore < 0) {
            g.setColor(Color.RED);
        } else if (karmaScore > 0) {
            g.setColor(Color.GREEN);
        }
    }

    /**
     * Sets color of the remaining time. If there a 10 seconds or less
     * remaining the remaining time gets red. Otherwise the Color is#
     * set to black.
     *
     * @param g Graphics
     */
    private void setColorForRemainingTime(Graphics g) {
        if(remainingTime <= 10) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLACK);
        }
    }

    private void callTaskFinished(Object[] input, boolean finished) {
        if(input[2] instanceof Integer && input[3] instanceof Integer) {
            int taskDuration = ((Integer) input[2] - (Integer) input[3]);
            roundTime -= taskDuration;
            if(input[4] instanceof Integer && input[5] instanceof Integer && input[6] instanceof Integer) {
                calculateNewRoundScore(taskDuration, (Integer) input[4], finished);
                calculateNewKarmaScore((Integer) input[6], (Integer) input[5] == 1);
            }
        }
    }

    private void callTaskFailed(Object[] input, boolean finished) {
        if(input[2] instanceof Integer && input[3] instanceof Integer) {
            int taskDuration = ((Integer) input[2] - (Integer) input[3]);
            roundTime -= taskDuration;
            if(input[4] instanceof Integer && input[5] instanceof Integer) {
                calculateNewRoundScore(taskDuration, (Integer) input[4], finished);
                calculateNewKarmaScore((Integer) input[5], (Integer) input[5] == 1);
            }
        }
    }

    /**
     * Calculates the new round score.
     *
     * @param taskDuration time the task took
     * @param scoredPoints scored Point
     * @param finished shows if the task was finshed in time
     */
    private void calculateNewRoundScore(Integer taskDuration, Integer scoredPoints, boolean finished) {
        if(finished) {
            roundScore = (taskDuration / 2) * scoredPoints;
        }
    }

    /**
     * Calculates the new karma score.
     *
     * @param scoredKarma karma that got scored, only positive Integer should be passed
     * @param positiveKarma shows if the karma is negative or positive
     */
    private void calculateNewKarmaScore(Integer scoredKarma, boolean positiveKarma) {
        if(positiveKarma) {
            karmaScore += scoredKarma;
        } else {
            karmaScore -= scoredKarma;
        }
    }

    public void updateKarmaScore(int karmaScore) {
        this.karmaScore += karmaScore;
    }

    public void updateRoundScore(int roundScore) {
        this.roundScore += roundScore;
    }

    public Integer getGameScore() {
        return gameScore;
    }

    public Integer getRoundScore() {
        return roundScore;
    }

    public void setGameScore(Integer gameScore) {
        this.gameScore = gameScore;
    }

    public Integer getKarmaScore() {
        return karmaScore;
    }

    public void setKarmaScore(Integer karmaScore) {
        this.karmaScore = karmaScore;
    }

    public void resetScore(){
        this.gameScore += roundScore;
        this.roundScore = 0;
    }
}

enum StatCommand {
    UNKNOWN(-1),
    TASK_FINISHED(0),
    TASK_FAILED(1);

    private Integer id;

    StatCommand(Integer id) {
        this.id = id;
    }

    public static StatCommand getById(Integer id){
        for(StatCommand currStatCommand : values()) {
            if(currStatCommand.id.equals(id)) {
                return currStatCommand;
            }
        }
        return UNKNOWN;
    }
}