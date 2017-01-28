package kub.kubSolver;

import java.io.*;
import java.nio.ByteBuffer;
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
    private SimpleDeepTables simpleDeepTables;
    private ExtendDeepTables extendDeepTables;
    private XsYTable xsYTable;

    public static Tables INSTANCE;

    public Tables(boolean inResource){
        if(!inResource)computeTables();
        else readData();
    }

    public static void main(String[] args) {
        Tables tables=new Tables(false);
        int kol=0;
        for(int d=0;d<19;d++) {
            for (int i = 0; i < tables.xsYTable.xsYDeep.length; i++) {
                for (int j = 0; j < tables.xsYTable.xsYDeep[0].length; j++) {
                    if(d==tables.xsYTable.xsYDeep[i][j])kol++;
                }
            }
            System.out.println("deep= "+d+" pos= "+kol);
            kol=0;
        }
    }

    private void computeTables(){
        moveTables=new MoveTables();
        simpleDeepTables=new SimpleDeepTables(moveTables);
        extendDeepTables=new ExtendDeepTables(moveTables);
        xsYTable=new XsYTable(moveTables);
    }

    public void save() throws IOException {
        try(FileOutputStream fos=new FileOutputStream("dataTable.bin")) {
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            DataOutputStream dos = new DataOutputStream(bos);
            moveTables.save(dos);
            simpleDeepTables.save(dos);
            extendDeepTables.save(dos);
        }
    }
    private void readData(){
        try(InputStream fis = Tables.class.getResourceAsStream("/dataTable.bin")) {
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);
            moveTables=new MoveTables(dis);
            simpleDeepTables=new SimpleDeepTables(dis);
            extendDeepTables=new ExtendDeepTables(dis);
        }
        catch (IOException e){
            throw new RuntimeException("Can't read tables",e);
        }
    }
    public final int tryMoveAndGetDepth2(int[] k, int p){
        int x = moveTables.x2Move[p][k[0]];
        int y = moveTables.y2Move[p][k[1]];
        int z = moveTables.z2Move[p][k[2]];
        int d1= xsYTable!=null ? xsYTable.getDeep(x,y) : 0;
        int d2=extendDeepTables.xz2Deep[x][z];
        int d3=extendDeepTables.yz2Deep[y][z];
        return Math.max(d1,Math.max(d2,d3));
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
    int[][] x1Move;     // 166 212
    int[][] y1Move;     // 155 648
    int[][] z1Move;     // 37 620
    int[][] x2Move;     // 1 774 080
    int[][] y2Move;     // 1 774 080
    int[][] z2Move;     // 1056

    MoveTables(DataInputStream dis) throws IOException {
        readTables(dis);
    }
    MoveTables(){
        computeTables();
    }
    void save(DataOutputStream dos) throws IOException {
        writeIntMassiv(x1Move, dos);
        writeIntMassiv(y1Move, dos);
        writeIntMassiv(z1Move, dos);
        writeIntMassiv(x2Move, dos);
        writeIntMassiv(y2Move, dos);
        writeIntMassiv(z2Move, dos);
    }
    private void readTables(DataInputStream dis) throws IOException {
        x1Move = ReaderWriter.readIntMassiv(19, x1_max, dis);
        y1Move = ReaderWriter.readIntMassiv(19, y1_max, dis);
        z1Move = ReaderWriter.readIntMassiv(19, z1_max, dis);
        x2Move = ReaderWriter.readIntMassiv(HodTransforms.NUM_HODS_2, x2_max, dis);
        y2Move = ReaderWriter.readIntMassiv(HodTransforms.NUM_HODS_2, y2_max, dis);
        z2Move = ReaderWriter.readIntMassiv(HodTransforms.NUM_HODS_2, z2_max, dis);
    }
    private void computeTables(){
        x1Move=createX1Move();
        y1Move=createY1Move();
        z1Move=createZ1Move();
        x2Move=createX2Move();
        y2Move=createY2Move();
        z2Move=createZ2Move();
    }

    private static int[][] createX1Move(){
        int[] u_o=new int[8];
        int[][] table=new int[19][x1_max];
        for(int pos=0;pos<x1_max;pos++){
            for(int pov=0;pov<19;pov++){
                KubCubie.povorotUO(KubKoordinates.x1ToCubie(pos),u_o,pov);
                table[pov][pos]= KubCubie.uoToX1(u_o);
            }
        }
        return table;
    }
    private static int[][] createY1Move(){
        int[] r_o=new int[12];
        int[][] table=new int[19][y1_max];
        for(int pos=0;pos<y1_max;pos++){
            for(int pov=0;pov<19;pov++){
                KubCubie.povorotRO(KubKoordinates.y1ToCubie(pos),r_o,pov);
                table[pov][pos]= KubCubie.roToY1(r_o);
            }
        }
        return table;
    }
    private static int[][] createZ1Move(){
        int[] r_p=new int[12];
        int[][] table=new int[19][z1_max];
        for(int pos=0;pos<z1_max;pos++){
            for(int pov=0;pov<19;pov++){
                KubCubie.povorotRP(KubKoordinates.z1ToCubie(pos),r_p,pov);
                table[pov][pos]= KubCubie.rpToZ1(r_p);
            }
        }
        return table;
    }
    private static int[][] createX2Move(){
        int[] convertPovorot=HodTransforms.getP10To18();
        int[] u_p=new int[8];
        int[][] table=new int[HodTransforms.NUM_HODS_2][x2_max];
        for(int pos=0;pos<x2_max;pos++){
            for(int pov=0;pov<HodTransforms.NUM_HODS_2;pov++){
                KubCubie.povorotUP(KubKoordinates.x2ToCubie(pos),u_p,convertPovorot[pov]);
                table[pov][pos]= KubCubie.upToX2(u_p);
            }
        }
        return table;
    }
    private static int[][] createY2Move(){
        int[] convertPovorot=HodTransforms.getP10To18();
        int[] r_p=new int[12];
        int[][] table=new int[HodTransforms.NUM_HODS_2][y2_max];
        for(int pos=0;pos<y2_max;pos++){
            for(int pov=0;pov<HodTransforms.NUM_HODS_2;pov++){
                KubCubie.povorotRP(KubKoordinates.y2ToCubie(pos),r_p,convertPovorot[pov]);
                table[pov][pos]=KubCubie.rpToY2(r_p);
            }
        }
        return table;
    }
    private static int[][] createZ2Move(){
        int[] convertPovorot=HodTransforms.getP10To18();
        int[] r_p=new int[12];
        int[][] table=new int[HodTransforms.NUM_HODS_2][z2_max];
        for(int pos=0;pos<z2_max;pos++){
            for(int pov=0;pov<HodTransforms.NUM_HODS_2;pov++){
                KubCubie.povorotRP(KubKoordinates.z2ToCubie(pos),r_p,convertPovorot[pov]);
                table[pov][pos]=KubCubie.rpToZ2(r_p);
            }
        }
        return table;
    }
}

class SimpleDeepTables{
    private byte[] x1Deep;      // 2187
    private byte[] y1Deep;      // 2048
    private byte[] z1Deep;      // 495
    private byte[] x2Deep;      // 40 320
    private byte[] y2Deep;      // 40 320
    private byte[] z2Deep;      // 24

    SimpleDeepTables(DataInputStream dis) throws IOException {
        readTables(dis);
    }
    SimpleDeepTables(MoveTables moveTables){
        computeTables(moveTables);
    }
    void save(DataOutputStream dos) throws IOException {
        writeByteMassiv(x1Deep, dos);
        writeByteMassiv(y1Deep, dos);
        writeByteMassiv(z1Deep, dos);
        writeByteMassiv(x2Deep, dos);
        writeByteMassiv(y2Deep, dos);
        writeByteMassiv(z2Deep, dos);
    }
    private void readTables(DataInputStream dis) throws IOException {
        x1Deep=readByteMassiv(x1_max,dis);
        y1Deep=readByteMassiv(y1_max,dis);
        z1Deep=readByteMassiv(z1_max,dis);
        x2Deep=readByteMassiv(x2_max,dis);
        y2Deep=readByteMassiv(y2_max,dis);
        z2Deep=readByteMassiv(z2_max,dis);
    }
    private void computeTables(MoveTables moveTables){
        x1Deep=createDeepTable(moveTables.x1Move);
        y1Deep=createDeepTable(moveTables.y1Move);
        z1Deep=createDeepTable(moveTables.z1Move);
        x2Deep=createDeepTable(moveTables.x2Move);
        y2Deep=createDeepTable(moveTables.y2Move);
        z2Deep=createDeepTable(moveTables.z2Move);
    }

    private static byte[] createDeepTable(int[][] move){
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
    byte[][] xy1Deep;   // 4 478 976
    byte[][] xz1Deep;   // 1 082 565
    byte[][] yz1Deep;   // 1 013 760
    byte[][] xz2Deep;   // 967 680
    byte[][] yz2Deep;   // 967 680

    ExtendDeepTables(DataInputStream dis) throws IOException {
        readTables(dis);
    }
    ExtendDeepTables(MoveTables moveTables){
        computeTables(moveTables);
    }
    void save(DataOutputStream dos) throws IOException {
        writeByteMassiv(xy1Deep, dos);
        writeByteMassiv(yz1Deep, dos);
        writeByteMassiv(xz1Deep, dos);
        writeByteMassiv(xz2Deep, dos);
        writeByteMassiv(yz2Deep, dos);
    }
    private void readTables(DataInputStream dis) throws IOException {
        xy1Deep=readByteMassiv(x1_max,y1_max,dis);
        yz1Deep=readByteMassiv(y1_max,z1_max,dis);
        xz1Deep=readByteMassiv(x1_max,z1_max,dis);
        xz2Deep=readByteMassiv(x2_max,z2_max,dis);
        yz2Deep=readByteMassiv(y2_max,z2_max,dis);
    }
    private void computeTables(MoveTables moveTables){
        xy1Deep=createDeepTable(moveTables.x1Move,moveTables.y1Move);
        yz1Deep=createDeepTable(moveTables.y1Move,moveTables.z1Move);
        xz1Deep=createDeepTable(moveTables.x1Move,moveTables.z1Move);
        xz2Deep=createDeepTable(moveTables.x2Move,moveTables.z2Move);
        yz2Deep=createDeepTable(moveTables.y2Move,moveTables.z2Move);
    }
    private static byte[][] createDeepTable(int[][] move1, int[][] move2){
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

class XsYTable{
    private static final int KOL_SYM=16;
    private final int[][] xTransform;
    private final int[][] yTransform;
    private final int[] inverseSymmetry=Symmetry.inverseSymmetry;
    private final int[][] moveX;
    private final int[][] moveY;

    private static final int  x2Size=40320;
    private static final int xSize=2768;
    private static final int ySize=y2_max;

    final int[][] xToSymClass;
    final byte[][] xsYDeep;

    XsYTable(MoveTables moveTables){
        moveX=moveTables.x2Move;
        moveY=moveTables.y2Move;
        xTransform=InitializerSymTransformTable2Fase.createSymTable(moveX);
        yTransform=InitializerSymTransformTable2Fase.createSymTable(moveY);
        xToSymClass=splitXToClasses();
        xsYDeep=computeDeepTable();
    }
    private byte[][] computeDeepTable(){
        int[] extendX=getExtendX();
        byte[][] deep_table=new byte[xSize][ySize];
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
                                for(int s=0;s<KOL_SYM;s++) {
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

    private int[][] splitXToClasses(){
        int[][] xToSymClass=getClass0();
        for(int p=0;p<xToSymClass[0].length;p++){
            if(xToSymClass[0][p]!=-1){
                for(int s=0;s<KOL_SYM;s++){
                    if(xToSymClass[0][xTransform[s][p]]==-1){
                        xToSymClass[0][xTransform[s][p]]=xToSymClass[0][p];
                        xToSymClass[1][xTransform[s][p]]=s;
                    }
                }
            }
        }
        return xToSymClass;
    }

    private int[] getExtendX(){
        int[] extendX=new int[xSize];
        for(int i=0;i<xToSymClass[0].length;i++)if(xToSymClass[1][i]==0)extendX[xToSymClass[0][i]]=i;
        return extendX;
    }

    private int[][] getClass0(){
        int[][] xToSymClass=new int[2][x2Size];
        Arrays.fill(xToSymClass[0],-1);
        Arrays.fill(xToSymClass[1],-1);

        xToSymClass[0][0]=0;
        xToSymClass[1][0]=0;

        byte[] deep_table=new byte[moveX[0].length];
        Arrays.fill(deep_table,(byte) 20);
        deep_table[0]=0;
        int symClass=1;
        for(int deep=0;deep<=20;deep++) {
            for(int i= 0;i< deep_table.length;i++) {
                if (deep_table[i]==deep&&xToSymClass[0][i]!=-1) {
                    for (int np = 1;np<moveX.length ;np++) {
                        if (deep_table[moveX[np][i]]>deep + 1){
                            xToSymClass[0][moveX[np][i]]=symClass++;//moveX[np][i];
                            xToSymClass[1][moveX[np][i]]=0;
                            for(int s=0;s<KOL_SYM;s++){
                                deep_table[xTransform[s][moveX[np][i]]] = (byte) (deep + 1);
                            }
                        }
                    }
                }
            }
        }
        return xToSymClass;
    }
    public int getDeep(int x,int y){
        int xs=xToSymClass[0][x];
        int s=xToSymClass[1][x];
        int yt=yTransform[inverseSymmetry[s]][y];
        return xsYDeep[xs][yt];
    }
    private static class InitializerSymTransformTable2Fase{
        static int[] p10to18=HodTransforms.getP10To18();
        static int[] p18to10=HodTransforms.getP18to10();
        // used only for 2 fase
        static int[][] createSymTable(int[][] move){
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
        private static void createSymTable1(int[][] move, int[][] sym_table,int[][] symHods){
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
        private static void createSymTable2(int[][] move, int[][] sym_table,int[][] symHods){
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
    }
}

class ReaderWriter {
    static void writeByteMassiv(byte[][] massiv, DataOutputStream dos) throws IOException {
        int size=0;
        for (byte[] srez:massiv)size+=srez.length;
        ByteBuffer byteBuffer=ByteBuffer.allocate(size);
        for(byte[] sr:massiv) for (byte srsr:sr) byteBuffer.put(srsr);
        byteBuffer.position(0);
        dos.write(byteBuffer.array());
    }
    static void writeByteMassiv(byte[] massiv, DataOutputStream dos) throws IOException {
        writeByteMassiv(new byte[][]{massiv},dos);
    }
    static void writeIntMassiv(int[] massiv, DataOutputStream dos) throws IOException {
        writeIntMassiv(new int[][]{massiv},dos);
    }
    static void writeIntMassiv(int[][] massiv, DataOutputStream dos) throws IOException {
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
    static int[] readIntMassiv(int j,DataInputStream dis) throws IOException {
        return readIntMassiv(1,j,dis)[0];
    }
    static byte[] readByteMassiv(int j,DataInputStream dis) throws IOException {
        return readByteMassiv(1,j,dis)[0];
    }
}