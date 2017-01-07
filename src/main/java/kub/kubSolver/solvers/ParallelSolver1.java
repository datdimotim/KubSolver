package kub.kubSolver.solvers;
import kub.kubSolver.Tables;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelSolver1 extends Fase1Solver.AbstractSolver1{
    static ForkJoinPool commonPool=new ForkJoinPool();
    public ParallelSolver1(){

    }
    @Override
    public void solve(int x, int y, int z, int[] hods) {
        new Fase1Parallel().fase1(x,y,z,hods);
    }
    private class Fase1Parallel {
        private static final int T1=9;
        private volatile boolean isStopped=false;
        void fase1(int x, int y, int z, int[] hods){
            int[] res= commonPool.invoke(new RecursiveTaskFase1(hods,x,y,z,1, true));
            System.arraycopy(res,0,hods,0,res.length);
        }
        private class RecursiveTaskFase1 extends RecursiveTask<int[]> {
            private final int[] hods;
            private final int x,y,z,deep;
            private final boolean repeat;
            RecursiveTaskFase1(int[] hods, int x, int y, int z, int deep, boolean repeat) {
                this.x = x;this.y = y;this.z = z;this.deep = deep;this.repeat = repeat;
                this.hods = new int[hods.length];
                System.arraycopy(hods, 0, this.hods, 0, hods.length);
            }
            @Override
            protected int[] compute() {
                return fase1Recurcive(x,y,z,hods,deep,repeat);
            }
            private int[] fase1Recurcive(int x,int y,int z, int[] hods,int deep,boolean repeat){
                TaskChain taskChainHead=null;
                TaskChain taskChainThale=null;
                final int st;
                if(repeat)st=hods[deep];
                else st=0;
                for(int np=st;np<=18;np++) {
                    if(isStopped)break; //возможен пропуск решений
                    if(!hodPredHod(np,hods[deep-1]))continue;
                    int xt=x1Move[np][x];int yt=y1Move[np][y];int zt=z1Move[np][z];
                    if (!(xz1Deep[xt][zt]>Tables.MAX_DEEP - deep||
                            yz1Deep[yt][zt]>Tables.MAX_DEEP - deep||
                            xy1Deep[xt][yt]>Tables.MAX_DEEP - deep)){
                        hods[deep]=np;
                        if(deep==Tables.MAX_DEEP){
                            isStopped=true; //возможен пропуск решений
                            return hods;
                        }
                        else {
                            if(Tables.MAX_DEEP -deep!=T1) {
                                final int[] res=fase1Recurcive(xt, yt, zt, hods, deep + 1,repeat);
                                if (res!=null) return res;
                            }
                            else {
                                final RecursiveTaskFase1 task=new RecursiveTaskFase1(hods,xt,yt,zt,deep+1, repeat);
                                if(taskChainThale==null){
                                    taskChainThale=new TaskChain();
                                    taskChainThale.task=task;
                                    taskChainHead=taskChainThale;
                                }
                                else {
                                    taskChainThale.next=new TaskChain();
                                    taskChainThale=taskChainThale.next;
                                    taskChainThale.task=task;
                                }
                                task.fork();
                            }
                        }
                    }
                    repeat=false;
                }
                TaskChain thisNode=taskChainHead;
                while (true){
                    if(thisNode==null)return null;
                    RecursiveTaskFase1 task=thisNode.task;
                    int[] res=task.join();
                    if(res!=null)return res;
                    thisNode=thisNode.next;
                }
            }
        }
    }
    private static final class TaskChain{
        Fase1Parallel.RecursiveTaskFase1 task;
        TaskChain next;
    }
}
