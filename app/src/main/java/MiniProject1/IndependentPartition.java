package MiniProject1;

import java.sql.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
        System.out.println("Independent Partition" + " " + numberOfThreads + " " + hashBits);
        long numberOfTuples = 16777216;

        Long[] list = new Long[(int) numberOfTuples];

        for (int i = 0; i < numberOfTuples; i++) {
            list[i] = (long) i;
        }

        Long[][] bucket = new Long[numberOfThreads][(int) numberOfTuples / numberOfThreads];
        
        int blockSize = (int) numberOfTuples / numberOfThreads;
        
        for (int i = 0; i < numberOfThreads; i++) {
            for (int j = i * blockSize; j < (i+1) * blockSize; j++) {
                bucket[i][j - i * blockSize] = list[j];
            }
        }
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < numberOfThreads; i++) {
            final int index = i;

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Long[][] partitions = new Long[hashBits][(bucket[index].length / hashBits)];
                    for (int j = 0; j < bucket[index].length; j++) {
                        partitions[bucket[index][j].intValue() % hashBits][j] = bucket[index][j];
                    }
                }
            });
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime));
        return 0;   
    }

    //     Thread[] threads = new Thread[numberOfThreads];
    //     for (int i = 0; i < numberOfThreads; i++) {
    //         threads[i] = new WorkerThread(buckets.get(i), hashBits);       
    //     };

    //     long startTime = System.currentTimeMillis();
        
    //     //start threads
    //     for (int i = 0; i < numberOfThreads; i++) {
    //         threads[i].start();
    //     }
    //     //wait for threads to finish
    //     for (int i = 0; i < numberOfThreads; i++) {
    //         try {
    //             threads[i].join();
    //         } catch (InterruptedException e) {
    //             e.printStackTrace();
    //         }
    //     }
    //     //stop timer
    //     long endTime = System.currentTimeMillis();
    //     results.add((int) (numberOfTuples));
    //     //return time
    //     return endTime - startTime;
    // }

}
