package org.dwd.queue.v2;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author duanweidong
 * @version 2021/6/25 22:52
 */
public class BoundedBlockingQueue<T> {

    private int size;

    private int capacity;

    private Lock lock;

    private Condition notFull;

    private Condition notEmpty;

    private LinkedList<T> data;

    public BoundedBlockingQueue(int capacity) {
        this.size = 0;
        this.capacity = capacity;
        this.lock = new ReentrantLock();
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();
        this.data = new LinkedList<T>();
    }

    /**
     * 入队
     * @param val
     * @throws InterruptedException
     */
    public void enQueue(T val) throws InterruptedException {
        lock.lock();
        try {
            while(size >= capacity) {
                notFull.await();
            }
            size++;
            this.data.add(val);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 出队
     * @return
     * @throws InterruptedException
     */
    public T deQueue() throws InterruptedException {
        lock.lock();
        try {
            while (size == 0) {
                notEmpty.await();
            }
            size--;
            T res = data.poll();
            notFull.signal();
            return res;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 大小
     * @return
     */
    public int size() {
        return this.size;
    }
}
