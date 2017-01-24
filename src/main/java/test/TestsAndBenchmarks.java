package test;

import kub.kubSolver.*;
import kub.kubSolver.Tables;
import kub.kubSolver.solvers.*;

import java.io.IOException;

public class TestsAndBenchmarks {
    public static void main(String[] args) throws IOException {
        speedSolve();
        //parallelSolve();
        //infiniteSolve();
        //solveAndView();
        //computeTables();
    }
    public static void speedSolve() throws IOException {
        KubSolver kubSolver=new KubSolver(new SimpleSolver1(),new SimpleSolver2());
        Kub kub = new Kub();
        final int time=1000;
        long st=System.currentTimeMillis();
        long kol=0;
        while (true) {
            kub.randomPos();
            Solution solution = kubSolver.solve(kub, null, 1);
            kol++;
            long th=System.currentTimeMillis();
            if(th-st>time){
                System.out.println(kol*1000/(th-st));
                st=th;
                kol=0;
            }
        }
    }
    public static void parallelSolve() throws IOException {
        KubSolver kubSolver=new KubSolver();
        Kub kub = new Kub();
        kub.randomPos();
        Solution solution=ParallelSymmetrySolver.solve(kub,kubSolver,1000);
        System.out.println(solution);
        for(int p:solution.getHods())kub.povorot(p);
        System.out.println(kub);
    }
    public static void infiniteSolve() throws IOException {
        KubSolver kubSolver=new KubSolver();
        Kub kub = new Kub();
        kub.randomPos();
        Solution solution=null;
        while (true) {
            solution = kubSolver.solve(kub, solution, 1);
            System.out.println(solution);
        }
    }
    public static void solveAndView(){
        Kub kub=new Kub();
        kub.randomPos();
        System.out.println(kub);

        KubSolver kubSolver=new KubSolver();
        Solution solution=kubSolver.solve(kub,null,1);
        System.out.println(solution);
        for(int p:solution.getHods())kub.povorot(p);

        System.out.println(kub);
    }
    public static void computeTables(){
        long ts=System.currentTimeMillis();
        Tables tables=Tables.computeTables();
        System.out.println(System.currentTimeMillis()-ts);
    }
}