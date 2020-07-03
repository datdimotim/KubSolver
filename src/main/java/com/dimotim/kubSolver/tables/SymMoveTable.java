package com.dimotim.kubSolver.tables;

import com.dimotim.kubSolver.kernel.Symmetry;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.IntBinaryOperator;

public final class SymMoveTable implements Serializable {
    public static final int SYM_COUNT =16;
    private final IntBinaryOperator symmetryMul; // matrix1*matrix2*vector -> matrix*vector
    private final int[] inverseSymmetry;
    private final int[][] symHods;
    public final int SYMMETRIES;
    public final int CLASSES;
    public final int RAW;

    private final char[][] symMoveTable;        // backing storage   //[povorot][position]=48*class+sym
    final char[][]   classToRaw;        // sym, class
    private final char[]   rawToClass;  // [pos]=class*48+sym

    SymMoveTable(char[][] rawMoveTable, int classes, int symmetries){
        if(symmetries==8) {
            symmetryMul = Symmetry.symmetryMulHalf;
            inverseSymmetry = Symmetry.getInverseSymmetryHalf();
            symHods = Symmetry.getSymHodsHalf();
        }
        else if(symmetries==16){
            symmetryMul = Symmetry.symmetryMul;
            inverseSymmetry = Symmetry.getInverseSymmetry();

            if(rawMoveTable.length==11)symHods=Symmetry.getSymHods10();
            else symHods=Symmetry.getSymHodsAllSymmetry();

        }else {
            throw new RuntimeException();
        }

        SYMMETRIES = symmetries;
        CLASSES=classes;
        RAW=rawMoveTable[0].length;
        symMoveTable=new char[symHods[0].length][CLASSES];
        classToRaw=new char[SYMMETRIES][CLASSES];
        int[][] symTable=createSymTable(rawMoveTable);
        initClassToRaw(rawMoveTable,symTable);
        rawToClass=initRawToClass(symTable);
        initSymMove(rawMoveTable);
    }

    public int getCountOfMoves(){
        return symMoveTable.length;
    }

    private char[] initRawToClass(int[][] symTable){   // <class, sym>[pos]
        char[] rawToClass=new char[symTable[0].length];
        for(int i=0;i<CLASSES;i++){
            for(int s=0;s<symTable.length;s++){
                rawToClass[classToRaw[s][i]]= (char) (i* SYM_COUNT +s);
            }
        }
        return rawToClass;
    }

    private void initClassToRaw(char[][] rawMoveTable,int[][] symTable){
        boolean[] mask=new boolean[rawMoveTable[0].length];
        Arrays.fill(mask,true);
        int classNumber=0;
        for(int i=0;i<symTable[0].length;i++){
            if(mask[i]){
                for(int s=0;s<symTable.length;s++){
                    classToRaw[s][classNumber]= (char) symTable[s][i];
                    mask[symTable[s][i]]=false;
                }
                classNumber++;
            }
        }
        //System.out.println(classNumber);
    }

    private int[][] createSymTable(char[][] move){
        int[][] sym_table=new int[SYMMETRIES][move[0].length];
        for(int[] m:sym_table)Arrays.fill(m,-1);
        for(int i=0;i<sym_table.length;i++)sym_table[i][0]=0;
        createSymTable1(move,sym_table,symHods);
        return sym_table;
    }
    private void createSymTable1(char[][] move, int[][] sym_table,int[][] symHods){
        boolean newMark=true;
        while (newMark) {
            newMark=false;
            for(int pos=0;pos<sym_table[0].length;pos++) {
                if(sym_table[0][pos]==-1)continue;
                for (int p = 1; p < move.length; p++) {
                    int newPos = move[p][pos];
                    if(sym_table[0][newPos]!=-1)continue;
                    newMark=true;
                    for (int s = 0; s < SYMMETRIES; s++) {
                        sym_table[s][newPos] = move[symHods[s][p]][sym_table[s][pos]];
                    }
                }
            }
        }
    }

    private void initSymMove(char[][] rawMoveTable){
        for (int i=0;i<CLASSES;i++){
            for (int np = 0; np< rawMoveTable.length; np++) {
                symMoveTable[np][i] = rawToClass[rawMoveTable[np][classToRaw[0][i]]];
            }
        }
    }

    void proofMove(char[][] rawMoveTable){
        for(int pos=0;pos<rawMoveTable[0].length;pos++){
            for(int np = 0; np< rawMoveTable.length; np++){
                int posEtalon= rawMoveTable[np][pos];
                int posCheck=rawHod(pos,np);
                if(posEtalon!=posCheck)throw new RuntimeException("pos="+pos+" np="+np+" posEtalon="+posEtalon+" posCheck="+posCheck);
            }
        }
    }

    int rawToSym(int raw){
        return rawToClass[raw];
    }

    int symPosToRaw(int symPos){
        return classToRaw[symPos% SYM_COUNT][symPos/ SYM_COUNT];
    }

    public int doMove(int in,int np){
        int inSym=in% SYM_COUNT;
        int inClass=in/ SYM_COUNT;

        int npSym=symHods[inverseSymmetry[inSym]][np];

        int inP=symMoveTable[npSym][inClass];

        return (inP/ SYM_COUNT)* SYM_COUNT + symmetryMul.applyAsInt(inSym,inP% SYM_COUNT);
    }

    int rawHod(int raw,int np){
        int in_class_sym=rawToClass[raw];
        int out_class_sym=doMove(in_class_sym,np);
        return classToRaw[out_class_sym% SYM_COUNT][out_class_sym/ SYM_COUNT];
    }
}
