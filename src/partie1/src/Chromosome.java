import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Chromosome {
    ArrayList<Integer> chromosome;
    //l'ensemble de clauses que satisfait cette solution
    ArrayList<Integer> SatLocal;
    int fitness;
    //la probabilite que ce chromosome soit choisi
    double proba;
    public Chromosome(){
        this.chromosome = new ArrayList<>();
        this.SatLocal = new ArrayList<>();
        this.fitness = 0;
        this.proba=0;
    }
    public void initialise(){
        double Zhukov;
        for (int gene=1;gene<=Population.dictionnaireClauses.nombreVariables;gene++){
            Zhukov = Math.random();
            if(Zhukov<0.5)
                this.chromosome.add(-gene);
            if(Zhukov>=0.5)
                this.chromosome.add(gene);
        }
    }
    public int Fitness(){
       // this.SatLocal.clear();
        for (int gene: this.chromosome)
            this.SatLocal = Union(this.SatLocal,Population.dictionnaireClauses.map.get(gene));
        this.fitness = this.SatLocal.size();
        if (this.fitness == Population.dictionnaireClauses.nombreClauses){
            Population.solutionFound = true;
            Population.SGbest = this;
        }
        return this.fitness;
    }
    public Chromosome mutation(double Pm){
        if (Math.random()<= Pm && this.chromosome.size()>0) {
            Random random = new Random();
            int randPoint = random.nextInt(Population.dictionnaireClauses.nombreVariables);//le point aleatoire de mutation
            this.chromosome.set(randPoint, -this.chromosome.get(randPoint));
            this.Fitness();
        }
        return this;
    }
    public ArrayList<Integer> Union(ArrayList<Integer> SATGlobal, ArrayList<Integer> SATLocal) {
        Set<Integer> set = new HashSet<>();
        set.addAll(SATGlobal);
        if(SATLocal!=null) set.addAll(SATLocal);
        return new ArrayList<>(set);
    }
    public String toString(){
        return "Chromosome : "+chromosome+"\n      fitness : "+fitness+"\nSatLocal : "+SatLocal;
    }
    public double CalculerProba(int fitnessglobal) {
        this.proba=((double)(this.fitness))/((double)fitnessglobal);
        return this.proba;
    }
}