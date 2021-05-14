package com.github.xfl12345.jsp_netdisk.model.utility;


import java.util.concurrent.atomic.AtomicReference;
public class UnreentrantLock {
    private final AtomicReference<Thread> owner = new AtomicReference<Thread>();
    public void lock() {
        Thread current = Thread.currentThread();
        //这句是很经典的“自旋”语法，AtomicInteger中也有
        for (;;) {
            if (!owner.compareAndSet(null, current)) {
                return;
            }
        }
    }
    public void unlock() {
        Thread current = Thread.currentThread();
        owner.compareAndSet(current, null);
    }
}