package MiniProject1;

import java.util.ArrayList;

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
        ArrayList<Triple> tupleList = new ArrayList<>();


        
        for (int i = 0; i < threads.length; i++) { //for each number of threads
            for (int j = 0; j < hashbits.length; j++) { //for each number of hashbits
                //System.out.println("Creating hash table with " + threads[i] + " threads and " + hashbits[j] + " hashbits");
                Triple tripl = new Triple(threads[i], hashbits[j], 3000000000L);
                tupleList.add(tripl);
            }
        }
        //print out all tupleList member
        for (int i = 0; i < tupleList.size(); i++) {
            System.out.println(tupleList.get(i));
        }
        // Write data to file
        //data.writeDataToFile();
    }
}