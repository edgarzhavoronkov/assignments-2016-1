package ru.spbau.mit;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static ru.spbau.mit.SecondPartTasks.*;

public class SecondPartTasksTest {
    private final static double EPS = 0.01;
    @Test
    public void testFindQuotes() throws Exception {
        List<String> expected = Arrays.asList(
                "Slow death, immense decay",
                "Smell your death as it burns",
                "His face of death staring down,",
                "Approaching the altar of death",
                "Satan's slaughter, ceremonial death",
                "Eyes dripping blood realization of death",
                "Holds the key to his own death",
                "Cold touch of death begins to chill your spine",
                "My sinful glare at nothing holds thoughts of death behind it",
                "Vessels in my brain carry death until my birth",
                "What I am, what I want, I'm only after death"
        );
        List<String> paths = Arrays.asList(
                "src/test/resources/test1.txt",
                "src/test/resources/test2.txt",
                "src/test/resources/test3.txt"
        );
        List<String> actual = findQuotes(paths, "death");
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testPiDividedBy4() throws Exception {
        assertEquals(Math.PI / 4, piDividedBy4(), EPS);
    }

    @Test
    public void testFindPrinter() throws Exception {
        List<String> compositions1 = Arrays.asList(
                "foo",
                "bar",
                "baz"
        );
        List<String> compositions2 = Arrays.asList(
                "foo",
                "baz"
        );
        List<String> compositions3 = Arrays.asList(
                "boo",
                "goo"
        );
        Map<String, List<String>> testCompositions = new HashMap<>();
        testCompositions.put("A", compositions1);
        testCompositions.put("B", compositions2);
        testCompositions.put("C", compositions3);

        String expected = "A";
        String actual = findPrinter(testCompositions);
        assertEquals(expected, actual);
    }

    @Test
    public void testCalculateGlobalOrder() throws Exception {
        Map<String, Integer> order1 = new HashMap<>();
        order1.put("spam", 10);
        order1.put("eggs", 10);

        Map<String, Integer> order2 = new HashMap<>();
        order2.put("eggs", 12);
        order2.put("bacon", 12);
        order2.put("ham", 12);

        Map<String, Integer> order3 = new HashMap<>();
        order3.put("eggs", 20);

        List<Map<String, Integer>> testOrders = Arrays.asList(
                order1,
                order2,
                order3
        );

        Map<String, Integer> expected = new HashMap<>();
        expected.put("spam", 10);
        expected.put("eggs", 42);
        expected.put("bacon", 12);
        expected.put("ham", 12);


        Map<String, Integer> actual = calculateGlobalOrder(testOrders);
        assertEquals(expected, actual);
    }
}
