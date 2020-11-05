package de.jspll.util.specific_json;

import de.jspll.util.specific_json.map_components.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler for the map file. Content of file is converted into matching java objects.
 *
 * @author Laura Schmidt
 */
public class JSONHandler {
    private static boolean isTilesets;
    private static boolean isLayers;
    private static boolean isLevels;
    private static boolean isLayerInstances;
    private static boolean isGridTiles;

    private static int countSquaredBrackets;

    private static final List<String> tilesetsList = new ArrayList<>();
    private static final List<String> layersList = new ArrayList<>();
    private static final List<String> levelsList = new ArrayList<>();
    private static final List<String> layerInstancesList = new ArrayList<>();

    private static List<Layer> layers = new ArrayList<>();
    private static List<Tileset> tilesets = new ArrayList<>();
    private static List<Level> levels = new ArrayList<>();
    private static List<LayerInstance> layerInstances = new ArrayList<>();
    private static List<GridTile> gridTiles = new ArrayList<>();

    /**
     * Reads the JSON-File line for line and evaluates which object the line matches.
     *
     * @param path path to map file
     */
    public static void readJSONFile(String path){
        /* start time measurement */
        long timeStart = System.currentTimeMillis();
        try (BufferedReader in = new BufferedReader(new FileReader(path))) {
            String zeile;
            while ((zeile = in.readLine()) != null) {
                /* evaluation */
                if(zeile.contains("layers") || isLayers) {
                    isLayers = true;
                    layersList.add(zeile/*.trim()*/);
                }
                if(zeile.contains("tilesets") || isTilesets){
                    isTilesets = true;
                    tilesetsList.add(zeile/*.trim()*/);
                }
                if(zeile.contains("levels") || isLevels) {
                    isLevels = true;
                    levelsList.add(zeile/*.trim()*/);
                }
                if(zeile.contains("layerInstances") || isLayerInstances) {
                    isLayerInstances = true;
                    layerInstancesList.add(zeile/*.trim()*/);
                }
                if(zeile.contains("[")){
                    countSquaredBrackets++;
                }
                if(zeile.contains("]")){
                    countSquaredBrackets--;
                }
                if(countSquaredBrackets == 0){
                    isLayers = false;
                    isTilesets = false;
                    isLevels = false;
                    isLayerInstances = false;
                    isGridTiles = false;
                }
            }
            /* turning the String lists into the java objects */
            turnStringsIntoObjects("layers", layersList);
            turnStringsIntoObjects("tilesets", tilesetsList);
            turnStringsIntoObjects("levels", levelsList);
            turnStringsIntoObjects("layerInstances", layerInstancesList);

            addGridTilesFromLayerInstancesToGridTileList();
        } catch (IOException e) {
            System.err.println("Something went wrong while reading the file.");
            e.printStackTrace();
        }
        /* print statements for debugging */
        layers.forEach(layer -> System.out.println(layer.toString()));
        tilesets.forEach(tileset -> System.out.println(tileset.toString()));
        levels.forEach(level -> System.out.println(level.toString()));
        layerInstances.forEach(layerInstance -> System.out.println(layerInstance.toString()));
        gridTiles.forEach(gridTile -> System.out.println(gridTile.toString()));
        /* end time measurement */
        long timeEnd = System.currentTimeMillis();
        System.out.println("Time needed: " + (timeEnd - timeStart) + " Millisek.");
    }

    /**
     * Turns a given list of Strings into a list of objects.
     *
     * @param kontext specifies the object that get created
     * @param strings given list of Strings
     * @throws IOException occurs when the context can't be mapped
     */
    private static void turnStringsIntoObjects(String kontext, List<String> strings) throws IOException {
        int count = 0;
        List<String> content = new ArrayList<>();
        for(String curr : strings){
            if(curr.contains("{")){
                count++;
            }
            if(curr.contains("}")){
                count--;
            }
            if(count > 0){
                content.add(curr);
            }
        }
        switch(kontext){
            case "layers":
                layers = createLayerObjects(content);
                break;
            case "tilesets":
                tilesets = createTilesetObjects(content);
                break;
            case "levels":
                levels = createLevelObjects(content);
                break;
            case "layerInstances":
                layerInstances = createLayerInstanceObjects(content);
                break;
            default:
                throw new IOException("Something went wrong.");
        }
    }

    /**
     * Creates Layer obejcts.
     *
     * @param content list of Strings that need to be converted to Layer objects
     * @return list of Layer objects
     * @see Layer
     */
    private static List<Layer> createLayerObjects(List<String> content){
        List<Layer> layerObjects = new ArrayList<>();
        for(int i = 0; i < content.size(); i += 14){
            layerObjects.add(new Layer(getStringForContructor(content.get(i + 2)),
                    getStringForContructor(content.get(i + 3)),
                    getStringForContructor(content.get(i + 4)),
                    getStringForContructor(content.get(i + 5)),
                    getStringForContructor(content.get(i + 6)),
                    getStringForContructor(content.get(i + 11)),
                    new Coordinate(getStringForContructor(content.get(i + 12)),
                            getStringForContructor(content.get(i + 13)))));
        }
        return layerObjects;
    }

    /**
     * Creates Tileset objects.
     *
     * @param content list of Strings that need to be converted to Tileset object
     * @return list of Tileset objects
     * @see Tileset
     */
    private static List<Tileset> createTilesetObjects(List<String> content) {
        List<Tileset> tilesetObjects = new ArrayList<>();
        for (int i = 0; i < content.size(); i += 10) {
            tilesetObjects.add(new Tileset(getStringForContructor(content.get(i + 1)),
                    getStringForContructor(content.get(i + 2)),
                    getStringForContructor(content.get(i + 3)),
                    getStringForContructor(content.get(i + 4)),
                    getStringForContructor(content.get(i + 5)),
                    getStringForContructor(content.get(i + 6)),
                    getStringForContructor(content.get(i + 7)),
                    getStringForContructor(content.get(i + 8))));
        }
        return tilesetObjects;
    }

    /**
     * Creates Level objects
     *
     * @param content list of Strings that need to be converted to Level objects
     * @return list of Level objects
     * @see Level
     */
    private static List<Level> createLevelObjects(List<String> content) {
        List<Level> levelObjects = new ArrayList<>();
        for(int i = 0; i < content.size(); i += content.size()) {
            levelObjects.add(new Level(getStringForContructor(content.get(i + 1)),
                    getStringForContructor(content.get(i + 2)),
                    getStringForContructor(content.get(i + 3)),
                    getStringForContructor(content.get(i + 4)),
                    null));
        }
        return levelObjects;
    }

    /**
     * Creates LayerInstance objects.
     *
     * @param content list of String that need to be converted to LayerInstance objects
     * @return list of LayerInstance objects
     * @see LayerInstance
     */
    private static List<LayerInstance> createLayerInstanceObjects(List<String> content) {
        List<LayerInstance> layerInstances = new ArrayList<>();
        int countLayerInstances = 0;
        for(String currString : content) {
            if(currString.contains("{") && !currString.contains("coordId")) {
                layerInstances.add(new LayerInstance());
                countLayerInstances++;
            }
            if(currString.contains("identifier")) {
                layerInstances.get(countLayerInstances - 1).setIdentifier(getStringForContructor(currString));
            }
            if(currString.contains("type")) {
                layerInstances.get(countLayerInstances - 1).setType(getStringForContructor(currString));
            }
            if(currString.contains("cWid")) {
                layerInstances.get(countLayerInstances - 1).setcWidth(getStringForContructor(currString));
            }
            if(currString.contains("cHei")) {
                layerInstances.get(countLayerInstances - 1).setcHeight(getStringForContructor(currString));
            }
            if(currString.contains("gridSize")) {
                layerInstances.get(countLayerInstances - 1).setGridSize(getStringForContructor(currString));
            }
            if(currString.contains("levelId")) {
                layerInstances.get(countLayerInstances - 1).setLevelId(getStringForContructor(currString));
            }
            if(currString.contains("layerDefUid")) {
                layerInstances.get(countLayerInstances - 1).setLayerDefUid(getStringForContructor(currString));
            }
            if(currString.contains("pxOffsetX")) {
                layerInstances.get(countLayerInstances - 1).setPxOffsetX(getStringForContructor(currString));
            }
            if(currString.contains("pxOffsetY")) {
                layerInstances.get(countLayerInstances - 1).setPxOffsetY(getStringForContructor(currString));
            }
            if(currString.contains("seed")) {
                layerInstances.get(countLayerInstances - 1).setSeed(getStringForContructor(currString));
            }
            if(currString.contains("coordId")) {
                List<GridTile> updatedGridTileList = layerInstances.get(countLayerInstances - 1).getGridTiles();
                updatedGridTileList.add(new GridTile(
                        getStringForGridTileConstructor("coordId", currString),
                        getStringForGridTileConstructor("tileId", currString),
                        new Coordinate(getStringForGridTileConstructor("__x", currString),
                                getStringForGridTileConstructor("__y", currString)),
                        new Coordinate(getStringForGridTileConstructor("__srcX", currString),
                                getStringForGridTileConstructor("__srcY", currString))));
                layerInstances.get(countLayerInstances - 1).setGridTiles(updatedGridTileList);
            }
        }
        return layerInstances;
    }

    private static void addGridTilesFromLayerInstancesToGridTileList() {
        layerInstances.forEach(layerInstance -> gridTiles.addAll(layerInstance.getGridTiles()));
    }

    private static String getStringForContructor(String string){
        return string.split(":")[1].replace(",", "").trim();
    }

    private static String getStringForGridTileConstructor(String context, String content) {
        String[] splittedString = content.split(",");
        switch (context) {
            case "coordId":
                return splittedString[0].split(":")[1].trim();
            case "tileId":
                return splittedString[1].split(":")[1].trim();
            case "__x":
                return splittedString[2].split(":")[1].trim();
            case "__y":
                return splittedString[3].split(":")[1].trim();
            case "__srcX":
                return splittedString[4].split(":")[1].trim();
            case "__srcY":
                return splittedString[5].split(":")[1].split("}")[0].trim();
            default:
                return "";
        }
    }

    public static List<Layer> getLayers() {
        return layers;
    }

    public static List<Tileset> getTilesets() {
        return tilesets;
    }

    public static List<Level> getLevels() {
        return levels;
    }

    public static List<LayerInstance> getLayerInstances() {
        return layerInstances;
    }

    public static List<GridTile> getGridTiles() {
        return gridTiles;
    }
}