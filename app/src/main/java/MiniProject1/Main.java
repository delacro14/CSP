package MiniProject1;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.io.FileWriter;
import java.io.IOException;

class Triple<T, U, V> {

    private final T first;
    private final U second;
    private final V third;

    public Triple(T first, U second, V third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T getFirst() { return first; }
    public U getSecond() { return second; }
    public V getThird() { return third; }

    //tostring
    @Override
    public String toString() {
        return first+","+second+","+third;
    }
}

public class Main {
    public static void main(String[] args) {
        // Create data
        //DataGenerator data = new DataGenerator(650);
        int[] threads = {1, 2, 4, 8, 16, 32};
        int[] hashbits = {1, 2, 4, 8, 16, 32};
        int numberOfRuns = 10;
        ArrayList<Triple> tupleList = new ArrayList<>();
        IndependentPartition ip = new IndependentPartition();
        ConcurrentPartition cp = new ConcurrentPartition();
        
        for (int i = 0; i < threads.length; i++) {
            for (int j = 0; j < hashbits.length; j++) {
                ArrayList<Long> results = new ArrayList<>();
                for (int k = 0; k < numberOfRuns; k++) {
                    results.add(ip.partition(threads[i], hashbits[j]));
                }
                //calculate the average
                long average = 0;
                for (int k = 0; k < results.size(); k++) {
                    average += results.get(k);
                }
                Triple tripl = new Triple(threads[i], hashbits[j], average);
                tupleList.add(tripl);
            }
        }
        //print out all tupleList member
        for (int i = 0; i < tupleList.size(); i++) {
            System.out.println(tupleList.get(i));
        }


        //write to output
        String fileName = "data.txt";
        try (FileWriter writer = new FileWriter(fileName, false)) {
            // for entry in TupleList
            for (int i = 0; i < tupleList.size(); i++) {
                writer.write(tupleList.get(i).toString());
                writer.append('\n');
            }
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}