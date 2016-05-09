package ru.spbau.mit;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by edgar on 09.05.16.
 */
public class ThreadPoolImplTest {

    @Test
    public void testSimple() {
        ThreadPool pool = new ThreadPoolImpl(10);
        List<LightFuture<Integer>> tasks = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            int temp = i;
            tasks.add(pool.submit(
                    () -> temp + 1
            ));
        }

        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        List<Integer> actual = tasks.stream().map((task) -> {
            try {
                return task.get();
            } catch (LightExecutionException e) {
                e.printStackTrace();
            }
            return 0;
        }).collect(Collectors.toList());

        assertArrayEquals(expected.toArray(), actual.toArray());
        pool.shutdown();
    }

    @Test(expected = LightExecutionException.class)
    public void testException() throws LightExecutionException {
        ThreadPool pool = new ThreadPoolImpl(1);
        LightFuture darkFuture = pool.submit(
                () -> {
                    throw new RuntimeException("Oops!");
                }
        );
        darkFuture.get();
    }

    @Test
    public void testSize() {
        ThreadPoolImpl pool = new ThreadPoolImpl(10);
        assertEquals(pool.size(), 10);
        pool.shutdown();
        assertEquals(pool.size(), 0);
    }

    @Test
    public void testThenApply() throws LightExecutionException {
        ThreadPool pool = new ThreadPoolImpl(1);
        LightFuture<Integer> task = pool.submit(
                () -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                    return 1;
                }
        );
        LightFuture<Integer> pending = task.thenApply(
                (x) -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                    return x * 2;
                }
        );

        assertFalse(task.isReady());
        assertFalse(pending.isReady());

        Integer res = task.get();
        assertTrue(task.isReady());
        assertFalse(pending.isReady());
        assertEquals(1, (int) res);

        Integer pendingRes = pending.get();
        assertTrue(task.isReady());
        assertTrue(pending.isReady());
        assertEquals(2, (int) pendingRes);
    }

}
