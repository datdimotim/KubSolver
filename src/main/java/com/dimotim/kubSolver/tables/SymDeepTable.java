package com.dimotim.kubSolver.tables;

import com.dimotim.kubSolver.kernel.Symmetry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dimotim.kubSolver.tables.SymMoveTable.SYM_COUNT;

public final class SymDeepTable implements Serializable {
    private static final long serialVersionUID = -2634966561356611763L;
    private final int[][] symmetryMul;
    private final int[] inverseSymmetry;
    final SymMoveTable symPart;
    final SymMoveTable rawPart;

    private final IntegerMatrix deepTable;



    SymDeepTable(SymMoveTable symPart, SymMoveTable rawPart) {
        this.symPart = symPart;
        this.rawPart = rawPart;
        if (symPart.SYMMETRIES == 8) {
            symmetryMul = Symmetry.symmetryMulHalf;
            inverseSymmetry = Symmetry.inverseSymmetryHalf;
        } else {
            symmetryMul = Symmetry.symmetryMul;
            inverseSymmetry = Symmetry.inverseSymmetry;
        }
        deepTable = packDeepTable(createDeepTable());
    }

    private byte[][] createDeepTable() {
        byte[][] symsForPos=calculateSymForPos();
        byte[][] deepTable = new byte[symPart.CLASSES][rawPart.RAW];
        for (byte[] b : deepTable) Arrays.fill(b, (byte) 20);
        deepTable[0][0] = 0;

        for (int deep = 0; deep < 20; deep++) {
            for (int classPos = 0; classPos < deepTable.length; classPos++) {
                for (int raw = 0; raw < deepTable[0].length; raw++) {
                    if (deepTable[classPos][raw] != deep) continue;
                    for (int np = 1; np < symPart.getCountOfMoves(); np++) {
                        int symPRotated = symPart.doMove(classPos * SYM_COUNT, np);
                        int rawPRotated = rawPart.doMove(rawPart.rawToSym(raw), np);
                        int rawPClass=rawPRotated/ SYM_COUNT;
                        int symPClass=symPRotated/ SYM_COUNT;
                        final  int sym_symmetry=symPRotated% SYM_COUNT;
                        final int raw_sym = rawPRotated % SYM_COUNT;

                        int rawNormalizedSymmetryExample=symmetryMul[inverseSymmetry[sym_symmetry]][raw_sym];
                        int rawRotatedExample=rawPart.symPosToRaw(rawPClass * SYM_COUNT +rawNormalizedSymmetryExample);
                        if(!(deepTable[symPClass][rawRotatedExample]>deep+1))continue;

                        for(byte eqSym:symsForPos[symPClass]){
                            /*  Здесь мы 'нормализуем' повернутую позицию:
                                У нас имеется симметричная часть (класс + симметрия) после поворота
                                и raw - часть (класс + симметрия)

                                Мы хотим хранить только класс симметричной части + класс + симметрия raw части
                                Соответственно нам нужно получить эквивалентную позицию когда симметрия sym - части равна 0

                                Тогда rawSymNormalized = sympartSym^(-1) * rawSym, но дело в том что не все позиции симметричной части
                                можно привести единственным способом, т.е. sympartSym^(-1) - это множество таких вариантов

                                Т.о мы вычисляем множество таких симметрий для каждого класса (byte[][] symsForPos),
                                А далее мы делаем переход:
                                    eqSym * sym_symmetry^-1 * raw_sym

                                Т.е сначала применяем эквивалентную симметрию которая не меняет симметричную часть, но возможно меняет
                                raw - часть, затем переходим 0 симметрии симметричной части
                                эти два преобразования и есть неоднозначный перевод симметричной части к 0 симметрии,
                                наконец перехомим к симметрии raw - части
                             */
                            int rawNormalizedSymmetry=symmetryMul[eqSym][symmetryMul[inverseSymmetry[sym_symmetry]][raw_sym]];

                            int rawRotated=rawPart.symPosToRaw(rawPClass * SYM_COUNT +rawNormalizedSymmetry);
                            deepTable[symPClass][rawRotated]=(byte) (deep+1);
                        }
                    }
                }
            }
        }
        return deepTable;
    }



    // rawPosSymPart -> [sym] - список симметрий при которых позиция переходит сама в себя (т.е. симметрии позиции)
    private byte[][] calculateSymForPos(){
        byte[][] table=new byte[symPart.CLASSES][];
        for(int classPos=0;classPos<symPart.CLASSES;classPos++){
            int rawExample=symPart.symPosToRaw(classPos* SYM_COUNT);
            List<Byte> ls=new ArrayList<>();
            for(int sym=0;sym<symPart.SYMMETRIES;sym++){
                int raw=symPart.symPosToRaw(classPos* SYM_COUNT +sym);
                if(raw==rawExample){
                    ls.add((byte) sym);
                }
            }
            table[classPos]=new byte[ls.size()];
            for(int i=0;i<ls.size();i++)table[classPos][i]=ls.get(i);
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
        int s = symmetryMul[inverseSymmetry[sym % SYM_COUNT]][raw % SYM_COUNT];
        return (int) deepTable.get(sym / SYM_COUNT, rawPart.classToRaw[s][raw / SYM_COUNT]);
    }
}
