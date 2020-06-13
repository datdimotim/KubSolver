package com.dimotim.kubSolver;

import com.dimotim.kubSolver.kernel.HodTransforms;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Solution {
    private static final int[][] symHods= HodTransforms.getSymHodsFor3Axis();
    private static final String[] hodString=HodTransforms.getHodString();
    private final int[] hods;
    private final int sym;

    public Solution(int sym, int[] hods) {
        this.sym=sym;
        this.hods=hods;
    }

    public static Solution fromFases(int sym, int[] fase1, int[] fase2){
        int[] hods=IntStream.concat(
                Arrays.stream(fase1),
                Arrays.stream(fase2)
        )
        .filter(h->h!=0)
        .map(h->symHods[sym-1][h])
        .toArray();

        return new Solution(sym,hods);
    }

    public int[] getHods() {
        return hods.clone();
    }

    public Solution inverse(){
        int[] h=IntStream.range(0,getLength())
                .map(i->getLength()-1-i)
                .map(i->hods[i])
                .map(HodTransforms::inverseHod18)
                .toArray();

        return new Solution(sym,h);
    }

    public Solution compose(Solution next){
        return new Solution(
                sym,
                Stream.of(hods,next.getHods())
                    .flatMapToInt(Arrays::stream)
                    .toArray()
        );
    }

    public int getLength(){
        return hods.length;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        for (int hod : hods) out.append(hodString[hod]);
        return out.toString() + getLength() + "f symmerty="+sym;
    }
}
