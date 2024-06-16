import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.apache.commons.io.FilenameUtils;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;


/**
 * TextGraph 类
 * 用于处理文本图.
 * 包含以下功能：
 * 1. 从文件中读取图
 * 2. 绘制图
 * 3. 查询桥接词
 * 4. 生成新文本
 * 5. 计算最短路径
 * 6. 随机游走
 */
public class TextGraph {
  private final SecureRandom random;
  private String text; // 处理后文本
  private final Set<String> vertices; // 顶点集合
  // 有向图
  private int vertexCount; // 顶点数
  private int edgeCount; // 边数
  private int[][] adj; // 邻接矩阵
  private int picNum; // 图片编号

  /**
   * 构造函数
   * 初始化顶点集合、顶点数、边数、图片编号.
   */
  public TextGraph() {
    random = new SecureRandom();
    vertices = new HashSet<>();
    vertexCount = 0;
    edgeCount = 0;
    picNum = 0;
  }

  /**
   * 主函数
   * 用于测试 TextGraph 类.
   *
   * @param args 命令行参数,根据提示信息输入.
   * @throws IOException 文件读写异常.
   */
  public static void main(String[] args) throws IOException {
    System.out.println("Hello and welcome!");
    TextGraph textGraph = new TextGraph();
    textGraph.inputGraph("src/main/java/input.txt");
    //    textGraph.inputGraph(args[0]);
    textGraph.showDirectedGraph(null);
    Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    while (true) {
      System.out.println("Please input the function you want to use:");
      System.out.println("1. Query bridge words");
      System.out.println("2. Generate new text");
      System.out.println("3. Calculate the shortest path");
      System.out.println("4. Random walk");
      System.out.println("5. Exit");
      String function = scanner.next();
      switch (function) {
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
          String word3 = scanner.next();
          System.out.println("Please input the word2:");
          String word4 = scanner.next();
          textGraph.calcShortestPath(word3, word4);
          break;
        case "4":
          String randomWalk = textGraph.randomWalk();
          textGraph.outPutFile(randomWalk);
          break;
        case "5":
          System.out.println("Goodbye!");
          return;
        default:
          System.out.println("Invalid input! Please try again.");
      }
    }
  }

  /**
   * 从文件中读取图.
   *
   * @param filename 文件名.
   *                 文件格式为一行文本，包含多个单词，单词之间用空格分隔.
   */
  public void inputGraph(String filename) {
    // 从文件中读取图
    // ...
    try {
      File file = new File(FilenameUtils.getPath(filename) + FilenameUtils.getName(filename));
      List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
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
    vertexCount = vertices.size(); // 顶点数
    adj = new int[  vertexCount][  vertexCount]; // 初始化邻接矩阵
    for (int i = 0; i <   vertexCount; i++) {
      for (int j = 0; j <   vertexCount; j++) {
        adj[i][j] = 0; // 初始化邻接矩阵
      }
    }
    String[] allWords = text.split("\\s+"); // 使用空格分割处理后字符串
    for (int i = 0; i < allWords.length - 1; i++) {
      int v =  getIndexOfV(allWords[i]); // 获取顶点索引
      int w =  getIndexOfV(allWords[i + 1]); // 获取顶点索引
      adj[v][w]++; // 更新邻接矩阵
      edgeCount++; // 更新边数
    }
  }

  /**
   * 获取顶点的索引.
   *
   * @param word 单词.
   * @return 单词在顶点数组中对应的索引.
   */
  private int getIndexOfV(String word) {
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

  /**
   * 绘制图.
   *
   * @param especial 特殊边,用于突出显示.
   *                 二维数组,1表示特殊边,0表示非特殊边.
   */
  public void showDirectedGraph(int [][]especial) {
    // 绘制图
    // ...
    System.setProperty("org.graphstream.ui", "swing"); // 使用 Swing
    Graph graph = new SingleGraph("TextGraph");
    for (String vertex : vertices) {
      Node node = graph.addNode(vertex);
      node.setAttribute("ui.label", vertex);
      node.setAttribute("ui.style", "text-size:20px;");
    }
    for (int i = 0; i <   vertexCount; i++) {
      for (int j = 0; j <   vertexCount; j++) {
        if (adj[i][j] > 0) {
          Edge edge = graph.addEdge(i + "-" + j, vertices.toArray()[i].toString(),
                  vertices.toArray()[j].toString(), true);
          edge.setAttribute("ui.label", String.valueOf(adj[i][j]));
          edge.setAttribute("ui.style", "text-size:20px;");
          if (especial != null && especial[i][j] == 1) {
            edge.setAttribute("ui.style", "fill-color: red;");
          }
        }
      }
    }
    graph.display();
    graph.addAttribute("ui.stylesheet", "node {size: 20px; fill-color: blue; text-size: 20px;}"
            +
            "edge {fill-color: black; text-size: 20px;}");
    graph.addAttribute("ui.quality");
    graph.addAttribute("ui.antialias");
    // 保存图片文件
    graph.addAttribute("ui.screenshot", "src/main/pic/pic" + picNum++ + ".png");
  }

  /**
   * 查询桥接词.
   *
   * @param word1    单词1.
   * @param word2    单词2.
   * @param isPrint  是否输出到屏幕.
   * @return 桥接词.
   */
  public String queryBridgeWords(String word1, String word2, boolean isPrint) {
    // 查询桥接词
    // ...
    int v1 = getIndexOfV(word1); // 获取顶点索引
    int v2 = getIndexOfV(word2); // 获取顶点索引
    if (v1 == -1) {
      if (isPrint) {
        System.out.println("No \"" + word1 + "\" in the graph!");
      }
      return null;
    }
    if (v2 == -1) {
      if (isPrint) {
        System.out.println("No \"" + word2 + "\" in the graph!");
      }
      return null;
    }
    StringBuilder bridgeWords = new StringBuilder();
    for (int i = 0; i <   vertexCount; i++) {
      if (adj[v1][i] > 0 && adj[i][v2] > 0) {
        bridgeWords.append(vertices.toArray()[i]).append(" ");
      }
    }
    if (bridgeWords.isEmpty()) {
      if (isPrint) {
        System.out.println("No bridge words from \"" + word1 + "\" to \"" + word2 + "\"!");
      }
      return null;
    }
    if (isPrint) {
      System.out.println("The bridge words from \"" + word1
              +
              "\" to \"" + word2 + "\" are: " + bridgeWords);
    }
    return bridgeWords.toString();
  }

  /**
   * 生成新文本.
   *
   * @param inputText 用户输入的文本.
   * @param isPrint   是否输出到屏幕.
   * @return 新文本.
   */
  public String generateNewText(String inputText, boolean isPrint) {
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
      if (bridgeWords != null) {
        String []bridgeWordsArray = bridgeWords.split("\\s+");
        if (bridgeWordsArray.length > 0) {
          int randomIndex = random.nextInt(bridgeWordsArray.length);
          newText.append(bridgeWordsArray[randomIndex]).append(" ");
        }
      }
    }
    newText.append(inputWords[inputWords.length - 1]);
    if (isPrint) {
      System.out.println("The new text is: " + newText);
    }
    return newText.toString();
  }

  /**
   * 计算最短路径.
   *
   * @param word1 单词1.
   * @param word2 单词2.
   * @return 最短路径.
   */
  public String calcShortestPath(String word1, String word2) {
    // 计算最短路径
    // 不使用 SimpleGraph 库
    // Dijsktra 算法
    // ...
    // 如果word2不为null
    word1 = word1.replaceAll("[^a-zA-Z ]", "");  // 将非字母字符替换
    word1 = word1.toLowerCase(); // 将大写替换为小写
    word2 = word2.replaceAll("[^a-zA-Z ]", "");  // 将非字母字符替换
    word2 = word2.toLowerCase(); // 将大写替换为小写
    if (word1.isEmpty()) {
      // 非法word1
      System.out.println("Invalid word1!");
      return null;
    }
    if (!word2.isEmpty()) {
      int v1;
      int v2;
      v1 =  getIndexOfV(word1); // 获取顶点索引
      v2 =  getIndexOfV(word2); // 获取顶点索引
      if (v1 == -1) {
        System.out.println("No \"" + word1 + "\" in the graph!");
        return null;
      }
      if (v2 == -1) {
        System.out.println("No \"" + word2 + "\" in the graph!");
        return null;
      }
      int[] dist = new int[  vertexCount];
      boolean[] visited = new boolean[  vertexCount];
      int[] path = new int[  vertexCount];
      for (int i = 0; i <   vertexCount; i++) {
        dist[i] = Integer.MAX_VALUE;
        visited[i] = false;
        path[i] = -1;
      }
      dist[v1] = 0;
      for (int i = 0; i <   vertexCount; i++) {
        int minDist = Integer.MAX_VALUE;
        int u = -1;
        for (int j = 0; j <   vertexCount; j++) {
          if (!visited[j] && dist[j] < minDist) {
            minDist = dist[j];
            u = j;
          }
        }
        if (u == -1) {
          break;
        }
        visited[u] = true;
        for (int j = 0; j <   vertexCount; j++) {
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
      int distance = dist[v2];
      while (v != v1) {
        shortestPath.insert(0, "->" + vertices.toArray()[v]);
        v = path[v];
      }
      shortestPath.insert(0, vertices.toArray()[v1]);
      System.out.println("The shortest path from \"" + word1 + "\" to \"" + word2 + "\" "
              +
              "is: " + shortestPath + ", with a distance of " + distance + ".");
      int [][]especial = new int[  vertexCount][  vertexCount];
      for (int i = 0; i <   vertexCount; i++) {
        for (int j = 0; j <   vertexCount; j++) {
          especial[i][j] = 0;
        }
      }
      v = v2;
      while (v != v1) {
        especial[path[v]][v] = 1;
        v = path[v];
      }
      especial[v1][v2] = 1;
      showDirectedGraph(especial);
      return shortestPath.toString();
    } else {
      // 计算出word1到图中其他任一单词的最短路径，并逐项展示出来
      System.out.println(
              "==================================================================================\n"
              +
              "The shortest path from \"" + word1 + "\" to other words are:\n");
      for (String vertex : vertices) {
        if (!vertex.equals(word1)) {
          calcShortestPath(word1, vertex);
        }
      }
      System.out.println(
              "==================================================================================");
      return null;
    }
  }

  /**
   * 随机游走.
   *
   * @return 随机游走的路径.
   * @throws IOException 文件读写异常.
   */
  public String randomWalk() throws IOException {
    // 随机游走
    // 从图中选择一个节点，以此为起点沿出边进行随机遍历,记录经过的所有节点和边
    // 直到出现第一条重复的边为止，或者进入的某个节点不存在出边为止
    // 在遍历过程中，用户也可随时停止遍历
    int v = random.nextInt(vertexCount);
    StringBuilder randomWalk = new StringBuilder();
    randomWalk.append(vertices.toArray()[v]).append(" ");
    boolean[][] visited = new boolean[  vertexCount][  vertexCount];
    for (int i = 0; i <   vertexCount; i++) {
      for (int j = 0; j <   vertexCount; j++) {
        visited[i][j] = false;
      }
    }
    System.out.println(
            "==================================================================================");
    System.out.println("Press any key to stop.");
    // 键盘读入任意字符时停止
    while (System.in.available() == 0) {
      int u = -1;
      for (int i = 0; i <   vertexCount; i++) {
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
    System.out.println(
            "==================================================================================");
    return randomWalk.toString();
  }

  /**
   * 输出到文件.
   *
   * @param output 输出内容.
   */
  private void outPutFile(String output) {
    // 输出到文件
    // ...
    try {
      // 文件名
      String filename = "src/main/java/output.txt";
      Files.write(Paths.get(filename), output.getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      System.out.println("An error occurred while writing the file.");
      e.printStackTrace();
    }
  }
}
