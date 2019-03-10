import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class LinkedListImplementation {
    private static List<Edge> edges = new LinkedList<>();
    private static List<Integer> notUsedNodes = new LinkedList<>();
    private static int n;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Введите количество рёбер для проверки: ");
        n = in.nextInt();
        // считываем ребра из файла
        readEdges(n);

        // считываем вершины
        countNodes();

        // выполняем алгоритм
        algorithm(edges);

        /*
        // автоматика, тест.

        n = 1000;
        while (n != 11000) {
            readEdges(n);

            countNodes();

            algorithm(edges);

            n += 100;
        }

        // плохо работает, жаль, programming sucks
        */
    }

    private static void algorithm(List<Edge> edges) {
        List<Edge> answer = new LinkedList<>();
        List<Edge> notUsedEdges = new LinkedList<>(edges);
        List<Integer> usedNodes = new LinkedList<>();

        // выбираем нулевую вершину (так как разницы нет)
        usedNodes.add(notUsedNodes.get(0));
        notUsedNodes.remove(0);

        // начало алгоритма, засекаем время
        long start = System.currentTimeMillis();
        int count = 0;
        while (notUsedNodes.size() > 0) {
            int minEdge = -1; // номер наименьшего ребра
            // поиск наименьшего ребра
            for (int i = 0; i < notUsedEdges.size(); i++) {
                count++;
                if ((usedNodes.indexOf(notUsedEdges.get(i).node1) != -1 && notUsedNodes.indexOf(notUsedEdges.get(i).node2) != -1) ||
                        (usedNodes.indexOf(notUsedEdges.get(i).node2) != -1 && notUsedNodes.indexOf(notUsedEdges.get(i).node1) != -1)) {
                    if (minEdge != -1) {
                        if (notUsedEdges.get(i).weight < notUsedEdges.get(minEdge).weight) {
                            minEdge = i;
                        }
                    } else {
                        minEdge = i;
                    }
                }
            }
            //заносим новую вершину в список использованных и удаляем ее из списка неиспользованных
            if (usedNodes.indexOf(notUsedEdges.get(minEdge).node1) != -1) {
                usedNodes.add(notUsedEdges.get(minEdge).node2);
                notUsedNodes.remove(new Integer(notUsedEdges.get(minEdge).node2));
            } else {
                usedNodes.add(notUsedEdges.get(minEdge).node1);
                notUsedNodes.remove(new Integer(notUsedEdges.get(minEdge).node1));
            }
            //заносим новое ребро в дерево и удаляем его из списка неиспользованных
            answer.add(notUsedEdges.get(minEdge));
            notUsedEdges.remove(minEdge);
        }
        // отсекаем время B-)
        long end = System.currentTimeMillis();
        System.out.println("Для " + n + " рёбер необходимо " + (end - start) + "мс и " + count + " итераций.");
        // строка для вывода информации
        String output = n + "," + (end - start) + "," + count + ",связный список\n";
        // вывод полученной информации в файл
        ValuesGenerator.writeCollectedData(output, "results", true);
    }

    private static void readEdges(int n) {
        Scanner in = null;
        try {
            in = new Scanner(new File("resources/data" + n));
        } catch (FileNotFoundException e) {
            System.out.println("Ты ошибся цифрой, малый.");
            System.exit(0);
        }
        while (in.hasNextLine()) {
            String line = in.nextLine();
            edges.add(new Edge(
                    Integer.parseInt(line.split(" ")[0]),
                    Integer.parseInt(line.split(" ")[1]),
                    Integer.parseInt(line.split(" ")[2])));

        }
    }

    private static void countNodes() {
        // записываем вершины в список
        for (Edge edge : edges) {
            if (!notUsedNodes.contains(edge.node1)) {
                notUsedNodes.add(edge.node1);
            }
            if (!notUsedNodes.contains(edge.node2)) {
                notUsedNodes.add(edge.node2);
            }
        }
        Collections.sort(notUsedNodes);
    }
}
