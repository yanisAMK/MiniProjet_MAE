import java.util.ArrayList;

public class BSO {


    public static Solution searchBSO(cnf clset, int flip, int numBees, int maxChances, int numLocalSearch, int maxIterations) {
        ArrayList<Taboo> taboo = new ArrayList<Taboo>();

        ArrayList<Solution> dance = new ArrayList<Solution>();

        ArrayList<Bee> bees;

        int iteration = 0;

        long startTime = System.currentTimeMillis(); /* Save the start time of the search */

        Taboo sRef = new Taboo(clset.getNombreVariables(), maxChances);

        /***********generate random sol sref***********/
        int literalValue;

        for(int i=0; i<sRef.getSolution().getSolutionSize(); i++) {
            literalValue = (int) (Math.random()*10)%2;

            sRef.getSolution().getSolution().set(i, ((i+1) * (literalValue == 0 ? -1 : 1)));
        }
      /*****************************************/
        taboo.add(sRef);

        Solution bestSolution = sRef.getSolution(); /* Initialize the best solution as the one generated randomly */

        /* The code must be duplicated to improve performance (the "IF" condition is outside the "WHILE" loop) */
      //  if(withThreads) { /* "With threads" version choosed : Each bee is a thread */
            while((iteration != maxIterations) && (! sRef.getSolution().isSolution(clset))) {

            //    if((System.currentTimeMillis() - startTime) >= execTimeMillis)
              //      break; /* If the search time has reached (or exceeded) the allowed run time, finish the search */

                /* Construct the list of artificial bees, each bee has a solution (search point) determined from the reference solution "sRef" */
                bees = determineSearchPoints(sRef, flip, numBees, clset, numLocalSearch);

                for(Bee bee : bees) /* For each bee, search locally for a better solution from its research point */
                    bee.start();

                for(Bee bee : bees) { /* Add the solution found for each bee in the table "dance" */
                    try { bee.join(); } catch (InterruptedException e) { e.printStackTrace(); }
                    dance.add(bee.getSolution());
                }

                if(sRef.getSolution().satisfiedClauses(clset) > bestSolution.satisfiedClauses(clset))
                    bestSolution = sRef.getSolution();

                System.out.println(dance+"");
                sRef = bestSolution(sRef, taboo, dance, clset, maxChances); /* Find the best solution in the set {"sRef" union "dance"} */

                iteration++;

                dance.clear();		bees.clear(); /* Clear "dance" and "bees" tables for the next iteration */
            }

        return sRef.getSolution();
    }


    private static ArrayList<Bee> determineSearchPoints(Taboo sRef, int flip, int numBees, cnf clset, int numLocalSearch) {
        ArrayList<Bee> bees = new ArrayList<Bee>(); /* List of generated artificial bees */
        Solution sol; /* Solution of the current bee */
        int step = 0, p; /* step : Value to add to the multiple of "flip" , p : Value to be multiplied by "flip" */

        while((numBees > 0) && (step < flip)) {
            sol = new Solution(sRef.getSolution());
            p = 0;

            do { /* Invert literals in positions : multiples of "flip" with the step "step" */

                int position=flip*p+step;
                /***********invert********/
                if(!(position < 0) || (position >= sol.getSolution().size())) /* Error : index out of array's bounds */

                sol.getSolution().set(position, - sol.getSolution().get(position));
                /*************************/
                p++;
            }while(flip*p+step < sRef.getSolution().getSolutionSize()); /* While size of the solution "not reached" or "not exceeded" */

            bees.add(new Bee(sol, clset, numLocalSearch));
            step++;
            numBees--; /* A search point assigned to a bee, decrement "numBees" */
        }

        return bees;
    }


    private static Taboo bestSolution(Taboo sRef, ArrayList<Taboo> taboo, ArrayList<Solution> dance, cnf clset, int maxChances) {
        Integer indexBest = 0; /* Index of the best solution in quality */

        do { /* Find the best solution in quality/diversity in the set {"sRef" union "dance"} */


            /*********** search for the best sol in dance list******* */


            if(dance.isEmpty()) /* If the list is empty, the best solution in quality doesn't exist */
                return null;

            for(int i=1; i<dance.size(); i++) /* Find the best solution in QUALITY (maximum number of satisfied clauses) in "DanceList" */
                {
                    System.out.println(indexBest+"");

                    System.out.println(dance.get(i)+"");
                    System.out.println(dance.get(indexBest)+"");
                if (dance.get(i).satisfiedClauses(clset) > dance.get(indexBest).satisfiedClauses(clset))
                    indexBest = i; /* Better solution in quality found, update "indexBestQuality" */
            }
            /********************************************/
            /* Calculate The difference in the number of satisfied clauses between the best solution in quality in "dance" and the current referece solution "sRef" */
            int deltaF = dance.get(indexBest).satisfiedClauses(clset) - sRef.getSolution().satisfiedClauses(clset);

            if(deltaF > 0) { /* The best solution in quality in "dance" is better in quality than "sRef" */
                if(taboo.contains(new Taboo(dance.get(indexBest)))) { /* If "taboo" already contains this solution */
                    dance.remove(dance.get(indexBest)); /* Remove it */
                    indexBest = 0; /* The best solution in quality has not been found yet */
                }
            }
            else { /* If current "sRef" still better in quality */
                indexBest = -1; /* The best solution still the old "sRef" */
                sRef.setChances(sRef.getChances()-1); /* Decrement its number of chances */

                if(sRef.getChances() <= 0) { /* If all chances of "sRef" were used */
                    do { /* Find the best solution in diversity in "dance" which isn't already in "taboo" */
                        indexBest = bestSolutionDiversity(dance,clset); /* find the best solution in DIVERSITY in "dance" */
                        if(taboo.contains(new Taboo(dance.get(indexBest)))) { /* If "taboo" already contains this solution */
                            dance.remove(dance.get(indexBest)); /* Remove it */
                            indexBest = null; /* The best solution in diversity has not been found yet */
                        }
                    }while((! dance.isEmpty()) && (indexBest == null));
                }
            }
        }while((! dance.isEmpty()) && (indexBest == null));

        if(indexBest == null) { /* If ALL of the solutions in the set {"sRef" union "dance"} already exist in "taboo" */
            Solution s;
            do { /* Generate a random solution that doesn't already exist in "taboo" */
                s = new Solution(clset.getNombreVariables());
                /***********generate random sol sref***********/
                int literalValue;

                for(int i=0; i<s.getSolution().size(); i++) {
                    literalValue = (int) (Math.random()*10)%2;

                    s.getSolution().set(i, ((i+1) * (literalValue == 0 ? -1 : 1)));
                }
                /*****************************************/
                sRef = new Taboo(s, maxChances);
            }while(taboo.contains(sRef));
        }
        else if(indexBest >= 0) { /* There is a better solution in quality/diversity in "dance" that isn't already in "taboo" */
            sRef = new Taboo(dance.get(indexBest), maxChances);
            taboo.add(sRef);
        }
        else { /* The current "sRef" still better in quality (indexBest == -1) */
            taboo.remove(sRef); /* Delete "sRef" (to recreate it with the new number of chances) */
            taboo.add(new Taboo(sRef)); /* Add the new reference solution "sRef" */
        }

        return sRef;
    }

    public static Integer bestSolutionDiversity(ArrayList<Solution> dance, cnf clset) { /* Get index of best solution in diversity in current "DanceList" */
        int indexBestDiversity = 0; /* Index of the best solution in diversity */
        int currentSolutionDiv; /* Diversity of the current solution */
        int tempDiversity = 0; /* The diversity of the current solution of external loop [index "i"] compared to the current solution of internal loop [index "j"] */
        int bestDiversity = 0; /* Best diversity (solution of the index "indexBestDiversity") */

        if(dance.isEmpty()) /* If the list is empty, the best solution in diversity doesn't exist */
            return null;

        /* To find the best solution in diversity : we must find the MAXIMUM diversity compared to the other solutions (in "DanceList")  */
        for(int i=0; i<dance.size(); i++) { /* External loop */
            /* To find the diversity of the current solution (external loop), we must find the MINIMUM diversity compared to the other solutions (internal loop) */
            currentSolutionDiv = dance.get(i).getSolutionSize();

            for(int j=0; j<dance.size(); j = (j==i) ? j+2 : j+1) { /* Internal loop */
                tempDiversity = difference(dance.get(i),dance.get(j));
                if(tempDiversity < currentSolutionDiv)
                    currentSolutionDiv = tempDiversity;
            }

            if(bestDiversity < currentSolutionDiv) { /* If greater diversity was found */
                indexBestDiversity = i;		bestDiversity = currentSolutionDiv;
            }
        }

        return indexBestDiversity;
    }


    public static Integer difference(Solution sol, Solution solution) { /* Find Hamiltonian Distance: The number of positions with different values between two solutions */
        int numDiff = 0;

        if(sol.getSolutionSize() != solution.getSolutionSize())
            return null; /* If the size of the two solutions isn't the same, we cannot compare them */

        for(int i=0; i<sol.getSolutionSize(); i++)
            if(sol.getLiteral(i) != solution.getLiteral(i))
                numDiff++; /* Count number of different positions */

        return numDiff;
    }
}
