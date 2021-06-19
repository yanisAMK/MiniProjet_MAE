
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.util.*;

import static java.lang.Math.abs;


public class MainSceneController implements Initializable {
    @FXML private Text nbclauses;
    @FXML private Text exetime;
    @FXML private Text nbvariables;
    @FXML private Label filenamelable;
    @FXML private ChoiceBox<String> algorithmeBox;
    @FXML private ChoiceBox<String> chartBox;

    @FXML private ListView<String> solutionlist;
    //Myclass simple structure to hold the row values of Cnf Files
    @FXML   private TableView<Myclass> table;
    @FXML   private TableColumn<Myclass,String> c1 ;
    @FXML   private TableColumn<Myclass, String> c2 ;
    @FXML   private TableColumn<Myclass,String> c3 ;

    @FXML private CategoryAxis x=new CategoryAxis();
    @FXML private NumberAxis y=new NumberAxis();
    @FXML private CategoryAxis xr=new CategoryAxis();
    @FXML private NumberAxis yr=new NumberAxis();
    @FXML   private BarChart<String,Number> bch=new BarChart<String, Number>(x,y);
    @FXML   private BarChart<String,Number> bch_rate=new BarChart<String, Number>(xr,yr);

    @FXML private Label par1;
    @FXML private Label par2;
    @FXML private Label par3;
    @FXML private Label par4;
    @FXML private Label par5;

    @FXML private TextField param1;
    @FXML private TextField param2;
    @FXML private TextField param3;
    @FXML private TextField param4;
    @FXML private TextField param5;


    /********chart**********/
/*
    static BarChart<String,Number> bc =
            new BarChart<>(xAxis,yAxis);
    private static HashMap<String,Number> chartData=new HashMap<>();*/
    public static XYChart.Series <String, Number> series_dfs = new XYChart.Series<>();
    public static XYChart.Series <String, Number> series_bfs= new XYChart.Series<>();
    public static XYChart.Series <String, Number> series_pso = new XYChart.Series<>();
    public static XYChart.Series <String, Number> series_ga = new XYChart.Series<>();
    public static XYChart.Series <String, Number> series_a = new XYChart.Series<>();
    public static XYChart.Series <String, Number> series_bso = new XYChart.Series<>();
    /////////////////////////pour sat rate
    public static XYChart.Series <String, Number> rate_dfs = new XYChart.Series<>();
    public static XYChart.Series <String, Number> rate_bfs= new XYChart.Series<>();
    public static XYChart.Series <String, Number> rate_pso = new XYChart.Series<>();
    public static XYChart.Series <String, Number> rate_ga = new XYChart.Series<>();
    public static XYChart.Series <String, Number> rate_a = new XYChart.Series<>();
    public static XYChart.Series <String, Number> rate_bso = new XYChart.Series<>();



    //Contains the path to the file
    public String filePath;

    //Container contains all the rows structured
    public ArrayList<Myclass> container = new ArrayList<>();

    //number of variables in the whole File Todo: check this one
    public int nbvar = -1 ;

    //the appropriate structure to populate TableVeiw table
    public ObservableList<Myclass> m = FXCollections.observableArrayList();

    public cnf cnfObject ;
    public SolutionFormat solution = new SolutionFormat();
    public long timer;


    //Initializing columns to the appropriate type of values
    //DO NOT USE PropretyValueFactory to initialize !
    @Override
    public void initialize(URL url, ResourceBundle rb){
        c1.setCellValueFactory( data -> data.getValue().getfirst());
        c2.setCellValueFactory( data -> data.getValue().getsecond());
        c3.setCellValueFactory( data -> data.getValue().getthird());
        algorithmeBox.getItems().addAll("PSO","BSO","GA","BFS","DFS","A*");
        chartBox.getItems().addAll("Satisfiability","Temps d'execution");

        chartBox.setVisible(false);


        series_dfs.setName("DFS");
        series_bfs.setName("BFS");
        series_ga.setName("GA");
        series_a.setName("A*");
        series_pso.setName("PSO");
        series_bso.setName("BSO");


        rate_dfs.setName("DFS");
        rate_bfs.setName("BFS");
        rate_ga.setName("GA");
        rate_a.setName("A*");
        rate_pso.setName("PSO");
        rate_bso.setName("BSO");


        x.setLabel("Algo");
        y.setLabel("Execution Time");
        bch.getData().addAll(series_pso,series_ga,series_bso,series_dfs,series_bfs,series_a);
        bch_rate.getData().addAll(rate_dfs,rate_bfs,rate_ga,rate_a,rate_pso,rate_bso);
        bch_rate.setVisible(false);

        setLisibile(false);





    }
    public void parameters()
    {

        if(algorithmeBox.getValue().equals("PSO")) {
            setLisibile(true);
            par1.setText("Particles :");
            par2.setText("Cnst1 :");
            par3.setText("Cnst2 :");
            par4.setText("Weight :");
            par5.setText("Iterations :");
            param1.setText("60");
            param2.setText("1");
            param3.setText("0.5");
            param4.setText("10");
            param5.setText("100");

        }

        if(algorithmeBox.getValue().equals("BSO")) {
            setLisibile(true);
            par1.setText("Bees :");
            par2.setText("Flip :");
            par3.setText("Max Chances :");
            par4.setText("n°Local Searches :");
            par5.setText("Iterations :");
            param1.setText("10");
            param2.setText("15");
            param3.setText("5");
            param4.setText("5");
            param5.setText("300");
        }
    }

    public void lanchChart()
    {
        if(algorithmeBox.getValue()!=null && !filenamelable.equals("")) {
            try {
                compute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
            filePath = f.getAbsolutePath();
            readfile(f);
            populate();
            table.getItems().removeAll();
            table.setItems(m);
        }
    }

    /*
    reads the whole file
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
    ALL cnf file clauses are in the container (ignores the number of closes present on the file)
     */
    public void populate(){
        nbclauses.setText("Nbclauses : " + container.size());
        nbvariables.setText("Nbvariables : "+ nbvar);
        for (Myclass myclass : container) {
            //uncomment to check the values in container
            //System.out.print(myclass.getthird() + " " + myclass.getsecond() + " " + myclass.getthird());
            //System.out.println(myclass.getthird().get() + " " + myclass.getsecond().get() + " " + myclass.getthird().get());
            m.add(new Myclass(myclass.getfirst().get(), myclass.getsecond().get(), myclass.getthird().get()));
        }

    }

    /*
    lunches solving methods according to the chosen algorithm
    updates the values of exeTime and solutionlist
     */
    public void compute() throws IOException {

//F initialisation



        System.out.println("Computing ...");
        if(algorithmeBox.getValue().equals("BFS")){
            setLisibile(false);
            bfsAction();
            /***chart*****/
            if(chartBox.getValue().equals("Satisfiability"))
            {
                bch.setVisible(false);
                bch_rate.setVisible(true);
                rate_bfs.getData().add(new XYChart.Data("", Double.valueOf(solution.getSatRate())));

            }
            if(chartBox.getValue().equals("Temps d'execution"))
            {
             //   bch.getData().addAll(series_bfs);
                bch_rate.setVisible(false);
                bch.setVisible(true);
                series_bfs.getData().add(new XYChart.Data("", Double.valueOf(solution.getTime())));

            }



        }
        if(algorithmeBox.getValue().equals("DFS")){
            setLisibile(false);

            dfsAction();
            /***chart*****/
            if(chartBox.getValue().equals("Satisfiability"))
            {
                bch.setVisible(false);
                bch_rate.setVisible(true);
                rate_dfs.getData().add(new XYChart.Data("", Double.valueOf(solution.getSatRate())));

            }
            if(chartBox.getValue().equals("Temps d'execution"))
            {
               // bch.getData().addAll(series_dfs);
                bch_rate.setVisible(false);
                bch.setVisible(true);
                series_dfs.getData().add(new XYChart.Data("", Double.valueOf(solution.getTime())));

            }

        }
        if(algorithmeBox.getValue().equals("A*")){
            setLisibile(false);

            aAction();
            /***chart*****/
            if(chartBox.getValue().equals("Satisfiability"))
            {

            }
            if(chartBox.getValue().equals("Temps d'execution"))
            {
               // bch.getData().addAll(series_a);
                series_a.getData().add(new XYChart.Data("", Double.valueOf(solution.getTime())));

            }


        }
        if(algorithmeBox.getValue().equals("PSO")){
            setLisibile(true);
            psoAction();
            if(chartBox.getValue().equals("Satisfiability"))
            {
                bch.setVisible(false);
                bch_rate.setVisible(true);
                rate_pso.getData().add(new XYChart.Data("", Double.valueOf(solution.getSatRate())));

            }
            if(chartBox.getValue().equals("Temps d'execution"))
            {
              //  bch.getData().addAll(series_pso);
                bch_rate.setVisible(false);
                bch.setVisible(true);
                series_pso.getData().add(new XYChart.Data("", Double.valueOf(solution.getTime())));

            }
        }
        if(algorithmeBox.getValue().equals("BSO")){
            setLisibile(true);
            bsoAction();
            if(chartBox.getValue().equals("Satisfiability"))
            {

            }
            if(chartBox.getValue().equals("Temps d'execution"))
            {
               // bch.getData().addAll(series_bso);
                bch_rate.setVisible(false);
                bch.setVisible(true);
                series_bso.getData().add(new XYChart.Data("", Double.valueOf(solution.getTime())));

            }
        }

        if(algorithmeBox.getValue().equals("GA")) {
            setLisibile(false);

            gaAction();
            /***chart*****/
            if (chartBox.getValue().equals("Satisfiability")) {
                bch.setVisible(false);
                bch_rate.setVisible(true);
                rate_dfs.getData().add(new XYChart.Data("", Double.valueOf(solution.getSatRate())));

            }
            if (chartBox.getValue().equals("Temps d'execution")) {
                // bch.getData().addAll(series_dfs);
                bch_rate.setVisible(false);
                bch.setVisible(true);
                series_ga.getData().add(new XYChart.Data("", Double.valueOf(solution.getTime())));

            }
        }
            System.out.println("Time: " + solution.getTime());
        System.out.println("Values: "+ solution.getSolutionValues());
        // TODO: set values in solutionlist
        exetime.setText(solution.getTime() + "ms");
        solutionlist.getItems().addAll(solution.getSolutionValues());
        System.out.println("Computing finished !");
    }





    // algoBox = bfs
    public void bfsAction(){
        cnfObject = new cnf(filePath);
        timer = System.currentTimeMillis();
       // ArrayList<Integer> s = BreadthFirstSearch(cnfObject).getSolution();
        ArrayList<Integer> s = BreadthFirstSearch(cnfObject).getSolution();
        solution.setTime(timer+"");
        solution.setSolutionValues(new ArrayList<String>());
        for (Integer i : s){
            int j = i>0 ? 1: 0;
            solution.addSolutionValues("X" + abs(i) + ": " + j);
        }
    }

    //algoBox = DFS
    public void dfsAction(){
        cnfObject = new cnf(filePath);
        classeMain dfs = new classeMain(cnfObject);
        ArrayList<Integer> SATGlobal = new ArrayList<>();
        dfs.startTime = System.currentTimeMillis();
        dfs.DFS(0,new ArrayList<>(),0,SATGlobal);
        solution.setTime(dfs.solution.getTime());
        solution.setSatRate(dfs.solution.getSatRate());
        solution.setSolutionValues(dfs.solution.getSolutionValues());
    }

    //algoBox = A*
    public void aAction() throws IOException {
        mainA solver = new mainA(filePath);
        solution = new SolutionFormat(solver.startA());

    }

    // algoBox = bso
    public void bsoAction(){
        cnfObject = new cnf(filePath);
        timer = System.currentTimeMillis();
        // ArrayList<Integer> s = BreadthFirstSearch(cnfObject).getSolution();


        ArrayList<Integer> s = BSO.searchBSO(cnfObject,Integer.valueOf(param2.getText()),Integer.valueOf(param1.getText()),
                Integer.valueOf(param3.getText()),Integer.valueOf(param4.getText()),Integer.valueOf(param5.getText())).getSolution();


        solution.setTime(System.currentTimeMillis()-timer +"");
        solution.setSolutionValues(new ArrayList<String>());
        for (Integer i : s){
            int j = i>0 ? 1: 0;
            solution.addSolutionValues("X" + abs(i) + ": " + j);
        }
    }



    // algoBox = pso
    public void psoAction(){
        cnfObject = new cnf(filePath);
        timer = System.currentTimeMillis();
        // ArrayList<Integer> s = BreadthFirstSearch(cnfObject).getSolution();
        ArrayList<Integer> s = PSO(cnfObject,Float.valueOf(param2.getText()),Float.valueOf(param3.getText()),
                Integer.valueOf(param1.getText()),Integer.valueOf(param4.getText()),Integer.valueOf(param5.getText())).getSolution();

        solution.setTime(System.currentTimeMillis()-timer +"");
        solution.setSolutionValues(new ArrayList<String>());
        for (Integer i : s){
            int j = i>0 ? 1: 0;
            solution.addSolutionValues("X" + abs(i) + ": " + j);
        }


    }

    public void gaAction(){
        int taillePopulation=50; //taille de population
        double Pc=1;             //taux de croisement
        double Pm=0.9;           //taux de mutation
        long startTime, stopTime;
        Population populationSAT = new Population(filePath);
        startTime  = System.currentTimeMillis();
        populationSAT.GeneticAlgorithm(taillePopulation,Pc, Pm);
        stopTime  = System.currentTimeMillis();
        solution = new SolutionFormat(populationSAT.SGbest.chromosome,String.valueOf(stopTime-startTime));
        System.out.println(solution);
    }
    public Solution BreadthFirstSearch(cnf clset) {////, long execTimeMillis) {
        LinkedList<Solution> open = new LinkedList<Solution>();
        Solution currentSol = new Solution(clset.getNombreVariables());


        Solution bestSolution = new Solution(currentSol);

        int randomLiteral;
        int nbsol=0;

        long startTime = System.currentTimeMillis(); /* Save the start time of the search */

        do {
            if (!open.isEmpty())
                currentSol = open.removeFirst();

            if (currentSol.getActiveLiterals() == clset.getNombreVariables())
                continue;

            randomLiteral = currentSol.randomLiteral(clset.getNombreVariables());

            for (int i = 0; i < 2; i++) { /* Loop TWO times for the chosen literal (left child) and its negation (right child) */
                currentSol.changeLiteral(Math.abs(randomLiteral) - 1, i == 0 ? randomLiteral : -randomLiteral);

                if (currentSol.satisfiedClauses(clset) > bestSolution.satisfiedClauses(clset)) {
                    bestSolution = new Solution(currentSol); /* If current solution is better, update the best solution */
                nbsol++;}
                if (currentSol.isSolution(clset)) /* If this solution satisfies all clauses in "clset", return it */
                    return bestSolution;

                open.add(new Solution(currentSol));
            }
        } while (!open.isEmpty());
        timer = System.currentTimeMillis() -  startTime ;
        bestSolution.setSatRate(100*nbsol/clset.getNombreClauses());
        return bestSolution;

    }


    private Solution PSO (cnf cnf,float c1,float c2,int numParticle,int weight,int ittr)
    {
        ArrayList<particle> particles = new ArrayList<particle>();

        int nbsol=0;
        Solution gbest=new Solution(cnf.getNombreVariables());//initialize gbest

        for(int i=0; i<numParticle; i++) {
            particle p=new particle();

            //initialize the particle
            //for (int j=0;j<cnf.getNombreVariables();j++)
            //{
               // p.setPbest(new Solution(cnf.getNombreVariables()));
                p.setCurrent_solution(new Solution(cnf.getNombreVariables()));
                /************generate a random solution as pbest****/
                for(int k=0; k<p.getCurrent_solution().getSolutionSize(); k++) {
                    int literalValue = (int) (Math.random()*10)%2;

                    p.getCurrent_solution().getSolution().set(k, ((k+1) * (literalValue == 0 ? -1 : 1)));
                }
                p.setPbest(p.getCurrent_solution());

               Random r=new Random();
               p.setVbest( r.nextInt(cnf.getNombreVariables()));

                particles.add(p);


                if(gbest.satisfiedClauses(cnf) < particles.get(i).getCurrent_solution().satisfiedClauses(cnf)) {
                    gbest = particles.get(i).getCurrent_solution();
                    nbsol++;
                }


                System.out.println("gbbst" + gbest);

        //}



            for(int c=0; c<ittr; c++) {

                for(particle particle : particles) {
                    /******update velocity************/
                    //v( t+1 )=
                    //w *v( t )+c 1 r 1 Pbest- x(t))+ c 2 r 2 Gbest -x(t))

                    Integer diffp;
                    Integer diffg;
                    int numDiffp=0;
                    int numDiffg=0;

                    /******pbest -x(t)***/
                    if(particle.getPbest().getSolutionSize() != particle.getCurrent_solution().getSolutionSize())
                        diffp= null; /* If the size of the two solutions isn't the same, we cannot compare them */

                    for(int ii=0; ii<particle.getCurrent_solution().getSolutionSize(); ii++)
                        if( particle.getPbest().getSolution().get(ii)!= particle.getCurrent_solution().getSolution().get(ii))
                            numDiffp++; /* Count number of different positions */

                    diffp= numDiffp;

                    /******gbest -x(t)***/
                    if(gbest.getSolutionSize() != particle.getCurrent_solution().getSolutionSize())
                        diffp= null; /* If the size of the two solutions isn't the same, we cannot compare them */

                    for(int ii=0; ii<particle.getCurrent_solution().getSolutionSize(); ii++)
                        if( gbest.getSolution().get(ii)!= particle.getCurrent_solution().getSolution().get(ii))
                            numDiffg++; /* Count number of different positions */

                    diffg= numDiffg;

                    /********************/
                    double v=weight* particle.vbest  + c1*  Math.random() *diffp +c2*Math.random()*diffg;

                    /***********************
                     * update position
                     *********************/
                    int position = 0;
                    ArrayList<Integer> avaibleLiterals = new ArrayList<Integer>(); /* List of literals that can be reversed */
                    Random random = new Random();

                    for(int il=0; il<particle.getCurrent_solution().getSolutionSize(); il++) /* Initialize the list (all literals can be reversed) */
                        avaibleLiterals.add(il);

                    for(int il=0; il<particle.getVbest(); il++) /* Reverse literals (chosen randomly) and delete them from "avaibleLiterals" (to not choose them again) */
                         position=avaibleLiterals.remove(random.nextInt(avaibleLiterals.size()));
                            {
                        if ((position < 0) || (position >= particle.getCurrent_solution().getSolutionSize())) /* Error : index out of array's bounds */


                            particle.getCurrent_solution().getSolution().set(position, -particle.getCurrent_solution().getSolution().get(position));
                    }





                    /**********************
                     *update PBEST
                     * ******************/
                    for(int k=0; k<particle.getVbest(); k++)

                        if(particle.getCurrent_solution().satisfiedClauses(cnf) > particle.getPbest().satisfiedClauses(cnf))
                            particle.setPbest(particle.getCurrent_solution());

                }

                for(particle particle : particles)
                    if(gbest.satisfiedClauses(cnf) < particle.getCurrent_solution().satisfiedClauses(cnf))
                        gbest = particle.getPbest();

                if(gbest.isSolution(cnf))
                    break;
            }
        }

        gbest.setSatRate( 100*nbsol/cnf.getNombreClauses());
      return gbest;
    }


    public void setLisibile(boolean l){
        par1.setVisible(l);
        par2.setVisible(l);
        par3.setVisible(l);
        par4.setVisible(l);
        par5.setVisible(l);
        param1.setVisible(l);
        param2.setVisible(l);
        param3.setVisible(l);
        param4.setVisible(l);
        param5.setVisible(l);

    }

}
