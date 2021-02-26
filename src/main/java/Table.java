import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Graph;

import javax.swing.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Table {
    public Table(Graph g) {
        initTable(g);
    }

    private static void initTable(Graph g) {
        JFrame frame = new JFrame("Table de routage");

        Dijkstra dij = new Dijkstra(Dijkstra.Element.EDGE, null, "p");
        dij.init(g);
        dij.setSource(g.getNode("Noeud1"));
        dij.compute();

        // Print the shortest path from 1 to 2
        System.out.println(dij.getPath(g.getNode("Noeud2")));
        System.out.println(dij.getPath(g.getNode("Noeud3")));
        System.out.println(dij.getPath(g.getNode("Noeud4")));
        System.out.println(dij.getPath(g.getNode("Noeud5")));
        System.out.println(dij.getPath(g.getNode("Noeud6")));

        frame.pack();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
