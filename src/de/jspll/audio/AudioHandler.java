package de.jspll.audio;

import sun.audio.AudioStream;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Samuel Assmann
 *
 * @version 1.0
 */

public class AudioHandler {
    String trackname = "";

    public void player(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){

                    try {
                        Clip clip = AudioSystem.getClip();
                        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(trackname));
                        clip.open(audioInputStream);
                        clip.addLineListener(new LineListener() {
                            @Override
                            public void update(LineEvent event) {
                                if (clip.getMicrosecondPosition() == clip.getMicrosecondLength() - 4000){
                                    clip.stop();

                                }
                                clip.getMicrosecondPosition();
                            }
                        });
                        long tracklen = clip.getMicrosecondLength();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }){

        }.start();
    }

}
