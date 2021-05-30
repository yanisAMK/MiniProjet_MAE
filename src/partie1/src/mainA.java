import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;



public class mainA {

	public sat x1;
	public LinkedList<sat> x2;
	public Vector<sat> x3;

	public mainA(String filepath){
		this.fichierCNF = new cnf(filepath);
	}
   // static ArrayList <ArrayList<String>> matrice = new ArrayList<>(); //matrice des clauses
	public cnf fichierCNF ;
    public SolutionFormat startA () throws IOException{
	
	//cnfFile2Matrix("cnfFile.cnf");//convertion du fichier en matrice de clauses
    	
	
	Vector <Literal> liste = new Vector<Literal>();//la liste des littéraux de notre sat
	int i,k=0;
	for(i=-fichierCNF.nombreVariables;i<=fichierCNF.nombreVariables;i++){//initialisatuion du vecteur de lit
		if(i!=0)
			{liste.add(new Literal(i));//0 ne sera pas instancié, chaque lit a comme : var=i et val=-1
			//System.out.print(liste.get(k).var);k++;
			}
		
	}
	
	Vector<clauses> clauses = new Vector<clauses>();//le vecteur des clauses de notre sat
	
	for(i=0; i<fichierCNF.matrice.size();i++) {
	    
	    Vector <Literal> ListeLitteraux = new Vector<Literal>();
	    for(int j=0; j<liste.size();j++) {//parcourir la liste de lit afin de recuperer les lit correspondant à la clause 
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
	Literal contraire=null; // la négation  du littéraux choisi
	sat sat = new sat(clauses, liste);//initialisation de notre prob sat
	Vector<Literal> listeRand=new Vector<Literal>(); //la liste dont on vas ordonner les littéraux par fréquences


	while(liste.size()>0) {//tanqu'il reste de littéraux dans la liste des littéraux 
	    choisi=Literal.maxfrequence(liste, sat); //selectionner le Literal qui a maximum de fréquence
	    contraire = Literal.getContraire(liste, choisi);//recuperer la negation du lit
	    Literal.removeChoisi(liste, choisi);//suprimer le littéral de la liste
	    listeRand.add(0,choisi);//l'ajouter au début de la liste (par ce qu'on va parcourir les noeuds selon de nombre de clause vérifiée et alors le nombre de fréquence maximum
	    listeRand.add(listeRand.size(), contraire);//rajouter la negation à la fin de la liste
	   }
	
	sat.liste=listeRand;//reordonnancement de la liste des littéraux 
	LinkedList<sat> open = new LinkedList<sat>();//liste ouvert qui va contenir les noeuds à developpés
	Vector <sat> closed = new Vector<sat>();//liste fermée qui va contenir les noeuds deja developpés
	this.x1 = sat;
	this.x2 = open;
	this.x3 = closed;
	return 	AlgorithmeAEtoile.methode(sat, open, closed, fichierCNF);
	}
}

