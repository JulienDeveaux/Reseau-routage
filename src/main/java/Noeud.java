public class Noeud {
    public String nom;
    public Noeud[] voisins;

    public Noeud(String nom, Noeud... voisin) {
        this.nom = nom;
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
}
