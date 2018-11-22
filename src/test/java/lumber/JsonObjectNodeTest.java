package lumber;

import org.junit.Test;

import java.util.ArrayList;

public class JsonObjectNodeTest {

    @Test
    public void main() {
        String str = "{\"id\":0,\"name\":\"admin\",\"users\":[{\"id\":2,\"name\":\"guest\"},{\"id\":3,\"name\":\"root\"}]}";

        JsonObjectJsonNode jsonObjectNode = new JsonObjectJsonNode("c","a.b.c",str);

        ArrayList<JsonNode> jsonNodes = jsonObjectNode.getChildren();

        for (JsonNode jn :jsonNodes) {
            System.out.println(jn);
        }


    }

}