package kub.kubSolver;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import static kub.kubSolver.Tables.*;
import static kub.kubSolver.ReaderWriter.*;

public class Tables{
    public static final int x1_max=2187;
    public static final int y1_max=2048;
    public static final int z1_max=495;
    public static final int x2_max=40320;
    public static final int y2_max=40320;
    public static final int z2_max=24;

    private MoveTables moveTables;
    private SimpleDeepTables simpleDeepTables;  // not used
    private ExtendDeepTables extendDeepTables;
    private SymTables2 symTables2;

    public static Tables INSTANCE;

    public Tables(boolean inResource){
        if(!inResource)computeTables();
        else readData();
    }

    public static void main(String[] args) throws IOException{
        new Tables(false).save();
    }

    private void computeTables(){
        moveTables=new MoveTables();
        simpleDeepTables=new SimpleDeepTables(moveTables);
        extendDeepTables=new ExtendDeepTables(moveTables);
        symTables2=new SymTables2(moveTables);
    }

    public void save() throws IOException {
        try(FileOutputStream fos=new FileOutputStream("dataTable.bin")) {
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            DataOutputStream dos = new DataOutputStream(bos);
            moveTables.save(dos);
            simpleDeepTables.save(dos);
            extendDeepTables.save(dos);
            symTables2.save(dos);
        }
    }
    private void readData(){
        try(InputStream fis = Tables.class.getResourceAsStream("/dataTable.bin")) {
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);
            moveTables=new MoveTables(dis);
            simpleDeepTables=new SimpleDeepTables(dis);
            extendDeepTables=new ExtendDeepTables(dis);
            symTables2=new SymTables2(dis);
        }
        catch (IOException e){
            throw new RuntimeException("Can't read tables",e);
        }
    }
    public final int tryMoveAndGetDepth2(int[] k, int p){
        int x = moveTables.x2Move[p][k[0]];
        int y = moveTables.y2Move[p][k[1]];
        int z = moveTables.z2Move[p][k[2]];
        return symTables2.getDeep(x,y,z);
    }
    public final void move2(int[] k,int[] kn,int p){
        kn[0] = moveTables.x2Move[p][k[0]];
        kn[1] = moveTables.y2Move[p][k[1]];
        kn[2] = moveTables.z2Move[p][k[2]];
    }
    public final int tryMoveAndGetDepth1(int[] k, int p){
        int x = moveTables.x1Move[p][k[0]];
        int y = moveTables.y1Move[p][k[1]];
        int z = moveTables.z1Move[p][k[2]];
        int d1=extendDeepTables.xy1Deep[x][y];
        int d2=extendDeepTables.xz1Deep[x][z];
        int d3=extendDeepTables.yz1Deep[y][z];
        return Math.max(d1,Math.max(d2,d3));
    }
    public final void move1(int[] k, int[] kn,int p){
        kn[0] = moveTables.x1Move[p][k[0]];
        kn[1] = moveTables.y1Move[p][k[1]];
        kn[2] = moveTables.z1Move[p][k[2]];
    }
}

class MoveTables{
    final char[][] x1Move;     // 166 212
    final char[][] y1Move;     // 155 648
    final char[][] z1Move;     // 37 620
    final char[][] x2Move;     // 1 774 080
    final char[][] y2Move;     // 1 774 080
    final char[][] z2Move;     // 1056

    MoveTables(DataInputStream dis) throws IOException {
        x1Move = ReaderWriter.readCharMassiv(19, x1_max, dis);
        y1Move = ReaderWriter.readCharMassiv(19, y1_max, dis);
        z1Move = ReaderWriter.readCharMassiv(19, z1_max, dis);
        x2Move = ReaderWriter.readCharMassiv(HodTransforms.NUM_HODS_2, x2_max, dis);
        y2Move = ReaderWriter.readCharMassiv(HodTransforms.NUM_HODS_2, y2_max, dis);
        z2Move = ReaderWriter.readCharMassiv(HodTransforms.NUM_HODS_2, z2_max, dis);
    }
    MoveTables(){
        x1Move=createX1Move();
        y1Move=createY1Move();
        z1Move=createZ1Move();
        x2Move=createX2Move();
        y2Move=createY2Move();
        z2Move=createZ2Move();
    }
    void save(DataOutputStream dos) throws IOException {
        writeMassiv(x1Move, dos);
        writeMassiv(y1Move, dos);
        writeMassiv(z1Move, dos);
        writeMassiv(x2Move, dos);
        writeMassiv(y2Move, dos);
        writeMassiv(z2Move, dos);
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

class SimpleDeepTables{
    private final byte[] x1Deep;      // 2187
    private final byte[] y1Deep;      // 2048
    private final byte[] z1Deep;      // 495
    private final byte[] x2Deep;      // 40 320
    private final byte[] y2Deep;      // 40 320
    private final byte[] z2Deep;      // 24

    SimpleDeepTables(DataInputStream dis) throws IOException {
        x1Deep=readByteMassiv(x1_max,dis);
        y1Deep=readByteMassiv(y1_max,dis);
        z1Deep=readByteMassiv(z1_max,dis);
        x2Deep=readByteMassiv(x2_max,dis);
        y2Deep=readByteMassiv(y2_max,dis);
        z2Deep=readByteMassiv(z2_max,dis);
    }
    SimpleDeepTables(MoveTables moveTables){
        x1Deep=createDeepTable(moveTables.x1Move);
        y1Deep=createDeepTable(moveTables.y1Move);
        z1Deep=createDeepTable(moveTables.z1Move);
        x2Deep=createDeepTable(moveTables.x2Move);
        y2Deep=createDeepTable(moveTables.y2Move);
        z2Deep=createDeepTable(moveTables.z2Move);
    }
    void save(DataOutputStream dos) throws IOException {
        ReaderWriter.writeMassiv(x1Deep, dos);
        ReaderWriter.writeMassiv(y1Deep, dos);
        ReaderWriter.writeMassiv(z1Deep, dos);
        ReaderWriter.writeMassiv(x2Deep, dos);
        ReaderWriter.writeMassiv(y2Deep, dos);
        ReaderWriter.writeMassiv(z2Deep, dos);
    }

    private static byte[] createDeepTable(char[][] move){
        byte[] deep_table=new byte[move[0].length];
        Arrays.fill(deep_table,(byte) 20);
        deep_table[0]=0;
        for(int deep=0;deep<=20;deep++) {
            for(int i= 0;i< deep_table.length;i++) {
                if (deep_table[i]==deep) {
                    for (int np = 1;np<move.length ;np++) {
                        if (deep_table[move[np][i]]>deep + 1){
                            deep_table[move[np][i]] = (byte) (deep + 1);
                        }
                    }
                }
            }
        }
        return deep_table;
    }
}

class ExtendDeepTables{
    final byte[][] xy1Deep;   // 4 478 976
    final byte[][] xz1Deep;   // 1 082 565
    final byte[][] yz1Deep;   // 1 013 760
    final byte[][] xz2Deep;   // 967 680
    final byte[][] yz2Deep;   // 967 680

    ExtendDeepTables(DataInputStream dis) throws IOException {
        xy1Deep=readByteMassiv(x1_max,y1_max,dis);
        yz1Deep=readByteMassiv(y1_max,z1_max,dis);
        xz1Deep=readByteMassiv(x1_max,z1_max,dis);
        xz2Deep=readByteMassiv(x2_max,z2_max,dis);
        yz2Deep=readByteMassiv(y2_max,z2_max,dis);
    }
    ExtendDeepTables(MoveTables moveTables){
        xy1Deep=createDeepTable(moveTables.x1Move,moveTables.y1Move);
        yz1Deep=createDeepTable(moveTables.y1Move,moveTables.z1Move);
        xz1Deep=createDeepTable(moveTables.x1Move,moveTables.z1Move);
        xz2Deep=createDeepTable(moveTables.x2Move,moveTables.z2Move);
        yz2Deep=createDeepTable(moveTables.y2Move,moveTables.z2Move);
    }
    void save(DataOutputStream dos) throws IOException {
        ReaderWriter.writeMassiv(xy1Deep, dos);
        ReaderWriter.writeMassiv(yz1Deep, dos);
        ReaderWriter.writeMassiv(xz1Deep, dos);
        ReaderWriter.writeMassiv(xz2Deep, dos);
        ReaderWriter.writeMassiv(yz2Deep, dos);
    }

    private static byte[][] createDeepTable(char[][] move1, char[][] move2){
        byte[][] deep_table=new byte[move1[0].length][move2[0].length];
        for (byte[] m : deep_table) {
            Arrays.fill(m, (byte) 20);
        }
        deep_table[0][0]=0;
        for(int deep=0;deep<=20;deep++) {
            for(int i= 0;i< deep_table.length;i++) {
                for(int j=0;j<deep_table[0].length;j++) {
                    if (deep_table[i][j] == deep) {
                        for (int np = 1; np < move1.length; np++) {
                            if (deep_table[move1[np][i]][move2[np][j]] > deep + 1) {
                                deep_table[move1[np][i]][move2[np][j]] = (byte) (deep + 1);
                            }
                        }
                    }
                }
            }
        }
        return deep_table;
    }
}

class SymTables2{
    private static final int xSymMax=2768;
    private static final int ySymMax=2768;
    private final int[] inverseSymmetry= Symmetry.inverseSymmetry;

    private final int[][] xTransform;
    private final int[][] yTransform;
    private final int[][] zTransform;

    private final int[][] xToSymClass;
    private final int[][] yToSymClass;

    private final byte[][] xsYDeep;
    private final byte[][] xsZDeep;
    private final byte[][] ysZDeep;

    SymTables2(MoveTables moveTables){
        char[][] moveX = moveTables.x2Move;
        char[][] moveY = moveTables.y2Move;
        char[][] moveZ = moveTables.z2Move;

        xTransform= InitializerSymTable.createSymTable(moveX);
        yTransform= InitializerSymTable.createSymTable(moveY);
        zTransform= InitializerSymTable.createSymTable(moveZ);

        xToSymClass= InitializerSymTable.splitToClasses(moveX,xTransform);
        yToSymClass= InitializerSymTable.splitToClasses(moveX,yTransform);

        xsYDeep= InitializerSymTable.computeDeepTable(xToSymClass,xTransform,yTransform, moveX, moveY);
        xsZDeep= InitializerSymTable.computeDeepTable(xToSymClass,xTransform,zTransform, moveX, moveZ);
        ysZDeep= InitializerSymTable.computeDeepTable(yToSymClass,yTransform,zTransform, moveY, moveZ);
    }
    SymTables2(DataInputStream dis)throws IOException{
        xTransform=readIntMassiv(16,Tables.x2_max,dis);
        yTransform=readIntMassiv(16,Tables.y2_max,dis);
        zTransform=readIntMassiv(16,Tables.z2_max,dis);

        xToSymClass=readIntMassiv(2,Tables.x2_max,dis);
        yToSymClass=readIntMassiv(2,Tables.y2_max,dis);

        xsYDeep=readByteMassiv(xSymMax,Tables.y2_max,dis);
        xsZDeep=readByteMassiv(xSymMax,Tables.z2_max,dis);
        ysZDeep=readByteMassiv(ySymMax,Tables.z2_max,dis);
    }
    void save(DataOutputStream dos)throws IOException{
        writeMassiv(xTransform,dos);
        writeMassiv(yTransform,dos);
        writeMassiv(zTransform,dos);

        writeMassiv(xToSymClass,dos);
        writeMassiv(yToSymClass,dos);

        writeMassiv(xsYDeep,dos);
        writeMassiv(xsZDeep,dos);
        writeMassiv(ysZDeep,dos);
    }
    int getDeep(int x,int y,int z){
        int xs=xToSymClass[0][x];
        int s=xToSymClass[1][x];
        int yt=yTransform[inverseSymmetry[s]][y];
        int zt=zTransform[inverseSymmetry[s]][z];
        int d1=xsYDeep[xs][yt];
        int d2=xsZDeep[xs][zt];

        int ys=yToSymClass[0][y];
        int s2=yToSymClass[1][y];
        int zt2=zTransform[inverseSymmetry[s2]][z];
        int d3=ysZDeep[ys][zt2];

        return Math.max(d1,Math.max(d2,d3));
    }
}

class ReaderWriter {
    static void writeMassiv(byte[][] massiv, DataOutputStream dos) throws IOException {
        int size=0;
        for (byte[] srez:massiv)size+=srez.length;
        ByteBuffer byteBuffer=ByteBuffer.allocate(size);
        for(byte[] sr:massiv) for (byte srsr:sr) byteBuffer.put(srsr);
        byteBuffer.position(0);
        dos.write(byteBuffer.array());
    }
    static void writeMassiv(char[] massiv, DataOutputStream dos) throws IOException {
        writeMassiv(new char[][]{massiv},dos);
    }
    static void writeMassiv(char[][] massiv, DataOutputStream dos) throws IOException {
        int size=0;
        for (char[] srez:massiv)size+=srez.length;
        size*=2;
        ByteBuffer byteBuffer=ByteBuffer.allocate(size);
        CharBuffer charBuffer=byteBuffer.asCharBuffer();
        for(char[] sr:massiv) for (char srsr:sr) charBuffer.put(srsr);
        byteBuffer.position(0);
        dos.write(byteBuffer.array());
    }
    static void writeMassiv(byte[] massiv, DataOutputStream dos) throws IOException {
        writeMassiv(new byte[][]{massiv},dos);
    }
    static void writeMassiv(int[] massiv, DataOutputStream dos) throws IOException {
        writeMassiv(new int[][]{massiv},dos);
    }
    static void writeMassiv(int[][] massiv, DataOutputStream dos) throws IOException {
        int size=0;
        for (int[] srez:massiv)size+=srez.length;
        size*=4;
        ByteBuffer byteBuffer=ByteBuffer.allocate(size);
        IntBuffer intBuffer=byteBuffer.asIntBuffer();
        for(int[] sr:massiv) for (int srsr:sr) intBuffer.put(srsr);
        byteBuffer.position(0);
        dos.write(byteBuffer.array());
    }
    static int[][] readIntMassiv(int i, int j, DataInputStream dis) throws IOException {
        int[][] massiv=new int[i][j];
        byte[] data=new byte[i*j*4];
        dis.readFully(data);
        IntBuffer intBuffer=ByteBuffer.wrap(data).asIntBuffer();
        for(int n=0;n<i;n++){
            for(int k=0;k<j;k++){
                massiv[n][k]=intBuffer.get(n*j+k);
            }
        }
        return massiv;
    }

    static byte[][] readByteMassiv(int i, int j, DataInputStream dis) throws IOException {
        byte[][] massiv=new byte[i][j];
        byte[] data=new byte[i*j];
        dis.readFully(data);
        ByteBuffer byteBuffer=ByteBuffer.wrap(data);
        for(int n=0;n<i;n++){
            for(int k=0;k<j;k++){
                massiv[n][k]=byteBuffer.get(n*j+k);
            }
        }
        return massiv;
    }
    static byte[] readByteMassiv(int j,DataInputStream dis) throws IOException {
        return readByteMassiv(1,j,dis)[0];
    }
    static int[] readIntMassiv(int j,DataInputStream dis) throws IOException {
        return readIntMassiv(1,j,dis)[0];
    }
    static char[][] readCharMassiv(int i, int j, DataInputStream dis) throws IOException {
        char[][] massiv=new char[i][j];
        byte[] data=new byte[i*j*2];
        dis.readFully(data);
        CharBuffer byteBuffer=ByteBuffer.wrap(data).asCharBuffer();
        for(int n=0;n<i;n++){
            for(int k=0;k<j;k++){
                massiv[n][k]=byteBuffer.get(n*j+k);
            }
        }
        return massiv;
    }
    static char[] readCharMassiv(int j,DataInputStream dis) throws IOException {
        return readCharMassiv(1,j,dis)[0];
    }
}

class InitializerSymTable {
    private static int[] p10to18=HodTransforms.getP10To18();
    private static int[] p18to10=HodTransforms.getP18to10();
    // used only for 2 fase
    static int[][] createSymTable(char[][] move){
        int[][] symHods=HodTransforms.getSymHodsAllSymmetry();
        if(move.length==19){
            int[][] sym_table=new int[48][move[0].length];
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
    // not used
    private static void createSymTable1(char[][] move, int[][] sym_table,int[][] symHods){
        if(true)throw new RuntimeException();
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
                            for(int s=0;s<transform.length;s++){
                                deep_table[transform[s][move[np][i]]] = (byte) (deep + 1);
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
        for(int c:toSymClass[0])if(size<c)size=c+1;
        int[] extend=new int[size];
        for(int i=0;i<toSymClass[0].length;i++)if(toSymClass[1][i]==0)extend[toSymClass[0][i]]=i;
        return extend;
    }

    static byte[][] computeDeepTable(int[][] xToSymClass,int[][] xTransform,int[][] yTransform,char[][] moveX,char[][] moveY){
        int[] inverseSymmetry=Symmetry.inverseSymmetry;
        int[] extendX=getExtend(xToSymClass);
        byte[][] deep_table=new byte[extendX.length][moveY[0].length];
        for (byte[] m : deep_table) {
            Arrays.fill(m, (byte) 20);
        }
        deep_table[0][0]=0;
        for(int deep=0;deep<=20;deep++) {
            for(int xs= 0;xs< deep_table.length;xs++) {
                for(int y=0;y<deep_table[0].length;y++) {
                    if (deep_table[xs][y] == deep) {
                        for (int np = 1; np < moveX.length; np++) {
                            int x=extendX[xs];
                            int xm=moveX[np][x];
                            //if(xToSymClass[1][xm]!=0)continue;
                            int sym=xToSymClass[1][xm];
                            int xms=xToSymClass[0][xm];
                            int xmt=xTransform[inverseSymmetry[sym]][xm];
                            // позиция была получена из extendX
                            // применением симметрии sym
                            // поэтому чтобы вернуть позицию в позицию из
                            // extendX применяем обратную симметрию

                            int ym=moveY[np][y];
                            if (deep_table[xms][yTransform[inverseSymmetry[sym]][ym]] > deep + 1) {
                                for(int s=0;s<xTransform.length;s++) {
                                    if(xmt!=xTransform[s][xm])continue;
                                    int ymt = yTransform[s][ym];
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