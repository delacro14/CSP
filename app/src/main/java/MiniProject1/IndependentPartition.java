package MiniProject1;

import java.util.ArrayList;

public class IndependentPartition {
    private static class WorkerThread extends Thread {
        private ArrayList<Long> bucket;
        private int hashBits;

        public WorkerThread(ArrayList<Long> bucket, int hashBits) {
            this.bucket = bucket;
            this.hashBits = hashBits;
        }
        // Find the maximum value in our particular piece of the array
        public void run() {
            //initialize partitions
            ArrayList<ArrayList<Long>> partitions = new ArrayList<>();
            for (int i = 0; i < hashBits; i++) {
                partitions.add(new ArrayList<>());
            }
            //partition the bucket
            for (int i = 0; i < bucket.size(); i++) {
                partitions.get((int)(bucket.get(i) % hashBits)).add(bucket.get(i));
            }
        }

    }


    public long partition(int numberOfThreads, int hashBits) {
        //System.out.println("Partition of " + numberOfThreads + " threads and " + hashBits + " hash bits");

        ArrayList<Long> list = new ArrayList<>();
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
        //give threads work
        for (int i = 0; i < numberOfThreads; i++) {
            //System.out.println(buckets.size());
            //System.out.println(i);
            threads[i] = new WorkerThread(buckets.get(i), hashBits);       
            };
            
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
        return endTime - startTime;
    }

}
