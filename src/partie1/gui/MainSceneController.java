
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;


public class MainSceneController implements Initializable {

    private static final ObservableList<List<Integer>> data = FXCollections.observableArrayList();
    @FXML private Label filenamelable;
    @FXML TableView<Myclass> table;
    @FXML TableColumn<Myclass,String> c1 ;
    @FXML TableColumn<Myclass, String> c2 ;
    @FXML TableColumn<Myclass,String> c3 ;

    public ObservableList<Myclass> Data = FXCollections.observableArrayList();

    ArrayList<Myclass> container = new ArrayList<>();
    int nbvar = -1 ;
    ObservableList<Myclass> m = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb){
        c1.setCellValueFactory( new PropertyValueFactory<Myclass,String>("first"));
        c2.setCellValueFactory( new PropertyValueFactory<Myclass,String>("second"));
        c3.setCellValueFactory( new PropertyValueFactory<Myclass,String>("third"));
    }

    /*
    @FXML
    private void buttonClicked() {
       // testTable.getColumns().addAll(c1, c2,c3);         // Set extension filter
           mainButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                FileChooser.ExtensionFilter extFilter =
                        new FileChooser.ExtensionFilter("CNF files", "*.cnf");
                fileChooser.getExtensionFilters().addAll(extFilter);
                Stage thisStage = (Stage) mainButton.getScene().getWindow();
                File file = fileChooser.showOpenDialog(thisStage);
                if (file != null) {
                    openFile(file);
                }
            }});
       testTable.setItems(data);
    }
    */

    /*
    new file choosing function
     */
    @FXML
    private void importfile(ActionEvent event) throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("cnf files", "*.cnf"));
        File f = chooser.showOpenDialog(null);
        if(f != null){
            filenamelable.setText("file: "+f.getName());
            //openFile(f);

            readfile(f);
            populate();
            table.setItems(m);


            System.out.println("dagi");
            //testTable.setItems(data);
        }
    }


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

            if(line.indexOf('c') == -1){   // reads lines and stores them in an array
                String[] cn = line.split(" ");
                container.add(new Myclass(cn[0], cn[1],cn[2]));
            }
        }
        br.close();
        line = null;
    }

    public void populate(){
        for (Myclass myclass : container) {
            System.out.println(myclass.getthird() + " " + myclass.getsecond() + " " + myclass.getthird());
            m.add(new Myclass(myclass.getfirst(), myclass.getsecond(), myclass.getthird()));
        }
    }
/*
    private void openFile(File file){
            cnf liste_clauses = new cnf(file.getPath());
            for(int i=0;i<liste_clauses.getMatrice().size();i++) {

                List<Integer> firstRow = new ArrayList<>();
                for (int k=0; k<liste_clauses.getMatrice().get(i).size();k++) {

                    firstRow.add(k, liste_clauses.getMatrice().get(i).get(k));
                }
                data.add(firstRow);
            }
            System.out.println(testTable.getItems());
            Solution list = BreadthFirstSearch(liste_clauses);
            System.out.println(list);
    }

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
