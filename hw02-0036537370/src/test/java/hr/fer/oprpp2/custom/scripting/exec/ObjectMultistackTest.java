package hr.fer.oprpp2.custom.scripting.exec;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ObjectMultistackTest {

    @Test
    public void testEmpty() {
        assertTrue(new ObjectMultistack().isEmpty("a"));
    }

    @Test
    public void testSingleStackValues() {
        ObjectMultistack multistack = new ObjectMultistack();

        multistack.push("a", new ValueWrapper(1));
        multistack.push("a", new ValueWrapper(2));

        assertEquals(multistack.peek("a"), new ValueWrapper(2));
        assertEquals(multistack.pop("a"), new ValueWrapper(2));
        assertEquals(multistack.pop("a"), new ValueWrapper(1));

        assertTrue(multistack.isEmpty("b"));
    }

    @Test
    public void testMultipleStackValues() {
        ObjectMultistack multistack = new ObjectMultistack();

        multistack.push("a", new ValueWrapper(1));
        multistack.push("b", new ValueWrapper(2));
        multistack.push("a", new ValueWrapper(3));

        assertEquals(multistack.peek("a"), new ValueWrapper(3));
        assertEquals(multistack.pop("a"), new ValueWrapper(3));
        assertEquals(multistack.peek("b"), new ValueWrapper(2));
        assertEquals(multistack.pop("b"), new ValueWrapper(2));
        assertEquals(multistack.pop("a"), new ValueWrapper(1));

        assertTrue(multistack.isEmpty("a"));
        assertTrue(multistack.isEmpty("b"));
    }

}
