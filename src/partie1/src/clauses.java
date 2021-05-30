import java.util.Vector;

public class clauses {
	Vector <Literal> ListeLitteraux;//liste de littéraux de la clause
	boolean check;// la valeur de la clause
	
	public clauses(Vector <Literal> Liste){//constructeur
		ListeLitteraux = Liste;
		check = false;
	}

	public clauses(clauses c){//constructeur
		this.ListeLitteraux=(Vector<Literal>) c.ListeLitteraux.clone();
		this.check = c.check;
	}

	public int clauseSat(){//verifier la valeur d'une clause 
		if(this.ListeLitteraux.size()==3){
			if((this.ListeLitteraux.get(0).val==1)||(this.ListeLitteraux.get(1).val==1)||(this.ListeLitteraux.get(2).val==1))
				return 1;
		}
		return 0;	
	}
}
