import java.util.ArrayList;

public class Population {
    ArrayList<Generation> population;
    static cnf dictionnaireClauses;
    static boolean solutionFound;
    static Chromosome SGbest;
    public Population(String file){
        Population.dictionnaireClauses = new cnf(file);
        this.population = new ArrayList<>();
        solutionFound = false;
    }

    public Population() {

    }

    public void GeneticAlgorithm(int taillePop, double Pc, double Pm){
        Generation generation = new Generation(taillePop, dictionnaireClauses.nombreVariables);
        generation.globalFitness();
        SGbest= generation.Sbest;
        generation.AfficherGeneration(0);
        int j=1, maxIter=1,MaxIter=4500;
        while(!solutionFound && maxIter<MaxIter){
            ArrayList<Chromosome []> Parents = generation.Selection(taillePop);
            ArrayList<Chromosome> Children = new ArrayList<>(); //fils de toute la g�n�rations
            for (Chromosome[] pair :Parents) {
                Chromosome [] Fils = {new Chromosome(), new Chromosome()};//2 fils de chaque pair
                Fils= generation.Croisement(pair, Pc);
                //calcule de fitness des nouveaux chromosomes obtenus apres croisement
                Fils[0].Fitness();
                Fils[1].Fitness();
                Fils[0]=Fils[0].mutation(Pm);
                Fils[1]=Fils[1].mutation(Pm);
                //modifier la fitness apres mutation
                Fils[0].Fitness();
                Fils[1].Fitness();
                //ajouter les fils du couple courant a la liste des fils globale
                Children.add(Fils[0]);
                Children.add(Fils[1]);
            }
            //creation de la generation des fils, afin de faire le sort() et construire la nouvelle generation
            Generation ChildrenGeneration = new Generation(Children);
            generation = generation.NewGeneration(generation, ChildrenGeneration);
            generation.globalFitness();
            generation.Sort();
            generation.AfficherGeneration(maxIter);
            if (generation.Sbest.fitness >= SGbest.fitness) SGbest= generation.Sbest;
            SGbest.Fitness();
            j++;
            maxIter++;
        }
    }
    public static void main(String[] args)  {
        int taillePopulation=50; //taille de population
        double Pc=1;             //taux de croisement
        double Pm=0.7;           //taux de mutation
        long startTime, stopTime;
        Population populationSAT = new Population();
        startTime  = System.currentTimeMillis();
        populationSAT.GeneticAlgorithm(taillePopulation,Pc, Pm);
        stopTime  = System.currentTimeMillis();
        SolutionFormat solution = new SolutionFormat(SGbest.chromosome,String.valueOf(stopTime-startTime));
        if (solutionFound) System.out.println("SAT");
        else System.out.println("non SAT");
        System.out.println(SGbest);
        System.out.println(solution);
    }
}