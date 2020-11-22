package de.jspll.handlers;

import com.google.gson.*;
import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.GameTrie;
import de.jspll.data.objects.TexturedObject;
import de.jspll.data.objects.game.map.Tile;
import de.jspll.data.objects.game.map.TileMap;
import de.jspll.data.objects.game.map.gridTiles;
import de.jspll.data.objects.game.map.layer;
import de.jspll.data.objects.game.tasks.Task;
import de.jspll.data.objects.game.tasks.TaskHolder;
import de.jspll.data.objects.loading.LoadingBar;
import de.jspll.data.objects.loading.LoadingCircle;
import de.jspll.data.objects.loading.ProgressReporter;
import de.jspll.data.objects.loading.Report;
import de.jspll.graphics.Camera;
import de.jspll.graphics.ResourceHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
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

        //loadScene(ChannelID.SCENE_2, new ArrayList<GameObject>(Arrays.asList(loadMap("assets\\map\\Sekretariat-Spiel-Plan_v2.json"))));

        /*for(TileMap tm : loadMap("assets\\map\\Sekretariat-Spiel-Plan_v2.json")){
            loadObject(tm);
            System.out.println(tm);
        }*/
        resourceHandler.start();

        //loadMap("assets\\map\\Sekretariat-Spiel-Plan_v2.json");
    }

    public Point getMousePos() {
        if (graphicsHandler == null)
            return null;
        return graphicsHandler.getMousePos();
    }

    public Camera getSelectedCamera() {
        return graphicsHandler.getSelectedCamera();
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

    private ChannelID activeScene = SCENE_LOADING;

    private GameTrie[] channels = new GameTrie[LAST_CHANNEL.valueOf() + 1];

    public void setGraphicsHandler(GraphicsHandler graphicsHandler) {
        this.graphicsHandler = graphicsHandler;
    }

    public void register(GameObject item) {
        if(item == null){
            return;
        }
        channels[INSTANCE_REGISTER.valueOf()].insert(item.getID(), item);
        item.setListener(this);
    }

    public void subscribe(GameObject item) {
        if(item == null) return;
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

    public void loadScene(ChannelID scene, ArrayList<GameObject> objects){
        for(GameObject obj: objects){
            //loadObject(obj);
            subscribe(obj,INSTANCE_REGISTER);
            subscribe(obj,scene);
            obj.setListener(this);
        }
    }

    public void loadScene(ChannelID scene, JsonArray objects){
        ArrayList<GameObject> out = new ArrayList<>();
        ProgressReporter pRpt = new Report();
        pRpt.setCount(objects.size() + 1);
        pRpt.setNextScene(scene);
        pRpt.setGameObjectHandler(this);
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
                    go.updateReferences();
                    if(go instanceof TexturedObject){
                        TexturedObject to = (TexturedObject) go;
                        to.requestTexture();
                    }
                    pRpt.update();
                }
                //for(TileMap ttm : loadMap("assets\\map\\Sekretariat-Spiel-Plan_v2.json")){
                //    out.add(ttm);
                //}
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
                TaskHolder th1 = new TaskHolder("test", "g.dflt.TaskHolder",
                        new Point(1280,1088),
                        new Dimension(32,16),
                        new Task());
                th1.setListener(goh);
                out.add(th1);
                pRpt.setPayload(out);


                loadScene(scene, out);

                pRpt.update();

            }
        });
        t1.start();
    }

    public void loadScene(ChannelID scene, String file){

        String jsonStr = getResourceHandler().fileToJson(file);
        JsonArray jsonArray = new JsonParser().parse(jsonStr).getAsJsonArray();
        loadScene(scene, jsonArray);
    }

    public TileMap[] loadMap(String mapJson){
        try {
            Map<String,?> json = (Map<String, ?>) resourceHandler.readJsonFromFile(mapJson); //complete json
            Map<String,?> levels =  ((ArrayList<Map<String,?>>)json.get("levels")).get(0);
            Map<String,?> defs =  (Map<String,?>) json.get("defs"); //defs -> get the png filenames
            ArrayList<Map<String,?>> layerInstances = (ArrayList<Map<String,?>>) levels.get("layerInstances"); //layer instances

            //Map sizing
            int mapWidth = ((Double) levels.get("pxWid")).intValue();
            int mapHeight = ((Double) levels.get("pxHei")).intValue();

            ArrayList<TileMap> tileMapsList = new ArrayList<>();
            //TileMap[] tileMaps = new TileMap[layerInstances.size()-3]; //because of missing files

            //layers
            ArrayList<layer> layerList = new ArrayList<>();
            layer collsions = new layer();

            //Lists for Texture finding
            ArrayList<Map<String,?>> defsLayers =  (ArrayList<Map<String,?>>)defs.get("layers");
            ArrayList<Map<String,?>> tilesets =  (ArrayList<Map<String,?>>)defs.get("tilesets");

            boolean b = false;

            for(Map<String, ?> layerI: layerInstances){
                b = false;

                //create layer
                layer l = new layer();

                //id
                l.setId((String) layerI.get("__identifier"));

                //sizing
                l.setWidth(((Double) layerI.get("__cWid")).intValue());
                l.setHeight(((Double) layerI.get("__cHei")).intValue());

                //get gridTiles
                ArrayList<Map<String,?>> temp = (ArrayList<Map<String, ?>>) layerI.get("gridTiles");

                if(temp.size() == 0)
                    continue;

                //List for converted Grid Tiles
                ArrayList<gridTiles> gT = new ArrayList<>();

                //Automatic conversion did not seem to work
                for(Map<String,?> o : temp){
                    //create grid tile
                    gridTiles t = new gridTiles();

                    //get values
                    t.setPx(((ArrayList<Double>) o.get("px")));
                    t.setSrc((ArrayList<Double>) o.get("src"));
                    Double f = (Double) o.get("f");
                    if(f != null){
                        t.setF(((Double) o.get("f")));
                    }
                    t.setD((ArrayList<Double>) o.get("d"));

                    //add to list
                    gT.add(t);
                }

                //add gridTile list to layer
                l.setgT(gT);
                //loop over layer lists

                for(Map<String,?> defLayer : defsLayers){

                    //if current layer is the one referenced in the gridTile layer
                    if(((Double)defLayer.get("uid")).intValue() == ((Double)layerI.get("layerDefUid")).intValue()){

                        //find matching tileset
                        for(Map<String,?> tileset : tilesets){
                            int tilesetID = ((Double)tileset.get("uid")).intValue();

                            if(defLayer.get("tilesetDefUid") != null){
                                Integer layerTilesetUid = ((Double)defLayer.get("tilesetDefUid")).intValue();
                                if(tilesetID == layerTilesetUid){

                                    String[] tex = new String[1];

                                    //get texture source
                                    String src = ((String) tileset.get("relPath"));

                                    //Because these files do not exist
                                    if(src == null || src.equals("Anwesenheit.png")){
                                        if(src.equals("Anwesenheit.png")){
                                            tex[0] = "assets\\map\\" + src;
                                            l.setTextures(tex);
                                            collsions = l;
                                        }
                                        b = true;
                                        continue;

                                    }

                                    tex[0] = "assets\\map\\" + src; //.substring(0,src.length()-4); //-4 to cut off the .png ending

                                    l.setTextures(tex);
                                }
                            }
                        }
                        if(b) {
                            continue;
                        }
                    }
                }
                if(b) {
                    continue;
                }
                layerList.add(l);
            }
            TileMap[] tileMaps = new TileMap[layerList.size()+1]; //+1 to add colissions

            for(int i = 0; i < layerList.size()+1; i++){
                layer l;
                boolean collidable;
                if(i == layerList.size()){
                    l = collsions;
                    collidable = true;
                } else {
                    l = layerList.get(i);
                    collidable = false;
                }
                if(l.getTextures() == null || l.getTextures()[0] == null){
                    System.out.println("Error");
                }
                TileMap tm;
                if(l.getId().contains("Ausstattung") || l.getId().contentEquals("Boden2")){
                    tm = new TileMap(l.getId(), "g.dflt.TileMap", 0,0,new Dimension(mapWidth, mapHeight), l.getHeight(), l.getWidth(), l.getTextures(),true);
                } else {
                    tm = new TileMap(l.getId(), "g.dflt.TileMap", 0,0,new Dimension(mapWidth, mapHeight), l.getHeight(), l.getWidth(), l.getTextures());
                }

                HashMap<String, Tile> tileCache = new HashMap<>();

                for(gridTiles gt : l.getgT()){
                    int[] arr = gt.getSrcArr(tm.getDefaultTileDimension());
                    String key = arr[0] + "|" + arr[1];
                    if(!tileCache.containsKey(key)) {
                        Tile t = new Tile(collidable, arr, tm);
                        tileCache.put(key, t);
                        tm.addTile(t);
                    }
                    int[] cord = gt.getPxArr();
                    tm.setTileToMap(tileCache.get(key), cord[0]/tm.getDefaultTileDimension().width, cord[1]/tm.getDefaultTileDimension().height);
                }
                tileMaps[i] = tm;
            }

            TileMap[] returnArr = new TileMap[tileMaps.length];

            for(int i = 0; i<tileMaps.length; i++){
                returnArr[tileMaps.length-1-i] = tileMaps[i];
            }
            return returnArr;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}



