package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.random;

public class Main {


    public static void main(String[] args) {
        HashMap<Long,Long> data = dataGenerator(6500000);
        //System.out.println(data);
        writeDataToFile(data);
    }
    public static HashMap<Long,Long> dataGenerator(int size) {
        HashMap<Long, Long> data = new HashMap<>();
        for (int i = 0; i < size; i++) {
            long temp_numb = Math.abs(new Random().nextLong());
            UUID uuid = UUID.randomUUID();
//            return ;
            data.put(Math.abs(uuid.getMostSignificantBits() ^ uuid.getLeastSignificantBits()), temp_numb);
        }
        return data;
    }

    public static void writeDataToFile(HashMap<Long,Long> data) {
        String fileName = "data4.txt";
        try (FileWriter writer = new FileWriter(fileName, false)) {
            for (Map.Entry<Long, Long> entry : data.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}