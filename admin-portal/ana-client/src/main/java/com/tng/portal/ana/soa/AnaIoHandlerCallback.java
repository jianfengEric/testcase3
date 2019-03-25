/*
package com.tng.portal.ana.soa;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnaIoHandlerCallback extends Thread {
    private static transient Logger log = LoggerFactory.getLogger(AnaIoHandlerCallback.class);
    private static Map<String,BlockingQueue> hashCall = new ConcurrentHashMap<>();
    private int clients = 0;
    private String key = null;

    public AnaIoHandlerCallback(final String key, final int clients){
        this.key = key;
        this.clients = clients;
        hashCall.put(key,new ArrayBlockingQueue(clients));
    }

    @Override
    public void run() {
        try{
            AtomicInteger counter = new AtomicInteger();
            while(clients > counter.get()){
                hashCall.get(this.key).take();
                counter.getAndIncrement();
            }
        }catch (Exception e){
            log.debug("Run Timeout Thread Error",e);
        }
    }
    private static Lock lock = new ReentrantLock();
    public static void push(String key){
        lock.lock();
        try {
            BlockingQueue blockingQueue = hashCall.get(key);
            if(blockingQueue!=null){
                blockingQueue.put(key);
            }else{
                blockingQueue = new ArrayBlockingQueue(6);
                blockingQueue.put(key);
            }
            hashCall.put(key,blockingQueue);
        } catch (Exception e) {
            log.warn("Push Callback Key Error",e);
        }finally {
            lock.unlock();
        }
    }
}
*/
