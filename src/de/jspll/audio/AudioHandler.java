package de.jspll.audio;

import de.jspll.graphics.ResourceHandler;
import de.jspll.handlers.GameObjectHandler;

import javax.sound.sampled.*;
import java.io.File;
import java.util.Random;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Samuel Assmann, Lukas Becker
 *
 * @version 1.0
 */

public class AudioHandler {

    /**
     *
     */
    public void playMusic(GameObjectHandler gh) {
        ResourceHandler rh = gh.getResourceHandler();
        Random randomGenerator = new Random(System.currentTimeMillis());
        new Thread(() -> {
            try {
                while(true) {
                    Clip clip = rh.getAudioByName(AudioFileName.getFileById(randomGenerator.nextInt(AudioFileName.length())));

                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    double gain = 0.05;
                    float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
                    gainControl.setValue(dB);

                    long clipLength = clip.getMicrosecondLength();
                    clipLength = (clipLength / 1000) - 7000;
                    System.out.println("(length) " + clipLength / 60000 + ":" + (clipLength / 1000) % 60);

                    clip.start();

                    try {
                        Thread.sleep(clipLength);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }){

        }.start();
    }
}

enum AudioFileName {
    File_1(0, "/assets/audio/Dag_Reinbott-Beautiful_Mood.wav"),
    File_2(1, "/assets/audio/Dag_Reinbott-Dance_of_the_Imps.wav"),
    File_3(2, "/assets/audio/Dag_Reinbott-Hopeful_Beginning.wav"),
    File_4(3, "/assets/audio/Dag_Reinbott-Melancholic_Sunday.wav"),
    File_5(4, "/assets/audio/Dag_Reinbott-Criminal_Cat.wav"),
    /*File_6(5, "/assets/audio/Dag_Reinbott-A_brilliant_Idea.wav"),
    File_7(6, "/assets/audio/Dag_Reinbott-Accelerator.wav"),
    File_8(7, "/assets/audio/Dag_Reinbott-Bad_Habit.wav"),
    File_9(8, "/assets/audio/Dag_Reinbott-Basic_Motion.wav"),
    File_10(9, "/assets/audio/Dag_Reinbott-Hardstyle.wav"),*/
    ;

    private int id;
    private String filepath;

    private AudioFileName(int id, String file){
        this.id = id;
        this.filepath = file;
    }

    public static String getFileById(int id){
        for(AudioFileName i : values()){
            if(i.id == id){
                return i.filepath;
            }
        }

        return null;
    }

    public static int length(){
        return values().length;
    }
}
