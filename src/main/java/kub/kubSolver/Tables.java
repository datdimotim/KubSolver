package kub.kubSolver;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Tables{
    public static final int x1_max=2187,y1_max=2048,z1_max=495,x2_max=40320,y2_max=40320,z2_max=24;
    public static final int MAX_DEEP =30;
    private static final int[] convertPovorot=new int[]{0,1,2,3,6,9,12,15,16,17,18};

    private byte[] x1Deep,y1Deep,z1Deep,x2Deep,y2Deep,z2Deep;
    private int[][] x1Move,y1Move,z1Move,x2Move,y2Move,z2Move;
    private byte[][] xy1Deep,xz1Deep,yz1Deep,xz2Deep,yz2Deep;
    public static final Tables INSTANCE;

    static {
        try {
            INSTANCE=readData();
        } catch (IOException e) {
            throw new RuntimeException("Can't read tables",e);
        }
    }

    private  Tables(){}

    public byte[] getX1Deep() {return arrayCopy(x1Deep);}
    public byte[] getX2Deep() {return arrayCopy(x2Deep);}
    public byte[] getY1Deep() {return arrayCopy(y1Deep);}
    public byte[] getY2Deep() {return arrayCopy(y2Deep);}
    public byte[] getZ1Deep() {return arrayCopy(z1Deep);}
    public byte[] getZ2Deep() {return arrayCopy(z2Deep);}
    public byte[][] getXy1Deep() {return arrayCopy(xy1Deep);}
    public byte[][] getXz1Deep() {return arrayCopy(xz1Deep);}
    public byte[][] getXz2Deep() {return arrayCopy(xz2Deep);}
    public byte[][] getYz1Deep() {return arrayCopy(yz1Deep);}
    public byte[][] getYz2Deep() {return arrayCopy(yz2Deep);}
    public int[][] getX1Move() {return arrayCopy(x1Move);}
    public int[][] getX2Move() {return arrayCopy(x2Move);}
    public int[][] getY1Move() {return arrayCopy(y1Move);}
    public int[][] getY2Move() {return arrayCopy(y2Move);}
    public int[][] getZ1Move() {return arrayCopy(z1Move);}
    public int[][] getZ2Move() {return arrayCopy(z2Move);}
    public static int[] getConvertPovorot() {return arrayCopy(convertPovorot);}

    private static byte[] arrayCopy(byte[] m){
        byte[] ret=new byte[m.length];
        System.arraycopy(m,0,ret,0,m.length);
        return ret;
    }
    private static byte[][] arrayCopy(byte[][] m){
        byte[][] ret=new byte[m.length][];
        for(int i=0;i<m.length;i++)ret[i]=arrayCopy(m[i]);
        return ret;
    }
    private static int[] arrayCopy(int[] m){
        int[] ret=new int[m.length];
        System.arraycopy(m,0,ret,0,m.length);
        return ret;
    }
    private static int[][] arrayCopy(int[][] m){
        int[][] ret=new int[m.length][];
        for(int i=0;i<m.length;i++)ret[i]=arrayCopy(m[i]);
        return ret;
    }

    public void writeData() throws IOException {
        try(FileOutputStream fos=new FileOutputStream("dataTable.bin")) {
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            DataOutputStream dos = new DataOutputStream(bos);
            writeByteMassiv(x1Deep, dos);
            writeByteMassiv(y1Deep, dos);
            writeByteMassiv(z1Deep, dos);
            writeByteMassiv(x2Deep, dos);
            writeByteMassiv(y2Deep, dos);
            writeByteMassiv(z2Deep, dos);
            writeIntMassiv(x1Move, dos);
            writeIntMassiv(y1Move, dos);
            writeIntMassiv(z1Move, dos);
            writeIntMassiv(x2Move, dos);
            writeIntMassiv(y2Move, dos);
            writeIntMassiv(z2Move, dos);
            writeByteMassiv(xy1Deep, dos);
            writeByteMassiv(xz1Deep, dos);
            writeByteMassiv(yz1Deep, dos);
            writeByteMassiv(xz2Deep, dos);
            writeByteMassiv(yz2Deep, dos);
        }
    }
    public static Tables readData() throws IOException {
        //try(FileInputStream fis = new FileInputStream("dataTable.bin")) {
        try(InputStream fis = Tables.class.getResourceAsStream("/dataTable.bin")) {
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);
            Tables tables = new Tables();
            tables.x1Deep = readByteMassiv(x1_max, dis);
            tables.y1Deep = readByteMassiv(y1_max, dis);
            tables.z1Deep = readByteMassiv(z1_max, dis);
            tables.x2Deep = readByteMassiv(x2_max, dis);
            tables.y2Deep = readByteMassiv(y2_max, dis);
            tables.z2Deep = readByteMassiv(z2_max, dis);
            tables.x1Move = readIntMassiv(19, x1_max, dis);
            tables.y1Move = readIntMassiv(19, y1_max, dis);
            tables.z1Move = readIntMassiv(19, z1_max, dis);
            tables.x2Move = readIntMassiv(convertPovorot.length, x2_max, dis);
            tables.y2Move = readIntMassiv(convertPovorot.length, y2_max, dis);
            tables.z2Move = readIntMassiv(convertPovorot.length, z2_max, dis);
            tables.xy1Deep = readByteMassiv(x1_max, y1_max, dis);
            tables.xz1Deep = readByteMassiv(x1_max, z1_max, dis);
            tables.yz1Deep = readByteMassiv(y1_max, z1_max, dis);
            tables.xz2Deep = readByteMassiv(x2_max, z2_max, dis);
            tables.yz2Deep = readByteMassiv(y2_max, z2_max, dis);
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
        int[] u_p=new int[8];
        int[][] table=new int[convertPovorot.length][x2_max];
        for(int pos=0;pos<x2_max;pos++){
            for(int pov=0;pov<convertPovorot.length;pov++){
                KubCubie.povorotUP(KubKoordinates.x2ToCubie(pos),u_p,convertPovorot[pov]);
                table[pov][pos]= KubCubie.upToX2(u_p);
            }
        }
        return table;
    }
    private static int[][] createY2Move(){
        int[] r_p=new int[12];
        int[][] table=new int[convertPovorot.length][y2_max];
        for(int pos=0;pos<y2_max;pos++){
            for(int pov=0;pov<convertPovorot.length;pov++){
                KubCubie.povorotRP(KubKoordinates.y2ToCubie(pos),r_p,convertPovorot[pov]);
                table[pov][pos]=KubCubie.rpToY2(r_p);
            }
        }
        return table;
    }
    private static int[][] createZ2Move(){
        int[] r_p=new int[12];
        int[][] table=new int[convertPovorot.length][z2_max];
        for(int pos=0;pos<z2_max;pos++){
            for(int pov=0;pov<convertPovorot.length;pov++){
                KubCubie.povorotRP(KubKoordinates.z2ToCubie(pos),r_p,convertPovorot[pov]);
                table[pov][pos]=KubCubie.rpToZ2(r_p);
            }
        }
        return table;
    }
    private static byte[] createDeepTable(int[][] move){
        byte[] deep_table=new byte[move[0].length];
        for(int i=0;i<deep_table.length;i++){
            deep_table[i]=20;
        }
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
        for(int i=0;i<deep_table.length;i++){
            for(int j=0;j<deep_table[0].length;j++) {
                deep_table[i][j] = 20;
            }
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
}
