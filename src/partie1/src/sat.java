import java.util.Vector;

public class sat {

	Vector<clauses>  ListeClauses; //liste de clause de notre prob sat
	Vector<Literal> liste; //liste des lit de notre prob sat
	int prof; //la profondeur
	

	public sat (Vector<clauses> Liste, Vector<Literal> l){//constructeur
		ListeClauses = Liste;
		liste = l;
	}


	public boolean test(){//si un probleme sat est verifié, au moins une clause false alors retourner false
		int i =0;
		for(i =0; i<ListeClauses.size();i++){
			if(this.ListeClauses.get(i).clauseSat()==0) return false;
		}
		return true;
	}
	
	public int nb(){//nombre de clause vérifées par notre instanciation
		int nb=0;
		for(int i=0;i<this.ListeClauses.size();i++){
			if(this.ListeClauses.get(i).clauseSat()==1) nb++;
		}
		return nb;
	}
	
	public boolean sameConfig(sat s, cnf fichierCNF){//si deux sat ont la mm conf
		for(int i=0;i<fichierCNF.nombreVariables;i++){
			if(this.liste.get(i).val!=s.liste.get(i).val){
				return false;
			}
		}
		return true;
	}
}
