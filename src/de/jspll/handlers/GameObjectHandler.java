package de.jspll.handlers;

import com.google.gson.*;
import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.GameTrie;
import de.jspll.data.objects.TexturedObject;
import de.jspll.data.objects.game.map.TileMap;
import de.jspll.data.objects.loading.LoadingBar;
import de.jspll.data.objects.loading.LoadingCircle;
import de.jspll.data.objects.loading.ProgressReporter;
import de.jspll.data.objects.loading.Report;
import de.jspll.graphics.Camera;
import de.jspll.graphics.ResourceHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static de.jspll.data.ChannelID.INSTANCE_REGISTER;
import static de.jspll.data.ChannelID.LAST_CHANNEL;
import static de.jspll.data.ChannelID.SCENE_LOADING;

/**
 * Created by reclinarka on 21-Oct-20.
 */
public class GameObjectHandler{
    public GameObjectHandler() {

        for (int i = 0; i < channels.length; i++) {
            channels[i] = new GameTrie();
        }
        loadMap("assets\\map\\Sekretariat-Spiel-Plan_v2.json");
        resourceHandler.start();
    }

    public Point getMousePos() {
        if (graphicsHandler == null)
            return null;
        return graphicsHandler.getMousePos();
    }

    public Camera getSelectedCamera() {
        return graphicsHandler.getSelectedCamera();
    }

    public void loadScene(ChannelID scene, ArrayList<GameObject> objects){
        for(GameObject obj: objects){
            loadObject(obj);
            subscribe(obj,scene);
        }
    }

    public void setup(){
        ArrayList<GameObject> loadingSceneBuilder = new ArrayList<>();
        loadingSceneBuilder.add(new LoadingCircle("LdC","system.loading",
                getGraphicsHandler().getWindow().getWidth() / 2,
                getGraphicsHandler().getWindow().getHeight() / 2,
                20, 150,new Dimension(40,40)));


        loadScene(SCENE_LOADING,loadingSceneBuilder);
    }

    public void switchScene(ChannelID newScene){
        unsubscribeScene(activeScene);
        subscribeScene(newScene);
        activeScene = newScene;
    }

    public void subscribeScene(ChannelID scene){
        for(GameObject obj: channels[scene.valueOf()].allValues()){
            subscribe(obj);
        }
    }

    public void unsubscribeScene(ChannelID scene){
        for(GameObject obj: channels[scene.valueOf()].allValues()){
            unsubscribe(obj);
        }
    }

    public void deleteScene(ChannelID scene){
        for(GameObject obj: channels[scene.valueOf()].allValues()){
            delete(obj);
        }
    }

    public boolean emptyScene(ChannelID scene){
        if(scene == activeScene || channels[scene.valueOf()].isEmpty()){
            return false;
        }
        deleteScene(scene);
        channels[scene.valueOf()].dropAll();
        return true;
    }

    public Dimension getScreenDimensions() {
        return graphicsHandler.getWindow().getSize();
    }

    public GraphicsHandler getGraphicsHandler() {
        return graphicsHandler;
    }

    public ResourceHandler getResourceHandler() {
        return resourceHandler;
    }

    private ResourceHandler resourceHandler = new ResourceHandler(this);
    private GraphicsHandler graphicsHandler;
    private LogicHandler logicHandler;
    private AtomicBoolean loadingScene = new AtomicBoolean(false);

    public LogicHandler getLogicHandler() {
        return logicHandler;
    }

    public void setLogicHandler(LogicHandler logicHandler) {
        this.logicHandler = logicHandler;
    }

    public static boolean DEBUG = true;

    private ChannelID activeScene = SCENE_LOADING;

    private GameTrie[] channels = new GameTrie[LAST_CHANNEL.valueOf() + 1];

    public void setGraphicsHandler(GraphicsHandler graphicsHandler) {
        this.graphicsHandler = graphicsHandler;
    }

    public void register(GameObject item) {
        channels[INSTANCE_REGISTER.valueOf()].insert(item.getID(), item);
        item.setListener(this);
    }

    public void subscribe(GameObject item) {
        if (item.getChannels() != null && item.getChannels().length > 0) {
            for (ChannelID id : item.getChannels()) {
                if (id == INSTANCE_REGISTER)
                    return;
                this.channels[id.valueOf()].insert(item.getID(), item);
            }
        }
    }

    public void subscribe(GameObject item, ChannelID channel) {
        if (channel == INSTANCE_REGISTER)
            return;
        channels[channel.valueOf()].insert(item.getID(), item);
    }

    public void subscribe(GameObject item, ChannelID channel, String id) {
        if (channel == INSTANCE_REGISTER)
            return;
        channels[channel.valueOf()].insert(id, item);
    }

    public void unsubscribe(GameObject item) {
        for (ChannelID id : item.getChannels()) {
            if (id == INSTANCE_REGISTER)
                continue;
            this.channels[id.valueOf()].delete(item.getID());
        }
    }

    public void unsubscribe(GameObject item, ChannelID channel) {
        if (channel == INSTANCE_REGISTER)
            return;
        channels[channel.valueOf()].delete(item.getID());
    }

    public void unsubscribe(ChannelID channel, String id) {
        if (channel == INSTANCE_REGISTER)
            return;
        channels[channel.valueOf()].delete(id);
    }

    public void delete(GameObject item) {
        unsubscribe(item);
        channels[INSTANCE_REGISTER.valueOf()].delete(item.getID());
    }

    public void dispatch(ChannelID[] targets, String scope, Object[] input) {
        for (ChannelID target : targets) {
            dispatch(target, scope, input);
        }
    }

    public void dispatch(ChannelID target, String scope, Object[] input) {
        for (GameObject object : channels[target.valueOf()].allValuesAfter(scope)) {
            object.call(input);
        }
    }

    public void dispatch(ChannelID target, Object[] input) {
        for (GameObject object : channels[target.valueOf()].allValues()) {
            object.call(input);
        }
    }

    public GameTrie getChannel(ChannelID channel) {
        return channels[channel.valueOf()];
    }

    public ChannelID getActiveScene() {
        return activeScene;
    }

    public void loadObjects(ArrayList<GameObject> objects) {
        for (GameObject object : objects) {
            loadObject(object);
        }
    }

    public void loadObject(GameObject object) {
        register(object);
        subscribe(object);
        if(object instanceof TexturedObject){
            TexturedObject obj = (TexturedObject) object;

            try {
                obj.requestTexture();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void loadScene(ChannelID scene, JsonArray objects){
        ArrayList<GameObject> out = new ArrayList<>();
        ProgressReporter pRpt = new Report();
        pRpt.setCount(objects.size() + 1);
        pRpt.setNextScene(scene);
        LoadingBar lb = new LoadingBar(pRpt);
        lb.setMessage("loading objects...");
        this.subscribe(lb);
        final GameObjectHandler goh = this;

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(JsonElement jsonObject: objects){
                    GameObject go = JSONSupport.fromJsonToGameObject(jsonObject);
                    out.add(go);
                    go.setListener(goh);
                    if(go instanceof TexturedObject){
                        TexturedObject to = (TexturedObject) go;
                        to.requestTexture();
                    }
                    pRpt.update();


                }
                lb.setMessage("loading textures...");
                boolean waitingForTexture = true;
                while(waitingForTexture){
                    waitingForTexture = false;
                    for(GameObject obj: out){
                        if(obj instanceof TexturedObject){
                            if(!((TexturedObject) obj).isTextureLoaded()){
                                ((TexturedObject) obj).loadTexture();
                                waitingForTexture = true;
                                break;
                            }
                        }
                    }
                }
                pRpt.setPayload(out);
                pRpt.update();

                loadScene(scene, out);
            }


        });
        t1.start();






    }

    public TileMap[] loadMap(String mapJson){
        try {

            Map<String,?> json = (Map<String, ?>) resourceHandler.readJsonFromFile(mapJson); //complete json
            Map<String,?> levels =  ((ArrayList<Map<String,?>>)json.get("levels")).get(0);
            Map<String,?> defs =  (Map<String,?>) json.get("defs"); //defs -> get the png filenames
            ArrayList<Map<String,?>> layerInstances = (ArrayList<Map<String,?>>) levels.get("layerInstances"); //layer instances

            TileMap[] tileMaps = new TileMap[layerInstances.size()];

            ArrayList<layer> layerList = new ArrayList<>();

            for(Map<String, ?> layerI: layerInstances){

                de.jspll.handlers.layer l = new layer();

                l.setId((String) layerI.get("__identifier"));

                ArrayList<Map<String,?>> temp = (ArrayList<Map<String, ?>>) layerI.get("gridTiles");
                ArrayList<gridTiles> gT = new ArrayList<>();

                for(Map<String,?> o : temp){
                    gridTiles t = new gridTiles();
                    t.setPx(((ArrayList<Double>) o.get("px")));
                    t.setSrc((ArrayList<Double>) o.get("src"));
                    Double f = (Double) o.get("f");
                    if(f != null){
                        t.setF(((Double) o.get("f")));
                    }

                    t.setD((ArrayList<Double>) o.get("d"));

                    gT.add(t);
                }

                l.setgT(gT);

                layerList.add(l);


                // -> get tiles for tilemap for each layer (position and source)
            }

            return tileMaps;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}

class layer {
    String id;
    ArrayList<gridTiles> gT = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<gridTiles> getgT() {
        return gT;
    }

    public void setgT(ArrayList<gridTiles> gT) {
        this.gT = gT;
    }

    @Override
    public String toString() {
        return "layers{" +
                "id='" + id + '\'' +
                ", gT=" + gT +
                '}';
    }
}

class gridTiles{
    ArrayList<Double> px = new ArrayList<>();
    ArrayList<Double> src = new ArrayList<>();
    double f;
    ArrayList<Double> d = new ArrayList<>();

    public ArrayList<Double> getPx() {
        return px;
    }

    public int[] getPxArr(){
        int[] res = new int[px.size()];
        for(int i = 0; i< px.size(); i++){
            res[i] = px.get(i).intValue();
        }
        return res;
    }

    public int[] getSrcArr(){
        int[] res = new int[src.size()];
        for(int i = 0; i< src.size(); i++){
            res[i] = src.get(i).intValue();
        }
        return res;
    }

    public int[] getDArr(){
        int[] res = new int[d.size()];
        for(int i = 0; i< d.size(); i++){
            res[i] = d.get(i).intValue();
        }
        return res;
    }

    public void setPx(ArrayList<Double> px) {
        this.px = px;
    }

    public ArrayList<Double> getSrc() {
        return src;
    }

    public void setSrc(ArrayList<Double> src) {
        this.src = src;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public ArrayList<Double> getD() {
        return d;
    }

    public void setD(ArrayList<Double> d) {
        this.d = d;
    }

    @Override
    public String toString() {
        return "gridTiles{" +
                "px=" + px +
                ", src=" + src +
                ", f=" + f +
                ", d=" + d +
                '}';
    }
}

