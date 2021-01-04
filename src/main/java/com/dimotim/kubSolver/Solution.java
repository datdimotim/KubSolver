package com.dimotim.kubSolver;

import com.dimotim.kubSolver.kernel.HodTransforms;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Solution {
    private static final String[] hodString=HodTransforms.getHodString();
    private final int[] hods;

    public Solution(int[] hods) {
        this.hods=hods;
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

        return new Solution(h);
    }

    public Solution compose(Solution next){
        return new Solution(
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
        return out.toString() + getLength() + "f";
    }

    public static Solution parse(String str){
        int[] hods = Arrays.stream(str.split("(\\s+)"))
                .map(p -> p.toUpperCase(Locale.ROOT).trim())
                .mapToInt(p -> IntStream.range(0,hodString.length)
                                .filter(i -> hodString[i].trim().equals(p))
                                .findFirst()
                                .getAsInt()
                )
                .toArray();

        return new Solution(hods);
    }
}
