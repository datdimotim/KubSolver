package kub.kubSolver;

import java.util.Arrays;

public class XsYTable{
    public static final int KOL_SYM=16;
    public static final int KOL_POV=11;
    private static final int[][] xTransform =Symmetry.x2SymTransform;
    private static final int[][] yTransform=Symmetry.y2SymTransform;
    private static final int[] inverseSymmetry=Symmetry.inverseSymmetry;
    private static final int[][] moveX=Tables.INSTANCE.getX2Move();
    private static final int[][] moveY=Tables.INSTANCE.getY2Move();

    public static final int  x2Size=40320;
    private static final int xSize=2768;
    private static final int ySize=Tables.y2_max;


    private static final int[] extendX=getExtendX();
    private static final int[][] xToSymClass=splitXToClasses();

    public byte[][] xsYDeep;

    public static void main(String[] args) {
        for(int symX=0;symX<xSize;symX++)if(symX!=xToSymClass[0][extendX[symX]])throw new RuntimeException();
        for(int x=0;x<x2Size;x++)if(x!=xTransform[xToSymClass[1][x]][extendX[xToSymClass[0][x]]])throw new RuntimeException();
        int k=0;
        for(int s:xToSymClass[1])if(s==0)k++;
        System.out.println(k);

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
                            if(xmt!=extendX[xToSymClass[0][xm]])throw new RuntimeException();
                                                                             // позиция была получена из extendX
                                                                             // применением симметрии sym
                                                                             // поэтому чтобы вернуть позицию в позицию из
                                                                             // extendX применяем обратную симметрию


                            int ym=moveY[np][y];
                            for(int s=0;s<KOL_SYM;s++) {
                                if(xmt!=xTransform[s][xm])continue;
                                int ymt = yTransform[s][ym];
                                if (deep_table[xms][ymt] > deep + 1) {
                                    deep_table[xms][ymt] = (byte) (deep + 1);
                                }
                            }
                        }
                    }
                }
            }
        }

        for(int d=0;d<20;d++){
            int kol=0;
            for(int i=0;i<deep_table.length;i++){
                for(int j=0; j<deep_table[0].length;j++){
                    if(deep_table[i][j]==d)kol++;
                }
            }
            System.out.println("deep="+d+"  pos="+kol);
        }
    }

    private static int[][] splitXToClasses(){
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

    private static int[] getExtendX(){
        int[][] xToSymClass=getClass0();
        int[] extendX=new int[xSize];
        for(int i=0;i<xToSymClass[0].length;i++)if(xToSymClass[0][i]!=-1)extendX[xToSymClass[0][i]]=i;
        return extendX;
    }

    private static int[][] getClass0(){
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
    private static void test(){
        int[][] xToSymClass=getClass0();
        int count=0;
        for(int b:xToSymClass[0])if(b!=-1)count++;
        System.out.println("count="+count);
        System.out.println(Arrays.deepToString(xToSymClass));

        int[] extendX=new int[xSize];
        for(int i=0;i<xToSymClass[0].length;i++)if(xToSymClass[0][i]!=-1)extendX[xToSymClass[0][i]]=i;

        byte[] deep_table=new byte[xSize];
        Arrays.fill(deep_table,(byte)20);
        deep_table[0]=0;
        for(int deep=0;deep<=20;deep++) {
            for(int i= 0;i< deep_table.length;i++) {
                if (deep_table[i]==deep) {
                    for (int np = 1;np<moveX.length ;np++) {
                        if(xToSymClass[0][moveX[np][extendX[i]]]==-1)continue;
                        if (deep_table[xToSymClass[0][moveX[np][extendX[i]]]]>deep + 1){
                            deep_table[xToSymClass[0][moveX[np][extendX[i]]]]=(byte)(deep+1);
                        }
                    }
                }
            }
        }


        for(int d=0;d<20;d++){
            int kol=0;
            for(int i=0;i<deep_table.length;i++){
                if(deep_table[i]==d)kol++;
            }
            System.out.println("deep="+d+"  pos="+kol);
        }

        System.out.println("==========================");
        deep_table=Tables.INSTANCE.x2Deep;
        for(int d=0;d<20;d++){
            int kol=0;
            for(int i=0;i<deep_table.length;i++){
                if(deep_table[i]==d&&xToSymClass[0][i]!=-1)kol++;
            }
            System.out.println("deep="+d+"  pos="+kol);
        }


        System.exit(0);
    }

}