import java.util.ArrayList;
import java.util.List;
public class litteral {
    //cette classe a seulment 2 attributs

    public int valeur; //1. la valeur du littéral : -1 , 2 ,-9 par exemple
    public ArrayList<Integer> SATLocal = new ArrayList<>(); // la liste des clauses que satisfait ce littéral


    //methode qui construit la liste de l'attribut 'SATLocal' à partir de la matrice des clauses du problème SAT
    public void ChercherSATLocal(int valeur, ArrayList <List<Integer>> matrice){
        int compteur = 1;
        for(List<Integer> liste : matrice){
            if(liste.contains(valeur)){
                SATLocal.add(compteur);
            }
            compteur = compteur+1;
        }
    }

    //Constructeur
    public litteral(int valeur, ArrayList <List<Integer>> matrice){
        this.valeur = valeur;
        ChercherSATLocal(valeur, matrice);
    }
    public String toString(){
       return "Le littéral "+valeur+" satisfait les clauses suivantes : "+SATLocal;
    }
}
