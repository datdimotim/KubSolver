package kub.kubSolver;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
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
}