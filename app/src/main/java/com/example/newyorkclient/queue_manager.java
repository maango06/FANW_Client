package com.example.newyorkclient;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class queue_manager {
    Queue<String> que = new LinkedList<>();
    Lock lock = new ReentrantLock();

    void insert(String _str) {
        lock.lock();
        que.add(_str);
        lock.unlock();
    }

    String pop() {
        lock.lock();
        String msg = que.poll();
        lock.unlock();
        return msg;

    }
}
