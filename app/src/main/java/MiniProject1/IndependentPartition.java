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


    public long partition(int numberOfThreads, int hashBits, ArrayList<Integer> results) {
        System.out.println("Independent Partition" + " " + numberOfThreads + " " + hashBits);
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

        Thread[] threads = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new WorkerThread(buckets.get(i), hashBits);       
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
