package MiniProject1;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentPartition {
    private static class WorkerThread implements Runnable {
        // Find the maximum value in our particular piece of the array
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

    public long partition(int numberOfThreads, int hashBits) {
//        System.out.println("number of threads: " + numberOfThreads);
//        System.out.println("hash bits: " + hashBits);
        ArrayList<Long> list = new ArrayList<>();

        for (int i = 0; i < numberOfThreads * 100000; i++) {
            list.add((long) (Math.random() * 10000000));
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

        ArrayList<ArrayList<Long>> buffer = new ArrayList<>();
        for (int i = 0; i < hashBits; i++) {
            buffer.add(new ArrayList<>());
        }
        ReentrantLock[] locks = new ReentrantLock[hashBits];

        for (int i = 0; i < hashBits; i++) {
            locks[i] = new ReentrantLock();
        }

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
//        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        //give threads work
        for (int i = 0; i < numberOfThreads; i++) {
            executor.execute(new ConcurrentPartition.WorkerThread(
                    buckets.get(i),
                    hashBits,
                    buffer,
                    locks));
        }

        //start timer
        long startTime = System.currentTimeMillis();

        //start threads
        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //stop timer
        long endTime = System.currentTimeMillis();
        //return time
//        int countBucket = 0;
//        int countBuffer = 0;
//        for (int i = 0; i < buckets.size(); i++) {
//            countBucket = countBucket + buckets.get(i).size();
//        }
//        for (int i = 0; i < buffer.size(); i++) {
//            countBuffer = countBuffer + buffer.get(i).size();
//            System.out.println("buffer[" + i + "]" + buffer.get(i).size());
//        }
//
//        System.out.println("buckets size: " + countBucket);
//        System.out.println("buffer size: " + countBuffer);
        return endTime - startTime;
    }

}