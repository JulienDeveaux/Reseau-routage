import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Graphics extends JFrame {
    protected String styleSheet =
        "node {" +
        "	fill-color: grey, red;" +
        "   text-background-mode: rounded-box;" +
        "   text-background-color: orange;" +
        "   text-alignment: above;" +
        "   fill-mode: gradient-radial;" +
        "}" ;
    private List<String> Liste;
    private List<String> Liens;

    public Graphics() {
        Liste = new ArrayList<>();
        Liens = new ArrayList<>();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Graphics g = new Graphics();
            g.initUI();
        });
    }

    private void initUI() {
        System.setProperty("org.graphstream.ui", "swing");
        Graph graph = new MultiGraph("Routage");
        graph.setAttribute("ui.stylesheet", styleSheet);

        graph.addNode("Noeud1");
        Node node = graph.getNode("Noeud1");
        node.setAttribute("ui.label", "Noeud1");
        graph.addNode("Noeud2");
        node = graph.getNode("Noeud2");
        node.setAttribute("ui.label", "Noeud2");
        graph.addNode("Noeud3");
        node = graph.getNode("Noeud3");
        node.setAttribute("ui.label", "Noeud3");
        Liste.add("Noeud1");
        Liste.add("Noeud2");
        Liste.add("Noeud3");
        Liens.add("Noeud1Noeud2");
        Liens.add("Noeud2Noeud3");
        Liens.add("Noeud3Noeud1");

        graph.addEdge("Noeud1Noeud2", "Noeud1", "Noeud2");
        graph.addEdge("Noeud2C", "Noeud2", "Noeud3");
        graph.addEdge("Noeud3Noeud1", "Noeud3", "Noeud1");

        graph.display();

        JFrame f = new JFrame("Controle");

        JButton add = new JButton("Ajouter Noeud");
        JButton addLien = new JButton("Ajouter Lien");
        JButton supprimerNoeud = new JButton("Supprimer Noeud");
        JButton supprimerLien = new JButton("Supprimer Lien");
        JButton quitter = new JButton("Quitter");

        quitter.addActionListener(actionEvent -> System.exit(0));
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame frame = new JFrame("Ajouter Noeud");
                JLabel n = new JLabel("Nom du Noeud : ");
                JTextField nom = new JTextField("             ");
                JButton ok = new JButton("OK");
				JButton annuler = new JButton("Annuler");
				JComboBox<String> combo = new JComboBox<>();
				for(int i = 0; i < Liste.size(); i++) {
				    combo.addItem(Liste.get(i));
                }

				nom.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        nom.setText("");
                    }
                });
				ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        if(Liste.contains(nom.getText())) {
                            graph.addEdge(nom.getText() + combo.getSelectedItem(), nom.getText(), (String) combo.getSelectedItem());
                        } else {
                            graph.addNode(nom.getText());
                            Liste.add(nom.getText());
                            graph.addEdge(nom.getText() + combo.getSelectedItem(), nom.getText(), (String) combo.getSelectedItem());
                            Liens.add(nom.getText() + combo.getSelectedItem());
                        }
                        Node node = graph.getNode(nom.getText());
                        node.setAttribute("ui.label", nom.getText());
                        frame.setVisible(false);
                    }
                });
				annuler.addActionListener(new ActionListener() {
				    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
				        frame.setVisible(false);
				    }
				});
				ButtonGroup bg = new ButtonGroup();
				bg.add(ok);
				bg.add(annuler);

				JPanel up = new JPanel(new BorderLayout());
				up.add(n, BorderLayout.WEST);
				up.add(nom, BorderLayout.EAST);
				up.add(combo);

				JPanel down = new JPanel(new BorderLayout());
				down.add(ok, BorderLayout.WEST);
				down.add(annuler, BorderLayout.EAST);

				frame.add(up, BorderLayout.NORTH);
				frame.add(down, BorderLayout.SOUTH);
				frame.pack();
				Rectangle bordure = f.getBounds();
                frame.setLocation(bordure.x + bordure.width, bordure.y);
                frame.setVisible(true);
            }
        });
        supprimerNoeud.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame frame = new JFrame("Supprimer Noeud");
                JButton ok = new JButton("OK");
                JButton annuler = new JButton("Annuler");
                JComboBox<String> combo = new JComboBox<>();
                for (String s : Liste) {
                    combo.addItem(s);
                }

                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        graph.removeNode((String)combo.getSelectedItem());
                        Liste.remove(combo.getSelectedItem());
                        List<String> temp = new ArrayList<>();
                        for(String string : Liens) {
                            if (string.matches("(?i)(" + combo.getSelectedItem() + ").*")) {
                                continue;
                            }
                            temp.add(string);
                        }
                        frame.setVisible(false);
                    }
                });
                annuler.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        frame.setVisible(false);
                    }
                });

                ButtonGroup bg = new ButtonGroup();
                bg.add(ok);
                bg.add(annuler);

                JPanel up = new JPanel(new BorderLayout());
                up.add(combo);

                JPanel down = new JPanel(new BorderLayout());
                down.add(ok, BorderLayout.WEST);
                down.add(annuler, BorderLayout.EAST);

                frame.add(down, BorderLayout.SOUTH);
                frame.add(up, BorderLayout.NORTH);
                frame.pack();
                Rectangle bordure = f.getBounds();
                frame.setLocation(bordure.x + bordure.width, bordure.y);
                frame.setVisible(true);
            }
        });
        supprimerLien.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame frame = new JFrame("Supprimer Lien");
                JButton ok = new JButton("OK");
                JButton annuler = new JButton("Annuler");
                JComboBox<String> combo = new JComboBox<>();
                for (String s : Liens) {
                    combo.addItem(s);
                }

                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        graph.removeEdge((String)combo.getSelectedItem());
                        Liens.remove(combo.getSelectedItem());
                        frame.setVisible(false);
                    }
                });
                annuler.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        frame.setVisible(false);
                    }
                });

                ButtonGroup bg = new ButtonGroup();
                bg.add(ok);
                bg.add(annuler);

                JPanel up = new JPanel(new BorderLayout());
                up.add(combo);

                JPanel down = new JPanel(new BorderLayout());
                down.add(ok, BorderLayout.WEST);
                down.add(annuler, BorderLayout.EAST);

                frame.add(down, BorderLayout.SOUTH);
                frame.add(up, BorderLayout.NORTH);
                frame.pack();
                Rectangle bordure = f.getBounds();
                frame.setLocation(bordure.x + bordure.width, bordure.y);
                frame.setVisible(true);
            }
        });
        addLien.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame frame = new JFrame("Ajouter Lien");
                JButton ok = new JButton("OK");
                JButton annuler = new JButton("Annuler");
                JComboBox<String> comboA = new JComboBox<>();
                for (String s : Liste) {
                    comboA.addItem(s);
                }
                JComboBox<String> comboB = new JComboBox<>();
                for (String s : Liste) {
                    comboB.addItem(s);
                }

                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        graph.addEdge((String) comboA.getSelectedItem() + comboB.getSelectedItem(), (String) comboA.getSelectedItem(), (String) comboB.getSelectedItem());
                        Liens.add((String)comboA.getSelectedItem() + comboB.getSelectedItem());
                        frame.setVisible(false);
                    }
                });
                annuler.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        frame.setVisible(false);
                    }
                });

                ButtonGroup bg = new ButtonGroup();
                bg.add(ok);
                bg.add(annuler);

                JPanel up = new JPanel(new BorderLayout());
                up.add(comboA, BorderLayout.WEST);
                up.add(new JLabel("Avec : "), BorderLayout.CENTER);
                up.add(comboB, BorderLayout.EAST);

                JPanel down = new JPanel(new BorderLayout());
                down.add(ok, BorderLayout.WEST);
                down.add(annuler, BorderLayout.EAST);

                frame.add(down, BorderLayout.SOUTH);
                frame.add(up, BorderLayout.NORTH);
                frame.pack();
                Rectangle bordure = f.getBounds();
                frame.setLocation(bordure.x + bordure.width, bordure.y);
                frame.setVisible(true);
            }
        });
        ButtonGroup bGroup = new ButtonGroup();
        bGroup.add(add);
        bGroup.add(supprimerNoeud);
        bGroup.add(supprimerLien);
        bGroup.add(quitter);

        JPanel right = new JPanel(new BorderLayout());
        JPanel rup = new JPanel(new BorderLayout());
        rup.add(add, BorderLayout.NORTH);
        rup.add(addLien, BorderLayout.SOUTH);
        JPanel rdown = new JPanel(new BorderLayout());
        rdown.add(rup, BorderLayout.NORTH);
        rdown.add(supprimerNoeud, BorderLayout.WEST);
        rdown.add(supprimerLien, BorderLayout.EAST);
        rdown.add(quitter, BorderLayout.SOUTH);
        right.add(rdown, BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(right, BorderLayout.WEST);

        f.getContentPane().add(mainPanel);
        f.pack();
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
        double hauteur = tailleEcran.getHeight();
        double largeur = tailleEcran.getHeight();
        System.out.println(hauteur + " " + largeur);
		f.setLocation((int)hauteur / 4, (int)largeur/2);
        f.setVisible(true);
    }
}
