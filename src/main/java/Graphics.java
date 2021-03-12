import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.stream.GraphParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Graphics extends JFrame {
    protected String styleSheet =
        "node {" +
        "   size: 10px, 10px;" +
        "   text-size: 15px;" +
        "	fill-color: grey, red;" +
        "   text-background-mode: rounded-box;" +
        "   text-background-color: orange;" +
        "   text-alignment: above;" +
        "   fill-mode: gradient-radial;" +
        "}" +
        "edge {" +
        "   text-alignment: above;" +
        "   text-background-color: orange;" +
        "   text-background-mode: rounded-box;" +
        "   text-size: 20px;" +
        "}";
    private List<String> Liste;
    private List<String> Liens;

    public Graphics() {
        Liste = new ArrayList<>();
        Liens = new ArrayList<>();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Choix fichier Graph");
        JButton g1 = new JButton("Graph Custom");
        JButton g2 = new JButton("Exemple du cours");
        JButton ok = new JButton("OK");
        JTextField link = new JTextField("src/main/resources/graphPerso.dgs");
        JLabel lien = new JLabel("fichier dgs : ");
        JCheckBox rand = new JCheckBox("Génération aléatoire des poids ?", true);

        g1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SwingUtilities.invokeLater(() -> {
                    Graphics g = new Graphics();
                    g.initUI("src/main/resources/graph.dgs", true);
                });
                frame.setVisible(false);
            }
        });
        g2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SwingUtilities.invokeLater(() -> {
                    Graphics g = new Graphics();
                    g.initUI("src/main/resources/graphEx.dgs", false);
                });
                frame.setVisible(false);
            }
        });
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SwingUtilities.invokeLater(() -> {
                    Graphics g = new Graphics();
                    g.initUI(link.getText(), rand.isSelected());
                });
                frame.setVisible(false);
            }
        });

        ButtonGroup bg = new ButtonGroup();
        bg.add(g1);
        bg.add(g2);

        JPanel up = new JPanel(new BorderLayout());
        up.add(g1, BorderLayout.NORTH);
        up.add(g2, BorderLayout.SOUTH);

        JPanel down = new JPanel(new BorderLayout());
        down.add(lien, BorderLayout.WEST);
        down.add(link, BorderLayout.CENTER);
        down.add(rand, BorderLayout.EAST);
        down.add(ok, BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(up, BorderLayout.NORTH);
        mainPanel.add(down, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    private void initUI(String link, boolean random) {
        System.setProperty("org.graphstream.ui", "swing");
        Graph graph = new MultiGraph("Routage");
        graph.setAttribute("ui.stylesheet", styleSheet);

        try{
            graph.read(link);
        } catch (IOException | GraphParseException e) {
            e.printStackTrace();
            System.out.println("DGS FILE ERROR");
        }

        for(int i = 1; i < graph.getNodeCount() + 1; i++) {
            Liste.add("Noeud" + i);
        }
        for(int i = 0; i < graph.getEdgeCount(); i++) {
            Liens.add(graph.getEdge(i).toString().replaceAll("\\[([^\\)]+)\\]", ""));
        }

        /* Génération aléatoire de poids de lien entre 1 et 10 */
        if(random) {
            Random r = new Random();
            for (int i = 0; i < Liens.size(); i++) {
                Edge e = graph.getEdge(i);
                int rand = r.nextInt(10 - 1) + 1;
                e.setAttribute("p", rand);
                e.setAttribute("ui.label", rand);
            }
        }

        graph.display();

        JFrame f = new JFrame("Controle");

        JButton add = new JButton("Ajouter Noeud avec Lien");
        JButton modPoids = new JButton("Modifier le poids d'un Noeud");
        JButton addLien = new JButton("Ajouter Lien");
        JButton supprimerNoeud = new JButton("Supprimer Noeud");
        JButton supprimerLien = new JButton("Supprimer Lien");
        JButton table = new JButton("Table de routage");
        JButton quitter = new JButton("Quitter");

        quitter.addActionListener(actionEvent -> System.exit(0));
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame frame = new JFrame("Ajouter Noeud");
                JLabel n = new JLabel("Nom du Noeud : ");
                JTextField nom = new JTextField("             ");
                JLabel p = new JLabel("Poids du lien : ");
                JTextField poids = new JTextField("             ");
                JButton ok = new JButton("OK");
				JButton annuler = new JButton("Annuler");
				JComboBox<String> combo = new JComboBox<>();
                for (String s : Liste) {
                    combo.addItem(s);
                }

				nom.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        nom.setText("");
                    }
                });
				poids.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        poids.setText("");
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
                            graph.addEdge(nom.getText() + "-" + combo.getSelectedItem(), nom.getText(), (String) combo.getSelectedItem());
                            Liens.add(nom.getText() + "-" + combo.getSelectedItem());
                        }
                        Edge e = graph.getEdge(nom.getText() + "-" + combo.getSelectedItem());
                        e.setAttribute("p", Integer.valueOf(poids.getText()));
                        e.setAttribute("ui.label", poids.getText());
                        Node node = graph.getNode(nom.getText());
                        node.setAttribute("ui.label", nom.getText());
                        frame.setVisible(false);
                    }
                });
				annuler.addActionListener(new ActionListener() {
				    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
				        frame.setVisible(false);
                        frame.dispose();
				    }
				});
				ButtonGroup bg = new ButtonGroup();
				bg.add(ok);
				bg.add(annuler);

				JPanel up = new JPanel(new BorderLayout());
				up.add(n, BorderLayout.WEST);
				up.add(nom, BorderLayout.EAST);
				JPanel inside = new JPanel(new BorderLayout());
				inside.add(p, BorderLayout.WEST);
				inside.add(poids, BorderLayout.EAST);
				up.add(inside, BorderLayout.NORTH);
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
                        Liens = temp;
                        frame.setVisible(false);
                    }
                });
                annuler.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        frame.setVisible(false);
                        frame.dispose();
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
                        frame.dispose();
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
                JLabel text = new JLabel("Poids du Lien : ");
                JTextField poids = new JTextField("", 10);
                JComboBox<String> comboA = new JComboBox<>();
                for (String s : Liste) {
                    comboA.addItem(s);
                }
                JComboBox<String> comboB = new JComboBox<>();
                for (String s : Liste) {
                    comboB.addItem(s);
                }

                poids.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        poids.setText("");
                    }
                });
                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        graph.addEdge(comboA.getSelectedItem() + "-" + comboB.getSelectedItem(), (String) comboA.getSelectedItem(), (String) comboB.getSelectedItem());
                        Liens.add(comboA.getSelectedItem() + "-" + comboB.getSelectedItem());
                        Edge e = graph.getEdge(comboA.getSelectedItem() + "-" + comboB.getSelectedItem());
                        e.setAttribute("p", Integer.valueOf(poids.getText()));
                        e.setAttribute("ui.label", poids.getText());
                        frame.setVisible(false);
                    }
                });
                annuler.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        frame.setVisible(false);
                        frame.dispose();
                    }
                });

                ButtonGroup bg = new ButtonGroup();
                bg.add(ok);
                bg.add(annuler);

                JPanel up = new JPanel(new BorderLayout());
                JPanel inside = new JPanel(new BorderLayout());
                inside.add(text, BorderLayout.WEST);
                inside.add(poids, BorderLayout.EAST);
                up.add(inside, BorderLayout.NORTH);
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
        modPoids.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame frame = new JFrame("Modifier le poids");
                JTextField poids = new JTextField("      ");
                JButton ok = new JButton("OK");
                JButton quitter = new JButton("Quitter");
                JComboBox combo = new JComboBox();
                Edge e;
                for(int i = 0; i < Liens.size(); i++){
                    e = graph.getEdge(Liens.get(i));
                    combo.addItem(Liens.get(i) + " : " + e.getAttribute("p"));
                }

                combo.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        poids.setText(String.valueOf(combo.getSelectedIndex()));
                    }
                });
                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        Edge e = graph.getEdge(combo.getSelectedIndex());
                        e.setAttribute("p", Integer.valueOf(poids.getText()));
                        e.setAttribute("ui.label", poids.getText());
                        frame.setVisible(false);
                    }
                });
                quitter.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        frame.setVisible(false);
                        frame.dispose();
                    }
                });

                ButtonGroup bg = new ButtonGroup();
                bg.add(quitter);
                bg.add(ok);

                JPanel up = new JPanel(new BorderLayout());
                up.add(poids, BorderLayout.NORTH);
                up.add(combo, BorderLayout.SOUTH);

                JPanel down = new JPanel(new BorderLayout());
                down.add(ok, BorderLayout.WEST);
                down.add(quitter, BorderLayout.EAST);

                frame.add(up, BorderLayout.NORTH);
                frame.add(down, BorderLayout.SOUTH);
                frame.pack();
                Rectangle bordure = f.getBounds();
                frame.setLocation(bordure.x + bordure.width, bordure.y);
                frame.setVisible(true);
                poids.setText("");
            }
        });
        table.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new Table(graph, Liste, Liens);
            }
        });
        ButtonGroup bGroup = new ButtonGroup();
        bGroup.add(add);
        bGroup.add(modPoids);
        bGroup.add(addLien);
        bGroup.add(supprimerNoeud);
        bGroup.add(supprimerLien);
        bGroup.add(table);
        bGroup.add(quitter);

        JPanel right = new JPanel(new BorderLayout());
        right.add(addLien, BorderLayout.NORTH);
        right.add(supprimerNoeud, BorderLayout.SOUTH);

        JPanel left = new JPanel(new BorderLayout());
        left.add(add, BorderLayout.SOUTH);
        left.add(supprimerLien, BorderLayout.NORTH);

        JPanel down = new JPanel(new BorderLayout());
        down.add(modPoids, BorderLayout.NORTH);
        down.add(quitter, BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(table, BorderLayout.NORTH);
        mainPanel.add(down, BorderLayout.SOUTH);
        mainPanel.add(right, BorderLayout.WEST);
        mainPanel.add(left, BorderLayout.EAST);

        f.getContentPane().add(mainPanel);
        f.pack();
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
        double hauteur = tailleEcran.getHeight();
        double largeur = tailleEcran.getHeight();
		f.setLocation((int)hauteur / 4, (int)largeur/2);
        f.setVisible(true);
    }
}