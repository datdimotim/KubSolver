package kub.kubSolver.solvers;

import kub.kubSolver.Tables;

public interface BaseFaseSolver {
    int MAX_DEEP=30;
    void init(Tables tables);
    void solve(int x,int y,int z,int[] hods);
}
