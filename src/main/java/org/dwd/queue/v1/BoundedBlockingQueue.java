package org.dwd.queue.v1;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * @author duanweidong
 * @version 2021/6/25 22:40
 */
public class BoundedBlockingQueue<T> {

    private LinkedList<T> data;

    private Semaphore full;

    private Semaphore empty;

    int capacity;

    int size;

    public BoundedBlockingQueue(int capacity) {
        this.data = new LinkedList<T>();
        this.full = new Semaphore(capacity);
        this.empty = new Semaphore(0);
        this.capacity = capacity;
        this.size = 0;
    }

    /**
     * 入队
     * @param val
     * @throws InterruptedException
     */
    public void enQueue(T val) throws InterruptedException {
        full.acquire();
        data.add(val);
        empty.release();
    }

    /**
     * 出队
     * @return
     * @throws InterruptedException
     */
    public T deQueue() throws InterruptedException {
        empty.acquire();
        T res = data.poll();
        full.acquire();
        return res;
    }

    /**
     * 大小
     * @return
     */
    public int size() {
        return data.size();
    }
}
