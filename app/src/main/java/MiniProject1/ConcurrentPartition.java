package MiniProject1;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.lang.Math;
import java.util.concurrent.Callable;

public class ConcurrentPartition {
    // public static class PartitionJob implements Callable<Void> {
    //     Long[][] partitions;
    //     AtomicIntegerArray[] counters;
    //     int index;
    //     Long[][] bucket;
    //     int hashBits;
    
    //     public PartitionJob(Long[][] partitions, AtomicIntegerArray[] counters, int index, Long[][] bucket, int hashBits) {
    //         this.partitions = partitions;
    //         this.counters = counters;
    //         this.index = index;
    //         this.bucket = bucket;
    //         this.hashBits = hashBits;
    //     }
    
    //     @Override
    //     public Void call() throws Exception {
    //         for (int j = 0; j < bucket[index].length; j++) {
    //             int partitionIndex = bucket[index][j].intValue() % hashBits;
    //             int position = counters.getAndIncrement(bucket[index][j].intValue() % hashBits);
    //             partitions[partitionIndex][position] = bucket[index][j];
    //         }        
    //     }
    // }
    
    public long partition(int numberOfThreads, int hashBits) {
        System.out.println("Concurrent Partition" + " " + numberOfThreads + " " + hashBits);
        long numberOfTuples = 3200000;
        
        ReentrantLock[] locks = new ReentrantLock[hashBits];

        // for (int i = 0; i < hashBits; i++) {
        //     locks[i] = new ReentrantLock();
        // }
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
        Long[][] partitions = new Long[hashBits][(int) numberOfTuples / hashBits + 1];
        AtomicIntegerArray counters = new AtomicIntegerArray(hashBits);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < numberOfThreads; i++) {
            final int index = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < bucket[index].length; j++) {
                        int partitionIndex = bucket[index][j].intValue() % hashBits;
                        int position = counters.getAndIncrement(partitionIndex);
                        partitions[partitionIndex][position] = bucket[index][j];
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
        return endTime - startTime;   

    }

}