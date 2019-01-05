package com.dimotim.kubSolver.tables;

import com.dimotim.kubSolver.Kub2x2;
import com.dimotim.kubSolver.Solution;
import com.dimotim.kubSolver.kernel.Cubie;
import com.dimotim.kubSolver.kernel.CubieKoordinateConverter;
import com.dimotim.kubSolver.kernel.GraniCubieConverter;
import com.dimotim.kubSolver.test.SizeOf;


import java.io.*;
import java.util.Arrays;

import static com.dimotim.kubSolver.kernel.Tables.X_1_MAX;
import static com.dimotim.kubSolver.kernel.Tables.X_2_MAX;

public class Complex2x2Tables implements Serializable{

    // run first for initialization
    public static void main(String[] args)throws IOException {
        Complex2x2Tables tables=new Complex2x2Tables();
        ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("src/main/resources/tables2x2.object"));
        oos.writeObject(tables);
        oos.close();
    }

    public static Complex2x2Tables readTables(){
        try(InputStream fis = SymTables.class.getResourceAsStream("/tables2x2.object")) {
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (Complex2x2Tables) ois.readObject();
        }
        catch (IOException | ClassNotFoundException e){
            throw new RuntimeException("Can't read tables",e);
        }
    }
    public Complex2x2Tables(){
        x1move=createX1Move();
        x2move=createX2Move();
        x1deep=createDeepTable(x1move);
        x2deep=createDeepTable(x2move);
    }
    private char[][] x1move;
    private char[][] x2move;
    private byte[] x1deep;
    private byte[] x2deep;

    static char[][] createX1Move(){
        int[] u_o=new int[8];
        char[][] table=new char[19][X_1_MAX];
        for(int pos = 0; pos< X_1_MAX; pos++){
            for(int pov=0;pov<19;pov++){
                Cubie.povorotUO(CubieKoordinateConverter.x1ToCubie(pos),u_o,pov);
                table[pov][pos]= (char) CubieKoordinateConverter.uoToX1(u_o);
            }
        }
        return table;
    }
    public static char[][] createX2Move(){
        int[] u_p=new int[8];
        char[][] table=new char[19][X_2_MAX];
        for(int pos = 0; pos< X_2_MAX; pos++){
            for(int pov=0;pov<19;pov++){
                Cubie.povorotUP(CubieKoordinateConverter.x2ToCubie(pos),u_p,pov);
                table[pov][pos]= (char) CubieKoordinateConverter.upToX2(u_p);
            }
        }
        return table;
    }

    private byte[] createDeepTable(char[][] move){
        byte[] deep=new byte[move[0].length];
        Arrays.fill(deep,(byte) 20);
        deep[0]=0;
        for(int d=0;d<20;d++){
            for(int p=0;p<deep.length;p++){
                if(deep[p]!=d)continue;
                for (int np=0;np<move.length;np++){
                    if(deep[move[np][p]]>d+1)deep[move[np][p]]=(byte)(d+1);
                }
            }
        }
        return deep;
    }

    public Solution solve(Kub2x2 kub2x2){
        int[] hods=new int[20];
        int[][][] grani=kub2x2.getGrani3x3();
        int x1= CubieKoordinateConverter.uoToX1(GraniCubieConverter.graniToUO(grani));
        int x2=CubieKoordinateConverter.upToX2(GraniCubieConverter.graniToUP(grani));
        if(!solve(x1,x2,hods))throw new RuntimeException();
        return new Solution(1,hods,new int[0]);
    }

    private boolean solve(int x1, int x2, int[] hods) {
        int[][] state=new int[hods.length][2];
        state[0][0]=x1;
        state[0][1]=x2;
        if(Math.max(x1deep[x1],x2deep[x2])==0)return true;
        int deep=1;
        mega: while(deep<hods.length) {
            for(int np = hods[deep];np<=18;np++) {
                if(!hodPredHod(np,hods[deep-1]))continue;
                state[deep][0]=x1move[np][state[deep-1][0]];
                state[deep][1]=x2move[np][state[deep-1][1]];
                if (Math.max(x1deep[state[deep][0]],x2deep[state[deep][1]])<=hods.length-deep-1) {
                    hods[deep] = np;
                    deep++;
                    continue mega;
                }
            }
            hods[deep]=0;
            deep--;
            if(deep<1)return false;
            hods[deep]++;
        }
        return Math.max(x1deep[state[state.length-1][0]],x2deep[state[state.length-1][1]])==0;
    }

    private static boolean hodPredHod(int hod,int predHod){
        if(predHod!=0& hod ==0)return false;
        if(predHod!=0) {
            if ((predHod - 1) / 3==(hod - 1) / 3)return false;
            if ((predHod - 1) / 3==0& (hod - 1) / 3==5)return false;
            if ((predHod - 1) / 3==1&(hod - 1) / 3==4)return false;
            if ((predHod - 1) / 3==2&(hod - 1) / 3==3)return false;
        }
        return true;
    }
}
