
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;


public class MainSceneController implements Initializable {

    @FXML private Label filenamelable;
    //Myclass simple structure to hold the row values of Cnf Files
    @FXML TableView<Myclass> table;
    @FXML TableColumn<Myclass,String> c1 ;
    @FXML TableColumn<Myclass, String> c2 ;
    @FXML TableColumn<Myclass,String> c3 ;
    //Container contains all the rows structured
    ArrayList<Myclass> container = new ArrayList<>();
    //number of variables in the whole File Todo: check this one
    int nbvar = -1 ;
    //the appropriate structure to populate TableVeiw table
    public ObservableList<Myclass> m = FXCollections.observableArrayList();


    //Initializing columns to the appropriate type of values
    //DO NOT USE PropretyValueFactory to initialize !
    @Override
    public void initialize(URL url, ResourceBundle rb){
        c1.setCellValueFactory( data -> data.getValue().getfirst());
        c2.setCellValueFactory( data -> data.getValue().getsecond());
        c3.setCellValueFactory( data -> data.getValue().getthird());
    }

    /*
    Selecting and importing a file
    calls : readfile function
            populate function
            and sets the values of tableValues' columns
     */
    @FXML
    private void importfile() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("cnf files", "*.cnf"));
        File f = chooser.showOpenDialog(null);
        if(f != null){
            filenamelable.setText("file: "+f.getName());
            readfile(f);
            populate();
            table.setItems(m);
            System.out.println("dagi");
        }
    }

    /*
    reads the file
    ignores comments and stuff
    turns lines into a three variable clause stored as Myclass type in container
     */
    public void readfile(File file) throws IOException { // file into array, stores the number of variables in nbvar, nbclauses = the size of the array
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line;
        while((line = br.readLine()) != null){
            line = line.strip();
            if(line.indexOf('%') == 0){ // stops at the end of the file
                break;
            }
            if(line.indexOf('c') == 0 ){ // ignores comments
                continue;
            }

            if (line.contains("cnf")){ //reads the number of variables
                nbvar = Integer.parseInt(line.split(" ")[2]);
            }
            // reads lines and stores them in an array
            if(line.indexOf('c') == -1){
                String[] cn = line.split(" ");
                container.add(new Myclass(cn[0], cn[1],cn[2]));
            }
        }
        br.close();
        line = null;
    }
    /*
    populates ObservableListe object which is necessary to populate TableView
    takes the data from container
     */
    public void populate(){
        for (Myclass myclass : container) {
            System.out.println(myclass.getthird() + " " + myclass.getsecond() + " " + myclass.getthird());
            m.add(new Myclass(myclass.getfirst().get(), myclass.getsecond().get(), myclass.getthird().get()));
        }
    }

        /*
        TODO: where to use this one ?
        Solution list = BreadthFirstSearch(liste_clauses);
        */
    public Solution BreadthFirstSearch(cnf clset) {////, long execTimeMillis) {
        LinkedList<Solution> open = new LinkedList<Solution>();
        Solution currentSol = new Solution(clset.getNombreVariables());

        Solution bestSolution = new Solution(currentSol);

        int randomLiteral;

        long startTime = System.currentTimeMillis(); /* Save the start time of the search */

        do {
            // if((System.currentTimeMillis() - startTime) >= execTimeMillis)
            //   break; /* If the search time has reached (or exceeded) the allowed run time, finish the search */

            if (!open.isEmpty())
                currentSol = open.removeFirst();

            if (currentSol.getActiveLiterals() == clset.getNombreVariables())
                continue;

            randomLiteral = currentSol.randomLiteral(clset.getNombreVariables());

            for (int i = 0; i < 2; i++) { /* Loop TWO times for the chosen literal (left child) and its negation (right child) */
                currentSol.changeLiteral(Math.abs(randomLiteral) - 1, i == 0 ? randomLiteral : -randomLiteral);

                if (currentSol.satisfiedClauses(clset) > bestSolution.satisfiedClauses(clset))
                    bestSolution = new Solution(currentSol); /* If current solution is better, update the best solution */

                if (currentSol.isSolution(clset)) /* If this solution satisfies all clauses in "clset", return it */
                    return bestSolution;

                open.add(new Solution(currentSol));
            }
        } while (!open.isEmpty());

        return bestSolution;

    }


}
