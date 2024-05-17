import java.nio.file.Files;
import java.nio.file.Paths;
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

    public TextGraph() {
        // 构造函数
        // ...
        vertices = new HashSet<>();
        V = 0;
        E = 0;
    }

    public static void main(String[] args) {
        System.out.println("Hello and welcome!");
        TextGraph textGraph = new TextGraph();
        textGraph.inputGraph("src/main/java/input.txt");
        System.out.println(textGraph.queryBridgeWords("to", "and"));
        textGraph.drawGraph();
    }
    public void inputGraph(String filename) {
        // 从文件中读取图
        // ...
//        System.out.println(Files.exists(Paths.get(filename)));
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
//        System.out.println(text);
        String[] allWords = text.split("\\s+"); // 使用空格分割处理后字符串
        for (int i = 0; i < allWords.length - 1; i++) {
            int v = getVIndex(allWords[i]); // 获取顶点索引
            int w = getVIndex(allWords[i + 1]); // 获取顶点索引
            adj[v][w]++; // 更新邻接矩阵
            E++; // 更新边数
        }
//        // 打印邻接矩阵
//        for (int i = 0; i < V; i++) {
////            System.out.print(vertices.toArray()[i] + ": \t\t");
//            for (int j = 0; j < V; j++) {
//                System.out.print(adj[i][j] + " ");
//            }
//            System.out.println();
//        }
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
        return 0;
    }

    public void drawGraph() {
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

    public String queryBridgeWords(String word1, String word2){
        // 查询桥接词
        // ...
        int v1,v2 = -1;
        v1 = getVIndex(word1); // 获取顶点索引
        v2 = getVIndex(word2); // 获取顶点索引
        if (v1 == -1) {
            System.out.println("No \""+word1+"\" in the graph!");
            return null;
        }
        if (v2 == -1) {
            System.out.println("No \""+word2+"\" in the graph!");
            return null;
        }
        StringBuilder bridgeWords = new StringBuilder();
        for (int i = 0; i < V; i++) {
            if (adj[v1][i] > 0 && adj[i][v2] > 0) {
                bridgeWords.append(vertices.toArray()[i]).append(" ");
            }
        }
        if (bridgeWords.length() == 0) {
            System.out.println("No bridge words from \""+word1+"\" to \""+word2+"\"!");
            return null;
        }
        System.out.println("The bridge words from \""+word1+"\" to \""+word2+"\" is: "+bridgeWords);
        return bridgeWords.toString();
    }
}
