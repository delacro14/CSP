package MiniProject1;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentPartition {
    private static class WorkerThread extends Thread {
        private int hashBits;
        private ArrayList<Long> bucket;
        ArrayList<ArrayList<Long>> buffer;
        ReentrantLock[] locks;

        public WorkerThread(
                ArrayList<Long> bucket,
                int hashBits,
                ArrayList<ArrayList<Long>> buffer,
                ReentrantLock[] locks) {
            this.bucket = bucket;
            this.hashBits = hashBits;
            this.buffer = buffer;
            this.locks = locks;
        }

        public void run() {
            //partition the bucket
            for (int i = 0; i < bucket.size(); i++) {
                Long value = bucket.get(i);
                int partition = (int) (value % hashBits);
                locks[partition].lock();
                buffer.get(partition).add(value);
                locks[partition].unlock();
            }
        }
    }

    public long partition(int numberOfThreads, int hashBits, ArrayList<Integer> results) {
        System.out.println("Concurrent Partition" + " " + numberOfThreads + " " + hashBits);
        long numberOfTuples = 3200000;

        ArrayList<Long> list = new ArrayList<>();

        for (int i = 0; i < numberOfTuples; i++) {
            list.add((long) (Math.random() * 10000000));
        }

        ArrayList<ArrayList<Long>> buckets = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            buckets.add(new ArrayList<>());
        }

        for (int i = 0; i < numberOfThreads; i++) {
            for (int j = i; j < (i+1) * (numberOfTuples / numberOfThreads); j++) {
                buckets.get(i).add(list.get(j));
            }
        }

        ArrayList<ArrayList<Long>> buffer = new ArrayList<>();
        for (int i = 0; i < hashBits; i++) {
            buffer.add(new ArrayList<>());
        }
        ReentrantLock[] locks = new ReentrantLock[hashBits];

        for (int i = 0; i < hashBits; i++) {
            locks[i] = new ReentrantLock();
        }

        //start timer

        Thread[] threads = new Thread[numberOfThreads];
        //give threads work
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new WorkerThread(
                buckets.get(i),
                hashBits,
                buffer,
                locks
            );
        };
        
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
        results.add((int) (numberOfTuples));
        //return time
        return endTime - startTime;
    }

}