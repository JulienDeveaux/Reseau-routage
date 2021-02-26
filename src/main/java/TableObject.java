import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Graph;

import javax.swing.table.AbstractTableModel;
import java.util.List;

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
        this.tableau = new TableNode[Liste.size()];
        Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "p");
        dijkstra.init(g);
        dijkstra.setSource(g.getNode(entete[1]));
        dijkstra.compute();
        for(int i = 0; i < Liste.size(); i++) {
            String ch = String.valueOf(dijkstra.getPath(g.getNode(Liste.get(i))));
            if(ch == "[]") {
                this.tableau[i] = new TableNode(Liste.get(i), "Aucune connexion entre les commutateurs");
            } else {
                this.tableau[i] = new TableNode(Liste.get(i), ch);
            }
        }
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
                System.out.println("GNNNNNNNNNNNNNNNNNNNNNNNNNNEEEEEEEEEEE");
                return null;
        }
    }
}
