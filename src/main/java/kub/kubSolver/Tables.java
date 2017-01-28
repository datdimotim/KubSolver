package kub.kubSolver;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

public class Tables{
    public static final int x1_max=2187;
    public static final int y1_max=2048;
    public static final int z1_max=495;
    public static final int x2_max=40320;
    public static final int y2_max=40320;
    public static final int z2_max=24;

    private byte[] x1Deep;      // 2187
    private byte[] y1Deep;      // 2048
    private byte[] z1Deep;      // 495
    byte[] x2Deep;      // 40 320
    private byte[] y2Deep;      // 40 320
    private byte[] z2Deep;      // 24
    private int[][] x1Move;     // 166 212
    private int[][] y1Move;     // 155 648
    private int[][] z1Move;     // 37 620
    private int[][] x2Move;     // 1 774 080
    private int[][] y2Move;     // 1 774 080
    private int[][] z2Move;     // 1056
    private byte[][] xy1Deep;   // 4 478 976
    private byte[][] xz1Deep;   // 1 082 565
    private byte[][] yz1Deep;   // 1 013 760
    private byte[][] xz2Deep;   // 967 680
    private byte[][] yz2Deep;   // 967 680
                                // total: 12 504 571 bytes
    public XsYTable xsYTable;
    public static final Tables INSTANCE;

    static {
        try {
            INSTANCE=readData();
        } catch (IOException e) {
            throw new RuntimeException("Can't read tables",e);
        }
    }

    private  Tables(){}

    public int[][] getX2Move() {return arrayCopy(x2Move);}
    public int[][] getY2Move() {return arrayCopy(y2Move);}

    private static int[][] arrayCopy(int[][] m){
        int[][] ret=new int[m.length][];
        for(int i=0;i<m.length;i++)ret[i]=m[i].clone();
        return ret;
    }

    public void writeData() throws IOException {
        try(FileOutputStream fos=new FileOutputStream("dataTable.bin")) {
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            DataOutputStream dos = new DataOutputStream(bos);
            ReaderWriter.writeByteMassiv(x1Deep, dos);
            ReaderWriter.writeByteMassiv(y1Deep, dos);
            ReaderWriter.writeByteMassiv(z1Deep, dos);
            ReaderWriter.writeByteMassiv(x2Deep, dos);
            ReaderWriter.writeByteMassiv(y2Deep, dos);
            ReaderWriter.writeByteMassiv(z2Deep, dos);
            ReaderWriter.writeIntMassiv(x1Move, dos);
            ReaderWriter.writeIntMassiv(y1Move, dos);
            ReaderWriter.writeIntMassiv(z1Move, dos);
            ReaderWriter.writeIntMassiv(x2Move, dos);
            ReaderWriter.writeIntMassiv(y2Move, dos);
            ReaderWriter.writeIntMassiv(z2Move, dos);
            ReaderWriter.writeByteMassiv(xy1Deep, dos);
            ReaderWriter.writeByteMassiv(xz1Deep, dos);
            ReaderWriter.writeByteMassiv(yz1Deep, dos);
            ReaderWriter.writeByteMassiv(xz2Deep, dos);
            ReaderWriter.writeByteMassiv(yz2Deep, dos);

        }
    }
    public static Tables readData() throws IOException {
        try(InputStream fis = Tables.class.getResourceAsStream("/dataTable.bin")) {
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);
            Tables tables = new Tables();
            tables.x1Deep = ReaderWriter.readByteMassiv(x1_max, dis);
            tables.y1Deep = ReaderWriter.readByteMassiv(y1_max, dis);
            tables.z1Deep = ReaderWriter.readByteMassiv(z1_max, dis);
            tables.x2Deep = ReaderWriter.readByteMassiv(x2_max, dis);
            tables.y2Deep = ReaderWriter.readByteMassiv(y2_max, dis);
            tables.z2Deep = ReaderWriter.readByteMassiv(z2_max, dis);
            tables.x1Move = ReaderWriter.readIntMassiv(19, x1_max, dis);
            tables.y1Move = ReaderWriter.readIntMassiv(19, y1_max, dis);
            tables.z1Move = ReaderWriter.readIntMassiv(19, z1_max, dis);
            tables.x2Move = ReaderWriter.readIntMassiv(HodTransforms.NUM_HODS_2, x2_max, dis);
            tables.y2Move = ReaderWriter.readIntMassiv(HodTransforms.NUM_HODS_2, y2_max, dis);
            tables.z2Move = ReaderWriter.readIntMassiv(HodTransforms.NUM_HODS_2, z2_max, dis);
            tables.xy1Deep = ReaderWriter.readByteMassiv(x1_max, y1_max, dis);
            tables.xz1Deep = ReaderWriter.readByteMassiv(x1_max, z1_max, dis);
            tables.yz1Deep = ReaderWriter.readByteMassiv(y1_max, z1_max, dis);
            tables.xz2Deep = ReaderWriter.readByteMassiv(x2_max, z2_max, dis);
            tables.yz2Deep = ReaderWriter.readByteMassiv(y2_max, z2_max, dis);
            return tables;
        }
    }
    public static Tables computeTables(){
        Tables tables=new Tables();
        tables.x1Move=createX1Move();
        tables.y1Move=createY1Move();
        tables.z1Move=createZ1Move();
        tables.x2Move=createX2Move();
        tables.y2Move=createY2Move();
        tables.z2Move=createZ2Move();
        tables.x1Deep=createDeepTable(tables.x1Move);
        tables.y1Deep=createDeepTable(tables.y1Move);
        tables.z1Deep=createDeepTable(tables.z1Move);
        tables.x2Deep=createDeepTable(tables.x2Move);
        tables.y2Deep=createDeepTable(tables.y2Move);
        tables.z2Deep=createDeepTable(tables.z2Move);
        tables.xy1Deep=createDeepTable(tables.x1Move,tables.y1Move);
        tables.xz1Deep=createDeepTable(tables.x1Move,tables.z1Move);
        tables.yz1Deep=createDeepTable(tables.y1Move,tables.z1Move);
        tables.xz2Deep=createDeepTable(tables.x2Move,tables.z2Move);
        tables.yz2Deep=createDeepTable(tables.y2Move,tables.z2Move);
        return tables;
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
    public final int tryMoveAndGetDepth2(int[] k, int p){
        int x = x2Move[p][k[0]];
        int y = y2Move[p][k[1]];
        int z = z2Move[p][k[2]];
        int d1= 0;//xy2Deep[x][y];  // or 0
        int d2=xz2Deep[x][z];
        int d3=yz2Deep[y][z];
        return Math.max(d1,Math.max(d2,d3));
    }
    public final void move2(int[] k,int[] kn,int p){
        kn[0] = x2Move[p][k[0]];
        kn[1] = y2Move[p][k[1]];
        kn[2] = z2Move[p][k[2]];
    }
    public final int tryMoveAndGetDepth1(int[] k, int p){
        int x = x1Move[p][k[0]];
        int y = y1Move[p][k[1]];
        int z = z1Move[p][k[2]];
        int d1=xy1Deep[x][y];
        int d2=xz1Deep[x][z];
        int d3=yz1Deep[y][z];
        return Math.max(d1,Math.max(d2,d3));
    }
    public final void move1(int[] k, int[] kn,int p){
        kn[0] = x1Move[p][k[0]];
        kn[1] = y1Move[p][k[1]];
        kn[2] = z1Move[p][k[2]];
    }
    private static class ReaderWriter{
        private static void writeByteMassiv(byte[][] massiv, DataOutputStream dos) throws IOException {
            int size=0;
            for (byte[] srez:massiv)size+=srez.length;
            ByteBuffer byteBuffer=ByteBuffer.allocate(size);
            for(byte[] sr:massiv) for (byte srsr:sr) byteBuffer.put(srsr);
            byteBuffer.position(0);
            dos.write(byteBuffer.array());
        }
        private static void writeByteMassiv(byte[] massiv, DataOutputStream dos) throws IOException {
            writeByteMassiv(new byte[][]{massiv},dos);
        }
        private static void writeIntMassiv(int[] massiv, DataOutputStream dos) throws IOException {
            writeIntMassiv(new int[][]{massiv},dos);
        }
        private static void writeIntMassiv(int[][] massiv, DataOutputStream dos) throws IOException {
            int size=0;
            for (int[] srez:massiv)size+=srez.length;
            size*=4;
            ByteBuffer byteBuffer=ByteBuffer.allocate(size);
            IntBuffer intBuffer=byteBuffer.asIntBuffer();
            for(int[] sr:massiv) for (int srsr:sr) intBuffer.put(srsr);
            byteBuffer.position(0);
            dos.write(byteBuffer.array());
        }
        private static int[][] readIntMassiv(int i, int j, DataInputStream dis) throws IOException {
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
        private static byte[][] readByteMassiv(int i, int j, DataInputStream dis) throws IOException {
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
        private static int[] readIntMassiv(int j,DataInputStream dis) throws IOException {
            return readIntMassiv(1,j,dis)[0];
        }
        private static byte[] readByteMassiv(int j,DataInputStream dis) throws IOException {
            return readByteMassiv(1,j,dis)[0];
        }
    }
}
