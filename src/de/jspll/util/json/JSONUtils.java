package de.jspll.util.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Stack;


/**
 * Created by reclinarka on 02-Nov-20.
 */
public class JSONUtils {

    private JSONUtils(){

    }

    public static JSONUtils singleton = new JSONUtils();

    public JSONObject readJSON(String file){
        JSONObject out = new JSONObject();
        InputStream inputStream = this.getClass().getResourceAsStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        String buffer = "";
        String string = null;
        JSONValue value = null;
        short cache = 0;
        boolean stringValue = false;
        Stack<String> stringStack = new Stack<>();
        Stack<JSONArray> arrayBuilder = new Stack<>();
        Stack<HashMap<String,JSONValue>> jsonBuilder = new Stack<>();
        Stack<JSONObject> readObjects = new Stack<>();
        Stack<Mode> modeStack = new Stack<>();
        Stack<SyntaxState> stateStack = new Stack<>();
        SyntaxState state = SyntaxState.NULL;
        Mode mode = Mode.START;

        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        do {

            for (int i = 0; i < line.length(); i++) {
                if(i == 50){
                    int breakpoint = 0;
                }
                switch (mode) {
                    case START:
                        switch (line.charAt(i)){
                            case '{':
                                arrayBuilder.add(new JSONArray());
                                state = SyntaxState.NULL;
                                mode = Mode.OBJECT;
                                i--;
                                break;
                            case '[':
                                mode = Mode.ARRAY;
                                break;
                            case '"':
                                mode = Mode.STRING;
                                i--;
                                break;
                            case '-':
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                mode = Mode.NUMBER;
                                i--;
                                break;
                        }
                        break;
                    case OBJECT:
                        switch (state){

                            case NULL:
                                switch (line.charAt(i)){
                                    case '{':
                                        jsonBuilder.push(new HashMap<>());
                                        state = SyntaxState.CURLY_OPEN;
                                        break;
                                }
                                break;
                            case CURLY_OPEN:
                                switch (line.charAt(i)){
                                    case ' ':
                                        state = SyntaxState.WHITESPACE;
                                        break;
                                }
                                break;
                            case WHITESPACE:
                                switch (line.charAt(i)) {
                                    case '}':
                                        jsonBuilder.peek().put(string,value);
                                        readObjects.push( new JSONObject().setObject( jsonBuilder.pop() ) );
                                        state = SyntaxState.CURLY_CLOSED;

                                        break;
                                    case '"':
                                        stateStack.push(SyntaxState.STRING);
                                        modeStack.push(mode);
                                        mode = Mode.STRING;
                                        break;
                                    case ':':
                                        state = SyntaxState.COLON;
                                        break;


                                }
                                break;
                            case STRING:
                                switch (line.charAt(i)){
                                    case ' ':
                                        state = SyntaxState.WHITESPACE;
                                        break;
                                    case ':':
                                        state = SyntaxState.COLON;
                                        break;
                                }
                                break;
                            case COLON:
                                switch (line.charAt(i)){
                                    case '{':
                                        modeStack.push(mode);
                                        stateStack.push(SyntaxState.VALUE);
                                        state = SyntaxState.NULL;
                                        i--;
                                        break;
                                    case '"':
                                        stringValue = true;
                                        stateStack.push(SyntaxState.VALUE);
                                        modeStack.push(mode);
                                        mode = Mode.STRING;
                                        break;
                                    case '-':
                                    case '0':
                                    case '1':
                                    case '2':
                                    case '3':
                                    case '4':
                                    case '5':
                                    case '6':
                                    case '7':
                                    case '8':
                                    case '9':
                                        stateStack.push(SyntaxState.VALUE);
                                        modeStack.push(mode);
                                        mode = Mode.NUMBER;
                                        i--;
                                        break;
                                    case 't':
                                        stateStack.push(SyntaxState.VALUE);
                                        modeStack.push(mode);
                                        mode = Mode.TRUE;
                                        i--;
                                        break;
                                    case 'f':
                                        stateStack.push(SyntaxState.VALUE);
                                        modeStack.push(mode);
                                        mode = Mode.FALSE;
                                        i--;
                                        break;
                                    case 'n':
                                        stateStack.push(SyntaxState.VALUE);
                                        modeStack.push(mode);
                                        mode = Mode.NULL;
                                        i--;
                                        break;
                                    case '[':
                                        stateStack.push(SyntaxState.VALUE);
                                        modeStack.push(mode);
                                        state = SyntaxState.SQUARE_OPEN;
                                        arrayBuilder.push(new JSONArray());
                                        mode = Mode.ARRAY;
                                        break;

                                }
                                break;
                            case VALUE:
                                switch (line.charAt(i)){
                                    case ',':
                                        if(stringValue){
                                            String val = stringStack.pop();
                                            String key = stringStack.pop();
                                            jsonBuilder.peek().put(key,new JSONValue(val));
                                            stringValue = false;
                                        } else {
                                            jsonBuilder.peek().put(stringStack.pop(),value);
                                        }
                                        state = SyntaxState.COMMA;
                                        break;
                                    case '}':
                                        if(stringValue){
                                            String val = stringStack.pop();
                                            String key = stringStack.pop();
                                            jsonBuilder.peek().put(key,new JSONValue(val));
                                            stringValue = false;
                                        } else {
                                            jsonBuilder.peek().put(stringStack.pop(),value);
                                        }
                                        state = SyntaxState.CURLY_CLOSED;
                                        break;
                                }
                                break;
                            case COMMA:
                                switch (line.charAt(i)){
                                    case ' ':
                                        state = SyntaxState.WHITESPACE;
                                        break;
                                }
                                break;
                            case CURLY_CLOSED:
                                readObjects.push( new JSONObject().setObject( jsonBuilder.pop() ) );
                                if(modeStack.size() > 0){
                                    mode = modeStack.pop();
                                    if(stateStack.size() > 0){
                                        state = stateStack.pop();
                                        if(state == SyntaxState.VALUE){
                                            value = new JSONValue(readObjects.pop());
                                        }
                                    }
                                }
                                i--;
                                break;
                        }
                        break;
                    case ARRAY:
                        //ARRAY NEEDS TO GO HERE
                        switch (state){


                            case VALUE:
                                switch (line.charAt(i)){
                                    case ',':
                                        arrayBuilder.peek().values.add(value);
                                        state = SyntaxState.COMMA;
                                        break;
                                    case ']':
                                        arrayBuilder.peek().values.add(value);
                                        state = SyntaxState.SQUARE_CLOSED;
                                        i--;
                                        break;
                                }
                                break;
                            case COMMA:
                            case NULL:
                            case WHITESPACE:
                            case SQUARE_OPEN:
                                switch (line.charAt(i)){
                                    case ' ':
                                        state = SyntaxState.WHITESPACE;
                                        break;
                                    case '"':
                                        modeStack.push(mode);
                                        stateStack.push(SyntaxState.VALUE);
                                        mode = Mode.STRING;
                                        break;
                                    case '-':
                                    case '0':
                                    case '1':
                                    case '2':
                                    case '3':
                                    case '4':
                                    case '5':
                                    case '6':
                                    case '7':
                                    case '8':
                                    case '9':
                                        stateStack.push(SyntaxState.VALUE);
                                        modeStack.push(mode);
                                        mode = Mode.NUMBER;
                                        i--;
                                        break;
                                    case 't':
                                        stateStack.push(SyntaxState.VALUE);
                                        modeStack.push(mode);
                                        mode = Mode.TRUE;
                                        i--;
                                        break;
                                    case 'f':
                                        stateStack.push(SyntaxState.VALUE);
                                        modeStack.push(mode);
                                        mode = Mode.FALSE;
                                        i--;
                                        break;
                                    case 'n':
                                        stateStack.push(SyntaxState.VALUE);
                                        modeStack.push(mode);
                                        mode = Mode.NULL;
                                        i--;
                                        break;
                                    case '[':
                                        stateStack.push(SyntaxState.VALUE);
                                        modeStack.push(mode);
                                        state = SyntaxState.SQUARE_OPEN;
                                        arrayBuilder.push(new JSONArray());
                                        mode = Mode.ARRAY;
                                        break;
                                    case ']':
                                        state = SyntaxState.SQUARE_CLOSED;
                                        i--;
                                        break;
                                    case '{':
                                        modeStack.push(mode);
                                        mode = Mode.OBJECT;
                                        stateStack.push(SyntaxState.VALUE);
                                        state = SyntaxState.NULL;
                                        i--;
                                        break;


                                }
                                break;
                            case SQUARE_CLOSED:
                                readObjects.push( arrayBuilder.pop() );
                                if(modeStack.size() > 0){
                                    mode = modeStack.pop();
                                    if(stateStack.size() > 0){
                                        state = stateStack.pop();
                                        if(state == SyntaxState.VALUE){
                                            value = new JSONValue(readObjects.pop());
                                        }
                                    }
                                }
                                i--;
                                break;
                        }

                        break;
                    case STRING:
                        switch (line.charAt(i)){
                            case '\"':
                                if(modeStack.size() > 0){
                                    mode = modeStack.pop();
                                    state = stateStack.pop();
                                    stringStack.push("" + buffer);
                                    buffer = "";
                                } else {
                                    mode = Mode.END;
                                }
                                break;
                            default:
                                buffer += line.charAt(i);
                                break;
                        }
                        break;
                    case NUMBER:
                        switch (line.charAt(i)){
                            case '}':
                            case ',':
                            case ' ':
                                value = getNumber(buffer);
                                buffer = "";
                                if(modeStack.size() > 0){
                                    mode = modeStack.pop();
                                    if(stateStack.size() > 0){
                                        state = stateStack.pop();
                                    }
                                }

                                i--;
                                break;
                            default:
                                buffer += line.charAt(i);
                                break;
                        }
                        break;
                    case END:
                        if(readObjects.size() == 1){
                            out = readObjects.pop();
                        }
                        break;

                    case TRUE:
                        switch (line.charAt(i)){
                            case '}':
                            case ',':
                            case ' ':
                                value = new JSONValue<>(true);
                                buffer = "";
                                if(modeStack.size() > 0){
                                    mode = modeStack.pop();
                                    if(stateStack.size() > 0){
                                        state = stateStack.pop();
                                    }
                                }

                                i--;
                                break;
                        }
                        break;
                    case FALSE:
                        switch (line.charAt(i)){
                            case '}':
                            case ',':
                            case ' ':
                                value = new JSONValue<>(false);
                                buffer = "";
                                if(modeStack.size() > 0){
                                    mode = modeStack.pop();
                                    if(stateStack.size() > 0){
                                        state = stateStack.pop();
                                    }
                                }

                                i--;
                                break;
                        }
                        break;
                    case NULL:
                        switch (line.charAt(i)){
                            case '}':
                            case ',':
                            case ' ':
                                value = null;
                                buffer = "";
                                if(modeStack.size() > 0){
                                    mode = modeStack.pop();
                                    if(stateStack.size() > 0){
                                        state = stateStack.pop();
                                    }
                                }

                                i--;
                                break;
                        }
                        break;
                }

            }

            try {
                line = reader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (line != null);

        if(state == SyntaxState.CURLY_CLOSED && jsonBuilder.size() == 1){
            readObjects.push( new JSONObject().setObject( jsonBuilder.pop() ) );
        }

        if(readObjects.size() == 1)
            return readObjects.pop();
        return out;
    }

    private JSONValue getNumber(String number){
        if(number.contains(".")) return new JSONValue(Float.parseFloat(number));
        return new JSONValue(Integer.parseInt(number));
    }

    private enum SyntaxState{
        NULL,
        CURLY_OPEN,
        WHITESPACE,
        STRING,
        COLON,
        VALUE,
        COMMA,
        CURLY_CLOSED,
        SQUARE_OPEN,
        SQUARE_CLOSED

    }


    private enum Mode{
        START,
        OBJECT,
        ARRAY,
        STRING,
        NUMBER,
        END,
        TRUE,
        FALSE,
        NULL

    }

}

