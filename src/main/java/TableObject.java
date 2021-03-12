import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import javax.swing.table.AbstractTableModel;
import java.util.*;

public class TableObject extends AbstractTableModel {
    public class TableNode {
        private String nom;
        private String noeud;

        public TableNode(String nom, String noeud) {
            this.nom = nom;
            this.noeud = noeud;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getNoeud() {
            return noeud;
        }

        public void setNoeud(String noeud) {
            this.noeud = noeud;
        }
    }

    private String[] entete = {"", ""};
    private TableNode[] tableau;
    private Graph g;
    private List<String> Liste;
    private List<String> Liens;

    public TableObject(String noeudDepart, Graph g, List<String> liens, List<String> liste) {
        super();
        this.g = g;
        this.Liste = liste;
        this.Liens = liens;
        this.entete[1] = noeudDepart;
        this.tableau = new TableNode[Liste.size() - 1];

        int j = 0;
        for(int i = 0; i < Liste.size() && (j+i) < Liste.size(); i++) {
            if (Liste.get(i).equals(noeudDepart) && !noeudDepart.equals(Liste.get(Liste.size() - 1))) {
                j++;
            }
            if(i == Liste.size()  -1 && noeudDepart.equals(Liste.get(Liste.size() - 1))) {
                continue;
            }
            String ch = routage(noeudDepart, Liste.get(i + j));
            if (ch.equals("")) {
                this.tableau[i] = new TableNode(Liste.get(i + j), "Aucune connexion entre les commutateurs");
            } else {
                this.tableau[i] = new TableNode(Liste.get(i + j), ch);
            }
        }
    }

    private String routage(String noeudDepart, String noeudArrive) {
        List<Node> voisins = new ArrayList<>();
        String res = "";

        Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "p");
        dijkstra.init(g);

        for(Edge e : g.getNode(noeudDepart)) {
            voisins.add(e.getOpposite(g.getNode(noeudDepart)));
        }

        int[] pStore = new int[voisins.size()];
        for(int i = 0; i < voisins.size(); i++) {
            try {
                pStore[i] = (int) g.getEdge(g.getNode(noeudDepart).getAttribute("ui.label") + "-" + voisins.get(i).getAttribute("ui.label")).getAttribute("p");
                g.getEdge(g.getNode(noeudDepart).getAttribute("ui.label") + "-" + voisins.get(i).getAttribute("ui.label")).setAttribute("p", 500000);
            } catch (NullPointerException e) {
                pStore[i] = (int) g.getEdge(voisins.get(i).getAttribute("ui.label") + "-" + g.getNode(noeudDepart).getAttribute("ui.label")).getAttribute("p");
                g.getEdge(voisins.get(i).getAttribute("ui.label") + "-" + g.getNode(noeudDepart).getAttribute("ui.label")).setAttribute("p", 500000);
            }
        }
        HashMap <Integer, String> map = new HashMap<>();

        for(int i = 0; i < voisins.size(); i++) {
            dijkstra.setSource(noeudArrive);
            dijkstra.compute(); //chemin entre l'arrivée et le voisin i
            String chemin = String.valueOf(dijkstra.getPath(voisins.get(i)));
            chemin = chemin.substring(1, chemin.length() - 1);
            String[] noeuds = chemin.split(", ");
            int poids = 0;
            for(int j = 0; j < noeuds.length - 1; j++) {
                try {
                    poids = poids + (int) g.getEdge(noeuds[j] + "-" + noeuds[j + 1]).getAttribute("p");
                } catch (NullPointerException e) {
                    poids = poids + (int) g.getEdge(noeuds[j + 1] + "-" + noeuds[j]).getAttribute("p");
                }
            }
            poids += pStore[i];
            if(map.get(poids) != null) {
                map.put(poids, "( " + map.get(poids) + " / " + voisins.get(i) + " )");
            } else {
                map.put(poids, String.valueOf(voisins.get(i)));
            }
        }

        List<Integer> sorted = new ArrayList<>(map.keySet());
        Collections.sort(sorted);
        for(int i = 0; i < sorted.size(); i++) {
            if(i < sorted.size() - 1 && sorted.get(i) == sorted.get(i + 1)) {
                res = res + "( " + map.get(sorted.get(i)) + "->" + sorted.get(i) + " / " + map.get(sorted.get(i + 1)) + " " + sorted.get(i + 1) + " ) / ";
            } else {
                res = res + map.get(sorted.get(i)) + "->" + sorted.get(i) + " / ";
            }
        }
        res = res.substring(0, res.length() - 2);

        for(int i = 0; i < voisins.size(); i++) {
            try {
                g.getEdge(g.getNode(noeudDepart).getAttribute("ui.label") + "-" + voisins.get(i).getAttribute("ui.label")).setAttribute("p", pStore[i]);
            } catch (NullPointerException e) {
                g.getEdge(voisins.get(i).getAttribute("ui.label") + "-" + g.getNode(noeudDepart).getAttribute("ui.label")).setAttribute("p", pStore[i]);
            }
        }

        dijkstra.clear();
        return res;
    }

    public int getRowCount() {
        return tableau.length;
    }

    public int getColumnCount() {
        return entete.length;
    }

    public String getColumnName(int columnIndex) {
        return entete[columnIndex];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return tableau[rowIndex].getNom();
            case 1:
                return tableau[rowIndex].getNoeud();
            default:
                return null;
        }
    }
}
