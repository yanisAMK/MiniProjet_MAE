/*import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class cnf {
    private ArrayList <List<Integer>> matrice = new ArrayList<>();
    private int nombreVariables, nombreClauses;
    //constructeur qui parcour le fichier.cnf et remplit l'attribut 'matrice' qui contient
    // toutes les clauses du probleme SAT, ainsi que le nombre de clauses et le nombre de variables.
    public cnf(String file) {
        ArrayList <String> satInformations = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            boolean bool1=true, bool2=true;
            Pattern p = Pattern.compile("p cnf");
            Pattern p1 = Pattern.compile("-?[0-9]+");
            while((line = br.readLine()) != null && bool1) {
                Matcher m = p.matcher(line);
                Matcher m1 = p1.matcher(line);
                if(m.find()) {
                    bool1 = false;
                    while (m1.find())
                        satInformations.add(m1.group());
                }
            }
            this.nombreVariables = Integer.parseInt(satInformations.get(0));
            this.nombreClauses = Integer.parseInt(satInformations.get(1));
            System.out.println(satInformations);
            int i=0;
            while (line != null && bool2) {
                Matcher m1 = p1.matcher(line);
                //ArrayList <String> allMatches = new ArrayList<>();
                List <Integer> allMatches = new ArrayList<>();
                while (m1.find())
                    if(Integer.parseInt(m1.group())!=0) allMatches.add(Integer.parseInt(m1.group()));
                if(allMatches.isEmpty()) bool2=false;
                else this.matrice.add(allMatches);
                line = br.readLine();
            }
           System.out.println(this.matrice);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<List<Integer>> getMatrice() {
        return matrice;
    }

    public void setMatrice(ArrayList<List<Integer>> matrice) {
        this.matrice = matrice;
    }

    public int getNombreVariables() {
        return nombreVariables;
    }

    public void setNombreVariables(int nombreVariables) {
        this.nombreVariables = nombreVariables;
    }

    public int getNombreClauses() {
        return nombreClauses;
    }

    public void setNombreClauses(int nombreClauses) {
        this.nombreClauses = nombreClauses;
    }
}
*/import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class cnf {
    ArrayList <List<Integer>> matrice = new ArrayList<>();
    int nombreVariables, nombreClauses;
    //constructeur qui parcour le fichier.cnf et remplit l'attribut 'matrice' qui contient
    // toutes les clauses du probleme SAT, ainsi que le nombre de clauses et le nombre de variables.
    public cnf(String file) {
        ArrayList <String> satInformations = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            boolean bool1=true, bool2=true;
            Pattern p = Pattern.compile("p cnf");
           // Pattern p1 = Pattern.compile("-?[1-9]+");
            Pattern p1 = Pattern.compile("-?[1-9]+[0-9]*");
            while((line = br.readLine()) != null && bool1) {
                Matcher m = p.matcher(line);
                Matcher m1 = p1.matcher(line);
                if(m.find()) {
                    bool1 = false;
                    while (m1.find())
                        satInformations.add(m1.group());
                }
            }
            this.nombreVariables = Integer.parseInt(satInformations.get(0));
            this.nombreClauses = Integer.parseInt(satInformations.get(1));
            System.out.println("Nombre de Variables & Nombre de Clauses : "+satInformations);
            int i=0;
            while (line != null && bool2) {
                Matcher m1 = p1.matcher(line);
                //ArrayList <String> allMatches = new ArrayList<>();
                List <Integer> allMatches = new ArrayList<>();
                while (m1.find())
                    allMatches.add(Integer.parseInt(m1.group()));
                if(allMatches.isEmpty()) bool2=false;
                else this.matrice.add(allMatches);
                line = br.readLine();
            }
            System.out.println("Les Clauses du probleme SAT : "+this.matrice);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }



    public ArrayList<List<Integer>> getMatrice() {
        return matrice;
    }

    public void setMatrice(ArrayList<List<Integer>> matrice) {
        this.matrice = matrice;
    }

    public int getNombreVariables() {
        return nombreVariables;
    }

    public void setNombreVariables(int nombreVariables) {
        this.nombreVariables = nombreVariables;
    }

    public int getNombreClauses() {
        return nombreClauses;
    }

    public void setNombreClauses(int nombreClauses) {
        this.nombreClauses = nombreClauses;
    }
}
