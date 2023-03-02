package org.miniProject1;

public class Main {
    public static void main(String[] args) {
        // Create data
        DataGenerator data = new DataGenerator(6500000);

        // Write data to file
        data.writeDataToFile();
    }
}