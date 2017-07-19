package kub.kubSolver;
import java.io.*;

import static kub.kubSolver.SymTables.*;

public interface Tables<KubState> extends Serializable{
    int X_1_MAX =2187;
    int Y_1_MAX =2048;
    int Z_1_MAX =495;
    int X_2_MAX =40320;
    int Y_2_MAX =40320;
    int Z_2_MAX =24;
    int X_1_SYM_CLASSES =324;
    int Y_1_SYM_CLASSES =336;
    int Z_1_SYM_CLASSES =81;
    int X_2_SYM_CLASSES =2768;
    int Y_2_SYM_CLASSES =2768;
    int Z_2_SYM_CLASSES =8;
    KubState initKubStateFase1(int x,int y,int z);
    KubState initKubStateFase2(int x,int y,int z);
    int moveAndGetDetphFase1(KubState in, KubState out, int np);
    int moveAndGetDetphFase2(KubState in, KubState out, int np);
    KubState newKubState();
    KubState[] newArrayKubState(int length);
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
        int[] convertPovorot=HodTransforms.p10To18;
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
        int[] convertPovorot=HodTransforms.p10To18;
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
        int[] convertPovorot=HodTransforms.p10To18;
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