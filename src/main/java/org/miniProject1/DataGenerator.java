package org.miniProject1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class DataGenerator {
    HashMap<Long,Long> data;
    public DataGenerator(int size) {
        this.data = new HashMap<>();
        for (int i = 0; i < size; i++) {
            long temp_numb = Math.abs(new Random().nextLong());
            UUID uuid = UUID.randomUUID();
            data.put(Math.abs(uuid.getMostSignificantBits() ^ uuid.getLeastSignificantBits()), temp_numb);
        }
    }

    public void writeDataToFile() {
        String fileName = "data.txt";
        try (FileWriter writer = new FileWriter(fileName, false)) {
            for (Map.Entry<Long, Long> entry : this.data.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
