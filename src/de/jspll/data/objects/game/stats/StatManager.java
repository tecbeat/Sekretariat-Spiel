package de.jspll.data.objects.game.stats;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.TexturedObject;
import de.jspll.graphics.Camera;
import de.jspll.handlers.GameManager;

import java.awt.*;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Laura Schmidt
 *
 * @version 1.0
 */

public class StatManager extends TexturedObject {

    private Integer roundScore;
    private Integer karmaScore;
    private float roundTime;
    private float remainingTime;
    private GameManager gameManager;

    public StatManager(GameManager gm) {
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
        }
        return 0;
    }

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        // TODO: prettify
        g.setColor(Color.WHITE);
        g.fillRect(camera.getWidth() - 200, 0, 200, 120);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Serif", Font.PLAIN, 18));

        // draw round score
        g.drawString("Round score: " + roundScore, camera.getWidth() - 195, 45);

        // draw karma score
        setColorForKarmaScore(g);
        g.drawString("Karma score: " + karmaScore, camera.getWidth() - 195, 20);

        g.setColor(Color.BLACK);
        g.drawString("Next Task in: " + Math.round(getParent().getGameManager().getTimeTillNextTask()), camera.getWidth() - 195, 70);

        // draw remaining time
        remainingTime = gameManager.getRemainingTime();
        setColorForRemainingTime(g);
        g.drawString("Remaining time: " + (remainingTime >= 0 ? (int) remainingTime : 0),
                camera.getWidth() - 195, 95);

        // TODO: implement what should happen if time is over
        if (remainingTime <= 0) {
            g.setFont(new Font("Serif", Font.PLAIN, 48));
            g.drawString("TIME IS UP", camera.getWidth() / 2 - 100, camera.getHeight() / 2);
        }
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

    public Integer getRoundScore() {
        return roundScore;
    }

    public void setRoundScore(Integer roundScore) {
        this.roundScore = roundScore;
    }

    public Integer getKarmaScore() {
        return karmaScore;
    }

    public void setKarmaScore(Integer karmaScore) {
        this.karmaScore = karmaScore;
    }

    public void resetKarma(){
        this.karmaScore = 0;
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