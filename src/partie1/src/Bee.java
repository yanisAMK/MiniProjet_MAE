import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Random;

public class Bee extends Thread{
    private Solution solution;
    private cnf clset;
    private int numLocalSearch;

    public Bee(Solution solution, cnf clset, int numLocalSearch) {
        this.solution = solution;
        this.clset = clset;
        this.numLocalSearch = numLocalSearch;
    }


    public Solution getSolution() {
        return solution;
    }


    @Override
    public void run() {
        this.localSearch();
    }


    public Solution localSearch() {
        Solution temporarySol = new Solution(this.getSolution());
        Random random = new Random(); /* "Random" object used instead of "Math.random()" because the latter is a SYNCHRONIZED method */

        for(int i=0; i<this.numLocalSearch; i++) {

            int position=random.nextInt(this.clset.getNombreVariables());
            /***********invert********/
            if(!(position < 0) || (position >= temporarySol.getSolution().size())) /* Error : index out of array's bounds */

                temporarySol.getSolution().set(position, - temporarySol.getSolution().get(position));
            /*************************/
            if(temporarySol.satisfiedClauses(this.clset) > this.solution.satisfiedClauses(this.clset))
                this.solution = new Solution(temporarySol);
        }

        return this.solution;
    }


    @Override
    public String toString() {
        return "Bee ("+this.solution+")";
    }

}
