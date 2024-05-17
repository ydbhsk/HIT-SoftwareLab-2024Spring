import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class DrawTest {
    public static void main(String args[]) {
        System.setProperty("org.graphstream.ui", "swing"); // Use Swing
        Graph graph = new SingleGraph("Tutorial 1");

        Node A = graph.addNode("A");
        Node B = graph.addNode("B");
        Node C = graph.addNode("C");

        graph.addEdge("AB", "A", "B", true); // Directed edge
        graph.addEdge("BC", "B", "C", true); // Directed edge

        for (Node node : graph) {
            node.setAttribute("ui.label", node.getId());
        }

        graph.display();
    }
}