package de.jspll.util;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by reclinarka on 12-Nov-20.
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
