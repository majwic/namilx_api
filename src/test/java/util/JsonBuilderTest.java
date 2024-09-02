package util;

import com.majwic.util.JsonBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonBuilderTest {

    @Test
    public void testSimpleJson() {
        JsonBuilder builder = new JsonBuilder()
            .add("key1", "value1")
            .add("key2", 123)
            .add("key3", true);

        String expectedJson = "{\"key1\":\"value1\",\"key2\":123,\"key3\":true}";
        assertEquals(expectedJson, builder.build());
    }

    @Test
    public void testNestedJson() {
        JsonBuilder builder = new JsonBuilder()
            .add("key1", "value1")
            .add("nested", new JsonBuilder()
                .add("nestedKey1", "nestedValue1")
                .add("nestedKey2", 123)
            );

        String expectedJson = "{\"key1\":\"value1\",\"nested\":{\"nestedKey1\":\"nestedValue1\",\"nestedKey2\":123}}";
        assertEquals(expectedJson, builder.build());
    }

    @Test
    public void testJsonWithList() {
        JsonBuilder builder = new JsonBuilder()
            .add("key1", "value1")
            .add("list", List.of(1, 2, 3));

        String expectedJson = "{\"key1\":\"value1\",\"list\":[1,2,3]}";
        assertEquals(expectedJson, builder.build());
    }

    @Test
    public void testComplexJson() {
        JsonBuilder builder = new JsonBuilder()
            .add("key1", "value1")
            .add("listOfNested", List.of(
                    new JsonBuilder()
                        .add("listKey1", "listValue1")
                        .add("listInList1", List.of(1, 2, 3)),
                    new JsonBuilder()
                        .add("listKey2", "listValue2")
                        .add("listInList2", List.of(1, 2, 3))
                )
            );

        String expectedJson = "{\"key1\":\"value1\",\"listOfNested\":[{\"listKey1\":\"listValue1\"," +
            "\"listInList1\":[1,2,3]},{\"listKey2\":\"listValue2\",\"listInList2\":[1,2,3]}]}";
        assertEquals(expectedJson, builder.build());
    }

    @Test
    public void testEmptyJson() {
        JsonBuilder builder = new JsonBuilder();
        String expectedJson = "{}";
        assertEquals(expectedJson, builder.build());
    }

    @Test
    public void testEmptyNestedJson() {
        JsonBuilder builder = new JsonBuilder()
            .add("emptyNested", new JsonBuilder());
        String expectedJson = "{\"emptyNested\":{}}";
        assertEquals(expectedJson, builder.build());
    }

    @Test
    public void testJsonWithEmptyList() {
        JsonBuilder builder = new JsonBuilder()
            .add("emptyList", List.of());
        String expectedJson = "{\"emptyList\":[]}";
        assertEquals(expectedJson, builder.build());
    }

    @Test
    public void testToStringMethod() {
        JsonBuilder builder = new JsonBuilder()
            .add("key1", "value1")
            .add("nested", new JsonBuilder()
                .add("nestedKey1", "nestedValue1")
                .add("nestedKey2", 123)
            );

        String expectedJson = "{\"key1\":\"value1\",\"nested\":{\"nestedKey1\":\"nestedValue1\",\"nestedKey2\":123}}";
        assertEquals(expectedJson, builder.toString());
    }
}
