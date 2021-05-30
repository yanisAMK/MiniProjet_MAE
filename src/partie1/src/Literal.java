import java.util.Vector;

public class Literal {

    int var;
    int val;

    public Literal(int x){
        var = x;
        val = -1;
    }
    public Literal(Literal l){
        this.var=l.var;
        this.val=l.val;
    }

    public int frequence(sat s) {//calculer le nombre d'occurences d'un litt�ral dans les clauses d'un probleme sat

        int f=0;
        for(int i=0; i<s.ListeClauses.size();i++) {//parcourir toutes les clauses du sat
            for(int j=0; j<s.ListeClauses.get(i).ListeLitteraux.size(); j++) {//parcourir chaque litt�ral de chaque clause
                if(this.var==s.ListeClauses.get(i).ListeLitteraux.get(j).var) {
                    f++;
                }
            }
        }
        return f;
    }


    public static Literal maxfrequence(Vector<Literal> v, sat s){//retourner le litt�ral qui a plus de fr�quence dans les clauses
        int indice =0;
        int freq = v.get(0).frequence(s);
        for(int i=0;i<v.size();i++){
            if(v.get(i).frequence(s)>freq){
                freq = v.get(i).frequence(s);
                indice = i;
            }
        }
        return v.get(indice);
    }

    public static void  removeChoisi(Vector<Literal> v, Literal x){//suppression du litt�ral choisi
        for(int i=0;i<v.size();i++){
            if((v.get(i).var==x.var)){
                v.remove(i);
            }
        }
    }

    public static Literal getContraire(Vector<Literal> v, Literal x){
        for(int i=0;i<v.size();i++){
            if(v.get(i).var==-x.var) return v.get(i);
        }
        return null;
    }

}
