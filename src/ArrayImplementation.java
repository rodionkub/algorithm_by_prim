import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ArrayImplementation {
    private static Edge[] edges = new Edge[0];
    private static Integer[] notUsedNodes = new Integer[0];
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


        // автоматика, тест
    /*
        n = 1000;
        while (n != 11000) {
            readEdges(n);

            countNodes();

            algorithm(edges);

            n += 100;
        }
    */
        // плохо работает, жаль, programming sucks

    }

    private static void algorithm(Edge[] edges) {
        Edge[] answer = new Edge[0];
        Edge[] notUsedEdges = new Edge[edges.length];
        System.arraycopy(edges, 0, notUsedEdges, 0, edges.length);
        Integer[] usedNodes = new Integer[0];

        // выбираем нулевую вершину (так как разницы нет)
        usedNodes = addInt(usedNodes, notUsedNodes[0]);
        notUsedNodes = removeInt(notUsedNodes, 0);

        // начало алгоритма, засекаем время
        long start = System.currentTimeMillis();
        int count = 0;
        while (notUsedNodes.length > 0) {
            int minEdge = -1; // номер наименьшего ребра
            // поиск наименьшего ребра
            for (int i = 0; i < notUsedEdges.length; i++) {
                count++;
                if ((indexOf(usedNodes, notUsedEdges[i].node1) != -1 && indexOf(notUsedNodes, notUsedEdges[i].node2) != -1) ||
                        (indexOf(usedNodes, notUsedEdges[i].node2) != -1 && indexOf(notUsedNodes, notUsedEdges[i].node1) != -1)) {
                    if (minEdge != -1) {
                        if (notUsedEdges[i].weight < notUsedEdges[minEdge].weight) {
                            minEdge = i;
                        }
                    } else {
                        minEdge = i;
                    }
                }
            }
            //заносим новую вершину в список использованных и удаляем ее из списка неиспользованных
            if (indexOf(usedNodes, notUsedEdges[minEdge].node1) != -1) {
                usedNodes = addInt(usedNodes, notUsedEdges[minEdge].node2);
                notUsedNodes = removeInt(notUsedNodes, notUsedEdges[minEdge].node2);
            } else {
                usedNodes = addInt(usedNodes, notUsedEdges[minEdge].node1);
                notUsedNodes = removeInt(notUsedNodes, notUsedEdges[minEdge].node1);
            }
            //заносим новое ребро в дерево и удаляем его из списка неиспользованных
            answer = addEdge(answer, notUsedEdges[minEdge]);
            notUsedEdges = removeIndex(notUsedEdges, minEdge);
        }
        // отсекаем время B-)
        long end = System.currentTimeMillis();
        System.out.println("Для " + n + " рёбер необходимо " + (end - start) + "мс и " + count + " итераций.");
        // строка для вывода информации
        String output = n + "," + (end - start) + "," + count + ",массив\n";
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
            edges = addEdge(edges, new Edge(
                    Integer.parseInt(line.split(" ")[0]),
                    Integer.parseInt(line.split(" ")[1]),
                    Integer.parseInt(line.split(" ")[2])));

        }
    }

    private static void countNodes() {
        // записываем вершины в список
        for (Edge edge : edges) {
            if (!contains(notUsedNodes, edge.node1)) {
                notUsedNodes = addInt(notUsedNodes, edge.node1);
            }
            if (!contains(notUsedNodes, edge.node2)) {
                notUsedNodes = addInt(notUsedNodes, edge.node2);
            }
        }
        for (int i = 0; i < notUsedNodes.length; i++) {
            for (int j = 0; j < notUsedNodes.length; j++) {
                if (notUsedNodes[i] > notUsedNodes[j]) {
                    int temp = notUsedNodes[i];
                    notUsedNodes[i] = notUsedNodes[j];
                    notUsedNodes[j] = temp;
                }
            }
        }
    }

    private static int indexOf(Integer[] array, Integer o) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    private static Edge[] addEdge(Edge[] array, Edge element) {
        Edge[] temp = new Edge[array.length + 1];
        int i;
        for (i = 0; i < array.length; i++) {
            temp[i] = array[i];
        }
        temp[i] = element;
        return temp;
    }

    private static Integer[] addInt(Integer[] array, Integer element) {
        Integer[] temp = new Integer[array.length + 1];
        int i;
        for (i = 0; i < array.length; i++) {
            temp[i] = array[i];
        }
        temp[i] = element;
        return temp;
    }

    private static Integer[] removeInt(Integer[] array, Integer o) { // works
        Integer[] res = new Integer[array.length - 1];
        int j = 0;
        boolean flag = false;
        for (Integer t : array) {
            if (!flag) {
                if (!t.equals(o) && j < res.length) {
                    res[j] = t;
                    j++;
                } else {
                    flag = true;
                }
            }
            else {
                res[j] = t;
                j++;
            }
        }
        return res;
    }

    private static Edge[] removeIndex(Edge[] array, int index) {
        Edge[] temp = new Edge[array.length - 1];
        Edge element = null;
        int j = 0;
        for (int i = 0; i < array.length; i++) {
            if (i != index) {
                temp[j] = array[i];
                j++;
            }
            else {
                element = array[i];
            }
        }
        return temp;
    }

    private static boolean contains(Object[] array, Object o) {
        for (Object o1 : array) {
            if (o1.equals(o)) {
                return true;
            }
        }
        return false;
    }
}
