package MiniProject1;

import java.util.ArrayList;

public class ConcurrentPartition {
    private static class WorkerThread extends Thread {
        // Find the maximum value in our particular piece of the array
        public void run() {
            //count numbers from 1 to 10000
            int j = 0;
            for (int i = 0; i < 10000; i++) {
                j = i + 1;
            }
        }

    }


    public long partition(int numberOfThreads, int hashBits) {
        //start timer
        long startTime = System.currentTimeMillis();
        //create numberOfThreads threads
        Thread[] threads = new Thread[numberOfThreads];
        //give threads work
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new WorkerThread();       
            };
        
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
