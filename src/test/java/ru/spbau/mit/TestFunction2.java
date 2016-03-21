package ru.spbau.mit;

import org.junit.Test;
import ru.spbau.mit.functional.Function1;
import ru.spbau.mit.functional.Function2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by edgar
 * on 18.03.16.
 */



public class TestFunction2 {
    private static final Integer TWO = 2;
    private static final Integer FOUR = 4;

    private static Function2<Integer, Integer, Integer> sum = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer integer, Integer integer2) {
            return integer + integer2;
        }
    };

    private static Function2<Integer, Integer, Integer> mult = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer integer, Integer integer2) {
            return integer * integer2;
        }
    };

    private static Function1<Object, String> toStr = new Function1<Object, String>() {
        @Override
        public String apply(Object o) {
            return o.toString();
        }
    };

    @Test
    public void testCreate() {
        assertNotNull(sum);
        assertNotNull(mult);
        assertNotNull(toStr);
    }

    @Test
    public void testApply() {
        assertEquals(FOUR, sum.apply(TWO, TWO));
        assertEquals(FOUR, mult.apply(TWO, TWO));
    }

    @Test
    public void testCompose() {
        Function2<Integer, Integer, String> sumCmps = sum.compose(toStr);
        Function2<Integer, Integer, String> multCmps = mult.compose(toStr);

        assertNotNull(sumCmps);
        assertNotNull(multCmps);

        assertEquals("4", sumCmps.apply(TWO, TWO));
        assertEquals("4", multCmps.apply(TWO, TWO));
    }

    @Test
    public void testBind1() {
        Function1<Integer, Integer> sumBind1 = sum.bind1(TWO);
        Function1<Integer, Integer> multBind1 = mult.bind1(TWO);

        assertNotNull(sumBind1);
        assertNotNull(multBind1);

        assertEquals(FOUR, sumBind1.apply(TWO));
        assertEquals(FOUR, multBind1.apply(TWO));
    }

    @Test
    public void testBind2() {
        Function1<Integer, Integer> sumBind2 = sum.bind2(TWO);
        Function1<Integer, Integer> multBind2 = mult.bind2(TWO);

        assertNotNull(sumBind2);
        assertNotNull(multBind2);

        assertEquals(FOUR, sumBind2.apply(TWO));
        assertEquals(FOUR, multBind2.apply(TWO));
    }

    @Test
    public void testCurry() {
        Function1<Integer, Function1<Integer, Integer>> sumCurry = sum.curry();
        Function1<Integer, Function1<Integer, Integer>> multCurry = mult.curry();

        Function1<Integer, Integer> plusTwo = sumCurry.apply(TWO);
        Function1<Integer, Integer> multTwo = multCurry.apply(TWO);

        assertNotNull(sumCurry);
        assertNotNull(multCurry);
        assertNotNull(plusTwo);
        assertNotNull(multTwo);

        assertEquals(FOUR, plusTwo.apply(TWO));
        assertEquals(FOUR, multTwo.apply(TWO));
    }
}
