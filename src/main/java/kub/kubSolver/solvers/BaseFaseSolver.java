package kub.kubSolver.solvers;

import kub.kubSolver.Tables;

public interface BaseFaseSolver {
    void init(Tables tables);
    void solve(int x,int y,int z,int[] hods);
}
