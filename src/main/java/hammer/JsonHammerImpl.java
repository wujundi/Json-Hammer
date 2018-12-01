package hammer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import hammer.JsonHammer;
import node.JsonNode;

import java.awt.*;
import java.util.*;
import java.util.List;

public class JsonHammerImpl implements JsonHammer {

    private final static String ROOT_PREIFIX = "$";
    private final static String LEVEL_SEPARATOR = ".";
    private static int count = 0;

    public ArrayList<String> hammer(String json_str) {
        Object object = JSON.parse(json_str,Feature.OrderedField);
        JsonNode jsonNode = new JsonNode(ROOT_PREIFIX,ROOT_PREIFIX,object);
        ArrayList<JsonNode> ancestor = new ArrayList<JsonNode>();
        ancestor.add(jsonNode);
        return hammer(ancestor,null,null);
    }

    public ArrayList<String> hammer(String json_str, String... params) {
        Object object = JSON.parse(json_str,Feature.OrderedField);
        JsonNode jsonNode = new JsonNode(ROOT_PREIFIX,ROOT_PREIFIX,object);
        ArrayList<JsonNode> ancestor = new ArrayList<JsonNode>();
        ancestor.add(jsonNode);
        HashSet<String> fixParamsSet = null;
        HashSet<String> pathParamSet = null;
        if(params != null){
            fixParamsSet = new HashSet<String>(Arrays.asList(params));
            pathParamSet = fixParam2PathParam(fixParamsSet);
        }
        return hammer(ancestor,fixParamsSet,pathParamSet);
    }

    /**
     * 由指定路径得出递归过程中所有需要放行的路径
     * @param fixParamsSet
     * @return
     */
    public HashSet<String> fixParam2PathParam (HashSet<String> fixParamsSet){
        HashSet<String> pathParamSet = new HashSet<String>();
        for (String str : fixParamsSet) {
            pathParamSet.add(str);
            while(str.contains(LEVEL_SEPARATOR)){
                int endIndex = str.lastIndexOf(LEVEL_SEPARATOR);
                str = str.substring(0,endIndex);
                pathParamSet.add(str);
            }
        }
        return pathParamSet;
    }

    /**
     * 递归入口与数据出口
     * @param ancestor
     * @param fixParamsSet
     * @param pathParamSet
     * @return
     */
    private ArrayList<String> hammer(ArrayList<JsonNode> ancestor,HashSet<String> fixParamsSet,HashSet<String> pathParamSet) {
        ArrayList<String> output  = new ArrayList<String>();
        for(ArrayList<JsonNode> nodeChildren : explode(ancestor,fixParamsSet,pathParamSet)){
            JSONObject node = new JSONObject(true);
            for(JsonNode jsonNode : nodeChildren) {
                node.put(jsonNode.getPathName(),jsonNode.getBody());
            }
            output.add(node.toString());
        }
        return output;
    }

    /**
     * 递归核心方法，扫描当前列表中的json结构，对于 JSONObject 深入下一层，对于 JSONArray 进行膨胀
     * @param ancestor
     * @param fixParamsSet
     * @param pathParamSet
     * @return
     */
    public ArrayList<ArrayList<JsonNode>> explode(ArrayList<JsonNode> ancestor,HashSet<String> fixParamsSet,HashSet<String> pathParamSet) {
        // 调试用递归计数器
        // System.out.println( (count++) + "   " + ancestor.size());
        // 暂存空间升级为二维
        ArrayList<ArrayList<JsonNode>> tempExplode = new ArrayList<ArrayList<JsonNode>>();
        // 路径控制开关，当用户未指定路劲集合时，探索与膨胀不受限制，结果会将所有数组元素按照笛卡尔积输出
        Boolean isFree = (fixParamsSet == null);
        // 用于收集本轮产生的 JsonNode 的，临时列表
        ArrayList<JsonNode> tempJNArr = new ArrayList<JsonNode>();
        // 遍历输入 node 的子节点
        for (int i=0; i<ancestor.size(); i++){
            JsonNode jn = ancestor.get(i);
            // 如果该节点的路径，在用户指定参数集合中的，即便还未解析到叶子节点，也放入最终结果中
            if( !isFree && fixParamsSet.contains(jn.getPathName())){
                tempJNArr.add(jn);
                continue;
            }
            if( isFree || pathParamSet.contains(jn.getPathName()) ){

                // 1、如果子节点是数组的话，那么遍历该数组使数据膨胀
                if (jn.getBody() instanceof JSONArray){
                    JSONArray ja = (JSONArray)jn.getBody();
                    if(ja.size() > 0){
                        for(int j=0; j<ja.size(); j++){
                            // 使已经存入临时列表的数据，与该json数组中每一个元素，形成一个独立的"分支"
                            Object element = ja.get(j);
                            ArrayList<JsonNode> a = new ArrayList<JsonNode>(tempJNArr);
                            a.add(new JsonNode(jn.getName(),jn.getPathName(),element));
                            ArrayList<JsonNode> b = arrayCopy(a,ancestor,i);
                            // 对分支进行进一步的扫描与膨胀
                            ArrayList<ArrayList<JsonNode>> explodedArr = explode(b,fixParamsSet,pathParamSet);
                            for(ArrayList<JsonNode> jnArr : explodedArr) {
                                // 如果下一层探索毫无建树，那么也没有必要将空的列表加入返回的结果集中
                                if(!jnArr.isEmpty()){ tempExplode.add(jnArr);}
                            }
                        }
                        return tempExplode;
                    }
                    // 如果这是一个空数组，那么将 "nullArray":[] 视为 "nullArray":""，不再进行探索，直接塞入临时列表中
                    else {
                        tempJNArr.add(new JsonNode(jn.getName(),jn.getPathName(),""));
                    }
                }

                // 2、如果子节点是 JSONObject， 那么继续探索下一级孩子
                else if(jn.getBody() instanceof JSONObject){
                    JSONObject body = (JSONObject)jn.getBody();
                    Iterator localIterator = body.keySet().iterator();
                    // 如果该jsonObject是空的话，即，将 "a":{} 视为 "a":""，不再进行探索，直接塞入临时列表中
                    if(!localIterator.hasNext()){
                        tempJNArr.add(new JsonNode(jn.getName(),jn.getPathName(),""));
                    }
                    while (localIterator.hasNext()) {
                        Object key = localIterator.next();
                        String childName = (String) key;
                        Object value = body.get(key);
                        // 将探索到的孩子加入临时列表
                        tempJNArr.add(new JsonNode(childName,jn.getPathName()+LEVEL_SEPARATOR+childName,value));
                    }
                }

                // 3、不是 jsonObject 也不是 JsonArray 那就可以认为当前节点的 value 已不再嵌套下一层 json 结构，那么只有在 fixParamsSet 集中，才能进入列表，否则就会被无视
                else if ( isFree || fixParamsSet.contains(jn.getPathName()) ){
                    tempJNArr.add(jn);
                } else {
                    continue;
                }
            }
        }
        // 只有本次遍历中没有出现数组的情况才会到达这里。即，到达此处，说明本轮的输入无需膨胀。那么，进入常规的广度优先遍历环节
        tempExplode.add(tempJNArr);
        // 当输入与本轮处理的临时列表 tempJNArr 相同时，说明再进行扫描已无意义
        if(tempJNArr.equals(ancestor)){
            return tempExplode;
        }else {
            return explode(tempJNArr,fixParamsSet,pathParamSet);
        }
    }

    /**
     * 根据已有列表，与当前的遍历状况，创建链表的分支
     * @param a 子链表
     * @param b 父链表
     * @param n 此时子链表已经扫描到的，父链表的index
     * @return
     */
    public ArrayList<JsonNode> arrayCopy (ArrayList<JsonNode> a, ArrayList<JsonNode> b, int n) {
        ArrayList<JsonNode> result = new ArrayList<JsonNode>(a);
        for(int i=n+1; i<b.size(); i++){
            result.add(b.get(i));
        }
        return result;
    }

}
