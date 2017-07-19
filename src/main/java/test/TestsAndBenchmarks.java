package test;

import kub.kubSolver.*;
import kub.kubSolver.solvers.RecursiveSolver1;
import kub.kubSolver.solvers.RecursiveSolver2;
import kub.kubSolver.solvers.SimpleSolver1;
import kub.kubSolver.solvers.SimpleSolver2;
import parallel_solver.ParallelSymmetrySolver;

import java.io.IOException;
import static kub.kubSolver.KubSolver.Solution;

public class TestsAndBenchmarks {
    public static void main(String[] args) throws IOException {
        speedSolve();

        //parallelSolve();
        //infiniteSolve();
        //solveAndView();
        //while (true)computeTables();
    }
    public static void speedSolve() throws IOException {
        KubSolver kubSolver=new KubSolver<>(new DoubleTables(),new RecursiveSolver1<>(),new RecursiveSolver2<>());
        Kub kub = new Kub(false);
        final int time=1000;
        long st=System.currentTimeMillis();
        long kol=0;
        int len=0;
        while (true) {
            kub.randomPos();
            Solution solution = kubSolver.solve(kub, null, 1);
            len+=solution.length;
            kol++;
            long th=System.currentTimeMillis();
            if(th-st>time){
                System.out.println(kol*1000/(th-st)+"pos/s"+"  avgLen="+len/kol);
                st=th;
                kol=0;
                len=0;
            }
        }
    }
    public static void parallelSolve() throws IOException {
        KubSolver kubSolver=new KubSolver<>(new DoubleTables(),new RecursiveSolver1<>(),new SimpleSolver2<>());
        Kub kub = new Kub(true);
        Solution solution= ParallelSymmetrySolver.solve(kub,kubSolver,1000);
        System.out.println(solution);
        for(int p:solution.getHods())kub.povorot(p);
        System.out.println(kub);
    }
    public static void infiniteSolve() throws IOException {
        KubSolver kubSolver=new KubSolver<>(new DoubleTables(),new RecursiveSolver1<>(),new SimpleSolver2<>());
        Kub kub = new Kub(true);
        Solution solution=null;
        while (true) {
            solution = kubSolver.solve(kub, solution, 1);
            System.out.println(solution);
        }
    }
    public static void solveAndView(){
        Kub kub=new Kub(true);
        System.out.println(kub);

        KubSolver kubSolver=new KubSolver<>(SymTables.readTables());
        Solution solution=kubSolver.solve(kub,null,1);
        System.out.println(solution);
        for(int p:solution.getHods())kub.povorot(p);

        System.out.println(kub);
    }

    public static void computeAndTestSymTables(){
        long ts=System.currentTimeMillis();
        SymTables symTables =new SymTables();
        System.out.println("Completed... time="+(System.currentTimeMillis()-ts)/1000+"s");
        symTables.proof();
        System.out.println("Completed... time="+(System.currentTimeMillis()-ts)/1000+"s");
    }
}
