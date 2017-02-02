package kub.kubSolver.solvers;

public class RecursiveSolver1 extends Fase1Solver.AbstractSolver1{
    public RecursiveSolver1(){

    }
    @Override
    public void solve(int x, int y, int z, int[] hods) {/*
        int[][] koords_tmp=new int[MAX_DEEP +1][3];
        koords_tmp[0][0]=x;
        koords_tmp[0][1]=y;
        koords_tmp[0][2]=z;
        fase1Recurcive(koords_tmp,hods, 1);
    }
    private boolean fase1Recurcive(int[][] koords_tmp,int[] hods,int deep){
        for(int np = hods[deep];np<=18;np++) {
            if(!hodPredHod(np,hods[deep-1]))continue;
            if (tables.tryMoveAndGetDepth1(koords_tmp[deep-1],np)> MAX_DEEP - deep) continue;
            hods[deep]=np;
            if(deep==MAX_DEEP)return true;
            else {
                tables.move1(koords_tmp[deep-1],koords_tmp[deep],np);
                if(fase1Recurcive(koords_tmp, hods, deep + 1))return true;
            }
        }
        hods[deep]=0;
        return false;*/
    }
}
