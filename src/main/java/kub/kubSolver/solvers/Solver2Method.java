package kub.kubSolver.solvers;

import java.util.Arrays;

import static kub.kubSolver.Tables.MAX_DEEP;

public class Solver2Method extends Fase2Solver.AbstractSolver2{
    public Solver2Method(){

    }
    @Override
    public void solve(int x, int y, int z, int[] hods) {
        Arrays.fill(hods,0);
        int pos[][]=new int[MAX_DEEP+1][3];
        pos[0][0]=x;
        pos[0][1]=y;
        pos[0][2]=z;

        int deep=1;
        mega: while(deep<= MAX_DEEP) {
            for(int np = hods[deep];np<=10;np++) {
                if(!hodPredHod(np,hods[deep-1]))continue;
                move(pos[deep-1],pos[deep],np);
                if (testPos(pos[deep],deep)){
                    hods[deep] = np;
                    deep++;
                    continue mega;
                }
            }
            hods[deep]=0;
            deep--;
            hods[deep]++;
        }
    }
    private void move(int[] inPos,int[] outPos, int n){
        outPos[0] = x2Move[n][inPos[0]];
        outPos[1] = y2Move[n][inPos[1]];
        outPos[2] = z2Move[n][inPos[2]];
    }
    private boolean testPos(int[] pos, int deep){
        return !(xz2Deep[pos[0]][pos[2]]> MAX_DEEP - deep||yz2Deep[pos[1]][pos[2]]> MAX_DEEP - deep);
    }
}
