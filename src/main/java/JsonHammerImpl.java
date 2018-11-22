import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class JsonHammerImpl implements JsonHammer{


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
