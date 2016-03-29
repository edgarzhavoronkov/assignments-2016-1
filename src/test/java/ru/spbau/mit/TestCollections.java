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

    private final Function1<Integer, Integer> id = new Function1<Integer, Integer>() {
        @Override
        public Integer apply(Integer integer) {
            return integer;
        }
    };

    private final Function2<Integer, Integer, Integer> sub = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer integer, Integer integer2) {
            return integer - integer2;
        }
    };

    private final Predicate<Integer> greaterthanTen = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer integer) {
            return integer > 10;
        }
    };

    @Test
    public void testMap() {
        Collection<Integer> mappedArray = Collections.map(
                id,
                Arrays.asList(1, 2, 4, 8, 16, 32, 64));

        assertNotNull(mappedArray);
        assertEquals(Arrays.asList(1, 2, 4, 8, 16, 32, 64).size(), mappedArray.size());
        assertArrayEquals(Arrays.asList(1, 2, 4, 8, 16, 32, 64).toArray(), mappedArray.toArray());
    }

    @Test
    public void testFilter() {
        Collection<Integer> filteredArray = Collections.filter(
                greaterthanTen,
                Arrays.asList(1, 2, 4, 8, 16, 32, 64));

        assertNotNull(filteredArray);
        assertEquals(Arrays.asList(16, 32, 64).size(), filteredArray.size());
        assertArrayEquals(Arrays.asList(16, 32, 64).toArray(), filteredArray.toArray());

    }

    @Test
    public void testTakeWhile() {
        Collection<Integer> takenArray = Collections.takeWhile(
                greaterthanTen,
                Arrays.asList(1, 2, 4, 8, 16, 32, 64));
        Collection<Integer> anotherTakenArray = Collections.takeWhile(
                greaterthanTen,
                Arrays.asList(128, 256, 512));

        assertNotNull(takenArray);
        assertNotNull(anotherTakenArray);

        assertTrue(takenArray.isEmpty());
        assertArrayEquals(Arrays.asList(128, 256, 512).toArray(), anotherTakenArray.toArray());
    }

    @Test
    public void testTakeUnless() {
        Collection<Integer> takenArray = Collections.takeUnless(
                greaterthanTen,
                Arrays.asList(1, 2, 4, 8, 16, 32, 64));

        assertNotNull(takenArray);
        assertEquals(Arrays.asList(1, 2, 4, 8).size(), takenArray.size());
        assertArrayEquals(Arrays.asList(1, 2, 4, 8).toArray(), takenArray.toArray());
    }

    @Test
    public void testFoldr() {
        int res = Collections.foldr(sub, 0, Arrays.asList(1, 2, 4, 8, 16, 32, 64));
        int one = Collections.foldr(sub, 1, new ArrayList<Integer>());

        assertEquals(43, res);
        assertEquals(1, one);
    }

    @Test
    public void testFoldl() {
        int res = Collections.foldl(sub, 0, Arrays.asList(1, 2, 4, 8, 16, 32, 64));
        int one = Collections.foldl(sub, 1, new ArrayList<Integer>());

        assertEquals(-127, res);
        assertEquals(1, one);
    }

    @Test
    public void polymorphicFoldrTest() {
        Function2<Integer, Number, Integer> someFun = new Function2<Integer, Number, Integer>() {
            @Override
            public Integer apply(Integer integer, Number number) {
                return integer + number.intValue();
            }
        };

        int res = Collections.foldr(someFun, 0, Arrays.asList(1, 2, 3));
        assertEquals(6, res);
    }
}
