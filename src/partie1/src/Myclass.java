import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
//not fancy but worky
public class Myclass{
    public StringProperty first;
    public StringProperty second;
    public StringProperty third;

    public Myclass(String first, String second, String third){
        this.first = new SimpleStringProperty(first);
        this.second= new SimpleStringProperty(second);
        this.third = new SimpleStringProperty(third);
    }
    public StringProperty getfirst(){
        return this.first;
    }
    public StringProperty getsecond(){
        return this.second;
    }
    public StringProperty getthird(){
        return this.third;
    }
}