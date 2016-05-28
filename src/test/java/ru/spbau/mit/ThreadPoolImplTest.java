package ru.spbau.mit;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
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
            } catch (Exception e) {
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
        try {
            darkFuture.get();
        } catch (InterruptedException e) {
            fail();
        }
    }

    @Test
    public void testSize() {
        ThreadPool pool = new ThreadPoolImpl(10);
        CyclicBarrier barrier = new CyclicBarrier(10);
        List<LightFuture<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            futures.add(pool.submit(
                    () -> {
                        try {
                            return barrier.await() + 1;
                        } catch (InterruptedException | BrokenBarrierException ignored) {
                        }
                        return -1;
                    }
            ));
        }

        int expected = -1;
        for (LightFuture<Integer> future : futures) {
            try {
                int ret = future.get();
                expected = (expected > ret) ? expected : ret;
            } catch (LightExecutionException | InterruptedException ignored) {
                fail();
            }
        }
        assertEquals(expected, 10);
    }

    @Test
    public void testThenApply() throws LightExecutionException {
        ThreadPool pool = new ThreadPoolImpl(10);
        LightFuture<Integer> task = pool.submit(
                () -> 1
        );
        LightFuture<Integer> pending = task.thenApply(
                (x) -> x * 2
        );

        Integer res;
        try {
            res = task.get();
            assertEquals(1, (int) res);
        } catch (InterruptedException e) {
            fail();
        }

        Integer pendingRes;
        try {
            pendingRes = pending.get();
            assertEquals(2, (int) pendingRes);
        } catch (InterruptedException ignored) {
            fail();
        }
    }
}
