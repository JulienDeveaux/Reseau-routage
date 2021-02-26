import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.stream.GraphParseException;

import javax.swing.*;
import javax.swing.border.Border;
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

        for(int i = 1; i < 7; i++) {
            Liste.add("Noeud" + i);
            for(int j = i + 1; j < 7; j++) {
                Liens.add("Noeud" + i + "-" + "Noeud" + j);
            }
        }

        try{
            graph.read("src/main/java/graph.dgs");
        } catch (IOException | GraphParseException e) {
            e.printStackTrace();
            System.out.println("DGS FILE ERROR");
        }

        /* Génération aléatoire de poids de lien entre 1 et 10 */
        Random r = new Random();
        for(int i = 0;i < Liste.size(); i++) {
            Node n = graph.getNode(i);
            n.setAttribute("p", r.nextInt(10 - 1) + 1);
        }

        graph.display();

        JFrame f = new JFrame("Controle");

        JButton add = new JButton("Ajouter Noeud avec Lien");
        JButton modPoids = new JButton("Modifier le poids d'un Noeud");
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
                            graph.addEdge(nom.getText() + "-" + combo.getSelectedItem(), nom.getText(), (String) combo.getSelectedItem());
                            Liens.add(nom.getText() + "-" + combo.getSelectedItem());
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
                        graph.addEdge(comboA.getSelectedItem() + "-" + comboB.getSelectedItem(), (String) comboA.getSelectedItem(), (String) comboB.getSelectedItem());
                        Liens.add(comboA.getSelectedItem() + "-" + comboB.getSelectedItem());
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
        modPoids.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame frame = new JFrame("Modifier le poids");
                JTextField poids = new JTextField("      ");
                JButton ok = new JButton("OK");
                JButton quitter = new JButton("Quitter");
                JComboBox combo = new JComboBox();
                Node noeud;
                for(int i = 0;i < Liste.size();i++){
                    noeud = graph.getNode(Liste.get(i));
                    combo.addItem(Liste.get(i) + " : " + noeud.getAttribute("p"));
                }

                combo.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        poids.setText((String)combo.getSelectedItem()); //TODO Faire un format pour avoir que ce qui est après du " : "
                    }
                });
                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        Node n = graph.getNode((String)combo.getSelectedItem()); //TODO Pareil
                        //n.setAttribute("p", poids.getText()); //nullPointerException
                        frame.setVisible(false);
                    }
                });
                quitter.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        frame.setVisible(false);
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
                poids.setText("");  //TODO A voir si c'est nécéssaire
            }
        });
        ButtonGroup bGroup = new ButtonGroup();
        bGroup.add(add);
        bGroup.add(supprimerNoeud);
        bGroup.add(supprimerLien);
        bGroup.add(quitter);

        JPanel right = new JPanel(new BorderLayout());
        JPanel rup = new JPanel(new BorderLayout());
        rup.add(add, BorderLayout.EAST);
        rup.add(modPoids, BorderLayout.NORTH);
        rup.add(addLien, BorderLayout.WEST);
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
		f.setLocation((int)hauteur / 4, (int)largeur/2);
        f.setVisible(true);
    }
}
