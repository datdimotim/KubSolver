package com.dimotim.kubSolver.tables;

import com.dimotim.kubSolver.kernel.Symmetry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dimotim.kubSolver.tables.SymMoveTable.COUNT_OF_SYMMETRIES;

public final class SymDeepTable implements Serializable {
    private final int[][] symmetryMul; // matrix1*matrix2*vector -> matrix*vector
    private final int[] inverseSymmetry;
    final SymMoveTable symPart;
    final SymMoveTable rawPart;

    private final IntegerMatrix deepTable;

    SymDeepTable(SymMoveTable symPart, SymMoveTable rawPart) {
        this.symPart = symPart;
        this.rawPart = rawPart;
        if (symPart.SYMMETRIES == 8) {
            symmetryMul = Symmetry.getSymmetryMulHalf();
            inverseSymmetry = Symmetry.getInverseSymmetryHalf();
        } else {
            symmetryMul = Symmetry.getSymmetryMul();
            inverseSymmetry = Symmetry.getInverseSymmetry();
        }
        deepTable = packDeepTable(createDeepTable());
    }

    private byte[][] createDeepTable() {
        long startTime=System.currentTimeMillis();
        byte[][] symsForPos=calculateSymForPos();
        byte[][] deepTable = new byte[symPart.CLASSES][rawPart.RAW];
        for (byte[] b : deepTable) Arrays.fill(b, (byte) 20);
        deepTable[0][0] = 0;

        for (int deep = 0; deep < 20; deep++) {
            for (int classPos = 0; classPos < deepTable.length; classPos++) {
                for (int raw = 0; raw < deepTable[0].length; raw++) {
                    if (deepTable[classPos][raw] != deep) continue;
                    for (int np = 1; np < symPart.getCountOfMoves(); np++) {
                        int symPRotated = symPart.doMove(classPos * COUNT_OF_SYMMETRIES, np);
                        int rawPRotated = rawPart.doMove(rawPart.rawToSym(raw), np);
                        final int raw_sym = rawPRotated % COUNT_OF_SYMMETRIES;
                        int symRawRotated = symPart.symPosToRaw(symPRotated);

                        for(byte symInv:symsForPos[symRawRotated]){
                            int t=symPRotated/ COUNT_OF_SYMMETRIES* COUNT_OF_SYMMETRIES+symInv;
                            if(symPart.symPosToRaw(symPRotated)!=symPart.symPosToRaw(t))continue;
                            rawPRotated=rawPRotated/ COUNT_OF_SYMMETRIES* COUNT_OF_SYMMETRIES+symmetryMul[inverseSymmetry[symInv]][raw_sym];
                            int rawRotated=rawPart.symPosToRaw(rawPRotated);
                            if(deepTable[symPRotated/ COUNT_OF_SYMMETRIES][rawRotated]>deep+1)deepTable[symPRotated/ COUNT_OF_SYMMETRIES][rawRotated]=(byte) (deep+1);
                        }
                    }
                }
            }
        }
        System.out.println(System.currentTimeMillis()-startTime);
        return deepTable;
    }



    // rawPosSymPart -> [sym] - список симметрий при которых позиция переходит сама в себя (т.е. симметрии позиции)
    private byte[][] calculateSymForPos(){
        byte[][] table=new byte[symPart.RAW][];
        for(int rawPos=0;rawPos<symPart.RAW;rawPos++){
            int symClass=symPart.rawToSym(rawPos)/COUNT_OF_SYMMETRIES;
            List<Byte> ls=new ArrayList<>();
            for(int sym=0;sym<symPart.SYMMETRIES;sym++){
                int raw=symPart.symPosToRaw(symClass*COUNT_OF_SYMMETRIES+sym);
                if(raw==rawPos){
                    ls.add((byte) sym);
                }
            }
            table[rawPos]=new byte[ls.size()];
            for(int i=0;i<ls.size();i++)table[rawPos][i]=ls.get(i);
        }
        return table;
    }

    private IntegerMatrix packDeepTable(byte[][] deepTable) {
        IntegerMatrix m = new IntegerMatrix(deepTable.length, deepTable[0].length, 3);
        for (int i = 0; i < m.iLength; i++) for (int j = 0; j < m.jLength; j++) m.set(i, j, deepTable[i][j] % 3);
        return m;
    }

    private byte[][] createDeepTableRaw() {
        byte[][] deepTable = new byte[symPart.RAW][rawPart.RAW];
        for (byte[] b : deepTable) Arrays.fill(b, (byte) 20);
        deepTable[0][0] = 0;
        for (int deep = 0; deep < 20; deep++) {
            for (int i = 0; i < deepTable.length; i++) {
                for (int j = 0; j < deepTable[0].length; j++) {
                    if (deepTable[i][j] != deep) continue;
                    for (int np = 0; np < symPart.getCountOfMoves(); np++) {
                        if (deepTable[symPart.rawHod(i, np)][rawPart.rawHod(j, np)] > deep + 1)
                            deepTable[symPart.rawHod(i, np)][rawPart.rawHod(j, np)] = (byte) (deep + 1);
                    }
                }
            }
        }
        return deepTable;
    }

    void proofDeepTable() {
        byte[][] deepTableRaw = createDeepTableRaw();
        for (int i = 0; i < deepTableRaw.length; i++) {
            for (int j = 0; j < deepTableRaw[0].length; j++) {
                int symP = symPart.rawToSym(i);
                int rawP = rawPart.rawToSym(j);
                if (deepTableRaw[i][j] % 3 != getDepth(symP, rawP))
                    throw new RuntimeException("pos: " + i + " " + j + "\n" +
                            "raw: " + deepTableRaw[i][j] % 3 + "\n" +
                            "sym: " + getDepth(symP, rawP));
            }
        }
    }

    public int getDepth(int sym, int raw) {
        int s = symmetryMul[inverseSymmetry[sym % COUNT_OF_SYMMETRIES]][raw % COUNT_OF_SYMMETRIES];
        return (int) deepTable.get(sym / COUNT_OF_SYMMETRIES, rawPart.classToRaw[s][raw / COUNT_OF_SYMMETRIES]);
    }
}
