package kub.kubSolver;

import kub.kubSolver.utills.BigDecimalConverter;

import java.math.BigDecimal;
import java.util.Random;

public class Kub{
    private Cubie cubie=new Cubie();
    public Kub(boolean isRandom){if(isRandom)randomPos();}
    public Kub(int[][][] grani) throws InvalidPositionException {
        cubie=new Cubie(grani);
    }
    public Kub(BigDecimal pos){
        cubie=Cubie.valueOf(pos);
    }
    public BigDecimal getNumberPos(){
        return cubie.toNumberPos();
    }
    public void randomPos(){
        cubie=Cubie.valueOf(BigDecimalConverter.randomPos());
    }
    public int[][][] getGrani(){
        return cubie.toGrani();
    }
    public String toString(){
        int[][][] grani=getGrani();
        String str="gran 0         gran 1         gran 2         gran 3         gran 4         gran 5\n";
        for(int i=0;i<3;i++){
            for(int g=0;g<6;g++){
                for(int j=0;j<3;j++){
                    str=str+grani[g][i][j];
                }
                str=str+"            ";
            }
            str=str+"\n";
        }
        return str;
    }
    public void povorot(int np){
        cubie=cubie.povorot(np);
    }
}
