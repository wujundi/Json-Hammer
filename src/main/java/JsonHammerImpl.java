import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class JsonHammerImpl implements JsonHammer{

    /*
    思路小记
    josn-hammer 的本质是可能存在多层嵌套的数组完全打横，
    那么可以理解成将各个数组并排放在一起，取其中元素组合的笛卡尔积
    [][][][][]
    ()()()
    <><><><>
    所以，将所有 jsonObject 一字排开
    将所有 jsonArray 各自拆成List
    然后用一个多层嵌套的循环来完成就可以了（当然在实现上可以采用二位数组，不一定非得是多层循环）
     */


    public List<String> hammer(String json_str) {
        if(json_str == null || json_str.length() <= 1){
            return null;
        }

        try {
            JSONObject jsonObject = JSON.parseObject(json_str);

        } catch(Exception e)
        {
            System.out.println("fast json error");
        }

        return null;
    }
}
