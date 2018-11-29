package hammer;

import java.util.ArrayList;
import java.util.List;

public interface JsonHammer {

    /**
     * 默认方法，扫描所有json节点，将所有数组元素组成笛卡尔积输出
     * @param json_str
     * @return
     */
    public ArrayList<String> hammer (String json_str);

    /**
     * 可控方法，扫描所有json节点，只保留参数中提到的 json 节点
     * @param json_str
     * @param params
     * @return
     */
    public ArrayList<String> hammer (String json_str, String...params);
}
