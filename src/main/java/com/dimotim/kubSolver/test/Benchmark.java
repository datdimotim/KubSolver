package com.dimotim.kubSolver.test;

public class Benchmark{
    private final Benchmarkable benchmarkable;
    public Benchmark(Benchmarkable benchmarkable){
        this.benchmarkable=benchmarkable;
    }
    public void runBenchmark(final int timeToResults){
        final long totalStartTime=System.currentTimeMillis();
        long st=System.currentTimeMillis();
        long kol=0;
        long totalKol=0;
        while (true) {
            benchmarkable.getSolveAndAddNewTask();
            kol++;
            long th=System.currentTimeMillis();
            if(th-st>timeToResults){
                totalKol+=kol;
                System.out.println("avg: "+totalKol*1000/(th-totalStartTime)+" tasks/s "+kol*1000/(th-st)+" tasks/s"+"  results="+benchmarkable.resultToStringAndClear());
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
