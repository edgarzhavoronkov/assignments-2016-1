package ru.spbau.mit;

import org.junit.Test;
import ru.spbau.mit.functional.Predicate;

import static org.junit.Assert.*;

/**
 * Created by edgar
 * on 19.03.16.
 */

public class TestPredicate {
    private static final Integer FOURTY_ONE = 41;
    private static final Integer FOURTY_TWO = 42;
    private static final Integer FIFTY_TWO = 52;
    private static final Integer FIFTY_ONE = 51;

    private static final Integer ONE = 1;
    private static final Integer TW0 = 2;

    private static Predicate<Integer> greaterThanFourtyTwo = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer integer) {
            return integer > FOURTY_TWO;
        }
    };

    private static Predicate<Integer> isOdd = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer integer) {
            return integer % TW0 == ONE;
        }
    };

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> trve = Predicate.ALWAYS_TRUE;

        assertNotNull(trve);

        assertTrue(trve.apply("42"));
        assertTrue(trve.apply(FOURTY_ONE));
        assertTrue(trve.apply(FIFTY_TWO));
        assertTrue(trve.apply(FOURTY_TWO));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Object> notTrve = Predicate.ALWAYS_FALSE;

        assertNotNull(notTrve);

        assertFalse(notTrve.apply("42"));
        assertFalse(notTrve.apply(FOURTY_ONE));
        assertFalse(notTrve.apply(FIFTY_TWO));
        assertFalse(notTrve.apply(FOURTY_TWO));
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

        assertTrue(greaterOrOdd.apply(FIFTY_TWO));
        assertTrue(greaterOrOdd.apply(FOURTY_ONE));
        assertFalse(greaterOrOdd.apply(FOURTY_TWO));

        assertTrue(greaterThanFourtyTwo.or(Predicate.ALWAYS_TRUE).apply(FOURTY_TWO));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> greaterAndOdd = greaterThanFourtyTwo.and(isOdd);

        assertNotNull(greaterAndOdd);

        assertFalse(greaterAndOdd.apply(FIFTY_TWO));
        assertFalse(greaterAndOdd.apply(FOURTY_ONE));
        assertFalse(greaterAndOdd.apply(FOURTY_TWO));
        assertTrue(greaterAndOdd.apply(FIFTY_ONE));

        assertFalse(greaterThanFourtyTwo.and(Predicate.ALWAYS_FALSE).apply(FIFTY_TWO));
    }

    @Test
    public void testNot() {
        Predicate<Integer> lessOrEqualThanFourtyTwo = greaterThanFourtyTwo.not();
        Predicate<Integer> isEven = isOdd.not();

        assertNotNull(lessOrEqualThanFourtyTwo);
        assertNotNull(isEven);

        assertTrue(lessOrEqualThanFourtyTwo.apply(FOURTY_ONE));
        assertTrue(isEven.apply(FOURTY_TWO));
    }
}
