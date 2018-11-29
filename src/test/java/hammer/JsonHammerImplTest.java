package hammer;

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


    @Test
    public void fullTest() {
        String str = "{\"type\":\"test\",\"content\":{\"id\":0,\"name\":\"admin\",\"users\":[{\"id\":2,\"name\":\"guest\"},{\"id\":3,\"name\":\"root\"}]}}";
        String str1 = "{\"employees\": [{ \"firstName\":\"Bill\" , \"lastName\":\"Gates\" },{ \"firstName\":\"George\" , \"lastName\":\"Bush\" },{ \"firstName\":\"Thomas\" , \"lastName\":\"Carter\" }]}";

        Object object = JSON.parse(str1,Feature.OrderedField);
        JsonNode jsonNode = new JsonNode("$","$",object);
        ArrayList<JsonNode> ancestor = new ArrayList<JsonNode>();
        ancestor.add(jsonNode);
        ArrayList<JSONObject> expChildren = recursionFull(ancestor);

        // 输出结果
        for(int i= 0;i < expChildren.size(); i++) {
            System.out.println(expChildren.get(i));
        }
    }

    /**
     * 进行输入的封装与输出的拼接
     * @param ancestor
     * @return
     */
    public ArrayList<JSONObject> recursionFull(ArrayList<JsonNode> ancestor) {
        ArrayList<ArrayList<JsonNode>> result = recursionFullDo(ancestor);
        ArrayList<JSONObject> output  = new ArrayList<JSONObject>();
        for(ArrayList<JsonNode> nodeChildren : result){
            JSONObject node = new JSONObject(true);
            for(int i= 0;i < nodeChildren.size(); i++) {
                node.put(nodeChildren.get(i).getPathName(),nodeChildren.get(i).getBody());
            }
            output.add(node);
        }
        return output;
    }



    public ArrayList<ArrayList<JsonNode>> recursionFullDo(ArrayList<JsonNode> ancestor) {
        ArrayList<ArrayList<JsonNode>> tempExplode = new ArrayList<ArrayList<JsonNode>>(); // 暂存空间升级为二维
        ArrayList<JsonNode> tempJNArr = new ArrayList<JsonNode>(); // 收集产出的临时列表
        for (int i=0; i<ancestor.size(); i++){ // 遍历输入node 的子节点
            JsonNode jn = ancestor.get(i);
            if(jn.getBody() instanceof JSONObject){ // 如果子节点是 jO 那么继续探索下一级孩子
                JSONObject body = (JSONObject)jn.getBody();
                Iterator localIterator = body.keySet().iterator();
                while (localIterator.hasNext()) {
                    Object key = localIterator.next();
                    String childName = (String) key;
                    Object value = body.get(key);
                    tempJNArr.add(new JsonNode(childName,jn.getPathName()+"."+childName,value)); // 将探索到的孩子加入临时列表
                }
            } else if (jn.getBody() instanceof JSONArray){ // 如果子节点是数组的话，遍历，且在遍历过程中进行递归
                JSONArray ja = (JSONArray)jn.getBody();
                for(int j=0; j<ja.size(); j++){
                    Object element = ja.get(j);
                    ArrayList<JsonNode> a = new ArrayList<JsonNode>(tempJNArr); // 根据已经添加的node创建"分支"
                    a.add(new JsonNode(jn.getName(),jn.getPathName(),element));
                    ArrayList<JsonNode> b = arrayCopy(a,ancestor,i);
                    for(ArrayList<JsonNode> bjn : recursionFullDo(b)) {
                        tempExplode.add(bjn);
                    }
                }
                return tempExplode;
            } else {
                // 不是 jsonObject 也不是 JsonArray 那就可以认为当前节点的 value 已不再嵌套下一层 json 结构，那么还留在列表中即可
                tempJNArr.add(jn);
            }
        }
        tempExplode.add(tempJNArr);
        if(tempJNArr.equals(ancestor)){
            return tempExplode;
        }else {
            return recursionFullDo(tempJNArr);
        }
    }

    public ArrayList<JsonNode> arrayCopy (ArrayList<JsonNode> a, ArrayList<JsonNode> b, int n) {
        ArrayList<JsonNode> result = new ArrayList<JsonNode>(a);
        for(int i=n+1; i<b.size(); i++){
            result.add(b.get(i));
        }
        return result;
    }

}