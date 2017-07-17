package test;

import kub.kubSolver.*;
import parallel_solver.ParallelSymmetrySolver;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.IdentityHashMap;

public class TestsAndBenchmarks {
    public static void main(String[] args) throws IOException {
        speedSolve();

        //parallelSolve();
        //infiniteSolve();
        //solveAndView();
        //while (true)computeTables();
    }
    public static void speedSolve() throws IOException {
        KubSolver kubSolver=new KubSolver();
        Kub kub = new Kub(false);
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
        Kub kub = new Kub(true);
        Solution solution= ParallelSymmetrySolver.solve(kub,kubSolver,1000);
        System.out.println(solution);
        for(int p:solution.getHods())kub.povorot(p);
        System.out.println(kub);
    }
    public static void infiniteSolve() throws IOException {
        KubSolver kubSolver=new KubSolver();
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

        KubSolver kubSolver=new KubSolver();
        Solution solution=kubSolver.solve(kub,null,1);
        System.out.println(solution);
        for(int p:solution.getHods())kub.povorot(p);

        System.out.println(kub);
    }
}

class T{
    public static void main(String[] args) {
        SizeOf.sizeof(new KubSolver());
    }
}

