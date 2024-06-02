import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class TextGraph {
    private String text; // 处理后文本
    private Set<String> vertices; // 顶点集合
    // 有向图
    private int V; // 顶点数
    private int E; // 边数
    private int[][] adj; // 邻接矩阵
    private String filename = "output.txt"; // 文件名

    public TextGraph() {
        // 构造函数
        // ...
        vertices = new HashSet<>();
        V = 0;
        E = 0;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Hello and welcome!");
        TextGraph textGraph = new TextGraph();
        textGraph.inputGraph(args[0]);
        textGraph.showDirectedGraph();
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("Please input the function you want to use:");
            System.out.println("1. Query bridge words");
            System.out.println("2. Generate new text");
            System.out.println("3. Calculate the shortest path");
            System.out.println("4. Random walk");
            System.out.println("5. Exit");
            String function = scanner.next();
            switch (function){
                case "1":
                    System.out.println("Please input two words:");
                    String word1 = scanner.next();
                    String word2 = scanner.next();
                    textGraph.queryBridgeWords(word1, word2, true);
                    break;
                case "2":
                    System.out.println("Please input the text:");
                    scanner.nextLine(); // 消除换行符
                    String inputText = scanner.nextLine();
                    textGraph.generateNewText(inputText, true);
                    break;
                case "3":
                    System.out.println("Please input the word1:");
                    String word1_ = scanner.next();
                    System.out.println("Please input the word2:");
                    String word2_ = scanner.next();
                    textGraph.calcShortestPath(word1_, word2_);
                    break;
                case "4":
                    textGraph.randomWalk();
                    break;
                case "5":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid input! Please try again.");
            }
        }
    }
    public void inputGraph(String filename) {
        // 从文件中读取图
        // ...
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            for (String line : lines) {
                line = line.replaceAll("[^a-zA-Z ]", " ");  // 将非字母字符替换为空格
                line = line.toLowerCase(); // 将大写替换为小写
                String[] words = line.split("\\s+");  // 使用空格分割字符串
                for (String word : words) { // 遍历每个单词
                    if (!word.isEmpty()) {
                        if (text == null) {
                            text = word; // 初始化文本
                        } else {
                            text = text + " " + word; // 构建符合格式文本
                        }
                        vertices.add(word); // 构建顶点集合
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
            }
        V = vertices.size(); // 顶点数
        adj = new int[V][V]; // 初始化邻接矩阵
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                adj[i][j] = 0; // 初始化邻接矩阵
            }
        }
        String[] allWords = text.split("\\s+"); // 使用空格分割处理后字符串
        for (int i = 0; i < allWords.length - 1; i++) {
            int v = getVIndex(allWords[i]); // 获取顶点索引
            int w = getVIndex(allWords[i + 1]); // 获取顶点索引
            adj[v][w]++; // 更新邻接矩阵
            E++; // 更新边数
        }
    }

    private int getVIndex(String word) {
        // 获取顶点的索引
        // ...
        int index = 0;
        for (String vertex : vertices) {
            if (vertex.equals(word)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public void showDirectedGraph() {
        // 绘制图
        // ...
        System.setProperty("org.graphstream.ui", "swing"); // 使用 Swing
        Graph graph = new SingleGraph("TextGraph");
        for (String vertex : vertices) {
            Node node = graph.addNode(vertex);
            node.setAttribute("ui.label", vertex);
            node.setAttribute("ui.style", "text-size:20px;");
        }
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (adj[i][j] > 0) {
                    Edge edge = graph.addEdge(i + "-" + j, vertices.toArray()[i].toString(), vertices.toArray()[j].toString(), true);
                    edge.setAttribute("ui.label", String.valueOf(adj[i][j]));
                    edge.setAttribute("ui.style", "text-size:20px;");
                }
            }
        }
        graph.display();
    }

    public String queryBridgeWords(String word1, String word2, boolean isPrint){
        // 查询桥接词
        // ...
        int v1,v2 = -1;
        v1 = getVIndex(word1); // 获取顶点索引
        v2 = getVIndex(word2); // 获取顶点索引
        if (v1 == -1) {
            if(isPrint){
                System.out.println("No \"" + word1 + "\" in the graph!");
            }
            return null;
        }
        if (v2 == -1) {
            if(isPrint){
                System.out.println("No \"" + word2 + "\" in the graph!");
            }
            return null;
        }
        StringBuilder bridgeWords = new StringBuilder();
        for (int i = 0; i < V; i++) {
            if (adj[v1][i] > 0 && adj[i][v2] > 0) {
                bridgeWords.append(vertices.toArray()[i]).append(" ");
            }
        }
        if (bridgeWords.isEmpty()) {
            if(isPrint){
                System.out.println("No bridge words from \"" + word1 + "\" to \"" + word2 + "\"!");
            }
            return null;
        }
        if(isPrint){
            System.out.println("The bridge words from \"" + word1 + "\" to \"" + word2 + "\" are: " + bridgeWords);
        }
        return bridgeWords.toString();
    }

    public String generateNewText(String inputText, boolean isPrint){
        // 生成新文本
        // 用户输入一行新文本，程序根据之前输入文件生成的图，计算该新文本中两两相邻的单词的bridge word
        // 将bridge word插入新文本的两个单词之间，输出到屏幕上展示
        // 如果两个单词无bridge word，则保持不变，不插入任何单词；
        // 如果两个单词之间存在多个bridge words，则随机从中选择一个插入进去形成新文本
        // ...
        inputText = inputText.replaceAll("[^a-zA-Z ]", " ");  // 将非字母字符替换为空格
        inputText = inputText.toLowerCase(); // 将大写替换为小写
        String[] inputWords = inputText.split("\\s+");  // 使用空格分割字符串
        StringBuilder newText = new StringBuilder();
        for (int i = 0; i < inputWords.length - 1; i++) {
            newText.append(inputWords[i]).append(" ");
            String bridgeWords = queryBridgeWords(inputWords[i], inputWords[i + 1], false);
            if (bridgeWords != null){
                String []bridgeWordsArray = bridgeWords.split("\\s+");
                if (bridgeWordsArray.length > 0) {
                    int randomIndex = (int) (Math.random() * bridgeWordsArray.length);
                    newText.append(bridgeWordsArray[randomIndex]).append(" ");
                }
            }
        }
        newText.append(inputWords[inputWords.length - 1]);
        if(isPrint){
            System.out.println("The new text is: " + newText);
        }
        return newText.toString();
    }

    public String calcShortestPath(String word1, String word2){
        // 计算最短路径
        // 不使用 SimpleGraph 库
        // Dijsktra 算法
        // ...
        // 如果word2不为null
        word1 = word1.replaceAll("[^a-zA-Z ]", "");  // 将非字母字符替换
        word1 = word1.toLowerCase(); // 将大写替换为小写
        word2 = word2.replaceAll("[^a-zA-Z ]", "");  // 将非字母字符替换
        word2 = word2.toLowerCase(); // 将大写替换为小写
        if(word1.isEmpty()){
            // 非法word1
            System.out.println("Invalid word1!");
            return null;
        }
        if(!word2.isEmpty()){
            int v1,v2;
            v1 = getVIndex(word1); // 获取顶点索引
            v2 = getVIndex(word2); // 获取顶点索引
            if (v1 == -1) {
                System.out.println("No \"" + word1 + "\" in the graph!");
                return null;
            }
            if (v2 == -1) {
                System.out.println("No \"" + word2 + "\" in the graph!");
                return null;
            }
            int[] dist = new int[V];
            boolean[] visited = new boolean[V];
            int[] path = new int[V];
            for (int i = 0; i < V; i++) {
                dist[i] = Integer.MAX_VALUE;
                visited[i] = false;
                path[i] = -1;
            }
            dist[v1] = 0;
            for (int i = 0; i < V; i++) {
                int minDist = Integer.MAX_VALUE;
                int u = -1;
                for (int j = 0; j < V; j++) {
                    if (!visited[j] && dist[j] < minDist) {
                        minDist = dist[j];
                        u = j;
                    }
                }
                if (u == -1) {
                    break;
                }
                visited[u] = true;
                for (int j = 0; j < V; j++) {
                    if (!visited[j] && adj[u][j] > 0 && dist[u] + adj[u][j] < dist[j]) {
                        dist[j] = dist[u] + adj[u][j];
                        path[j] = u;
                    }
                }
            }
            if (dist[v2] == Integer.MAX_VALUE) {
                System.out.println("No path from \"" + word1 + "\" to \"" + word2 + "\"!");
                return null;
            }
            StringBuilder shortestPath = new StringBuilder();
            int v = v2;
            while (v != v1) {
                shortestPath.insert(0, "->" + vertices.toArray()[v]);
                v = path[v];
            }
            shortestPath.insert(0, vertices.toArray()[v1]);
            System.out.println("The shortest path from \"" + word1 + "\" to \"" + word2 + "\" is: " + shortestPath);
            return shortestPath.toString();
        }
        else {
            // 计算出word1到图中其他任一单词的最短路径，并逐项展示出来
            System.out.println("==================================================================================\n" +
                    "The shortest path from \"" + word1 + "\" to other words are:\n");
            for (String vertex : vertices) {
                if (!vertex.equals(word1)) {
                    calcShortestPath(word1, vertex);
                }
            }
            System.out.println("==================================================================================");
            return null;
        }
    }

    public String randomWalk() throws IOException {
        // 随机游走
        // 从图中选择一个节点，以此为起点沿出边进行随机遍历,记录经过的所有节点和边
        // 直到出现第一条重复的边为止，或者进入的某个节点不存在出边为止
        // 在遍历过程中，用户也可随时停止遍历
        // ...
        int v = (int) (Math.random() * V);
        StringBuilder randomWalk = new StringBuilder();
        randomWalk.append(vertices.toArray()[v]).append(" ");
        boolean[][] visited = new boolean[V][V];
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                visited[i][j] = false;
            }
        }
        System.out.println("==================================================================================");
        System.out.println("Press any key to stop.");
        // 键盘读入任意字符时停止
        while (System.in.available() == 0){
            int u = -1;
            for (int i = 0; i < V; i++) {
                if (adj[v][i] > 0 && !visited[v][i]) {
                    u = i;
                    break;
                }
            }
            if (u == -1) {
                break;
            }
            visited[v][u] = true;
            randomWalk.append(vertices.toArray()[u]).append(" ");
            v = u;
            System.out.print("\rThe random walk is: \t" + randomWalk);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\nDone!\nThe final random walk is: \t" + randomWalk);
        System.out.println("==================================================================================");
        return randomWalk.toString();
    }

    private void outPutFile(String output){
        // 输出到文件
        // ...
        try {
            Files.write(Paths.get(filename), output.getBytes());
        } catch (IOException e) {
            System.out.println("An error occurred while writing the file.");
            e.printStackTrace();
        }
    }
}
