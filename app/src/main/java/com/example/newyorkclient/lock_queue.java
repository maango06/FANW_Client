package com.example.newyorkclient;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class lock_queue {
    Queue<String> que = new LinkedList<>();
    ReentrantLock lock = new ReentrantLock();

    void push(String _str) {
        lock.lock();
        this.que.add(_str);
        lock.unlock();
    }

    String pop() {
        lock.lock();
        String _str = this.que.poll();
        lock.unlock();
        return _str;
    }
}
