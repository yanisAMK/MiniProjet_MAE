import java.util.ArrayList;

public class SolutionFormat {
    /*
        Normalize sollutions with this class
        each sollution has two attributes
        1-Execution time : exeTime  type: String
        2-an array of sollutions : solutionValues  type: ArrayList<String>
     */
    private String exeTime;
    private float satRate;
    private ArrayList<String> solutionValues;

    public SolutionFormat(ArrayList<Integer> solutionValues,String time) {
        this.exeTime = time;
        this.solutionValues=new ArrayList<>();
        for(Integer element:solutionValues)
            this.solutionValues.add(String.valueOf(element));
    }
    public String getTime() {
        return exeTime;
    }
    public SolutionFormat(SolutionFormat s){
        this.setTime(s.getTime());
        this.setSolutionValues(s.getSolutionValues());
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

    public float getSatRate() {
        return satRate;
    }

    public void setSatRate(float satRate) {
        this.satRate = satRate;
    }
}
