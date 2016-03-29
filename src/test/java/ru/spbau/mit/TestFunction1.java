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
        assertEquals(8, (int) id.apply(8));
        assertEquals(10, (int) id.apply(10));

        assertEquals(10, (int) plusTwo.apply(8));

        assertEquals("8", toStr.apply(8));
        assertEquals("8.0", toStr.apply((double) 8));
    }

    @Test
    public void testCompose() {
        Function1<Integer, String> idToStrCmps = id.compose(toStr);
        Function1<Integer, Integer> plusIdCmps = plusTwo.compose(id);

        assertNotNull(idToStrCmps);
        assertNotNull(plusIdCmps);

        assertEquals("8", idToStrCmps.apply(8));
        assertEquals(10, (int) plusIdCmps.apply(8));
    }
}
