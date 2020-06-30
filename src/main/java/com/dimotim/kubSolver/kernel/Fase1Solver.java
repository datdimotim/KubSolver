package com.dimotim.kubSolver.kernel;

public interface Fase1Solver<KubState,SolveState>{
    int MAX_DEEP=30;
    void init(Tables<KubState> tables);
    SolveState initSolveState(int x,int y,int z);
    void solve(SolveState lastSolveState);
    int[] getResultFromSolveState(SolveState state);
}
