package de.jspll.util;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author
 *
 * @version 1.0
 */

public class Logger extends Thread {

    public static ConcurrentLinkedDeque<String> d = new ConcurrentLinkedDeque<>();

    @Override
    public void run() {
        while (true){
            if(!d.isEmpty())
             System.out.println(d.poll());
        }
    }
}
