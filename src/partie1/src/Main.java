
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class Main extends Application {

//    private Desktop desktop = Desktop.getDesktop();

    @Override
    public void start(final Stage stage) throws IOException {
        stage.setTitle("Sat solver");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("projetmae.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }


    /*
    public static ArrayList<Integer> BreadthFirstSearch( cnf matrice) {
        LinkedList<ArrayList<Integer>> open = new LinkedList<ArrayList<Integer>>();
       ArrayList<Integer> currentSol = new ArrayList<>();
        // //= new Solution(matrice.nu);

        //create empty solution
        for(int i=0; i<matrice.nombreClauses; i++)
            currentSol.add(0);

        ArrayList<Integer> bestSolution = new ArrayList<>();// = new Solution(currentSol);
        for(int i=0; i<currentSol.size(); i++)
            bestSolution.add(currentSol.get(i));



        long startTime = System.currentTimeMillis(); /* Save the start time of the search

        do {
           if((System.currentTimeMillis() - startTime) >= Runtime.getRuntime().freeMemory())
              break; /* If the search time has reached (or exceeded) the allowed run time, finish the search

            if(! open.isEmpty())
                currentSol = open.removeFirst();


            int activeLiterals=0;
            for(int literal : currentSol)
                if(literal != 0)
                    activeLiterals++;
            //
            if(activeLiterals == matrice.nombreVariables)
                continue;


            /****générate  a random literal**

            int randomLiteral;
            do {
                randomLiteral = (int) (Math.random()*100)%matrice.nombreVariables + 1;
            }while(currentSol.get(randomLiteral) != 0);

            randomLiteral= randomLiteral * (((int) (Math.random()*10)%2) == 0 ? 1 : -1);
           // randomLiteral = currentSol.randomLiteral(clset.getNumberVariables());




            for(int i=0; i<2; i++) { /* Loop TWO times for the chosen literal (left child) and its negation (right child)

                int position=Math.abs(randomLiteral)-1;
                int value= i==0 ? randomLiteral : -randomLiteral;
                if((position < 0) || (position >= currentSol.size())) /* Error : index out of array's bounds */
               /******todo afficher error**

                currentSol.set(position,value);


             //   currentSol.changeLiteral(Math.abs(randomLiteral)-1, i==0 ? randomLiteral : -randomLiteral);

                int literal;
                int  countsol = 0;
                int  countbest = 0;
                for(int k=0; k<matrice.nombreClauses-1; k++) { /* Browse all clauses of "clauses set"
                    for (int j = 0; j < matrice.matrice.get(k).size()-1; j++) { /* Browse all literals of actual clause
                        literal = matrice.matrice.get(k).get(j);

                        if (literal == currentSol.get(Math.abs(literal)-1)) {
                            countsol++;
                            break; /* At least one literal satisfies the clause ==> this clause is satisfied
                        }
                        if (literal == bestSolution.get(Math.abs(literal) - 1)) {
                            countbest++;
                            break; /* At least one literal satisfies the clause ==> this clause is satisfied
                        }
                    }

                    //   if(currentSol.satisfiedClauses(clset) > bestSolution.satisfiedClauses(clset))
                    if (countsol > countbest)
                        //    bestSolution = new Solution(currentSol); /* If current solution is better, update the best solution
                        for (int l = 0; l < currentSol.size(); l++)
                            bestSolution.add(currentSol.get(l));

                    //  if(currentSol.isSolution(clset)) /* If this solution satisfies all clauses in "clset", return it

                    if (currentSol.size() == countsol)
                        return bestSolution;


                    ArrayList<Integer> sol = new ArrayList<>();
                    for (int l = 0; l < currentSol.size(); l++)
                        sol.add(currentSol.get(l));
                    //  open.add(new Solution(currentSol));
                    open.add(sol);
                }
            }
        }while(! open.isEmpty());

        return bestSolution;
    }
    */




}
