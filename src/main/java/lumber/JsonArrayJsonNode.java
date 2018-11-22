package lumber;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 本意是想对 JSONArray 进行包装，为其添加一个名字，和路径名
 */
public class JsonArrayJsonNode extends JsonNode {

    private String name; // 当前原木枝干的名称

    private String pathName; // 从主干到达该枝干的路径名称(父级路径+name)

    private JSONArray body; // 底层的实现还是依赖 JsonObject

    private ArrayList<JsonNode> children;

    public JsonArrayJsonNode(String name, String pathName, JSONArray jsonArray) {
        this.name = name;
        this.pathName = pathName;
        this.body = jsonArray;
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

//    public ArrayList<JsonNode> split (){
//
//        Iterator iterator = this.body.iterator();
//
//        while (iterator.hasNext()) {
//
//            String key = String.valueOf(iterator.next()); //得到键
//
//            String value = this.body.getString(key);//得到值
//
//
//    }

}
