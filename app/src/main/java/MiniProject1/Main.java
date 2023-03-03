package MiniProject1;

public class Main {
    public static void main(String[] args) {
        // Create data
        DataGenerator data = new DataGenerator(650);

        // Write data to file
        data.writeDataToFile();
    }
}