package com.dimotim.kubSolver.solvers;

import com.dimotim.kubSolver.kernel.Fase1Solver;
import com.dimotim.kubSolver.kernel.Tables;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
//import java.util.stream.Stream;

import static com.dimotim.kubSolver.kernel.HodTransforms.hodPredHod1Fase;

public final class SimpleSolver1<KS> implements Fase1Solver<KS,SimpleSolver1.SolveState<KS>> {
    private Tables<KS> tables;
    @Override
    public void init(Tables<KS> tables) {
        this.tables=tables;
    }

    @Override
    public SolveState<KS> initSolveState(int x, int y, int z) {
        return new SolveState<>(tables,x,y,z);
    }

    @Override
    public void getResultFromSolveState(SolveState<KS> state, int[] hods) {
        System.arraycopy(state.hods,0,hods,0,state.hods.length);
    }

    @Override
    public void solve(SolveState<KS> state) {
        if(true){
            /*
                41tasks/s  results=23.404762
                102tasks/s  results=23.142857
                114tasks/s  results=22.983051
                103tasks/s  results=22.971153
                107tasks/s  results=23.046297
                93tasks/s  results=23.127659
                97tasks/s  results=23.060606
                113tasks/s  results=22.965517
                114tasks/s  results=23.172413
                91tasks/s  results=23.25
                111tasks/s  results=22.955357
                113tasks/s  results=23.298246
                131tasks/s  results=23.083334
             */

            solveStream(state);
            return;
        }
        if(Arrays.stream(state.hods).anyMatch(i->i>0))throw new RuntimeException(Arrays.toString(state.hods));
        while (true) {
            incrementHods1(state.hods);
            int deep = 1;
            mega:
            while (deep <= MAX_DEEP) {
                for (int np = state.hods[deep]; np <= 18; np++) {
                    if (!hodPredHod1Fase(np, state.hods[deep - 1])) continue;
                    if (tables.moveAndGetDepthFase1(state.state[deep - 1], state.state[deep], np) <= MAX_DEEP - deep) {
                        state.hods[deep] = np;
                        deep++;
                        continue mega;
                    }
                }
                state.hods[deep] = 1;
                deep--;
                state.hods[deep]++;
            }
            if(validFase1NewSolution(state.hods))return;
        }
    }

    private static boolean validFase1NewSolution(int[] face1NewSolutuion){
        int[] pureFase1hods={0,4,5,7,8,10,11,13,14}; //0 - т.к. решение не может заканчиваться 0
        for(int j:pureFase1hods)if(face1NewSolutuion[face1NewSolutuion.length-1]==j)return true;
        return false;
    }

    private static void incrementHods1(int[] hods){
        hods[hods.length-1]++;
    }

    public static class SolveState<KS>{
        private final int[] hods;
        private final KS[] state;
        private SolveState(Tables<KS> tables,int x, int y,int z) {
            hods=new int[MAX_DEEP+1];
            hods[hods.length-1]=-1;
            state = tables.newArrayKubState(MAX_DEEP + 1);
            for (int i = 0; i < state.length; i++) state[i] = tables.newKubState();
            state[0] = tables.initKubStateFase1(x, y, z);
        }
    }

    public void solveStream(SolveState<KS> state) {
        KS startState=state.state[0];
        int length=tables.moveAndGetDepthFase1(startState,startState,0);

        St solution=bactracking(
                new St(null,startState,length,0,0),
                root-> Observable.range(0, 19)
                        .filter(i->hodPredHod1Fase(i,root.predHod))
                        .map(hod->{
                            KS out=tables.newKubState();
                            int len=tables.moveAndGetDepthFase1(root.state,out,hod);
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
            state.state[state.state.length-1-i]=st.state;
            state.hods[state.hods.length-1-i]=st.predHod;
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

        public St(St prev, KS state, int moveCount, int predHod,int depth) {
            this.prev=prev;
            this.state = state;
            this.moveCount=moveCount;
            this.predHod = predHod;
            this.depth=depth;
        }
    }

    public static <T> Observable<T> bactracking(T root, Function<T, Observable<T>> childGenerator, Predicate<T> edgeReduser, Predicate<T> solutionValidator){
        return Observable.concat(
                Observable.just(root).filter(solutionValidator::test),
                childGenerator.apply(root)
                        .filter(edgeReduser::test)
                        .flatMap(ch->bactracking(ch,childGenerator,edgeReduser,solutionValidator))
        );
    }
}

