package de.jspll.logic;

import de.jspll.frames.SubHandler;
import de.jspll.graphics.GraphicsHandler;
import sun.text.normalizer.Trie;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by reclinarka on 05-Oct-20.
 */
public class LogicHandler implements SubHandler {
    public String ID = "main";

    private InputHandler inputHandler = new InputHandler(this);

    private InteractableGroup content = new InteractableGroup();

    public LogicHandler(GraphicsHandler graphicsHandler){
        graphicsHandler.setInputListener(inputHandler);
    }

    public void execute(float elapsedTime){

    }

    public void dispatch(String targetID,Object[] input){
        if(targetID.contentEquals(ID)){

        } else {
            content.call(targetID, input);
        }
    }

    public void drop(String id){
        content.drop(id);
    }

    public void add(String iD, Interactable input){
        content.add(iD, input);
    }
}

class InteractableGroup implements Interactable{
    String ID;
    LogicHandler listener;
    private HashMap<String, Interactable> content;

    @Override
    public void setListener(LogicHandler listener) {
        this.listener = listener;
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public char call(String targetID, Object[] input) {
        String[] subID = targetID.split("[.]");
        if(subID.length > 1){
            return content.get(subID[0]).call(targetID.substring(subID[0].length()), input);
        } else {
            return content.get(targetID).call(targetID,input);
        }

    }

    public void add(String iD, Interactable input){
        String[] subID = iD.split("[.]");
        if(subID.length > 1){
            ((InteractableGroup) content.get(subID[0]) ).add(iD.substring(subID[0].length()), input);
        } else {
            content.put(input.getID(),input);
        }

    }

    public void drop(String iD){
        String[] subID = iD.split("[.]");
        if(subID.length > 1){
            ((InteractableGroup) content.get(subID[0]) ).drop(iD.substring(subID[0].length()));
        } else {
            content.remove(iD);
        }
    }
}
