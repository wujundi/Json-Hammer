package node;

import org.junit.Test;

public class JsonNodeTest {

    @Test
    public void main(){

        JsonNode jsonNode = new JsonNode("c","a.b.c","{}");

        System.out.println(jsonNode instanceof JsonObjectNode);
    }

}