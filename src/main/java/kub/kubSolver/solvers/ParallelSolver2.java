package kub.kubSolver.solvers;

import kub.kubSolver.Tables;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelSolver2 extends Fase2Solver.AbstractSolver2{
    public ParallelSolver2(){

    }
    @Override
    public void solve(int x, int y, int z, int[] hods) {
        new Fase2Parallel().fase2(x,y,z,hods);
    }
    private class Fase2Parallel {
        private static final int T1 = 11;
        private static final int T2 = 14;
        private static final int T3 = 2;
        private volatile boolean isStopped = false;

        void fase2(int x, int y, int z, int[] hods) {
            int[] res = ParallelSolver1.commonPool.invoke(new RecursiveTaskFase2(hods, x, y, z, 1));
            System.arraycopy(res, 0, hods, 0, res.length);
        }

        private class RecursiveTaskFase2 extends RecursiveTask<int[]> {
            private final int[] hods;
            private final int x;
            private final int y;
            private final int z;
            private final int deep;

            RecursiveTaskFase2(int[] hods, int x, int y, int z, int deep) {
                this.hods = new int[hods.length];
                this.x = x;
                this.y = y;
                this.z = z;
                this.deep = deep;
                System.arraycopy(hods, 0, this.hods, 0, hods.length);
            }

            @Override
            protected int[] compute() {
                if (fase2Recurcive(x, y, z, hods, deep)) return hods;
                else return null;
            }

            private boolean fase2Recurcive(final int x, final int y, final int z, final int[] hods, final int deep) {
                final RecursiveTaskFase2[] child = new RecursiveTaskFase2[11];
                for (int np = hods[deep]; np <= 10; np++) {
                    if (isStopped) break;
                    if(!hodPredHod(np,hods[deep-1]))continue;
                    int xt = x2Move[np][x];
                    int yt = y2Move[np][y];
                    int zt = z2Move[np][z];
                    if (!(xz2Deep[xt][zt] > Tables.MAX_DEEP - deep || yz2Deep[yt][zt] > Tables.MAX_DEEP - deep)) {
                        hods[deep] = np;
                        if (deep == Tables.MAX_DEEP) {
                            isStopped = true;
                            return true;
                        } else {
                            if (Tables.MAX_DEEP - deep < T1 || Tables.MAX_DEEP - deep > T2 || getQueuedTaskCount() > T3) { //hods_t==0 тормозит
                                if (fase2Recurcive(xt, yt, zt, hods, deep + 1)) return true; // sequence
                            } else {
                                child[np] = new RecursiveTaskFase2(hods, xt, yt, zt, deep + 1);
                                child[np].fork();
                            }
                        }
                    }
                }
                for (RecursiveTaskFase2 task : child) {
                    if (task == null) continue;
                    int[] res = task.join();
                    if (res == null) continue;
                    System.arraycopy(res, 0, hods, 0, hods.length);
                    return true;
                }
                hods[deep] = 0;
                return false;
            }
        }
    }
}