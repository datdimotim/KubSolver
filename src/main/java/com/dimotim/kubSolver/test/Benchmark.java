package com.dimotim.kubSolver.test;

public class Benchmark{
    private final Benchmarkable benchmarkable;
    public Benchmark(Benchmarkable benchmarkable){
        this.benchmarkable=benchmarkable;
    }
    public void runBenchmark(final int timeToResults){
        long st=System.currentTimeMillis();
        long kol=0;
        while (true) {
            benchmarkable.getSolveAndAddNewTask();
            kol++;
            long th=System.currentTimeMillis();
            if(th-st>timeToResults){
                System.out.println(kol*1000/(th-st)+"tasks/s"+"  results="+benchmarkable.resultToStringAndClear());
                st=th;
                kol=0;
            }
        }
    }
    public interface Benchmarkable {
        void getSolveAndAddNewTask();
        String resultToStringAndClear();
    }
}
