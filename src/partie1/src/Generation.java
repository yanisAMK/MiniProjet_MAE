import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generation {
    ArrayList<Chromosome> generation;
    Chromosome Sbest; //la meilleure solution dans la generation
    public Generation(int taillePopulation, int nombreVariables){
        this.generation = new ArrayList<>();
        Sbest = new Chromosome();
        initialiseGeneration(taillePopulation);
    }
    //constructeur a utiliser pour la construction de la generation des fils
    public Generation(ArrayList<Chromosome> generation){
        this.generation = generation;
        Sbest = new Chromosome();
    }
    public void initialiseGeneration(int taille){
        for (int indiceIndividu = 1; indiceIndividu <= taille; indiceIndividu++){
            Chromosome individu = new Chromosome();
            individu.initialise();
            while(this.generation.contains(individu))
                individu.initialise();
            generation.add(individu);
        }
    }
    public void globalFitness(){
        int fitnessScore;
        for (Chromosome individual: this.generation){
            fitnessScore = individual.Fitness();
            if (fitnessScore >= this.Sbest.fitness)
                Sbest = individual;
        }
        this.Sort();//trier la population avant de faire la selection et le crossover
    }
    public static void heapify(ArrayList<Chromosome> array, int size, int i){
        int largest = i;
        int left = 2*i + 1;
        int right = 2*i + 2;
        int temp;
        if (left < size && array.get(left).fitness > array.get(largest).fitness)
            largest = left;
        if (right < size && array.get(right).fitness > array.get(largest).fitness)
            largest = right;
        if (largest != i){
            temp = array.get(i).fitness;
            array.get(i).fitness= array.get(largest).fitness;
            array.get(largest).fitness = temp;
            heapify(array, size, largest);
        }
    }
    public void Sort(){
        int i;
        int temp;
        for (i = this.generation.size() / 2 - 1; i >= 0; i--)
            heapify(this.generation, this.generation.size(), i);
        for (i=this.generation.size()-1; i>=0; i--){
            temp = this.generation.get(0).fitness;
            this.generation.get(0).fitness= this.generation.get(i).fitness;
            this.generation.get(i).fitness = temp;
            heapify(this.generation, i, 0);
        }
    }
    public void AfficherGeneration(int i){
        if(i==0)
            System.out.println("Génération "+i+" (génération initiale)");
        else System.out.println("Génération "+i);
        for(int individu=0;individu<this.generation.size();individu++)
            System.out.println((individu+1)+". "+this.generation.get(individu));
    }
    /*Principe de la selection :
     * 1 r�cup�rer la fitness de toute la population = somme des fitness des chromosomes
     * 2 pour chaque chromosome calculer sa probabilit� d'etre choisi = fitness chromosome/fitness population
     * 3 construire des interval
     * 4 on choisi un nombre al�atoire entre 0 et 1 (car la proba 1 est la proba max pour une fitness) ==> math.random()
     * 5 determiner � quel intervalle appartient ce nombre
     * 6 le chromosome qui correspond � la borne sup�rieure de l'intervalle choisi est un parent
     * retourner � 4 et r�p�ter Taille de population/2 fois
     */
    public ArrayList<Chromosome []> Selection(int Ps){
        ArrayList<Chromosome []> Parents = new ArrayList<Chromosome[]>();
        int sum = this.FitnessGeneration();//1
        for(int i=0; i<this.generation.size();i++) {//2
            this.generation.get(i).CalculerProba(sum);
        }
        //3 construction des intervalles
        ArrayList<Double> Intervalles = CalculerIntervalles(this.generation);
        //4 & 5 & 6
        int nbParents= Ps/2;
        if(nbParents%2==1)
            nbParents=nbParents+1;//si le nombre d it�rations est impair on rajoute une it�ration pour qu'on peut choisir les parents deux � deux
        int nbCouples= nbParents/2;
        for(int i=0; i<nbCouples; i++) {
            Chromosome c1= this.ChromoChoisi(Intervalles);
            Chromosome c2= this.ChromoChoisi(Intervalles);
            Chromosome [] tab= {new Chromosome(),new Chromosome()};//tableau qui va contenir un couple
            tab[0]=c1;
            tab[1]=c2;

            //	System.out.println("ana couple "+ tab[0].fitness + "  "+ tab[1].fitness);

            Parents.add(tab);
        }

        //	for(int j=0; j<Parents.size();j++) {
        //		System.out.println("ana parent1 "+Parents.get(j)[0].fitness+ "ana parent2 "+Parents.get(j)[1].fitness);
        //	}
        return Parents;
    }
    //� partir d'une liste de chromosomes on creer listes contenant les diff�rents intervalles de probabilit�s
    public ArrayList<Double> CalculerIntervalles(ArrayList<Chromosome> ListChromo){

        ArrayList<Double> Intervalles= new ArrayList<Double>(); //attention la taille de cette liste est le nombre de chromo +1 (le 0)
        Intervalles.add((double) 0);
        double sum=0;

        for(int i=this.generation.size()-1; i>=0;i--) {
            sum=sum+this.generation.get(i).proba;
            Intervalles.add(sum);
        }
        return Intervalles;
    }
    //choisir un nombre al�atoire, � quel intervalle il appartient, retourner le chromosome choisi
    public Chromosome ChromoChoisi(ArrayList<Double> Intervalles) {
        //5
        double rand = Math.random();
        //System.out.println("je suis le rand "+rand);
        //6
        for(int i=0; i<Intervalles.size()-1;i++) {
            if((rand>=Intervalles.get(i)) && (rand<=Intervalles.get(i+1))) {
                if(rand==Intervalles.get(i)) {
                    //System.out.println(Intervalles.get(i));
                    return this.generation.get(i-1);
                }else {return this.generation.get(i);}
            }
        }
        return new Chromosome();
    }
    public int FitnessGeneration() {//calculer la fitness globale de toutes la population
        int sum=0;

        for(int individu=0;individu<this.generation.size();individu++) {
            sum=sum+this.generation.get(individu).fitness;
        }
        return sum;
    }
    public Chromosome[] Croisement(Chromosome[] pair, double Pc) {
        Chromosome [] tab= {new Chromosome(), new Chromosome()};
        Random random = new Random();
        if(Math.random()<=Pc && pair[0].chromosome.size()>0) {
            int randPoint= random.nextInt(pair[0].chromosome.size());//le point al�atoire de croisement
            //cr�ation et remplissage du premier fils
            for(int i=0;i<=randPoint;i++) {
                tab[0].chromosome.add(pair[0].chromosome.get(i));
            }
            for(int i=randPoint+1;i<pair[0].chromosome.size();i++) {
                tab[0].chromosome.add(pair[1].chromosome.get(i));
            }
            //cr�ation et remplissage du premier fils
            for(int i=0;i<=randPoint;i++) {
                tab[1].chromosome.add(pair[1].chromosome.get(i));
            }
            for(int i=randPoint+1;i<pair[0].chromosome.size();i++) {
                tab[1].chromosome.add(pair[0].chromosome.get(i));
            }
        }
        return tab;
    }
    //construction de la nouvelle g�n�ration
    public Generation NewGeneration(Generation generation, Generation ChildrenGeneration) {
        int i = generation.generation.size()-1;        //taille de l'ancienne population
        int j = ChildrenGeneration.generation.size()-1;//taille de la g�n�ration children
        int k = 0; //pour le remplissage de la nouvelle population
        ArrayList<Chromosome> NewGen = new ArrayList<Chromosome>();
        while((i>=0) && (j>=0) && (k<generation.generation.size())) {
            if((generation.generation.get(i).fitness) >= ChildrenGeneration.generation.get(j).fitness) {
                NewGen.add(generation.generation.get(i));
                k++;
                i--;
            }
            else {
                NewGen.add(ChildrenGeneration.generation.get(j));
                k++;
                j--;
            }
        }
        while((i>=0) &&  (k<generation.generation.size())) {
            NewGen.add(generation.generation.get(i));
            k++;
            i--;
        }
        while((j>=0) &&  (k<generation.generation.size())) {
            NewGen.add(ChildrenGeneration.generation.get(j));
            k++;
            j--;
        }
        return new Generation(NewGen);
    }
}