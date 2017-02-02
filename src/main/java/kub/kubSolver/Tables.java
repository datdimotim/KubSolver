package kub.kubSolver;

import com.dimotim.compact_arrays.*;
import com.dimotim.compact_arrays.IntegerArray;

import java.io.*;
import java.util.Arrays;

import static kub.kubSolver.Tables.*;

class Tables implements Serializable{
    static final int x1_max=2187;
    static final int y1_max=2048;
    static final int z1_max=495;
    static final int x2_max=40320;
    static final int y2_max=40320;
    static final int z2_max=24;

    private final MoveTables moveTables;
    private final SymTables2 symTables2;
    private final SymTables1 symTables1;

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
        char[][] table=new char[19][x1_max];
        for(int pos=0;pos<x1_max;pos++){
            for(int pov=0;pov<19;pov++){
                KubCubie.povorotUO(KubKoordinates.x1ToCubie(pos),u_o,pov);
                table[pov][pos]= (char) KubCubie.uoToX1(u_o);
            }
        }
        return table;
    }
    private static char[][] createY1Move(){
        int[] r_o=new int[12];
        char[][] table=new char[19][y1_max];
        for(int pos=0;pos<y1_max;pos++){
            for(int pov=0;pov<19;pov++){
                KubCubie.povorotRO(KubKoordinates.y1ToCubie(pos),r_o,pov);
                table[pov][pos]= (char) KubCubie.roToY1(r_o);
            }
        }
        return table;
    }
    private static char[][] createZ1Move(){
        int[] r_p=new int[12];
        char[][] table=new char[19][z1_max];
        for(int pos=0;pos<z1_max;pos++){
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
        char[][] table=new char[HodTransforms.NUM_HODS_2][x2_max];
        for(int pos=0;pos<x2_max;pos++){
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
        char[][] table=new char[HodTransforms.NUM_HODS_2][y2_max];
        for(int pos=0;pos<y2_max;pos++){
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
        char[][] table=new char[HodTransforms.NUM_HODS_2][z2_max];
        for(int pos=0;pos<z2_max;pos++){
            for(int pov=0;pov<HodTransforms.NUM_HODS_2;pov++){
                KubCubie.povorotRP(KubKoordinates.z2ToCubie(pos),r_p,convertPovorot[pov]);
                table[pov][pos]=(char) KubCubie.rpToZ2(r_p);
            }
        }
        return table;
    }
}

class SymTables2 implements Serializable{
    //private static final int xSymMax=2768;
    //private static final int ySymMax=2768;
    private final int[] inverseSymmetry= Symmetry.inverseSymmetry;

    private final int[][] yTransform;
    private final int[][] zTransform;

    private final int[][] xToSymClass;
    private final int[][] yToSymClass;

    private final IntegerMatrix xsYDeep;
    private final IntegerMatrix xsZDeep;
    private final IntegerMatrix ysZDeep;

    SymTables2(MoveTables moveTables){
        char[][] moveX = moveTables.x2Move;
        char[][] moveY = moveTables.y2Move;
        char[][] moveZ = moveTables.z2Move;

        int[][] xTransform = InitializerSymTable.createSymTable(moveX);
        yTransform= InitializerSymTable.createSymTable(moveY);
        zTransform= InitializerSymTable.createSymTable(moveZ);

        xToSymClass= InitializerSymTable.splitToClasses(moveX, xTransform);
        yToSymClass= InitializerSymTable.splitToClasses(moveX,yTransform);

        byte[][] d1=InitializerSymTable.computeDeepTable(xToSymClass, xTransform,yTransform, moveX, moveY);
        xsYDeep= InitializerSymTable.packDeepTable(d1);
        d1=InitializerSymTable.computeDeepTable(xToSymClass, xTransform,zTransform, moveX, moveZ);
        xsZDeep= InitializerSymTable.packDeepTable(d1);
        d1=InitializerSymTable.computeDeepTable(yToSymClass,yTransform,zTransform, moveY, moveZ);
        ysZDeep= InitializerSymTable.packDeepTable(d1);
    }
    /*SymTables2(DataInputStream dis)throws IOException{
        xTransform=readIntMassiv(16,Tables.x2_max,dis);
        yTransform=readIntMassiv(16,Tables.y2_max,dis);
        zTransform=readIntMassiv(16,Tables.z2_max,dis);

        xToSymClass=readIntMassiv(2,Tables.x2_max,dis);
        yToSymClass=readIntMassiv(2,Tables.y2_max,dis);

        xsYDeep=readByteMassiv(xSymMax,Tables.y2_max,dis);
        xsZDeep=readByteMassiv(xSymMax,Tables.z2_max,dis);
        ysZDeep=readByteMassiv(ySymMax,Tables.z2_max,dis);
    }*/
    /*void save(DataOutputStream dos)throws IOException{
        writeMassiv(xTransform,dos);
        writeMassiv(yTransform,dos);
        writeMassiv(zTransform,dos);

        writeMassiv(xToSymClass,dos);
        writeMassiv(yToSymClass,dos);

        writeMassiv(xsYDeep,dos);
        writeMassiv(xsZDeep,dos);
        writeMassiv(ysZDeep,dos);
    }*/
    void getDeep(CoordSet pred, CoordSet out){
        int x=out.coord[0];
        int y=out.coord[1];
        int z=out.coord[2];
        int xs=xToSymClass[0][x];
        int s=xToSymClass[1][x];
        int yt=yTransform[inverseSymmetry[s]][y];
        int zt=zTransform[inverseSymmetry[s]][z];
        out.deep[0]=Tracker.track((int)xsYDeep.get(xs,yt),pred.deep[0]);
        out.deep[1]=Tracker.track((int)xsZDeep.get(xs,zt),pred.deep[1]);
        int ys=yToSymClass[0][y];
        int s2=yToSymClass[1][y];
        int zt2=zTransform[inverseSymmetry[s2]][z];
        out.deep[2]=Tracker.track((int)ysZDeep.get(ys,zt2),pred.deep[2]);
    }

    void initState(CoordSet set, final MoveTables moveTables){
        class LocalUtills{
            private void rotate(CoordSet in, CoordSet out, int p){
                out.coord[0] = moveTables.x2Move[p][in.coord[0]];
                out.coord[1] = moveTables.y2Move[p][in.coord[1]];
                out.coord[2] = moveTables.z2Move[p][in.coord[2]];
            }
            private void getDeepByModPrecision(CoordSet set){
                int x=set.coord[0];
                int y=set.coord[1];
                int z=set.coord[2];
                int xs=xToSymClass[0][x];
                int s=xToSymClass[1][x];
                int yt=yTransform[inverseSymmetry[s]][y];
                int zt=zTransform[inverseSymmetry[s]][z];
                set.deep[0]=(int) xsYDeep.get(xs,yt)+30;
                set.deep[1]=(int) xsZDeep.get(xs,zt)+30;
                int ys=yToSymClass[0][y];
                int s2=yToSymClass[1][y];
                int zt2=zTransform[inverseSymmetry[s2]][z];
                set.deep[2]=(int) ysZDeep.get(ys,zt2)+30;
            }
        }
        LocalUtills localUtills=new LocalUtills();

        for(int k=0;k<3;k++) {
            CoordSet set1=new CoordSet(set);
            CoordSet set2=new CoordSet();
            localUtills.getDeepByModPrecision(set1);
            int deep = -1;
            outside: while (true) {
                deep++;
                for (int p = 1; p < 11; p++) {
                    localUtills.rotate(set1, set2, p);
                    getDeep(set1, set2);
                    if (set2.deep[k] >= set1.deep[k]) continue;

                    CoordSet tmp = set1;
                    set1 = set2;
                    set2 = tmp;
                    continue outside;
                }
                break;
            }
            set.deep[k]=deep;
        }
    }
}

class SymTables1 implements Serializable{
    //private static final int xSymMax=168;
    //private static final int ySymMax=336;
    private final int[] inverseSymmetry=Symmetry.inverseSymmetry;

    private final int[][] xTransform;
    private final int[][] zTransform;

    private final int[][] xToSymClass;
    private final int[][] yToSymClassHalf;

    private final IntegerMatrix xsZDeep;
    private final IntegerMatrix ysZDeep;
    private final IntegerMatrix ysXDeep;

    SymTables1(MoveTables moveTables){
        char[][] moveX = moveTables.x1Move;
        char[][] moveY=  moveTables.y1Move;
        char[][] moveZ = moveTables.z1Move;

        xTransform= InitializerSymTable.createSymTable(moveX);
        int[][] yTransform = InitializerSymTable.createSymTable(moveY);
        zTransform= InitializerSymTable.createSymTable(moveZ);

        xToSymClass= InitializerSymTable.splitToClasses(moveX,xTransform);
        yToSymClassHalf= InitializerSymTableHalf.splitToClasses(moveY, yTransform);

        byte[][] d1=InitializerSymTable.computeDeepTable(xToSymClass,xTransform,zTransform, moveX, moveZ);
        xsZDeep = InitializerSymTable.packDeepTable(d1);
        d1 = InitializerSymTableHalf.computeDeepTable(yToSymClassHalf, yTransform,zTransform,moveY,moveZ);
        ysZDeep = InitializerSymTable.packDeepTable(d1);
        d1= InitializerSymTableHalf.computeDeepTable(yToSymClassHalf, yTransform,xTransform,moveY,moveX);
        ysXDeep = InitializerSymTable.packDeepTable(d1);
    }
    /*SymTables1(DataInputStream dis)throws IOException{
        xTransform=readIntMassiv(16,Tables.x1_max,dis);
        yTransform=readIntMassiv(16,Tables.y1_max,dis);
        zTransform=readIntMassiv(16,Tables.z1_max,dis);

        xToSymClass=readIntMassiv(2,Tables.x1_max,dis);
        yToSymClassHalf=readIntMassiv(2,Tables.y1_max,dis);

        xsZDeep=readByteMassiv(xSymMax,Tables.z1_max,dis);
        ysZDeep=readByteMassiv(ySymMax,Tables.z1_max,dis);
        ysXDeep=readByteMassiv(ySymMax,Tables.x1_max,dis);
    }*/
    /*void save(DataOutputStream dos)throws IOException{
        writeMassiv(xTransform,dos);
        writeMassiv(yTransform,dos);
        writeMassiv(zTransform,dos);

        writeMassiv(xToSymClass,dos);
        writeMassiv(yToSymClassHalf,dos);

        writeMassiv(xsZDeep,dos);
        writeMassiv(ysZDeep,dos);
        writeMassiv(ysXDeep,dos);
    }*/
    void getDeep(CoordSet pred, CoordSet out){
        int x=out.coord[0];
        int y=out.coord[1];
        int z=out.coord[2];

        int ys=yToSymClassHalf[0][y];
        int xt=xTransform[inverseSymmetry[yToSymClassHalf[1][y]]][x];
        out.deep[0]=Tracker.track((int) ysXDeep.get(ys,xt),pred.deep[0]);

        int zty=zTransform[inverseSymmetry[yToSymClassHalf[1][y]]][z];
        out.deep[1]=Tracker.track((int) ysZDeep.get(ys,zty),pred.deep[1]);

        int xs=xToSymClass[0][x];
        int s=xToSymClass[1][x];
        int ztx=zTransform[inverseSymmetry[s]][z];
        out.deep[2]=Tracker.track((int) xsZDeep.get(xs,ztx),pred.deep[2]);
    }

    void initState(CoordSet set, final MoveTables moveTables){
        class LocalUtills{
            private void rotate(CoordSet in, CoordSet out, int p){
                out.coord[0] = moveTables.x1Move[p][in.coord[0]];
                out.coord[1] = moveTables.y1Move[p][in.coord[1]];
                out.coord[2] = moveTables.z1Move[p][in.coord[2]];
            }
            private void getDeepByModPrecision(CoordSet set){
                int x=set.coord[0];
                int y=set.coord[1];
                int z=set.coord[2];

                int ys=yToSymClassHalf[0][y];
                int xt=xTransform[inverseSymmetry[yToSymClassHalf[1][y]]][x];
                set.deep[0]=(int) ysXDeep.get(ys,xt)+30;

                int zty=zTransform[inverseSymmetry[yToSymClassHalf[1][y]]][z];
                set.deep[1]=(int) ysZDeep.get(ys,zty)+30;

                int xs=xToSymClass[0][x];
                int s=xToSymClass[1][x];
                int ztx=zTransform[inverseSymmetry[s]][z];
                set.deep[2]=(int) xsZDeep.get(xs,ztx)+30;
            }
        }
        LocalUtills localUtills=new LocalUtills();

        for(int k=0;k<3;k++) {
            CoordSet set1=new CoordSet(set);
            CoordSet set2=new CoordSet();
            localUtills.getDeepByModPrecision(set1);
            int deep = -1;
            outside: while (true) {
                deep++;
                for (int p = 1; p < 19; p++) {
                    localUtills.rotate(set1, set2, p);
                    getDeep(set1, set2);
                    if (set2.deep[k] >= set1.deep[k]) continue;

                    CoordSet tmp = set1;
                    set1 = set2;
                    set2 = tmp;
                    continue outside;
                }
                break;
            }
            set.deep[k]=deep;
        }
    }
}

class InitializerSymTable {
    private static int[] p10to18=HodTransforms.getP10To18();
    private static int[] p18to10=HodTransforms.getP18to10();
    static int[][] createSymTable(char[][] move){
        int[][] symHods=HodTransforms.getSymHodsAllSymmetry();
        if(move.length==19){
            int[][] sym_table=new int[16][move[0].length];
            for(int[] m:sym_table)Arrays.fill(m,-1);
            for(int i=0;i<sym_table.length;i++)sym_table[i][0]=0;
            createSymTable1(move,sym_table,symHods);
            return sym_table;
        }
        else {
            int[][] sym_table=new int[16][move[0].length];
            for(int[] m:sym_table)Arrays.fill(m,-1);
            for(int i=0;i<sym_table.length;i++)sym_table[i][0]=0;
            createSymTable2(move,sym_table,symHods);
            return sym_table;
        }
    }
    private static void createSymTable1(char[][] move, int[][] sym_table,int[][] symHods){
        boolean newMark=true;
        while (newMark) {
            newMark=false;
            for(int pos=0;pos<sym_table[0].length;pos++) {
                if(sym_table[0][pos]==-1)continue;
                for (int p = 1; p < move.length; p++) {
                    int newPos = move[p][pos];
                    if(sym_table[0][newPos]!=-1)continue;
                    newMark=true;
                    for (int s = 0; s < 16; s++) {
                        sym_table[s][newPos] = move[symHods[s][p]][sym_table[s][pos]];
                    }
                }
            }
        }
    }
    private static void createSymTable2(char[][] move, int[][] sym_table,int[][] symHods){
        boolean newMark=true;
        while (newMark) {
            newMark=false;
            for(int pos=0;pos<sym_table[0].length;pos++) {
                if(sym_table[0][pos]==-1)continue;
                for (int p = 1; p < move.length; p++) {
                    int newPos = move[p][pos];
                    if(sym_table[0][newPos]!=-1)continue;
                    newMark=true;
                    for (int s = 0; s < 16; s++) {
                        sym_table[s][newPos] = move[p18to10[symHods[s][p10to18[p]]]][sym_table[s][pos]];
                    }
                }
            }
        }
    }

    private static int[][] getClass0(char[][] move, int[][] transform){
        int[][] toSymClass=new int[2][move[0].length];
        Arrays.fill(toSymClass[0],-1);
        Arrays.fill(toSymClass[1],-1);

        toSymClass[0][0]=0;
        toSymClass[1][0]=0;

        byte[] deep_table=new byte[move[0].length];
        Arrays.fill(deep_table,(byte) 20);
        deep_table[0]=0;
        int symClass=1;
        for(int deep=0;deep<=20;deep++) {
            for(int i= 0;i< deep_table.length;i++) {
                if (deep_table[i]==deep&&toSymClass[0][i]!=-1) {
                    for (int np = 1;np<move.length ;np++) {
                        if (deep_table[move[np][i]]>deep + 1){
                            toSymClass[0][move[np][i]]=symClass++;
                            toSymClass[1][move[np][i]]=0;
                            for (int[] srez : transform) {
                                deep_table[srez[move[np][i]]] = (byte) (deep + 1);
                            }
                        }
                    }
                }
            }
        }
        return toSymClass;
    }
    static int[][] splitToClasses(char[][] move, int[][] transform){
        int[][] toSymClass=getClass0(move,transform);
        for(int p=0;p<toSymClass[0].length;p++){
            if(toSymClass[0][p]!=-1){
                for(int s=0;s<transform.length;s++){
                    if(toSymClass[0][transform[s][p]]==-1){
                        toSymClass[0][transform[s][p]]=toSymClass[0][p];
                        toSymClass[1][transform[s][p]]=s;
                    }
                }
            }
        }
        return toSymClass;
    }
    private static int[] getExtend(int[][] toSymClass){
        int size=0;
        for(int c:toSymClass[0])if(size<=c)size=c+1;
        int[] extend=new int[size];
        for(int i=0;i<toSymClass[0].length;i++)if(toSymClass[1][i]==0)extend[toSymClass[0][i]]=i;
        return extend;
    }

    static byte[][] computeDeepTable(int[][] toSymClass1,int[][] transform1,int[][] transform2,char[][] move1,char[][] move2){
        int[] inverseSymmetry=Symmetry.inverseSymmetry;
        int[] extendX=getExtend(toSymClass1);
        byte[][] deep_table=new byte[extendX.length][move2[0].length];
        for (byte[] m : deep_table) {
            Arrays.fill(m, (byte) 20);
        }
        deep_table[0][0]=0;
        for(int deep=0;deep<=20;deep++) {
            for(int xs= 0;xs< deep_table.length;xs++) {
                for(int y=0;y<deep_table[0].length;y++) {
                    if (deep_table[xs][y] == deep) {
                        for (int np = 1; np < move1.length; np++) {
                            int x=extendX[xs];
                            int xm=move1[np][x];
                            int sym=toSymClass1[1][xm];
                            int xms=toSymClass1[0][xm];
                            int xmt=transform1[inverseSymmetry[sym]][xm];
                            // позиция была получена из extendX
                            // применением симметрии sym
                            // поэтому чтобы вернуть позицию в позицию из
                            // extendX применяем обратную симметрию

                            int ym=move2[np][y];
                            if (deep_table[xms][transform2[inverseSymmetry[sym]][ym]] > deep + 1) {
                                for(int s=0;s<transform1.length;s++) {
                                    if(xmt!=transform1[s][xm])continue;
                                    int ymt = transform2[s][ym];
                                    deep_table[xms][ymt] = (byte) (deep + 1);
                                }
                            }
                        }
                    }
                }
            }
        }
        return deep_table;
    }
    static IntegerMatrix packDeepTable(byte[][] deepTable){
        IntegerMatrix integerMatrix=new IntegerMatrix(deepTable.length,deepTable[0].length,3);
        for(int i=0;i<integerMatrix.iLength;i++){
            for(int j=0;j<integerMatrix.jLength;j++){
                integerMatrix.set(i,j,deepTable[i][j]%3);
            }
        }
        return integerMatrix;
    }
}

class InitializerSymTableHalf {
    private static int[] convertSymHalfToFull={0,1,4,5,8,9,12,13};

    private static int[][] getClass0(char[][] move, int[][] transform){
        int[][] toSymClass=new int[2][move[0].length];
        Arrays.fill(toSymClass[0],-1);
        Arrays.fill(toSymClass[1],-1);

        toSymClass[0][0]=0;
        toSymClass[1][0]=0;

        byte[] deep_table=new byte[move[0].length];
        Arrays.fill(deep_table,(byte) 20);
        deep_table[0]=0;
        int symClass=1;
        for(int deep=0;deep<=20;deep++) {
            for(int i= 0;i< deep_table.length;i++) {
                if (deep_table[i]==deep&&toSymClass[0][i]!=-1) {
                    for (int np = 1;np<move.length ;np++) {
                        if (deep_table[move[np][i]]>deep + 1){
                            toSymClass[0][move[np][i]]=symClass++;
                            toSymClass[1][move[np][i]]=0;
                            for(int s=0;s<8;s++){
                                deep_table[transform[convertSymHalfToFull[s]][move[np][i]]] = (byte) (deep + 1);
                            }
                        }
                    }
                }
            }
        }
        return toSymClass;
    }
    static int[][] splitToClasses(char[][] move, int[][] transform){
        int[][] toSymClass=getClass0(move,transform);
        for(int p=0;p<toSymClass[0].length;p++){
            if(toSymClass[0][p]!=-1){
                for(int s=0;s<8;s++){
                    if(toSymClass[0][transform[convertSymHalfToFull[s]][p]]==-1){
                        toSymClass[0][transform[convertSymHalfToFull[s]][p]]=toSymClass[0][p];
                        toSymClass[1][transform[convertSymHalfToFull[s]][p]]=convertSymHalfToFull[s];
                    }
                }
            }
        }
        return toSymClass;
    }
    private static int[] getExtend(int[][] toSymClass){
        int size=0;
        for(int c:toSymClass[0])if(size<=c)size=c+1;
        int[] extend=new int[size];
        for(int i=0;i<toSymClass[0].length;i++)if(toSymClass[1][i]==0)extend[toSymClass[0][i]]=i;
        return extend;
    }

    static byte[][] computeDeepTable(int[][] toSymClass1,int[][] transform1,int[][] transform2,char[][] move1,char[][] move2){
        int[] inverseSymmetry=Symmetry.inverseSymmetry;
        int[] extendX=getExtend(toSymClass1);
        byte[][] deep_table=new byte[extendX.length][move2[0].length];
        for (byte[] m : deep_table) {
            Arrays.fill(m, (byte) 20);
        }
        deep_table[0][0]=0;
        for(int deep=0;deep<=20;deep++) {
            for(int xs= 0;xs< deep_table.length;xs++) {
                for(int y=0;y<deep_table[0].length;y++) {
                    if (deep_table[xs][y] == deep) {
                        for (int np = 1; np < move1.length; np++) {
                            int x=extendX[xs];
                            int xm=move1[np][x];
                            int sym=toSymClass1[1][xm];
                            int xms=toSymClass1[0][xm];
                            int xmt=transform1[inverseSymmetry[sym]][xm];
                            // позиция была получена из extendX
                            // применением симметрии sym
                            // поэтому чтобы вернуть позицию в позицию из
                            // extendX применяем обратную симметрию

                            int ym=move2[np][y];
                            if (deep_table[xms][transform2[inverseSymmetry[sym]][ym]] > deep + 1) {
                                for(int s=0;s<8;s++) {
                                    if(xmt!=transform1[convertSymHalfToFull[s]][xm])continue;
                                    int ymt = transform2[convertSymHalfToFull[s]][ym];
                                    deep_table[xms][ymt] = (byte) (deep + 1);
                                }
                            }
                        }
                    }
                }
            }
        }
        return deep_table;
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