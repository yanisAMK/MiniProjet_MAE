import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainSceneController {

    @FXML
    private TableView<List<Integer>> testTable = new TableView<List<Integer>>();
    private static final ObservableList<List<Integer>> data = FXCollections.observableArrayList();

    final FileChooser fileChooser = new FileChooser();
    @FXML
    private Button mainButton;



    @FXML
    TableColumn c1 = new TableColumn("Log Entries");
    @FXML
    TableColumn c2 = new TableColumn("Date");
    @FXML
    TableColumn c3 = new TableColumn("Date");

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

    private void openFile(File file){
        try {
            //  desktop.open(file);
            cnf liste_clauses = new cnf(file.getPath());





            for(int i=0;i<liste_clauses.getNombreClauses();i++)
            {
                List<Integer> firstRow = new ArrayList<>();
                for (int k=0; k<liste_clauses.getMatrice().get(i).size();k++)
                {
                    firstRow.add(k, liste_clauses.getMatrice().get(i).get(k));

                }

                data.add(firstRow);
           //    testTable.getItems().addAll(firstRow);
            }



            System.out.println(testTable.getItems());

            /*****************
             * appeler la methode correspendante
             */

            Solution list = BreadthFirstSearch(liste_clauses);

            System.out.println(list);

      /*  } catch (IOException ex) {
            Logger.getLogger(
                    Main.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }*/
        } finally {

        }

    }
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
