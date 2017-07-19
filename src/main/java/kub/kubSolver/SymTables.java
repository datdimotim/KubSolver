package kub.kubSolver;

import com.dimotim.compact_arrays.CompactIntegerArrayShift;
import com.dimotim.compact_arrays.IntegerArray;
import java.io.*;
import java.util.Arrays;

public class SymTables implements Tables<SymTables.KubState>{
    private final SymMoveTable x1;
    private final SymMoveTable y1;
    private final SymMoveTable z1;
    private final SymMoveTable x2;
    private final SymMoveTable y2;
    private final SymMoveTable z2;
    private final SymDeepTable xy1;
    private final SymDeepTable xz1;
    private final SymDeepTable yz1;
    private SymDeepTable xy2;
    private final SymDeepTable xz2;
    private final SymDeepTable yz2;
    public SymTables() {
        MoveTables moveTables = new MoveTables();System.out.println("raw move tables created");
        x1 = new SymMoveTable(moveTables.x1Move, X_1_SYM_CLASSES);System.out.println("x1 move created");
        y1 = new SymMoveTable(moveTables.y1Move, Y_1_SYM_CLASSES);System.out.println("y1 move created");
        z1 = new SymMoveTable(moveTables.z1Move, Z_1_SYM_CLASSES);System.out.println("z1 move created");
        x2 = new SymMoveTable(moveTables.x2Move, X_2_SYM_CLASSES);System.out.println("x2 move created");
        y2 = new SymMoveTable(moveTables.y2Move, Y_2_SYM_CLASSES);System.out.println("y2 move created");
        z2 = new SymMoveTable(moveTables.z2Move, Z_2_SYM_CLASSES);System.out.println("z2 move created");
        xy1 = new SymDeepTable(x1, y1);System.out.println("xy1 deep created");
        xz1 = new SymDeepTable(x1, z1);System.out.println("xz1 deep created");
        yz1 = new SymDeepTable(y1, z1);System.out.println("yz1 deep created");
        //xy2 = new SymDeepTable(x2, y2);System.out.println("xy2 deep created");
        xz2 = new SymDeepTable(x2, z2);System.out.println("xz2 deep created");
        yz2 = new SymDeepTable(y2, z2);System.out.println("yz2 deep created");
    }
    public void proof(){
        MoveTables moveTables = new MoveTables();
        x1.proofMove(moveTables.x1Move);System.out.println("x1 move tested");
        y1.proofMove(moveTables.y1Move);System.out.println("y1 move tested");
        z1.proofMove(moveTables.z1Move);System.out.println("z1 move tested");
        x2.proofMove(moveTables.x2Move);System.out.println("x2 move tested");
        y2.proofMove(moveTables.y2Move);System.out.println("y2 move tested");
        z2.proofMove(moveTables.z2Move);System.out.println("z2 move tested");
        xy1.proofDeepTable();System.out.println("xy1 deep tested");
        xz1.proofDeepTable();System.out.println("xz1 deep tested");
        yz1.proofDeepTable();System.out.println("yz1 deep tested");
        //xy2.proofDeepTable();System.out.println("xy2 deep tested");
        xz2.proofDeepTable();System.out.println("xz2 deep tested");
        yz2.proofDeepTable();System.out.println("yz2 deep tested");
    }

    private int initDepth(SymDeepTable table, int s, int r){
        int si=s;
        int ri=r;
        int so;
        int ro;
        int count=0;
        int deepPred=21+table.getDepth(s,r); // 21 mod 3 = 0
        for(int np=1;np<table.symPart.symMoveTable.length;np++){
            so=table.symPart.doMove(si,np);
            ro=table.rawPart.doMove(ri,np);
            if(track(table.getDepth(so,ro),deepPred)<deepPred){
                deepPred=track(table.getDepth(so,ro),deepPred);
                count++;
                np=0;
                int tmp=si;
                si=so;
                so=tmp;
                tmp=ri;
                ri=ro;
                ro=tmp;
            }
        }
        return count;
    }

    private static int track(int mod, int deepPred){
        int old=deepPred%3;
        if(mod==old)return deepPred;
        if(old==2){
            if(mod==1)return deepPred-1;
            else return deepPred+1;
        }
        if(old==1){
            if(mod==0)return deepPred-1;
            else return deepPred+1;
        }
        if(old==0){
            if(mod==2)return deepPred-1;
            else return deepPred+1;
        }
        throw new RuntimeException("mod="+mod+" deepPred="+deepPred);
    }

    @Override
    public KubState initKubStateFase1(int x,int y,int z){
        KubState kubState=new KubState();
        kubState.x=x1.rawToSym(x);
        kubState.y=y1.rawToSym(y);
        kubState.z=z1.rawToSym(z);
        kubState.xyDeep=initDepth(xy1,kubState.x,kubState.y);
        kubState.xzDeep=initDepth(xz1,kubState.x,kubState.z);
        kubState.yzDeep=initDepth(yz1,kubState.y,kubState.z);
        return kubState;
    }

    @Override
    public int moveAndGetDetphFase1(KubState in, KubState out, int np){
        out.x=x1.doMove(in.x,np);
        out.y=y1.doMove(in.y,np);
        out.z=z1.doMove(in.z,np);
        out.xyDeep=track(xy1.getDepth(out.x,out.y),in.xyDeep);
        out.xzDeep=track(xz1.getDepth(out.x,out.z),in.xzDeep);
        out.yzDeep=track(yz1.getDepth(out.y,out.z),in.yzDeep);
        return Math.max(out.xyDeep,Math.max(out.xzDeep,out.yzDeep));
    }

    @Override
    public KubState initKubStateFase2(int x,int y,int z){
        KubState kubState=new KubState();
        kubState.x=x2.rawToSym(x);
        kubState.y=y2.rawToSym(y);
        kubState.z=z2.rawToSym(z);
        kubState.xyDeep=xy2 == null ? 0 : initDepth(xy2,kubState.x,kubState.y);
        kubState.xzDeep=initDepth(xz2,kubState.x,kubState.z);
        kubState.yzDeep=initDepth(yz2,kubState.y,kubState.z);
        return kubState;
    }

    @Override
    public int moveAndGetDetphFase2(KubState in, KubState out, int np){
        out.x=x2.doMove(in.x,np);
        out.y=y2.doMove(in.y,np);
        out.z=z2.doMove(in.z,np);
        out.xyDeep=xy2 == null ? 0 : track(xy2.getDepth(out.x,out.y),in.xyDeep);
        out.xzDeep=track(xz2.getDepth(out.x,out.z),in.xzDeep);
        out.yzDeep=track(yz2.getDepth(out.y,out.z),in.yzDeep);
        return Math.max(out.xyDeep,Math.max(out.xzDeep,out.yzDeep));
    }

    @Override
    public KubState newKubState(){
        return new KubState();
    }

    @Override
    public KubState[] newArrayKubState(int length){
        return new KubState[length];
    }

    // run first for initialization
    public static void main(String[] args)throws IOException {
        SymTables tables=new SymTables();
        ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("src/main/resources/tables.object"));
        oos.writeObject(tables);
        oos.close();
    }

    public static SymTables readTables(){
        try(InputStream fis = SymTables.class.getResourceAsStream("/tables.object")) {
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (SymTables) ois.readObject();
        }
        catch (IOException | ClassNotFoundException e){
            throw new RuntimeException("Can't read tables",e);
        }
    }
    static final class KubState{
        private int x;
        private int y;
        private int z;
        private int xyDeep;
        private int xzDeep;
        private int yzDeep;
    }
    private static final class IntegerMatrix implements Serializable{
        private final IntegerArray array;
        final int iLength;
        final int jLength;
        IntegerMatrix(int maxI,int maxJ,int maxVal){
            iLength =maxI;
            jLength =maxJ;
            int i=0;
            int v=1;
            while (v<maxVal){
                v*=2;
                i++;
            }
            array=new CompactIntegerArrayShift(maxI*maxJ,i);
            //array=new CompactIntegerArrayDivide(maxI*maxJ,maxVal);
        }
        void set(int i,int j,int val){
            array.set(i* jLength +j,val);
        }
        long get(int i,int j){
            return array.get(i* jLength +j);
        }
    }

    private static final class SymMoveTable implements Serializable{
        private final int[][] symmetryMul; // matrix1*matrix2*vector -> matrix*vector
        private final int[] inverseSymmetry;
        private final int[][] symHods;
        final int SYMMETRIES;
        final int CLASSES;
        final int RAW;

        final char[][] symMoveTable;        // backing storage   //[povorot][position]=16*class+sym
        final char[][]   classToRaw;        // sym, class
        private final char[]   rawToClass;  // [pos]=class*16+sym

        SymMoveTable(char[][] rawMoveTable, int classes){
            if(rawMoveTable.length==19) {
                symmetryMul = Symmetry.symmetryMulHalf;
                inverseSymmetry = Symmetry.inverseSymmetryHalf;
                symHods = Symmetry.symHodsHalf;
            }
            else {
                if(rawMoveTable.length!=11)throw new RuntimeException();
                symmetryMul = Symmetry.symmetryMul;
                inverseSymmetry = Symmetry.inverseSymmetry;
                symHods = Symmetry.symHods10;

            }
            SYMMETRIES = symmetryMul.length;
            CLASSES=classes;
            RAW=rawMoveTable[0].length;
            symMoveTable=new char[symHods[0].length][CLASSES];
            classToRaw=new char[SYMMETRIES][CLASSES];
            int[][] symTable=createSymTable(rawMoveTable);
            initClassToRaw(rawMoveTable,symTable);
            rawToClass=initRawToClass(symTable);
            initSymMove(rawMoveTable);
        }

        private char[] initRawToClass(int[][] symTable){   // <class, sym>[pos]
            char[] rawToClass=new char[symTable[0].length];
            for(int i=0;i<CLASSES;i++){
                for(int s=0;s<symTable.length;s++){
                    rawToClass[classToRaw[s][i]]=(char) (i*16+s);
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
                    symMoveTable[np][i] = (rawToClass[rawMoveTable[np][classToRaw[0][i]]]);
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

        final int rawToSym(int raw){
            return rawToClass[raw];
        }

        final int symPosToRaw(int symPos){
            return classToRaw[symPos%16][symPos/16];
        }

        final int doMove(int in,int np){
            int npSym=symHods[inverseSymmetry[in%16]][np];
            return (symMoveTable[npSym][in/16]/16)*16+symmetryMul[in%16][symMoveTable[npSym][in/16]%16];
        }

        final int rawHod(int raw,int np){
            int in_classPos=rawToClass[raw]/16;
            int in_sym=rawToClass[raw]%16;
            int npSym=symHods[inverseSymmetry[in_sym]][np];
            int out_classPos=symMoveTable[npSym][in_classPos]/16;
            int out_sym=symmetryMul[in_sym][symMoveTable[npSym][in_classPos]%16];
            return classToRaw[out_sym][out_classPos];
        }
    }

    private static final class SymDeepTable implements Serializable{
        private final int[][] symmetryMul; // matrix1*matrix2*vector -> matrix*vector
        private final int[] inverseSymmetry;
        final SymMoveTable symPart;
        final SymMoveTable rawPart;

        private final IntegerMatrix deepTable;
        SymDeepTable(SymMoveTable symPart, SymMoveTable rawPart){
            this.symPart=symPart;
            this.rawPart=rawPart;
            if(symPart.symMoveTable.length==19) {
                symmetryMul = Symmetry.symmetryMulHalf;
                inverseSymmetry = Symmetry.inverseSymmetryHalf;
            }
            else {
                if(symPart.symMoveTable.length!=11)throw new RuntimeException();
                symmetryMul = Symmetry.symmetryMul;
                inverseSymmetry = Symmetry.inverseSymmetry;
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
            for(byte[] b:deepTable)Arrays.fill(b,(byte) 20);
            deepTable[0][0]=0;
            for(int deep=0;deep<20;deep++){
                for(int classPos=0;classPos<deepTable.length;classPos++){
                    for(int raw=0;raw<deepTable[0].length;raw++){
                        if(deepTable[classPos][raw]!=deep)continue;
                        for(int np=0;np<symPart.symMoveTable.length;np++){
                            symP=classPos*16; // sym=0
                            rawP=rawPart.rawToSym(raw);

                            symPRotated=symPart.doMove(symP,np);
                            rawPRotated=rawPart.doMove(rawP,np);
                            final int raw_sym=rawPRotated%16;

                            for(int symInv=0;symInv<symPart.SYMMETRIES;symInv++){
                                t=symPRotated/16*16+symInv;
                                if(symPart.symPosToRaw(symPRotated)!=symPart.symPosToRaw(t))continue;
                                rawPRotated=rawPRotated/16*16+symmetryMul[inverseSymmetry[symInv]][raw_sym];
                                int rawRotated=rawPart.symPosToRaw(rawPRotated);
                                if(deepTable[symPRotated/16][rawRotated]>deep+1)deepTable[symPRotated/16][rawRotated]=(byte) (deep+1);
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
                    if(deepTableRaw[i][j]%3!=getDepth(symP,rawP))throw new RuntimeException();
                }
            }
        }

        final int getDepth(int sym, int raw){
            int s=symmetryMul[inverseSymmetry[sym%16]][raw%16];
            return (int) deepTable.get(sym/16,rawPart.classToRaw[s][raw/16]);
        }
    }
}