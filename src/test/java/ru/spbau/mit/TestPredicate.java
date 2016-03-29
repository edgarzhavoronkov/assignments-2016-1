package ru.spbau.mit;

import org.junit.Test;
import ru.spbau.mit.functional.Predicate;

import static org.junit.Assert.*;

/**
 * Created by edgar
 * on 19.03.16.
 */

public class TestPredicate {
    private static Predicate<Integer> greaterThanFourtyTwo = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer integer) {
            return integer > 42;
        }
    };

    private static Predicate<Integer> isOdd = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer integer) {
            return integer % 2 == 1;
        }
    };

    private static Predicate<Integer> dangerousPredicate = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer integer) {
            return integer / 0 == 0;
        }
    };

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> trve = Predicate.ALWAYS_TRUE;

        assertNotNull(trve);

        assertTrue(trve.apply("42"));
        assertTrue(trve.apply(41));
        assertTrue(trve.apply(52));
        assertTrue(trve.apply(42.0));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Object> notTrve = Predicate.ALWAYS_FALSE;

        assertNotNull(notTrve);

        assertFalse(notTrve.apply("42"));
        assertFalse(notTrve.apply(41));
        assertFalse(notTrve.apply(52));
        assertFalse(notTrve.apply(42.0));
    }

    @Test
    public void testCreate() {
        assertNotNull(greaterThanFourtyTwo);
        assertNotNull(isOdd);
    }

    @Test
    public void testOr() {
        Predicate<Integer> greaterOrOdd = greaterThanFourtyTwo.or(isOdd);

        assertNotNull(greaterOrOdd);

        assertTrue(greaterOrOdd.apply(52));
        assertTrue(greaterOrOdd.apply(41));
        assertFalse(greaterOrOdd.apply(42));

        assertTrue(greaterThanFourtyTwo.or(Predicate.ALWAYS_TRUE).apply(42));
    }

    @Test
    public void testLazyOr() {
        Predicate<Integer> lazyPredicate = greaterThanFourtyTwo.or(dangerousPredicate);

        assertNotNull(lazyPredicate);

        assertTrue(lazyPredicate.apply(43));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> greaterAndOdd = greaterThanFourtyTwo.and(isOdd);

        assertNotNull(greaterAndOdd);

        assertFalse(greaterAndOdd.apply(52));
        assertFalse(greaterAndOdd.apply(41));
        assertFalse(greaterAndOdd.apply(42));
        assertTrue(greaterAndOdd.apply(51));

        assertFalse(greaterThanFourtyTwo.and(Predicate.ALWAYS_FALSE).apply(52));
    }

    @Test
    public void testLazyAnd() {
        Predicate<Integer> lazyPredicate = greaterThanFourtyTwo.and(dangerousPredicate);

        assertNotNull(lazyPredicate);

        assertFalse(lazyPredicate.apply(41));
    }

    @Test
    public void testNot() {
        Predicate<Integer> lessOrEqualThanFourtyTwo = greaterThanFourtyTwo.not();
        Predicate<Integer> isEven = isOdd.not();

        assertNotNull(lessOrEqualThanFourtyTwo);
        assertNotNull(isEven);

        assertTrue(lessOrEqualThanFourtyTwo.apply(41));
        assertTrue(isEven.apply(42));
    }
}
