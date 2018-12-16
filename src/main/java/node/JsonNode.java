package node;

import com.alibaba.fastjson.JSON;

/**
 * json Node, 封装在遍历过程中需要用到的功能
 */
public class JsonNode {

    /**
     * 当前json Node 的 key 名称
     */
    private String name;

    /**
     * 从根位置到达当前json Node 的的路径名称(父级路径+name)
     */
    private String pathName;

    /**
     * "是否保留"标识，
     * 最原始的处理逻辑是，指定路径的节点直接留下来，不再探索其子节点
     * 为实现父子节点同时保留的功能，需要有一个标识来表明:
     * "这个节点的子节点已经被探索并且放入列表了，但是这个节点是入参指定的节点，需要留下来"
     * isSelected 就是干这事的，所以其默认是 false
     * 程序处理思路是：
     * 1、对于那些被用户指定的非叶子的节点，在打上 isSelected=true 后，仍然继续探索其子节点
     * 2、递归中对于列表中 isSelected=true 的节点，做到“视而不见”
     */
    private Boolean selected = false;

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

    public Boolean status() {
        return selected;
    }

    public void selected() {
        this.selected = true;
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
