package ru.spbau.mit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Supplier;

public class ThreadPoolImpl implements ThreadPool {
    private final List<Thread> threads;
    private final Queue<LightFuture<?>> tasks;

    public ThreadPoolImpl(int n) {
        threads = new ArrayList<>();
        tasks = new LinkedList<>();
        for (int i = 0; i < n; ++i) {
            threads.add(
                    new Thread(
                            () -> {
                                try {
                                    while (!Thread.currentThread().isInterrupted()) {
                                        LightFuture<?> task;
                                        synchronized (tasks) {
                                            while (tasks.isEmpty()) {
                                                tasks.wait();
                                            }
                                            task = tasks.remove();
                                        }
                                        ((LightFutureImpl<?>) task).execute();
                                    }
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                            })
            );
        }
        threads.forEach(Thread::start);
    }

    @Override
    public <R> LightFuture<R> submit(Supplier<R> supplier) {
        return addTask(new LightFutureImpl<>(supplier));
    }

    private <R> LightFuture<R> addTask(LightFuture<R> task) {
        synchronized (tasks) {
            tasks.add(task);
            tasks.notify();
        }
        return task;
    }

    private <R, U> LightFuture<U> createPendingTask(
            LightFuture<R> argument,
            Function<? super R, ? extends U> f
    ) {
        return addTask(
                new LightFutureImpl<>(
                        () -> {
                            try {
                                return f.apply(argument.get());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
        );
    }

    @Override
    public synchronized void shutdown() {
        threads.forEach(Thread::interrupt);
        threads.clear();
    }

    private class LightFutureImpl<R> implements LightFuture<R> {
        private R result;
        private volatile boolean isReady = false;
        private LightExecutionException failureCause;
        private Supplier<R> supplier;
        private final List<LightFuture<?>> pendingTasks = new ArrayList<>();

        LightFutureImpl(Supplier<R> supplier) {
            this.supplier = supplier;
        }

        @Override
        public boolean isReady() {
            return isReady;
        }

        @Override
        public R get() throws LightExecutionException, InterruptedException {
            if (!isReady) {
                synchronized (this) {
                    while (!isReady) {
                        wait();
                    }
                }
            }
            if (failureCause != null) {
                throw failureCause;
            }
            return result;
        }

        @Override
        public <U> LightFuture<U> thenApply(Function<? super R, ? extends U> f) {
            LightFuture<U> pending = createPendingTask(this, f);
            if (!isReady) {
                synchronized (this) {
                    pendingTasks.add(pending);
                }
            } else {
                addTask(pending);
            }
            return pending;
        }

        private void execute() {
            try {
                result = supplier.get();
            } catch (Exception e) {
                failureCause = new LightExecutionException(e);
            }
            isReady = true;

            synchronized (pendingTasks) {
                pendingTasks.forEach(ThreadPoolImpl.this::addTask);
                pendingTasks.clear();
            }

            synchronized (this) {
                notifyAll();
            }
        }
    }
}
