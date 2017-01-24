package kub.kubSolver;

import java.io.*;
import java.nio.ByteBuffer;

public class XsYTable{
    private static final int xSize=2768;
    private static final int ySize=Tables.y2_max;
    private static byte[][] xsYDeep;
    static {
        try(DataInputStream dis=new DataInputStream(new BufferedInputStream(new FileInputStream("xsy2.table")))){
            xsYDeep=readByteMassiv(xSize,ySize,dis);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    private static byte[][] readByteMassiv(int i, int j, DataInputStream dis) throws IOException {
        byte[][] massiv=new byte[i][j];
        byte[] data=new byte[i*j];
        dis.readFully(data);
        for(int n=0;n<i;n++){
            System.arraycopy(data, n * j, massiv[n], 0, j);
        }
        return massiv;
    }
    public static int getDepth(int x, int y){
        int xs=Symmetry.x2Sym[0][x];
        int s=Symmetry.x2Sym[1][x];
        y=Symmetry.y2SymTransform[s][y];
        return xsYDeep[xs][y];
    }

    static void computeTable(){
        int[][] x2Sym=Symmetry.x2Sym;
        byte[][] xsY2Table=new byte[x2Sym[0].length][Tables.y2_max];
        byte[][] xyRaw= createDeepTable(Tables.INSTANCE.getX2Move(),Tables.INSTANCE.getY2Move());
        for(int x=0;x<xyRaw.length;x++){
            int symX=x2Sym[0][x];
            if(x2Sym[1][x]!=0)continue;
            System.arraycopy(xyRaw[x],0,xsY2Table[symX],0,xyRaw[x].length);
        }
    }
    static void writeByteMassiv(byte[][] massiv, DataOutputStream dos) throws IOException {
        int size=0;
        for (byte[] srez:massiv)size+=srez.length;
        ByteBuffer byteBuffer=ByteBuffer.allocate(size);
        for(byte[] sr:massiv) for (byte srsr:sr) byteBuffer.put(srsr);
        byteBuffer.position(0);
        dos.write(byteBuffer.array());
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
}