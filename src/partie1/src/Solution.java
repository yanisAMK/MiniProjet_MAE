
import java.util.ArrayList;

public class Solution {
    private ArrayList<Integer> solution = new ArrayList<Integer>();
    private float satRate;


    public Solution(int size) { /* Constructor that creates an empty solution */
        for(int i=0; i<size; i++)
            this.solution.add(0);
    }


    public Solution(Solution solution) {
        for(int i=0; i<solution.getSolutionSize(); i++)
            this.solution.add(solution.getLiteral(i));
    }

    public float getSatRate() {
        return satRate;
    }

    public void setSatRate(float satRate) {
        this.satRate = satRate;
    }

    public int randomLiteral(int literals) { /* Generate a random literal */
        int randomLiteral;

        do {
            randomLiteral = (int) (Math.random()*100)%literals + 1;
        }while(this.getLiteral(randomLiteral-1) != 0);

        return randomLiteral * (((int) (Math.random()*10)%2) == 0 ? 1 : -1);
    }


    public ArrayList<Integer> getSolution() {
        return solution;
    }


    public int getLiteral(int position) { /* Get a literal at position "position" */
        return this.solution.get(position);
    }


    public int getSolutionSize() { /* Get number of literals in the solution */
        return this.solution.size();
    }


    public int getActiveLiterals() {
        int activeLiterals=0;

        for(int literal : this.solution)
            if(literal != 0)
                activeLiterals++;

        return activeLiterals;
    }


    public boolean changeLiteral(int position, int value) { /* Change truth value of literal in position "position" */
        if((position < 0) || (position >= this.solution.size())) /* Error : index out of array's bounds */
            return false;

        this.solution.set(position, value);

        return true;
    }


    public int satisfiedClauses(cnf clausesSet) { /* Count number of satisfied clauses by this solution */
        int count = 0; /* Counter of satisfied clauses */
        int literal; /* Store actual literal */


        for(int i=0; i<clausesSet.getMatrice().size(); i++) { /* Browse all clauses of "clauses set" */
            for(int j=0; j<clausesSet.getMatrice().get(i).size()-1; j++) { /* Browse all literals of actual clause */
                literal = clausesSet.getMatrice().get(i).get(j);

                if(literal == this.getLiteral(Math.abs(literal)-1)) {
                    count++;
                    break; /* At least one literal satisfies the clause ==> this clause is satisfied */


                }
            }
        }

        return count;
    }


    public boolean isSolution(cnf clausesSet) { /* Check if this solution satisfies ALL CLAUSES in "clauses set" */
        return clausesSet.getMatrice().size() == this.satisfiedClauses(clausesSet);
    }

    @Override
    public boolean equals(Object obj) { /* Test the equality between two solutions */
        Solution otherSol = (Solution) obj;

        if(this.getSolutionSize() != otherSol.getSolutionSize())
            return false; /* If the size of the two solutions is different, then the two solutions are NOT equal */

        for(int i=0; i<this.getSolutionSize(); i++)
            if(this.getLiteral(i) != otherSol.getLiteral(i))
                return false; /* There is at least one different literal */

        return true; /* Same size AND same literal ==> Equal solutions */
    }


    @Override
    public String toString() {
        return "Solution is : "+this.solution;
    }
}