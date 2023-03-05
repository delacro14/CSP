package MiniProject1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentPartition {
    private static class WorkerThread extends Thread {
        // Find the maximum value in our particular piece of the array
        private int hashBits;
        private ArrayList<Long> bucket;
        ArrayList<Long> buffer;
        ReentrantLock lock;

        public WorkerThread(
                ArrayList<Long> bucket,
                int hashBits,
                ArrayList<Long> buffer,
                ReentrantLock lock) {
            this.bucket = bucket;
            this.hashBits = hashBits;
            this.buffer = buffer;
            this.lock = lock;
        }

        public void run() {
            //partition the bucket
            for (int i = 0; i < bucket.size(); i++) {
                Long value = bucket.get(i);
                lock.lock();
                buffer.add(value);
                lock.unlock();
            }
        }
    }

    public long partition(int numberOfThreads, int hashBits) {

        ArrayList<Long> list = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads*100000; i++) {
            list.add((long)(Math.random() * 10000000));
        }

        ArrayList<ArrayList<Long>> buckets = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            buckets.add(new ArrayList<>());
        }

        for (int i = 0; i < numberOfThreads; i++) {
            for (int j = i; j < 100000; j++) {
                buckets.get(i).add(list.get(j));
            }
        }

        Thread[] threads = new Thread[numberOfThreads];
        ArrayList<Long> buffer = new ArrayList<>();

        ReentrantLock lock = new ReentrantLock();

        //give threads work
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new ConcurrentPartition.WorkerThread(
                    buckets.get(i),
                    hashBits,
                    buffer,
                    lock);
        }

        //start timer
        long startTime = System.currentTimeMillis();

        //start threads
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i].start();
        }
        //wait for threads to finish
        for (int i = 0; i < numberOfThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //stop timer
        long endTime = System.currentTimeMillis();
        //return time
        int count = 0;
        for (int i = 0; i < buckets.size(); i++) {
            count = count + buckets.get(i).size();
        }
        System.out.println("number of threads: " + numberOfThreads);
        System.out.println("hash bits: " + hashBits);
        System.out.println("buckets size: " + count);
        System.out.println("buffer size: " + buffer.size());
        return endTime - startTime;
    }

}
