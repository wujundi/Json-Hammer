import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

public class JsonHammerImpl implements JsonHammer{


    public ArrayList<String> hammer(String json_str) {
        Object object = JSON.parse(json_str);
        return null;
    }



}
