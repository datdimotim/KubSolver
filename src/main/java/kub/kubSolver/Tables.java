package kub.kubSolver;

import com.dimotim.compact_arrays.*;
import test.SizeOf;

import java.io.*;
import java.util.Arrays;

import static kub.kubSolver.SymTables.*;

class MoveTables implements Serializable{
    final char[][] x1Move;
    final char[][] y1Move;
    final char[][] z1Move;
    final char[][] x2Move;
    final char[][] y2Move;
    final char[][] z2Move;

    MoveTables(){
        x1Move=createX1Move();
        y1Move=createY1Move();
        z1Move=createZ1Move();
        x2Move=createX2Move();
        y2Move=createY2Move();
        z2Move=createZ2Move();
    }

    private static char[][] createX1Move(){
        int[] u_o=new int[8];
        char[][] table=new char[19][X_1_MAX];
        for(int pos = 0; pos< X_1_MAX; pos++){
            for(int pov=0;pov<19;pov++){
                KubCubie.povorotUO(KubKoordinates.x1ToCubie(pos),u_o,pov);
                table[pov][pos]= (char) KubCubie.uoToX1(u_o);
            }
        }
        return table;
    }
    private static char[][] createY1Move(){
        int[] r_o=new int[12];
        char[][] table=new char[19][Y_1_MAX];
        for(int pos = 0; pos< Y_1_MAX; pos++){
            for(int pov=0;pov<19;pov++){
                KubCubie.povorotRO(KubKoordinates.y1ToCubie(pos),r_o,pov);
                table[pov][pos]= (char) KubCubie.roToY1(r_o);
            }
        }
        return table;
    }
    private static char[][] createZ1Move(){
        int[] r_p=new int[12];
        char[][] table=new char[19][Z_1_MAX];
        for(int pos = 0; pos< Z_1_MAX; pos++){
            for(int pov=0;pov<19;pov++){
                KubCubie.povorotRP(KubKoordinates.z1ToCubie(pos),r_p,pov);
                table[pov][pos]= (char) KubCubie.rpToZ1(r_p);
            }
        }
        return table;
    }
    private static char[][] createX2Move(){
        int[] convertPovorot=HodTransforms.getP10To18();
        int[] u_p=new int[8];
        char[][] table=new char[HodTransforms.NUM_HODS_2][X_2_MAX];
        for(int pos = 0; pos< X_2_MAX; pos++){
            for(int pov=0;pov<HodTransforms.NUM_HODS_2;pov++){
                KubCubie.povorotUP(KubKoordinates.x2ToCubie(pos),u_p,convertPovorot[pov]);
                table[pov][pos]= (char) KubCubie.upToX2(u_p);
            }
        }
        return table;
    }
    private static char[][] createY2Move(){
        int[] convertPovorot=HodTransforms.getP10To18();
        int[] r_p=new int[12];
        char[][] table=new char[HodTransforms.NUM_HODS_2][Y_2_MAX];
        for(int pos = 0; pos< Y_2_MAX; pos++){
            for(int pov=0;pov<HodTransforms.NUM_HODS_2;pov++){
                KubCubie.povorotRP(KubKoordinates.y2ToCubie(pos),r_p,convertPovorot[pov]);
                table[pov][pos]=(char) KubCubie.rpToY2(r_p);
            }
        }
        return table;
    }
    private static char[][] createZ2Move(){
        int[] convertPovorot=HodTransforms.getP10To18();
        int[] r_p=new int[12];
        char[][] table=new char[HodTransforms.NUM_HODS_2][Z_2_MAX];
        for(int pos = 0; pos< Z_2_MAX; pos++){
            for(int pov=0;pov<HodTransforms.NUM_HODS_2;pov++){
                KubCubie.povorotRP(KubKoordinates.z2ToCubie(pos),r_p,convertPovorot[pov]);
                table[pov][pos]=(char) KubCubie.rpToZ2(r_p);
            }
        }
        return table;
    }
}

class Tracker{
    static int track(int mod, int deepPred){
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
}

class IntegerMatrix implements Serializable{
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

class SymMoveTable implements Serializable{
    private final int[][] symmetryMul; // matrix1*matrix2*vector -> matrix*vector
    private final int[] inverseSymmetry;
    private final int[][] symHods;
    final int SYMMETRIES;
    final int CLASSES;
    final int RAW;

    final char[][][] symMoveTable;   // backing storage   //<class, sym>[povorot][position]
    final char[][]   classToRaw;     // sym, class
    private final char[][]   rawToClass;     // <class, sym>[pos]

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
        symMoveTable=new char[2][symHods[0].length][CLASSES];
        classToRaw=new char[SYMMETRIES][CLASSES];
        int[][] symTable=createSymTable(rawMoveTable);
        initClassToRaw(rawMoveTable,symTable);
        rawToClass=initRawToClass(symTable);
        initSymMove(rawMoveTable);
    }

    private char[][] initRawToClass(int[][] symTable){   // <class, sym>[pos]
        char[][] rawToClass=new char[2][symTable[0].length];
        for(int i=0;i<CLASSES;i++){
            for(int s=0;s<symTable.length;s++){
                rawToClass[0][classToRaw[s][i]]=(char) i;
                rawToClass[1][classToRaw[s][i]]=(char) s;
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
                symMoveTable[0][np][i] = rawToClass[0][rawMoveTable[np][classToRaw[0][i]]];
                symMoveTable[1][np][i] = rawToClass[1][rawMoveTable[np][classToRaw[0][i]]];
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

    final void rawToSym(int raw,SymPos symPos){
        symPos.classPos=rawToClass[0][raw];
        symPos.sym=rawToClass[1][raw];
    }

    final int symPosToRaw(SymPos symPos){
        return classToRaw[symPos.sym][symPos.classPos];
    }

    final void doMove(SymPos in, SymPos out,int np){
        int npSym=symHods[inverseSymmetry[in.sym]][np];
        out.classPos=symMoveTable[0][npSym][in.classPos];
        out.sym=symmetryMul[in.sym][symMoveTable[1][npSym][in.classPos]];
    }

    final int rawHod(int raw,int np){
        int in_classPos=rawToClass[0][raw];
        int in_sym=rawToClass[1][raw];
        int npSym=symHods[inverseSymmetry[in_sym]][np];
        int out_classPos=symMoveTable[0][npSym][in_classPos];
        int out_sym=symmetryMul[in_sym][symMoveTable[1][npSym][in_classPos]];
        return classToRaw[out_sym][out_classPos];
    }
}

class SymDeepTable implements Serializable{
    private final int[][] symmetryMul; // matrix1*matrix2*vector -> matrix*vector
    private final int[] inverseSymmetry;
    private final SymMoveTable symPart;
    private final SymMoveTable rawPart;

    private final byte[][] deepTable;
    SymDeepTable(SymMoveTable symPart, SymMoveTable rawPart){
        this.symPart=symPart;
        this.rawPart=rawPart;
        if(symPart.symMoveTable[0].length==19) {
            symmetryMul = Symmetry.symmetryMulHalf;
            inverseSymmetry = Symmetry.inverseSymmetryHalf;
        }
        else {
            if(symPart.symMoveTable[0].length!=11)throw new RuntimeException();
            symmetryMul = Symmetry.symmetryMul;
            inverseSymmetry = Symmetry.inverseSymmetry;
        }
        deepTable=createDeepTable();
    }
    private byte[][] createDeepTable(){
        SymPos symP=new SymPos();
        SymPos rawP=new SymPos();
        SymPos symPRotated=new SymPos();
        SymPos rawPRotated=new SymPos();
        SymPos t=new SymPos();
        byte[][] deepTable=new byte[symPart.CLASSES][rawPart.RAW];
        for(byte[] b:deepTable)Arrays.fill(b,(byte) 20);
        deepTable[0][0]=0;
        for(int deep=0;deep<20;deep++){
            for(int classPos=0;classPos<deepTable.length;classPos++){
                for(int raw=0;raw<deepTable[0].length;raw++){
                    if(deepTable[classPos][raw]!=deep)continue;
                    for(int np=0;np<symPart.symMoveTable[0].length;np++){
                        symP.sym=0;
                        symP.classPos=classPos;
                        rawPart.rawToSym(raw,rawP);

                        symPart.doMove(symP,symPRotated,np);
                        rawPart.doMove(rawP,rawPRotated,np);
                        final int raw_sym=rawPRotated.sym;
                        t.classPos=symPRotated.classPos;

                        for(int symInv=0;symInv<symPart.SYMMETRIES;symInv++){
                            t.sym=symInv;
                            if(symPart.symPosToRaw(symPRotated)!=symPart.symPosToRaw(t))continue;
                            rawPRotated.sym=symmetryMul[inverseSymmetry[symInv]][raw_sym];
                            int rawRotated=rawPart.symPosToRaw(rawPRotated);
                            if(deepTable[symPRotated.classPos][rawRotated]>deep+1)deepTable[symPRotated.classPos][rawRotated]=(byte) (deep+1);
                        }
                    }
                }
            }
        }
        return deepTable;
    }

    private byte[][] createDeepTableRaw(){
        byte[][] deepTable=new byte[symPart.RAW][rawPart.RAW];
        for(byte[] b:deepTable)Arrays.fill(b,(byte) 20);
        deepTable[0][0]=0;
        for(int deep=0;deep<20;deep++){
            for(int i=0;i<deepTable.length;i++){
                for(int j=0;j<deepTable[0].length;j++){
                    if(deepTable[i][j]!=deep)continue;
                    for(int np=0;np<symPart.symMoveTable[0].length;np++){
                        if(deepTable[symPart.rawHod(i,np)][rawPart.rawHod(j,np)]>deep+1)deepTable[symPart.rawHod(i,np)][rawPart.rawHod(j,np)]=(byte) (deep+1);
                    }
                }
            }
        }
        return deepTable;
    }

    void proofDeepTable(){
        SymPos symP=new SymPos();
        SymPos rawP=new SymPos();
        byte[][] deepTableRaw=createDeepTableRaw();
        for(int i=0;i<deepTableRaw.length;i++){
            for(int j=0;j<deepTableRaw[0].length;j++){
                symPart.rawToSym(i,symP);
                rawPart.rawToSym(j,rawP);
                if(deepTableRaw[i][j]!=getDepth(symP,rawP))throw new RuntimeException();
            }
        }
    }

    final int getDepth(SymPos sym, SymPos raw){
        int s=symmetryMul[inverseSymmetry[sym.sym]][raw.sym];
        return deepTable[sym.classPos][rawPart.classToRaw[s][raw.classPos]];
    }
}

class SymPos{
    int classPos;
    int sym;
}



class SymTables implements Serializable{
    static final int X_1_MAX =2187;
    static final int Y_1_MAX =2048;
    static final int Z_1_MAX =495;
    static final int X_2_MAX =40320;
    static final int Y_2_MAX =40320;
    static final int Z_2_MAX =24;
    private static final int X_1_SYM_CLASSES =324;
    private static final int Y_1_SYM_CLASSES =336;
    private static final int Z_1_SYM_CLASSES =81;
    private static final int X_2_SYM_CLASSES =2768;
    private static final int Y_2_SYM_CLASSES =2768;
    private static final int Z_2_SYM_CLASSES =8;

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
    SymTables() {
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
    void proof(){
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

    static class KubState{
        SymPos x=new SymPos();
        SymPos y=new SymPos();
        SymPos z=new SymPos();
    }
    KubState initKubStateFase1(int x,int y,int z){
        KubState kubState=new KubState();
        x1.rawToSym(x,kubState.x);
        y1.rawToSym(y,kubState.y);
        z1.rawToSym(z,kubState.z);
        return kubState;
    }

    int moveAndGetDetphFase1(KubState in, KubState out, int np){
        x1.doMove(in.x,out.x,np);
        y1.doMove(in.y,out.y,np);
        z1.doMove(in.z,out.z,np);
        return Math.max(yz1.getDepth(out.y,out.z),Math.max(xy1.getDepth(out.x,out.y),xz1.getDepth(out.x,out.z)));
    }
    KubState initKubStateFase2(int x,int y,int z){
        KubState kubState=new KubState();
        x2.rawToSym(x,kubState.x);
        y2.rawToSym(y,kubState.y);
        z2.rawToSym(z,kubState.z);
        return kubState;
    }

    int moveAndGetDetphFase2(KubState in, KubState out, int np){
        x2.doMove(in.x,out.x,np);
        y2.doMove(in.y,out.y,np);
        z2.doMove(in.z,out.z,np);
        return Math.max(yz2.getDepth(out.y,out.z),Math.max(
                xy2 == null ? 0:
                        xy2.getDepth(out.x,out.y),
                        xz2.getDepth(out.x,out.z)));
    }

    // run first for initialization
    public static void main(String[] args)throws IOException{
        SymTables tables=new SymTables();
        ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("src/main/resources/tables.object"));
        oos.writeObject(tables);
        oos.close();
    }

    static SymTables readTables(){
        try(InputStream fis = SymTables.class.getResourceAsStream("/tables.object")) {
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (SymTables) ois.readObject();
        }
        catch (IOException | ClassNotFoundException e){
            throw new RuntimeException("Can't read tables",e);
        }
    }
}

class T{
    public static void main(String[] args) {
        //SizeOf.sizeof(new SymTables());
        //SizeOf.sizeof(new MoveTables());
        //SizeOf.sizeof(readTables());
        //SizeOf.sizeof(new SymTables());
        computeAndTest();
    }
    static void computeAndTest(){
        long ts=System.currentTimeMillis();
        SymTables symTables =new SymTables();
        System.out.println("Completed... time="+(System.currentTimeMillis()-ts)/1000+"s");
        symTables.proof();
        System.out.println("Completed... time="+(System.currentTimeMillis()-ts)/1000+"s");
    }
}