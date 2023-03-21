package MiniProject1;

import java.sql.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.lang.Math;

public class IndependentPartition {
    public long partition(int numberOfThreads, int hashBits) {
        System.out.println("Independent Partition" + " " + numberOfThreads + " " + hashBits);
        long numberOfTuples = 32000000;

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
                    Long[][] partitions = new Long[hashBits][(bucket[index].length / hashBits)+1];
                    int[] counters = new int[hashBits];
                    for (int i = 0; i < hashBits; i++) {
                        counters[i] = 0;
                    }
                    for (int j = 0; j < bucket[index].length; j++) {
                        int partitionIndex = bucket[index][j].intValue() % hashBits;
                        partitions[partitionIndex][counters[partitionIndex]] = bucket[index][j];
                        counters[partitionIndex]++;
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
