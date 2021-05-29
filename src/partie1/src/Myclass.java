import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class Myclass{
    public SimpleStringProperty first;
    public SimpleStringProperty second;
    public SimpleStringProperty third;

    public Myclass(String first, String second, String third){
        this.first = new SimpleStringProperty(first);
        this.second = new SimpleStringProperty(second);
        this.third = new SimpleStringProperty(third);
    }
    public Myclass(String[] lst){
        this.first = new SimpleStringProperty(lst[0]);
        this.second = new SimpleStringProperty(lst[1]);
        this.third = new SimpleStringProperty(lst[3]);
    }


    public String getfirst(){
        return this.first.get();
    }
    public String getsecond(){
        return this.second.get();
    }
    public String getthird(){
        return this.third.get();
    }

    public void setFirst(String first) {
        this.first.set(first);
    }

    public void setSecond(String second) {
        this.second.set(second);
    }

    public void setThird(String third) {
        this.third.set(third);
    }
}
