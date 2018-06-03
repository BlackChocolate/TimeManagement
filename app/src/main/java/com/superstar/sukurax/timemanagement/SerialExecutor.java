package com.superstar.sukurax.timemanagement;

import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

class SerialExecutor {
    final Queue<Runnable> tasks = new LinkedBlockingQueue<Runnable>();
    final Executor executor;
    Runnable active;

    SerialExecutor(Executor executor) {
        this.executor = executor;
    }

    public void addrun(Runnable r) {
        tasks.add(r);
    }

    public void execute(final Runnable r) {
        try {
            r.run();
        } finally {
            scheduleNext();
        }
    }

    protected void scheduleNext() {
        if ((active = tasks.poll()) != null) {
            this.execute(active);
        }
    }
}