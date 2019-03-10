import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class ValuesGenerator {
    public static void main(String[] args) throws IOException {
        // изначально n = 1000, далее увеличивается на 100 до 10000
        int n = 1000;
        while (n != 10100) {
            // создается файл с названием data{n}, где n - количество рёбер
            File file = new File("resources/data" + n);
            file.createNewFile();

            Random random = new Random();
            String output = "";

            // в формате ПЕРВАЯ_ВЕРШИНА ВТОРАЯ_ВЕРШИНА ВЕС создается n случайных строк
            for (int i = 0; i < n; i++) {
                // вершина от 0 до 100
                int first = random.nextInt(100);
                int second = random.nextInt(100);
                // исключаем вариант ребра 0-0, 1-1 и т.д.
                while (first == second) {
                    second = random.nextInt(100);
                }
                // вес от 1 до 101
                int third = random.nextInt(100) + 1;
                String edge = first + " " + second + " " + third;
                output = output.concat(edge + "\n");
            }
            System.out.println("Сгенерирован файл с " + n + " ребер");
            // записываем сгенерированные данные в файл data{n}
            writeCollectedData(output, "resources/data" + n, false);

            n += 100;
        }
    }

    public static void writeCollectedData(String output, String filename, boolean append) {
        FileWriter fr = null;
        try {
            fr = new FileWriter(filename, append);
            fr.write(output);
            System.out.println("Запись успешна. \n");
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                assert fr != null;
                fr.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
