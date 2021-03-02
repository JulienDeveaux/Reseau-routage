import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Graph;

import javax.swing.*;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Table {
    private static List<String> Liste;
    private static List<String> Liens;

    public Table(Graph g, List<String> liste, List<String> liens) {
        Liens = liens;
        Liste = liste;
        initTable(g);
    }

    private static void initTable(Graph g) {
        JFrame frame = new JFrame("Table de routage");
        JLabel LabelComm1 = new JLabel("Commutateur 1 : ");
        JLabel LabelComm2 = new JLabel("Commutateur 2 : ");
        JComboBox comm1 = new JComboBox();
        JComboBox comm2 = new JComboBox();
        JLabel res = new JLabel("                          ");
        JLabel resultat = new JLabel("Résultat : ");
        JButton court = new JButton("Plus court chemin");
        JButton table = new JButton("Table de routage commutateur de gauche");
        JButton quitter = new JButton("Quitter");

        for(int i = 0; i < Liste.size(); i++) {
            comm1.addItem(Liste.get(i));
            comm2.addItem(Liste.get(i));
        }

        quitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                frame.setVisible(false);
                frame.dispose();
            }
        });
        court.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "p");
                dijkstra.init(g);
                dijkstra.setSource(g.getNode(comm1.getSelectedIndex()));
                dijkstra.compute();
                String ch = String.valueOf(dijkstra.getPath(g.getNode(comm2.getSelectedIndex())));
                if(ch == "[]") {
                    res.setText("Aucune connexion entre les commutateurs");
                } else {
                    res.setText(ch);
                }
                dijkstra.clear();
            }
        });
        table.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame frame = new JFrame("Table de Routage de " + comm1.getSelectedItem());

                JTable tableau = new JTable(new TableObject((String) comm1.getSelectedItem(), g, Liens, Liste));
                TableColumnModel model = tableau.getColumnModel();
                model.getColumn(0).setPreferredWidth(100);
                model.getColumn(1).setPreferredWidth(450);

                frame.getContentPane().add(tableau.getTableHeader(), BorderLayout.NORTH);
                frame.getContentPane().add(tableau, BorderLayout.CENTER);
                frame.pack();
                frame.setVisible(true);
            }
        });

        ButtonGroup bGroup = new ButtonGroup();
        bGroup.add(court);
        bGroup.add(table);

        JPanel up = new JPanel(new BorderLayout());
        JPanel upIN = new JPanel(new BorderLayout());
        JPanel upING = new JPanel(new BorderLayout());
        upING.add(LabelComm1, BorderLayout.WEST);
        upING.add(comm1, BorderLayout.EAST);
        JPanel upIND = new JPanel(new BorderLayout());
        upIND.add(LabelComm2, BorderLayout.WEST);
        upIND.add(comm2, BorderLayout.EAST);
        upIN.add(upIND, BorderLayout.EAST);
        upIN.add(upING, BorderLayout.WEST);
        up.add(upIN, BorderLayout.NORTH);
        up.add(resultat, BorderLayout.WEST);
        up.add(res, BorderLayout.EAST);

        JPanel down = new JPanel(new BorderLayout());
        down.add(table, BorderLayout.WEST);
        down.add(court, BorderLayout.EAST);
        down.add(quitter, BorderLayout.CENTER);

        frame.add(up, BorderLayout.NORTH);
        frame.add(down, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }
}
