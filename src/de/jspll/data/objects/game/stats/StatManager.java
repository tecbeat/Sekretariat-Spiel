package de.jspll.data.objects.game.stats;

import de.jspll.data.objects.GameObject;

public class StatManager extends GameObject {

    private Integer roundScore;
    private Integer karmaScore;
    private Long roundTime;

    public StatManager(Long roundTime) {
        this.roundScore = 0;
        this.karmaScore = 0;
        this.roundTime = roundTime;
    }

    /**
     * <pre>
     *     Format:
     * </pre>
     *
     * @param input
     * @return
     */
    @Override
    public char call(Object[] input) {
        // statmanager statcommand taskTime remainingTime scoredPoints positive scoredKarma
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

    private void calculateNewRoundScore(Integer taskDuration, Integer scoredPoints, boolean finished) {
        if(finished) {
            roundScore = (taskDuration / 2) * scoredPoints;
        }
    }

    private void calculateNewKarmaScore(Integer scoredKarma, boolean positiveKarma) {
        if(positiveKarma) {
            karmaScore += scoredKarma;
        } else {
            karmaScore -= scoredKarma;
        }
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

    public Long getRoundTime() {
        return roundTime;
    }

    public void setRoundTime(Long roundTime) {
        this.roundTime = roundTime;
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