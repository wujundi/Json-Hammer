package node;

import java.util.ArrayList;


public class JsonObjectNode extends JsonNode {

    private String name;

    /**
     * pathName 包含 name
     */
    private String pathName;

    private String body;

    private ArrayList<JsonNode> children;

    public JsonObjectNode(String name, String pathName, String jsonObject) {
        this.name = name;
        this.pathName = pathName;
        this.body = jsonObject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

//    public ArrayList<JsonNode> getChildren() {
//        Iterator localIterator = this.body.keySet().iterator();
//        children = new ArrayList<JsonNode>();
//        while (localIterator.hasNext()) {
//            Object key = localIterator.next();
//            String childName = (String) key;
//            String value = this.body.getString(childName);
//            this.children.add(new JsonNode(childName,this.pathName+"."+childName,value));
//        }
//        return children;
//    }

}
