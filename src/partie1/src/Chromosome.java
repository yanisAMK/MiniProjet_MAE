import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Chromosome {
    ArrayList<Integer> chromosome;
    //l'ensemble de clauses que satisfait cette solution
    ArrayList<Integer> SatLocal;
    ArrayList<Integer> clausesQuiManquent;
    int fitness;
    //la probabilite que ce chromosome soit choisi
    double proba;
    public Chromosome(ArrayList<Integer> liste){
        this.chromosome = liste;
        this.SatLocal = new ArrayList<>();
        this.clausesQuiManquent = new ArrayList<>();
        this.fitness = 0;
        this.proba=0;
    }
    public Chromosome(){
        this.chromosome = new ArrayList<>();
        this.SatLocal = new ArrayList<>();
        this.clausesQuiManquent = new ArrayList<>();
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
        for (int gene: this.chromosome)
            this.SatLocal = Union(this.SatLocal,Population.dictionnaireClauses.map.get(gene));
        for (int i=1;i<=Population.dictionnaireClauses.nombreClauses;i++){
            if(!this.SatLocal.contains(i)&&!this.clausesQuiManquent.contains(i))
                this.clausesQuiManquent.add(i);
        }
        this.fitness = this.SatLocal.size();
        if (this.fitness == Population.dictionnaireClauses.nombreClauses)
            Population.solutionFound = true;
        return this.fitness;
    }
    public Chromosome mutation(double Pm){
        boolean b=false;
        ArrayList<Integer> p;
        for (int i=0;i<this.chromosome.size();i++) {
            p = Union(this.clausesQuiManquent, Population.dictionnaireClauses.map.get(-this.chromosome.get(i)));
            if (p.size()!=0){
                this.chromosome.set(Math.abs(i), -this.chromosome.get(i));
                this.clausesQuiManquent.remove(p);
                b = true;
            }
        }
        if(!b) {
            double i = Math.random();
            System.out.println(i);
            if (i <= Pm) {
                Random random = new Random();
                int randPoint = random.nextInt(Population.dictionnaireClauses.nombreVariables);//le point aleatoire de mutation
                this.chromosome.set(randPoint, -this.chromosome.get(randPoint));
            }
        }
        return this;
    }
    public ArrayList<Integer> Union(ArrayList<Integer> SATGlobal, ArrayList<Integer> SATLocal) {
        Set<Integer> set = new HashSet<>();
        set.addAll(SATGlobal);
        if(SATLocal!=null)set.addAll(SATLocal);
        return new ArrayList<>(set);
    }
    public String toString(){
        return "Chromosome : "+chromosome+"\n      fitness : "+fitness;
    }
    public double CalculerProba(int fitnessglobal) {
        this.proba=((double)(this.fitness))/((double)fitnessglobal);
        return this.proba;
    }
}