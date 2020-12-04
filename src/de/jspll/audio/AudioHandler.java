package de.jspll.audio;

import javax.sound.sampled.*;
import java.io.File;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Samuel Assmann
 *
 * @version 1.0
 */

public class AudioHandler {
    File[] files;

    public void playMusic() {
        new Thread(() -> {
            File dir = new File("assets/audio/");
            files = dir.listFiles();
            try {
                if (files != null) {
                    for (File f : files) {
                        if (f.exists()) {
                            System.out.println("(Play) " + f.getAbsolutePath());

                            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(f);
                            Clip clip = AudioSystem.getClip();
                            clip.open(audioInputStream);

                            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                            double gain = 0.05;
                            float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
                            gainControl.setValue(dB);

                            long clipLength = clip.getMicrosecondLength();
                            clipLength = (clipLength/1000)-7000;
                            System.out.println("(length) " + clipLength/60000 + ":" + (clipLength/1000)%60);

                            clip.start();

                            try {
                                Thread.sleep(clipLength);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("File dosent Exists");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }){

        }.start();
    }
}
