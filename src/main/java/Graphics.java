import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Graphics extends JFrame {
    private static Graph graph;
    private List<Noeud> Noeuds;
    private List<Node> Nodes;

    public Graphics(Noeud... noeuds) {
        Noeuds = new ArrayList<>();
        for(int i = 0; i < noeuds.length; i++) {
            Noeuds.add(noeuds[i]);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Graphics g = new Graphics();
                g.initUI();
            }
        });
        /*System.setProperty("org.graphstream.ui", "swing");

        Graph graph = new SingleGraph("Tutorial 1");

        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");

        graph.display();*/
    }

    private void initUI() {
        System.setProperty("org.graphstream.ui", "swing");
        Graph graph = new MultiGraph("Routage");

        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");

        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");

        JFrame f = new JFrame("Routage");

        JButton add = new JButton("Ajouter");
        JButton quitter = new JButton("Quitter");

        quitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame frame = new JFrame("Ajouter");
                JButton ok = new JButton("OK");
				JButton annuler = new JButton("Annuler");
                Graphics.graph.addNode("test");
            }
        });

        ButtonGroup bGroup = new ButtonGroup();
        bGroup.add(add);
        bGroup.add(quitter);

        JPanel left = new JPanel(new BorderLayout());
        /*Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        ViewPanel viewPanel = (ViewPanel) viewer.addDefaultView(false);
        viewPanel.setPreferredSize(new Dimension(200, 200));
        left.add(viewPanel);*/

        JPanel right = new JPanel(new BorderLayout());
        right.add(add, BorderLayout.NORTH);
        right.add(quitter, BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(right, BorderLayout.WEST);
        mainPanel.add(left, BorderLayout.EAST);

        f.getContentPane().add(mainPanel);
        f.pack();
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
