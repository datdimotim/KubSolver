package com.dimotim.kubSolver.solvers;

import com.dimotim.kubSolver.kernel.Fase2Solver;
import com.dimotim.kubSolver.kernel.HodTransforms;
import com.dimotim.kubSolver.kernel.Tables;
import io.reactivex.Observable;

import static com.dimotim.kubSolver.kernel.Fase1Solver.MAX_DEEP;
import static com.dimotim.kubSolver.kernel.HodTransforms.hodPredHod1Fase;
import static com.dimotim.kubSolver.kernel.HodTransforms.hodPredHod2Fase;
import static com.dimotim.kubSolver.solvers.SimpleSolver1.bactracking;

public final class SimpleSolver2<KS> implements Fase2Solver<KS> {
    private Tables<KS> tables;
    @Override
    public void init(Tables<KS> tables){
        this.tables=tables;
    }
    @Override
    public boolean solve(int x, int y, int z, int[] hods) {
        if(true){
            solveStream(x,y,z,hods);
            return true;
        }

        if(hods.length==0)return tables.getDepthInState(tables.initKubStateFase1(x,y,z))==0;
        KS[] state=tables.newArrayKubState(hods.length);
        for(int i=0;i<state.length;i++)state[i]=tables.newKubState();
        state[0]= tables.initKubStateFase2(x,y,z);
        if(tables.getDepthInState(state[0])==0)return true;
        int deep=1;
        mega: while(deep<hods.length) {
            for(int np = hods[deep];np<=10;np++) {
                if(!hodPredHod2Fase(np,hods[deep-1]))continue;
                if (tables.moveAndGetDepthFase2(state[deep-1],state[deep],np)<=hods.length-deep-1) {
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
        return tables.getDepthInState(state[state.length-1])==0;
    }

    public void solveStream(int x,int y, int z, int[] hods) {
        KS startState=tables.initKubStateFase2(x,y,z);

        int length=tables.moveAndGetDepthFase2(startState,startState,0);

        St solution=bactracking(
                new St(null,startState,length,0,0),
                root-> Observable.range(0, 11)
                        .filter(i->hodPredHod2Fase(i,root.predHod))
                        .map(hod->{
                            KS out=tables.newKubState();
                            int len=tables.moveAndGetDepthFase2(root.state,out,hod);
                            return new St(root,out,len,hod,root.depth+1);
                        })
                        .filter(st->st.moveCount<=MAX_DEEP-st.depth),
                e->true,
                pos-> pos.moveCount==0
        )
                .blockingFirst();
        //System.out.println(solution);
        St st=solution;
        int i=0;
        while (true){
            //state.state[state.state.length-1-i]=st.state;
            if(st.predHod==0)break;
            hods[hods.length-1-i]=st.predHod;
            i++;
            st=st.prev;
            if(st==null)break;
        }
    }

    private class St{
        final St prev;
        final KS state;
        final int moveCount;
        final int predHod;
        final int depth;

        public St(St prev, KS state, int moveCount, int predHod, int depth) {
            this.prev=prev;
            this.state = state;
            this.moveCount=moveCount;
            this.predHod = predHod;
            this.depth=depth;
        }
    }
}
