package com.dimotim.kubSolver.tables;

import com.dimotim.kubSolver.kernel.Symmetry;

import java.io.Serializable;
import java.util.Arrays;

import static com.dimotim.kubSolver.tables.SymMoveTable.*;

public final class SymDeepTable implements Serializable {
    private final int[][] symmetryMul; // matrix1*matrix2*vector -> matrix*vector
    private final int[] inverseSymmetry;
    final SymMoveTable symPart;
    final SymMoveTable rawPart;

    private final IntegerMatrix deepTable;
    SymDeepTable(SymMoveTable symPart, SymMoveTable rawPart){
        this.symPart=symPart;
        this.rawPart=rawPart;
        if(symPart.SYMMETRIES==8) {
            symmetryMul = Symmetry.getSymmetryMulHalf();
            inverseSymmetry = Symmetry.getInverseSymmetryHalf();
        }
        else {
            symmetryMul = Symmetry.getSymmetryMul();
            inverseSymmetry = Symmetry.getInverseSymmetry();
        }
        deepTable=packDeepTable(createDeepTable());
    }
    private byte[][] createDeepTable(){
        int symP;
        int rawP;
        int symPRotated;
        int rawPRotated;
        int t;
        byte[][] deepTable=new byte[symPart.CLASSES][rawPart.RAW];
        for(byte[] b:deepTable) Arrays.fill(b,(byte) 20);
        deepTable[0][0]=0;
        for(int deep=0;deep<20;deep++){
            for(int classPos=0;classPos<deepTable.length;classPos++){
                for(int raw=0;raw<deepTable[0].length;raw++){
                    if(deepTable[classPos][raw]!=deep)continue;
                    for(int np=0;np<symPart.symMoveTable.length;np++){
                        symP=classPos* COUNT_OF_SYMMETRIES; // sym=0
                        rawP=rawPart.rawToSym(raw);

                        symPRotated=symPart.doMove(symP,np);
                        rawPRotated=rawPart.doMove(rawP,np);
                        final int raw_sym=rawPRotated% COUNT_OF_SYMMETRIES;

                        for(int symInv=0;symInv<symPart.SYMMETRIES;symInv++){
                            t=symPRotated/ COUNT_OF_SYMMETRIES* COUNT_OF_SYMMETRIES+symInv;
                            if(symPart.symPosToRaw(symPRotated)!=symPart.symPosToRaw(t))continue;
                            rawPRotated=rawPRotated/ COUNT_OF_SYMMETRIES* COUNT_OF_SYMMETRIES+symmetryMul[inverseSymmetry[symInv]][raw_sym];
                            int rawRotated=rawPart.symPosToRaw(rawPRotated);
                            if(deepTable[symPRotated/ COUNT_OF_SYMMETRIES][rawRotated]>deep+1)deepTable[symPRotated/ COUNT_OF_SYMMETRIES][rawRotated]=(byte) (deep+1);
                        }
                    }
                }
            }
        }
        return deepTable;
    }

    private IntegerMatrix packDeepTable(byte[][] deepTable){
        IntegerMatrix m=new IntegerMatrix(deepTable.length,deepTable[0].length,3);
        for(int i=0;i<m.iLength;i++)for(int j=0;j<m.jLength;j++)m.set(i,j,deepTable[i][j]%3);
        return m;
    }

    private byte[][] createDeepTableRaw(){
        byte[][] deepTable=new byte[symPart.RAW][rawPart.RAW];
        for(byte[] b:deepTable)Arrays.fill(b,(byte) 20);
        deepTable[0][0]=0;
        for(int deep=0;deep<20;deep++){
            for(int i=0;i<deepTable.length;i++){
                for(int j=0;j<deepTable[0].length;j++){
                    if(deepTable[i][j]!=deep)continue;
                    for(int np=0;np<symPart.symMoveTable.length;np++){
                        if(deepTable[symPart.rawHod(i,np)][rawPart.rawHod(j,np)]>deep+1)deepTable[symPart.rawHod(i,np)][rawPart.rawHod(j,np)]=(byte) (deep+1);
                    }
                }
            }
        }
        return deepTable;
    }

    void proofDeepTable(){
        byte[][] deepTableRaw=createDeepTableRaw();
        for(int i=0;i<deepTableRaw.length;i++){
            for(int j=0;j<deepTableRaw[0].length;j++){
                int symP=symPart.rawToSym(i);
                int rawP=rawPart.rawToSym(j);
                if(deepTableRaw[i][j]%3!=getDepth(symP,rawP))
                    throw new RuntimeException("pos: "+i+" "+j+"\n"+
                            "raw: "+deepTableRaw[i][j]%3+"\n"+
                            "sym: "+getDepth(symP,rawP));
            }
        }
    }

    int getDepth(int sym, int raw){
        int s=symmetryMul[inverseSymmetry[sym% COUNT_OF_SYMMETRIES]][raw% COUNT_OF_SYMMETRIES];
        return (int) deepTable.get(sym/ COUNT_OF_SYMMETRIES,rawPart.classToRaw[s][raw/ COUNT_OF_SYMMETRIES]);
    }
}
