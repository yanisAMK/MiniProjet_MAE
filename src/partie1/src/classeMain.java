import java.io.IOException;
import java.util.*;

public class classeMain {
    //2 attributs de la classe 'Main' :
    //1. Une liste contenant toutes les solutions possibles du probleme SAT
    static ArrayList<List<Integer>> EnsembleSolutions = new ArrayList<>();
    //2. un objet de la classe 'cnf'
    cnf SATProblem;
    //Constructeur
    public classeMain(cnf SATProblem) {
        this.SATProblem = SATProblem;
    }
    //Methode qui fait l'union du SATGlobal (l'ensemble de clauses satisfaites au fur et à mesure
    // par le parcours en profondeur) et SATLocal (l'ensemble de clauses que satisfait un litteral donné)
    public ArrayList<Integer> Union(ArrayList<Integer> SATGlobal, ArrayList<Integer> SATLocal) {
        Set<Integer> set = new HashSet<>();
        set.addAll(SATGlobal);
        set.addAll(SATLocal);
        return new ArrayList<>(set);
    }
    //Methode qui verifie que toutes les clauses du probleme sont satisfaites
    public boolean ensembleSatisfait(List<Integer> vecteur) {
        for (int j = 1; j <= SATProblem.nombreClauses; j++)
            if (!vecteur.contains(j))
                return false;
        return true;
    }
    //Methode recursive qui fait un parcours en profondeur pour trouver une solution (si elle existe)
    // à un probleme SAT donné
    public void DFS (int noeud, ArrayList<Integer> solution, int niveau,ArrayList<Integer> SATGlobal){
        if(noeud==0){//Le Début du parcours
            DFS(Math.abs(noeud)+1,solution,niveau+1,SATGlobal);
            DFS(-(Math.abs(noeud)+1),solution,niveau+1,SATGlobal);
        }
        else{
            // Creer le litteral
            litteral Noeud = new litteral(noeud, SATProblem.matrice);
            // ajouter le litteral à la liste (ArrayList) 'solution'
            if(!solution.contains(Noeud.valeur) && !solution.contains(-Noeud.valeur))
                solution.add(Noeud.valeur);
            // ajouter les clauses que satisfait ce litteral à l'ensemble de clauses satisfaites
            //par les noeuds du parcours
            SATGlobal = Union(SATGlobal,Noeud.SATLocal);
            //appels recursives au litteral qui suit celui-là (par valeur positive et negative)
            if(niveau<SATProblem.nombreVariables){
                DFS(Math.abs(noeud)+1,solution,niveau+1,SATGlobal);
                DFS(-(Math.abs(noeud)+1),solution,niveau+1,SATGlobal);
            }
            else
                // Quand le parcours au feuilles de l'arbre, le programme verifie si la solution atteinte
                //résoue le problème SAT ou pas (si oui la solution est ajoutée  l'attribut 'EnsembleSolutions'
                if(niveau==SATProblem.nombreVariables){
                    //System.out.println(solution+" : "+ensembleSatisfait(SATGlobal)+" - "+SATGlobal);
                    if(ensembleSatisfait(SATGlobal)){
                        System.out.println(solution);
                        EnsembleSolutions.add(solution);
                    }
                }
            solution.remove(solution.size()-1);
        }
    }

    public static void main(String[] args) throws IOException {
        cnf fichierCNF = new cnf("uf20-01000.cnf");
        classeMain Recherche = new classeMain(fichierCNF);
        ArrayList<Integer> SATGlobal = new ArrayList<>();
        System.out.println("Les solutions possibles :");
        Recherche.DFS(0,new ArrayList<>(),0,SATGlobal);
        //System.out.println(Recherche.EnsembleSolutions);
    }
}