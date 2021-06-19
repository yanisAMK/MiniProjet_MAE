
import java.io.*;
import java.util.LinkedList;
import java.util.Vector;

public class mainA {

	public sat x1;
	public LinkedList<sat> x2;
	public Vector<sat> x3;

	public mainA(String filepath){
		this.fichierCNF = new cnf(filepath);
	}
	public cnf fichierCNF ;
    public SolutionFormat startA () throws IOException{

	Vector <Literal> liste = new Vector<Literal>();//la liste des litt�raux de notre sat
	int i,k=0;
	for(i=-fichierCNF.nombreVariables;i<=fichierCNF.nombreVariables;i++){//initialisatuion du vecteur de lit
		if(i!=0)
			{liste.add(new Literal(i));//0 ne sera pas instanci�, chaque lit a comme : var=i et val=-1
			}
		
	}
	
	Vector<clauses> clauses = new Vector<clauses>();//le vecteur des clauses de notre sat
	
	for(i=0; i<fichierCNF.matrice.size();i++) {
	    
	    Vector <Literal> ListeLitteraux = new Vector<Literal>();
	    for(int j=0; j<liste.size();j++) {//parcourir la liste de lit afin de recuperer les lit correspondant � la clause 
		if(fichierCNF.matrice.get(i).get(0)==liste.get(j).var) {
		    ListeLitteraux.add(liste.get(j));
		}
		if(fichierCNF.matrice.get(i).get(1)==liste.get(j).var) {
		    ListeLitteraux.add(liste.get(j));
		}
		if(fichierCNF.matrice.get(i).get(2)==liste.get(j).var) {
		    ListeLitteraux.add(liste.get(j));
		}
	    }
	    
	    clauses cl = new clauses(ListeLitteraux);
	    clauses.add(cl); //mettre toutes les clauses dans le vecteur clauses
	}
	
	
	Literal choisi=null; // le neoud choisi selon l'heuristique
	Literal contraire=null; // la n�gation  du litt�raux choisi
	sat sat = new sat(clauses, liste);//initialisation de notre prob sat
	Vector<Literal> listeRand=new Vector<Literal>(); //la liste dont on vas ordonner les litt�raux par fr�quences


	while(liste.size()>0) {//tanqu'il reste de litt�raux dans la liste des litt�raux 
	    choisi=Literal.maxfrequence(liste, sat); //selectionner le Literal qui a maximum de fr�quence
	    contraire = Literal.getContraire(liste, choisi);//recuperer la negation du lit
	    Literal.removeChoisi(liste, choisi);//suprimer le litt�ral de la liste
	    listeRand.add(0,choisi);//l'ajouter au d�but de la liste (par ce qu'on va parcourir les noeuds selon de nombre de clause v�rifi�e et alors le nombre de fr�quence maximum
	    listeRand.add(listeRand.size(), contraire);//rajouter la negation � la fin de la liste
	   }
	
	sat.liste=listeRand;//reordonnancement de la liste des litt�raux 
	LinkedList<sat> open = new LinkedList<sat>();//liste ouvert qui va contenir les noeuds � developp�s
	Vector <sat> closed = new Vector<sat>();//liste ferm�e qui va contenir les noeuds deja developp�s
	this.x1 = sat;
	this.x2 = open;
	this.x3 = closed;
	return 	AlgorithmeAEtoile.methode(sat, open, closed, fichierCNF);
	}
}

