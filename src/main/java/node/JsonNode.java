package node;

import com.alibaba.fastjson.JSON;

/**
 * json 原木，hammer敲击的目标
 */
public class JsonNode {

    /**
     * 当前原木枝干的名称
     */
    private String name;

    /**
     * 从主干到达该枝干的路径名称(父级路径+name)
     */
    private String pathName;

    /**
     * 用于实际存储数据的对象，可能是 JsonObject 也可能是 JsonArray
     */
    private Object body;

    public JsonNode() {
    }

    public JsonNode(String name, String pathName, Object body) {
        this.name = name;
        this.pathName = pathName;
        this.body = body;
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

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "JsonNode{" +
                "name='" + name + '\'' +
                ", pathName='" + pathName + '\'' +
                ", body=" + body +
                '}';
    }
}
