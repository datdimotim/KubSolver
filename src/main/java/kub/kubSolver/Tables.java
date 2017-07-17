package kub.kubSolver;

import com.dimotim.compact_arrays.*;
import java.io.*;
import java.util.Arrays;
import static kub.kubSolver.Tables.*;

class Tables implements Serializable{
    static final int X_1_MAX =2187;
    static final int Y_1_MAX =2048;
    static final int Z_1_MAX =495;
    static final int X_2_MAX =40320;
    static final int Y_2_MAX =40320;
    static final int Z_2_MAX =24;
    static final int X_1_SYM_CLASSES =324;
    static final int Y_1_SYM_CLASSES =336;
    static final int Z_1_SYM_CLASSES =81;
    static final int X_2_SYM_CLASSES =2768;
    static final int Y_2_SYM_CLASSES =2768;
    static final int Z_2_SYM_CLASSES =8;

    private final MoveTables moveTables;
    private final SymTables1 symTables1;
    private final SymTables2 symTables2;

    private Tables(){
        moveTables=new MoveTables();
        symTables2=new SymTables2(moveTables);
        symTables1=new SymTables1(moveTables);
    }

    // run first for initialization
    public static void main(String[] args)throws IOException{
        Tables tables=new Tables();
        ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("src/main/resources/tables.object"));
        oos.writeObject(tables);
        oos.close();
    }

    static Tables readTables(){
        try(InputStream fis = Tables.class.getResourceAsStream("/tables.object")) {
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (Tables)ois.readObject();
        }
        catch (IOException | ClassNotFoundException e){
            throw new RuntimeException("Can't read tables",e);
        }
    }

    final void initState1(CoordSet set){
        symTables1.initState(set,moveTables);
    }
    final void initState2(CoordSet set){
        symTables2.initState(set,moveTables);
    }
    final int moveAndGetDepth2(CoordSet in, CoordSet out, int p){
        out.coord[0] = moveTables.x2Move[p][in.coord[0]];
        out.coord[1] = moveTables.y2Move[p][in.coord[1]];
        out.coord[2] = moveTables.z2Move[p][in.coord[2]];
        symTables2.getDeep(in, out);
        return Math.max(out.deep[0],Math.max(out.deep[1],out.deep[2]));
    }
    final int moveAndGetDepth1(CoordSet in, CoordSet out, int p){
        out.coord[0] = moveTables.x1Move[p][in.coord[0]];
        out.coord[1] = moveTables.y1Move[p][in.coord[1]];
        out.coord[2] = moveTables.z1Move[p][in.coord[2]];
        symTables1.getDeep(in,out);
        return Math.max(out.deep[0],Math.max(out.deep[1],out.deep[2]));
    }
    static class CoordSet{
        final int[] coord;
        final int[] deep;
        CoordSet(){
            coord=new int[3];
            deep=new int[3];
        }
        CoordSet(CoordSet set){
            this.coord=set.coord.clone();
            this.deep=set.deep.clone();
        }
    }
}

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

class SymMoveTable {
    private final int[][] symmetryMul; // matrix1*matrix2*vector -> matrix*vector
    private final int[] inverseSymmetry;
    private final int[][] symHods;

    public final int SYMMETRIES;
    public final int ROTATIES;
    public final int CLASSES;
    public final int RAW;
    final char[][][] symMoveTable;   // backing storage   //<class, sym>[povorot][position]
    private final char[][]   classToRaw;     // sym, class
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
        ROTATIES = symHods[0].length;
        CLASSES=classes;
        RAW=rawMoveTable[0].length;
        symMoveTable=new char[2][ROTATIES][CLASSES];
        classToRaw=new char[SYMMETRIES][CLASSES];
        int[][] symTable=createSymTable(rawMoveTable);
        initClassToRaw(rawMoveTable,symTable);
        rawToClass=initRawToClass(symTable);
        initSymMove(rawMoveTable);

        proofRawToClassAndClassToRaw(rawMoveTable,symTable);
        proofMove(rawMoveTable);
        proofMove1(rawMoveTable);
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

    private void proofRawToClassAndClassToRaw(char[][] rawMoveTable,int[][] symTable){
        for(int i=0;i<rawMoveTable[0].length;i++){
            if(i!=classToRaw[rawToClass[1][i]][rawToClass[0][i]])throw new RuntimeException();
        }
    }

    private void proofMove(char[][] rawMoveTable){
        for(int pos=0;pos<rawMoveTable[0].length;pos++){
            for(int np = 0; np< rawMoveTable.length; np++){
                int posEtalon= rawMoveTable[np][pos];
                int posCheck=rawHod(pos,np);
                if(posEtalon!=posCheck)throw new RuntimeException("pos="+pos+" np="+np+" posEtalon="+posEtalon+" posCheck="+posCheck);
            }
        }
    }

    private void proofMove1(char[][] rawMoveTable){
        for(int c=0;c<CLASSES;c++){
            for(int s=0;s<SYMMETRIES;s++) {
                for (int np = 0; np < rawMoveTable.length; np++) {
                    SymPos symPos=new SymPos();
                    symPos.sym=s;
                    symPos.classPos=c;
                    int posEtalon = rawMoveTable[np][symPosToRaw(symPos)];
                    SymPos ratated=new SymPos();
                    doMove(symPos,ratated,np);
                    int posCheck = symPosToRaw(ratated);
                    if (posEtalon != posCheck) throw new RuntimeException();
                }
            }
        }
    }

    void rawToSym(int raw,SymPos symPos){
        symPos.classPos=rawToClass[0][raw];
        symPos.sym=rawToClass[1][raw];
    }

    int symPosToRaw(SymPos symPos){
        return classToRaw[symPos.sym][symPos.classPos];
    }

    void doMove(SymPos in, SymPos out,int np){
        int npSym=symHods[inverseSymmetry[in.sym]][np];
        out.classPos=symMoveTable[0][npSym][in.classPos];
        out.sym=symmetryMul[in.sym][symMoveTable[1][npSym][in.classPos]];
    }

    int rawHod(int raw,int np){
        SymPos in=new SymPos();
        SymPos out=new SymPos();
        rawToSym(raw,in);
        doMove(in,out,np);
        return symPosToRaw(out);
    }
}

class SymDeepTable {
    private final int[][] symmetryMul; // matrix1*matrix2*vector -> matrix*vector
    private final int[] inverseSymmetry;

    private final byte[][] deepTable;
    SymDeepTable(SymMoveTable symPart, SymMoveTable rawPart){
        if(symPart.symMoveTable[0].length==19) {
            symmetryMul = Symmetry.symmetryMulHalf;
            inverseSymmetry = Symmetry.inverseSymmetryHalf;
        }
        else {
            if(symPart.symMoveTable[0].length!=11)throw new RuntimeException();
            symmetryMul = Symmetry.symmetryMul;
            inverseSymmetry = Symmetry.inverseSymmetry;
        }
        deepTable=createDeepTable(symPart,rawPart);
    }
    private byte[][] createDeepTable(SymMoveTable symPart, SymMoveTable rawPart){
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

    private byte[][] createDeepTableRaw(SymMoveTable symPart, SymMoveTable rawPart){
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

    void proofDeepTable(SymMoveTable symPart, SymMoveTable rawPart){
        byte[][] deepTableRaw=createDeepTableRaw(symPart,rawPart);
        for(int i=0;i<deepTableRaw.length;i++){
            for(int j=0;j<deepTableRaw[0].length;j++){
                SymPos symP=new SymPos();
                SymPos rawP=new SymPos();
                symPart.rawToSym(i,symP);
                rawPart.rawToSym(j,rawP);
                rawP.sym=symmetryMul[inverseSymmetry[symP.sym]][rawP.sym];

                if(deepTableRaw[i][j]!=deepTable[symP.classPos][rawPart.symPosToRaw(rawP)])throw new RuntimeException(
                        "i="+i+" j="+j+" deepTable[i][j]="+deepTable[i][j]+" deepTableRaw="+deepTableRaw[symP.classPos][rawPart.symPosToRaw(rawP)]
                );
            }
        }
    }
}

class SymPos{
    int classPos;
    int sym;
}

class SymTables {
    SymMoveTable x1;
    SymMoveTable y1;
    SymMoveTable z1;
    SymMoveTable x2;
    SymMoveTable y2;
    SymMoveTable z2;
    SymDeepTable xy1;
    SymDeepTable xz1;
    SymDeepTable yz1;
    SymDeepTable xy2;
    SymDeepTable xz2;
    SymDeepTable yz2;
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
        xy2 = new SymDeepTable(x2, y2);System.out.println("xy2 deep created");
        xz2 = new SymDeepTable(x2, z2);System.out.println("xz2 deep created");
        yz2 = new SymDeepTable(y2, z2);System.out.println("yz2 deep created");
    }
}

class T{
    public static void main(String[] args) {
        //SizeOf.sizeof(new SymTables());
        //SizeOf.sizeof(new MoveTables());
        //SizeOf.sizeof(readTables());
        long ts=System.currentTimeMillis();
        SymTables symTables =new SymTables();
        new SymDeepTable(symTables.x1, symTables.y1).proofDeepTable(symTables.x1, symTables.y1);
        new SymDeepTable(symTables.x1, symTables.z1).proofDeepTable(symTables.x1, symTables.z1);
        new SymDeepTable(symTables.y1, symTables.z1).proofDeepTable(symTables.y1, symTables.z1);

        new SymDeepTable(symTables.x2, symTables.y2).proofDeepTable(symTables.x2, symTables.y2);
        new SymDeepTable(symTables.x2, symTables.z2).proofDeepTable(symTables.x2, symTables.z2);
        new SymDeepTable(symTables.y2, symTables.z2).proofDeepTable(symTables.y2, symTables.z2);


        //new SymDeepTable2(symTables.x2,symTables.y2);//.proofDeepTable(symTables.x2,symTables.y2);
        //new SymDeepTable2(symTables.x2,symTables.z2);//.proofDeepTable(symTables.x2,symTables.z2);
        //new SymDeepTable2(symTables.y2,symTables.z2);//.proofDeepTable(symTables.y2,symTables.z2);
        System.out.println("Completed... time="+(System.currentTimeMillis()-ts)/1000+"s");
    }
}