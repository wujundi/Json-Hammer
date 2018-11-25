import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import node.JsonNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;

public class JsonHammerImplTest {

    @Test
    public void demoAPITest() {
        String str = "{\"id\":0,\"name\":\"admin\",\"users\":[{\"id\":2,\"name\":\"guest\"},{\"id\":3,\"name\":\"root\"}]}";

        Object object = JSON.parse(str,Feature.OrderedField);
        ArrayList<String> names = new ArrayList<String>();
        ArrayList children = new ArrayList<Object>();

        if(object instanceof JSONObject){
            JSONObject jo = (JSONObject) object;
            Iterator localIterator = jo.keySet().iterator();
            while (localIterator.hasNext()) {
                Object key = localIterator.next();
                String childName = (String) key;
                names.add(childName);
                Object value = jo.get(key);
                children.add(value);
            }
        } else if (object instanceof JSONArray){

        }

        JSONObject result = new JSONObject(true);
        // 输出结果
        for(int i= 0;i < children.size(); i++) {
            System.out.println(children.get(i));
            result.put(names.get(i),children.get(i));
        }
        System.out.println(result);

    }

    @Test
    public void jsonNodeTest() {
        String str = "{\"id\":0,\"name\":\"admin\",\"users\":[{\"id\":2,\"name\":\"guest\"},{\"id\":3,\"name\":\"root\"}]}";
        String str1 = "{\"employees\": [{ \"firstName\":\"Bill\" , \"lastName\":\"Gates\" },{ \"firstName\":\"George\" , \"lastName\":\"Bush\" },{ \"firstName\":\"Thomas\" , \"lastName\":\"Carter\" }]}";
        Object object = JSON.parse(str1,Feature.OrderedField);
        ArrayList<JsonNode> children = new ArrayList<JsonNode>();

        if(object instanceof JSONObject){
            JSONObject jo = (JSONObject) object;
            Iterator localIterator = jo.keySet().iterator();
            while (localIterator.hasNext()) {
                Object key = localIterator.next();
                String childName = (String) key;
                Object value = jo.get(key);
                children.add(new JsonNode(childName,childName,value));
            }
        } else if (object instanceof JSONArray){

        }

        JSONObject result = new JSONObject(true);
        // 输出结果
        for(int i= 0;i < children.size(); i++) {
            System.out.println(children.get(i));
            result.put(children.get(i).getPathName(),children.get(i).getBody());
        }
        System.out.println(result);
    }

    @Test
    public void recursionTest() {
        String str = "{\"type\":\"test\",\"content\":{\"id\":0,\"name\":\"admin\",\"users\":[{\"id\":2,\"name\":\"guest\"},{\"id\":3,\"name\":\"root\"}]}}";
        String str1 = "{\"employees\": [{ \"firstName\":\"Bill\" , \"lastName\":\"Gates\" },{ \"firstName\":\"George\" , \"lastName\":\"Bush\" },{ \"firstName\":\"Thomas\" , \"lastName\":\"Carter\" }]}";
        Object object = JSON.parse(str,Feature.OrderedField);
        JsonNode jsonNode = new JsonNode("$","$",object);
        ArrayList<JsonNode> ancestor = new ArrayList<JsonNode>();
        ancestor.add(jsonNode);
        ArrayList<JsonNode> expChildren = recursionDo(ancestor);

        // 输出结果
        JSONObject result = new JSONObject(true);
        for(int i= 0;i < expChildren.size(); i++) {
            System.out.println(expChildren.get(i));
            result.put(expChildren.get(i).getPathName(),expChildren.get(i).getBody());
        }
        System.out.println(result);
    }


    public ArrayList<JsonNode> recursionDo(ArrayList<JsonNode> jnList) {
        ArrayList<JsonNode> tempExplode = new ArrayList<JsonNode>();
        for(JsonNode jn : jnList) {
            if(jn.getBody() instanceof JSONObject){
                JSONObject body = (JSONObject)jn.getBody();
                Iterator localIterator = body.keySet().iterator();
                while (localIterator.hasNext()) {
                    Object key = localIterator.next();
                    String childName = (String) key;
                    Object value = body.get(key);
                    tempExplode.add(new JsonNode(childName,jn.getPathName()+"."+childName,value));
                }
            } else if (jn.getBody() instanceof JSONArray){

            } else {
                // 不是 jsonObject 也不是 JsonArray 那就可以认为当前节点的 value 已不再嵌套下一层 json 结构，那么还留在列表中即可
                tempExplode.add(jn);
            }
        }
        if(tempExplode.equals(jnList)){
            return tempExplode;
        }else {
            return recursionDo(tempExplode);
        }

    }
}