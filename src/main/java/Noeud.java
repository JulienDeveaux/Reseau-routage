import java.util.Arrays;

public class Noeud {
    public String nom;
    public Noeud[] voisins;

    public Noeud(String nom, Noeud... voisin) {
        this.nom = nom;
        voisins = new Noeud[voisin.length];
        for(int i = 0; i < voisin.length; i++) {
            this.voisins[i] = voisin[i];
        }
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Noeud getVoisinPos(int pos) {
        return voisins[pos];
    }

    public Noeud[] getVoisin() {
        return voisins;
    }

    public void addVoisin(Noeud n) {
        Noeud[] tab = new Noeud[voisins.length + 1];
        for(int i = 0; i < voisins.length; i++) {
            tab[i] = voisins[i];
        }
        tab[tab.length - 1] = n;
        voisins = tab;
    }

    public void deleteVoisin(int pos) {
        Noeud[] tab = new Noeud[voisins.length - 1];
        voisins[pos] = null;
        boolean posPasse = false;
        for(int i = 0; i < tab.length; i++) {
            if(voisins[i] == null) {
                posPasse = true;
            }
            if(posPasse) {
                tab[i] = voisins[i + 1];
            } else {
                tab[i] = voisins[i];
            }
        }
        voisins = tab;
    }

    @Override
    public String toString() {
        return "Noeud{" +
                "nom='" + nom + '\'' +
                ", voisins=" + Arrays.toString(voisins) +
                '}';
    }
}
