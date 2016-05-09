package ru.spbau.mit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Supplier;

public class ThreadPoolImpl implements ThreadPool {

    private final List<Thread> threads;
    private final Queue<LightFutureImpl<?>> tasks;

    public ThreadPoolImpl(int n) {
        threads = new ArrayList<>();
        tasks = new LinkedList<>();
        for (int i = 0; i < n; ++i) {
            threads.add(
                    new Thread(
                            () -> {
                                try {
                                    while (!Thread.currentThread().isInterrupted()) {
                                        LightFutureImpl<?> task;
                                        synchronized (tasks) {
                                            while (tasks.isEmpty()) {
                                                tasks.wait();
                                            }
                                            task = tasks.remove();
                                        }
                                        task.execute();
                                    }
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                            })
            );
        }
        threads.forEach(Thread::start);
    }

    public int size() {
        return threads.size();
    }

    @Override
    public <R> LightFuture<R> submit(Supplier<R> supplier) {
        return addTask(createTask(supplier));
    }

    private <R> LightFutureImpl<R> addTask(LightFutureImpl<R> task) {
        synchronized (tasks) {
            tasks.add(task);
            tasks.notify();
        }
        return task;
    }

    private <R> LightFutureImpl<R> createTask(Supplier<R> supplier) {
        return new LightFutureImpl<R>() {
            @Override
            R compute() throws LightExecutionException {
                return supplier.get();
            }
        };
    }

    private <R, U> LightFutureImpl<U> addPendingTask(
            LightFutureImpl<R> argument,
            Function<? super R, ? extends U> f
    ) {
        return new LightFutureImpl<U>() {
            @Override
            U compute() throws LightExecutionException {
                return f.apply(argument.get());
            }
        };
    }

    @Override
    public synchronized void shutdown() {
        threads.forEach(Thread::interrupt);
        threads.clear();
    }

    private abstract class LightFutureImpl<R> implements LightFuture<R> {
        private R result;
        private volatile boolean isReady = false;
        private LightExecutionException failureCause;

        private final List<LightFutureImpl<?>> pendingTasks = new ArrayList<>();

        @Override
        public boolean isReady() {
            return isReady;
        }

        @Override
        public R get() throws LightExecutionException {
            if (!isReady) {
                synchronized (this) {
                    while (!isReady) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
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
            LightFutureImpl<U> pending = addPendingTask(this, f);
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
                result = compute();
            } catch (LightExecutionException e) {
                failureCause = e;
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

        abstract R compute() throws LightExecutionException;
    }
}
