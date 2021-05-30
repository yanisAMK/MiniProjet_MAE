import java.util.ArrayList;

public class SolutionFormat {
    /*
        Normalize sollutions with this class
        each sollution has two attributes
        1-Execution time : exeTime  type: String
        2-an array of sollutions : solutionValues  type: ArrayList<String>
     */
    private String exeTime;
    private ArrayList<String> solutionValues;

    public String getTime() {
        return exeTime;
    }

    public SolutionFormat(String time, ArrayList<String> solutionValues) {
        this.exeTime = time;
        this.solutionValues = solutionValues;
    }
    public SolutionFormat() {
    }
    public void setTime(String time) {
        this.exeTime = time;
    }

    public ArrayList<String> getSolutionValues() {
        return solutionValues;
    }
    public void addSolutionValues(String x){
        this.getSolutionValues().add(x);
    }

    public void setSolutionValues(ArrayList<String> solutionValues) {
        this.solutionValues = solutionValues;
    }
}
