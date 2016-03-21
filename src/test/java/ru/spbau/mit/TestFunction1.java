package ru.spbau.mit;

import org.junit.Test;
import ru.spbau.mit.functional.Function1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by edgar
 * on 19.03.16.
 */

public class TestFunction1 {
    private static final Integer EIGHT = 8;
    private static final Integer TEN = 10;

    private static Function1<Integer, Integer> id = new Function1<Integer, Integer>() {
        @Override
        public Integer apply(Integer integer) {
            return integer;
        }
    };

    private static Function1<Integer, Integer> plusTwo = new Function1<Integer, Integer>() {
        @Override
        public Integer apply(Integer integer) {
            return integer + 2;
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
        assertNotNull(id);
        assertNotNull(plusTwo);
        assertNotNull(toStr);
    }

    @Test
    public void testApply() {
        assertEquals(EIGHT, id.apply(EIGHT));
        assertEquals(TEN, id.apply(TEN));

        assertEquals(TEN, plusTwo.apply(EIGHT));

        assertEquals("8", toStr.apply(EIGHT));
        assertEquals("8.0", toStr.apply((double) EIGHT));
    }

    @Test
    public void testCompose() {
        Function1<Integer, String> idToStrCmps = id.compose(toStr);
        Function1<Integer, Integer> plusIdCmps = plusTwo.compose(id);

        assertNotNull(idToStrCmps);
        assertNotNull(plusIdCmps);

        assertEquals("8", idToStrCmps.apply(EIGHT));
        assertEquals(TEN, plusIdCmps.apply(EIGHT));
    }
}
