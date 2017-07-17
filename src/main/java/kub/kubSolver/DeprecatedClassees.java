package kub.kubSolver;

import java.io.Serializable;
import java.util.Arrays;

class InitializerSymTable {
    private static int[] p10to18=HodTransforms.getP10To18();
    private static int[] p18to10=HodTransforms.getP18to10();
    static int[][] createSymTable(char[][] move){
        int[][] symHods=HodTransforms.getSymHodsAllSymmetry();
        if(move.length==19){
            int[][] sym_table=new int[16][move[0].length];
            for(int[] m:sym_table) Arrays.fill(m,-1);
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

        /* */
        for(int d=0;d<20;d++){
            int count=0;
            for(int i=0;i<deep_table.length;i++){
                for(int j=0;j<deep_table[0].length;j++){
                    if(deep_table[i][j]==d)count++;
                }
            }
            System.out.println(count);
        }
        System.out.println();
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

class SymTables2 implements Serializable {
    //private static final int xSymMax=2768;
    //private static final int ySymMax=2768;
    private final int[] inverseSymmetry= Symmetry.inverseSymmetry;

    private final int[][] yTransform;
    private final int[][] zTransform;

    private final int[][] xToSymClass;
    private final int[][] yToSymClass;

    //private final IntegerMatrix xsYDeep;
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
        yToSymClass= InitializerSymTable.splitToClasses(moveX, yTransform);

        byte[][] d1;//=InitializerSymTable.computeDeepTable(xToSymClass, xTransform,yTransform, moveX, moveY);
        //xsYDeep= InitializerSymTable.packDeepTable(d1);
        d1=InitializerSymTable.computeDeepTable(xToSymClass, xTransform,zTransform, moveX, moveZ);
        xsZDeep= InitializerSymTable.packDeepTable(d1);
        d1=InitializerSymTable.computeDeepTable(yToSymClass,yTransform,zTransform, moveY, moveZ);
        ysZDeep= InitializerSymTable.packDeepTable(d1);
    }

    void getDeep(Tables.CoordSet pred, Tables.CoordSet out){
        int x=out.coord[0];
        int y=out.coord[1];
        int z=out.coord[2];
        int xs=xToSymClass[0][x];
        int s=xToSymClass[1][x];
        int yt=yTransform[inverseSymmetry[s]][y];
        int zt=zTransform[inverseSymmetry[s]][z];
        //out.deep[0]=Tracker.track((int)xsYDeep.get(xs,yt),pred.deep[0]);
        out.deep[1]=Tracker.track((int)xsZDeep.get(xs,zt),pred.deep[1]);
        int ys=yToSymClass[0][y];
        int s2=yToSymClass[1][y];
        int zt2=zTransform[inverseSymmetry[s2]][z];
        out.deep[2]=Tracker.track((int)ysZDeep.get(ys,zt2),pred.deep[2]);
    }

    void initState(Tables.CoordSet set, final MoveTables moveTables){
        class LocalUtills{
            private void rotate(Tables.CoordSet in, Tables.CoordSet out, int p){
                out.coord[0] = moveTables.x2Move[p][in.coord[0]];
                out.coord[1] = moveTables.y2Move[p][in.coord[1]];
                out.coord[2] = moveTables.z2Move[p][in.coord[2]];
            }
            private void getDeepByModPrecision(Tables.CoordSet set){
                int x=set.coord[0];
                int y=set.coord[1];
                int z=set.coord[2];
                int xs=xToSymClass[0][x];
                int s=xToSymClass[1][x];
                int yt=yTransform[inverseSymmetry[s]][y];
                int zt=zTransform[inverseSymmetry[s]][z];
                //set.deep[0]=(int) xsYDeep.get(xs,yt)+30;
                set.deep[1]=(int) xsZDeep.get(xs,zt)+30;
                int ys=yToSymClass[0][y];
                int s2=yToSymClass[1][y];
                int zt2=zTransform[inverseSymmetry[s2]][z];
                set.deep[2]=(int) ysZDeep.get(ys,zt2)+30;
            }
        }
        LocalUtills localUtills=new LocalUtills();

        for(int k=0;k<3;k++) {
            Tables.CoordSet set1=new Tables.CoordSet(set);
            Tables.CoordSet set2=new Tables.CoordSet();
            localUtills.getDeepByModPrecision(set1);
            int deep = -1;
            outside: while (true) {
                deep++;
                for (int p = 1; p < 11; p++) {
                    localUtills.rotate(set1, set2, p);
                    getDeep(set1, set2);
                    if (set2.deep[k] >= set1.deep[k]) continue;

                    Tables.CoordSet tmp = set1;
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
    final int[] inverseSymmetry=Symmetry.inverseSymmetry;
    final int[][] xTransform;
    final int[][] zTransform;

    final int[][] xToSymClass;
    final int[][] yToSymClassHalf;

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

    void getDeep(Tables.CoordSet pred, Tables.CoordSet out){
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

    void initState(Tables.CoordSet set, final MoveTables moveTables){
        class LocalUtills{
            private void rotate(Tables.CoordSet in, Tables.CoordSet out, int p){
                out.coord[0] = moveTables.x1Move[p][in.coord[0]];
                out.coord[1] = moveTables.y1Move[p][in.coord[1]];
                out.coord[2] = moveTables.z1Move[p][in.coord[2]];
            }
            private void getDeepByModPrecision(Tables.CoordSet set){
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
            Tables.CoordSet set1=new Tables.CoordSet(set);
            Tables.CoordSet set2=new Tables.CoordSet();
            localUtills.getDeepByModPrecision(set1);
            int deep = -1;
            outside: while (true) {
                deep++;
                for (int p = 1; p < 19; p++) {
                    localUtills.rotate(set1, set2, p);
                    getDeep(set1, set2);
                    if (set2.deep[k] >= set1.deep[k]) continue;

                    Tables.CoordSet tmp = set1;
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
