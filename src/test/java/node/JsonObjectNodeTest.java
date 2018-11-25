package node;

import org.junit.Test;

public class JsonObjectNodeTest {

    @Test
    public void main() {
        String str = "{\"id\":0,\"name\":\"admin\",\"users\":[{\"id\":2,\"name\":\"guest\"},{\"id\":3,\"name\":\"root\"}]}";

        JsonObjectNode jsonObjectNode = new JsonObjectNode("c","a.b.c",str);


    }

}