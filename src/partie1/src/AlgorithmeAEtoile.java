import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

public class AlgorithmeAEtoile {
	static ArrayList<sat> solution= new ArrayList();
	static ArrayList<String> sol= new ArrayList();
    public static SolutionFormat methode(sat s, LinkedList<sat> open, Vector<sat> closed, cnf fichierCNF) {
    	long lStartTime = System.nanoTime();
    	
	Literal contraire=null, choisi = null;
	sat pere =null;//noeud initial
	s.prof=0;// profondeur 0
	open.addFirst(s); 
	
	while(!open.isEmpty()){
		pere=open.removeFirst(); //recuperer l'élément qui a plus de fréquence
		closed.add(pere);//l'ajouter à la liste fermée
		if(pere.test()){
			solution.add(pere);
			System.out.println("SAT");// si toutes les clauses sont vérifiées alors c'est un SAT
			break;
		}
		//System.out.println("nb closes verified : " + pere.nb());//imprimer le nombre de clauses vérifiées
		
		if(pere.prof<fichierCNF.nombreVariables){
			Vector<clauses> clause1=new Vector<clauses>();
			Vector<clauses> clause2= new Vector<clauses>();
			
			//on copie la liste de litteraux dans deux listes liste1, liste2
			Vector<Literal> liste1 = new Vector<Literal>();
			for(int i=0;i<pere.liste.size();i++){
			    if(pere.liste.get(i)!=null) {
				liste1.add(new Literal(pere.liste.get(i)));
			    }
			}
			Vector<Literal> liste2 = new Vector<Literal>();
			for(int i=0;i<pere.liste.size();i++){
			    if(pere.liste.get(i)!=null) {
				liste2.add(new Literal(pere.liste.get(i)));
			    }
			}
		
			
			
		for(int i=0;i<pere.ListeClauses.size();i++){
			Vector <Literal> litteraux1= new Vector<Literal>();
			Vector <Literal> litteraux2= new Vector<Literal>();
			
			for(int j=0;j<pere.ListeClauses.get(i).ListeLitteraux.size();j++){
				if(pere.ListeClauses.get(i).ListeLitteraux.get(j).var<0) {
					litteraux1.add(liste1.get(pere.ListeClauses.get(i).ListeLitteraux.get(j).var+fichierCNF.nombreVariables));
					litteraux2.add(liste2.get(pere.ListeClauses.get(i).ListeLitteraux.get(j).var+fichierCNF.nombreVariables));
				}
				else{
					litteraux1.add(liste1.get(pere.ListeClauses.get(i).ListeLitteraux.get(j).var+fichierCNF.nombreVariables-1));
					litteraux2.add(liste2.get(pere.ListeClauses.get(i).ListeLitteraux.get(j).var+fichierCNF.nombreVariables-1));
				}
				
			}
			clause1.add(new clauses(litteraux1));
			clause2.add(new clauses(litteraux2));	
		}
		
		sat fils1 = new sat(clause1, liste1);
		sat fils2 = new sat(clause2, liste2);
		
		fils1.prof=pere.prof+1;
		fils2.prof=pere.prof+1;
		
		choisi = fils1.liste.get(fils1.prof-1); // recuperer le noeud de la profondeur plus inférieur 
		choisi.val=1;//mettre sa valeur à 1
		contraire = Literal.getContraire(fils1.liste, choisi);//recuperer son contraire
		if(contraire!=null)contraire.val=0;//mettre la valeur du contraire à 0
		choisi = fils2.liste.get(fils2.prof-1);//recuperation du 2em fils
		choisi.val=0;
		contraire = Literal.getContraire(fils2.liste, choisi);
		if(contraire!=null)contraire.val=1;
		
		boolean ajout = true;
		for(int i=0;i<closed.size();i++){//verifier si ce neoud est deja developpé
			if(fils2.sameConfig(closed.get(i), fichierCNF)){
				ajout = false;
				i=closed.size();
			}
		}
		if(ajout==true)
			{open.addFirst(fils2);//ajouter le premier fils à open
			}else {
				solution.add(fils2);
			}
		
		ajout = true;
		for(int i=0;i<closed.size();i++){
			if(fils1.sameConfig(closed.get(i), fichierCNF)){//verifier si ce neoud est deja developpé
				ajout = false;
				i=closed.size();
			}
		}
		if(ajout==true)
			{open.addFirst(fils1);//ajouter le deuxieme fils à open
			}else {
				solution.add(fils1);
			}
		
	}
		
    }
	for(int j=0; j<solution.get(solution.size()-1).liste.size();j++) {
		sol.add(String.valueOf(solution.get(solution.size()-1).liste.get(j).val));
	}

	
	if(open.isEmpty()){//si il reste encore des element dans la liste ouvert alors ce n'est pas un sat
		System.out.println("NON SAT");
	}
	long lEndTime = System.nanoTime();
	long output = lEndTime - lStartTime;
	
	SolutionFormat solutionFinal=new SolutionFormat(String.valueOf(output), sol);

	return solutionFinal;
	
}
}

