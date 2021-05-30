
import java.util.*;

import static java.lang.Math.abs;

public class classeMain {
    //3 attributs de la classe 'Main' :
    //1. Un objet de la classe SolutionFormat
    public SolutionFormat solution = new SolutionFormat();
    //2. un objet de la classe 'cnf'
    cnf SATProblem ;
    //3. Un boolean qui arrete le parcours dès que la fonction trouve une solution au probleme
    boolean solutionFound = false;
    //variables necessaires pour calculer le temps d'execution
    long startTime, stopTime;
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
    public void  DFS (int noeud, ArrayList<Integer> solution, int niveau,ArrayList<Integer> SATGlobal){
        if(noeud==0){//Le Début du parcours
            DFS(abs(noeud)+1,solution,niveau+1,SATGlobal);
            DFS(-(abs(noeud)+1),solution,niveau+1,SATGlobal);
        }
        else{
            if(!solutionFound){
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
                    DFS(abs(noeud)+1,solution,niveau+1,SATGlobal);
                    DFS(-(abs(noeud)+1),solution,niveau+1,SATGlobal);
                }
                else
                    // Quand le parcours au feuilles de l'arbre, le programme verifie si la solution atteinte
                    //résoue le problème SAT ou pas (si oui la solution est ajoutée  l'attribut 'Solution' et
                    // le processus de recherche de solution s'arrete )
                    if(niveau==SATProblem.nombreVariables){
                        if( ensembleSatisfait(SATGlobal)){
                            solutionFound = true;
                            System.out.println(solution);
                            ArrayList<String> s = new ArrayList<>();
                            stopTime  = System.currentTimeMillis();

                            for (Integer i : solution){
                                int j = i>0 ? 1: 0;
                                s.add("X" + abs(i) + ": " + j);
                            }
                            this.solution.setSolutionValues(s);
                            this.solution.setTime(String.valueOf(stopTime-startTime));
                        }
                    }
                solution.remove(solution.size()-1);
            }
        }
    }

}
