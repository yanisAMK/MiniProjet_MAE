public class particle {


    Solution current_solution,pbest;
    int vbest;


    public Solution getCurrent_solution() {
        return current_solution;
    }

    public void setCurrent_solution(Solution current_solution) {
        this.current_solution = current_solution;
    }

    public Solution getPbest() {
        return pbest;
    }

    public void setPbest(Solution pbest) {
        this.pbest = pbest;
    }

    public int getVbest() {
        return vbest;
    }

    public void setVbest(int vbest) {
        this.vbest = vbest;
    }
}
