package ru.spbau.mit;

import org.junit.Test;
import ru.spbau.mit.collections.Collections;
import ru.spbau.mit.functional.Function1;
import ru.spbau.mit.functional.Function2;
import ru.spbau.mit.functional.Predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Created by edgar
 * on 18.03.16.
 */


public class TestCollections {
    private static final Integer ONE = 1;
    private static final Integer TEN = 10;
    private static final Integer PRODUCT = 2097152;

    private final Function1<Integer, Integer> id = new Function1<Integer, Integer>() {
        @Override
        public Integer apply(Integer integer) {
            return integer;
        }
    };

    private final Function2<Integer, Integer, Integer> mult = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer integer, Integer integer2) {
            return integer * integer2;
        }
    };

    private final Predicate<Integer> greaterthanTen = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer integer) {
            return integer > TEN;
        }
    };

    private final ArrayList<Integer> testArray = new ArrayList<>(
            Arrays.asList(1, 2, 4, 8, 16, 32, 64)
    );

    private final ArrayList<Integer> anotherTestArray = new ArrayList<>(
            Arrays.asList(128, 256, 512)
    );

    private final ArrayList<Integer> anotherExpectedTestArray = new ArrayList<>(
            Arrays.asList(128, 256, 512)
    );

    private final ArrayList<Integer> expectedFilteredArray = new ArrayList<>(
            Arrays.asList(16, 32, 64)
    );

    private final ArrayList<Integer>  expectedTakenArray = new ArrayList<>(
            Arrays.asList(1, 2, 4, 8)
    );

    @Test
    public void testMap() {
        Collection<Integer> mappedArray = Collections.map(id, testArray);

        assertNotNull(mappedArray);
        assertEquals(testArray.size(), mappedArray.size());
        assertArrayEquals(testArray.toArray(), mappedArray.toArray());
    }

    @Test
    public void testFilter() {
        Collection<Integer> filteredArray = Collections.filter(greaterthanTen, testArray);

        assertNotNull(filteredArray);
        assertEquals(expectedFilteredArray.size(), filteredArray.size());
        assertArrayEquals(expectedFilteredArray.toArray(), filteredArray.toArray());

    }

    @Test
    public void testTakeWhile() {
        Collection<Integer> takenArray = Collections.takeWhile(greaterthanTen, testArray);
        Collection<Integer> anotherTakenArray = Collections.takeWhile(greaterthanTen, anotherTestArray);

        assertNotNull(takenArray);
        assertNotNull(anotherTakenArray);

        assertTrue(takenArray.isEmpty());
        assertArrayEquals(anotherExpectedTestArray.toArray(), anotherTakenArray.toArray());
    }

    @Test
    public void testTakeUnless() {
        Collection<Integer> takenArray = Collections.takeUnless(greaterthanTen, testArray);

        assertNotNull(takenArray);
        assertEquals(expectedTakenArray.size(), takenArray.size());
        assertArrayEquals(expectedTakenArray.toArray(), takenArray.toArray());
    }

    @Test
    public void testFoldr() {
        Integer product = Collections.foldr(mult, ONE, testArray);
        Integer one = Collections.foldr(mult, ONE, new ArrayList<Integer>());

        assertEquals(PRODUCT, product);
        assertEquals(ONE, one);
    }

    @Test
    public void testFoldl() {
        Integer product = Collections.foldl(mult, 1, testArray);
        Integer one = Collections.foldl(mult, ONE, new ArrayList<Integer>());

        assertEquals(PRODUCT, product);
        assertEquals(ONE, one);
    }
}
