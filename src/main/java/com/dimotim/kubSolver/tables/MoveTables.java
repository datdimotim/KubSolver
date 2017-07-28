package com.dimotim.kubSolver.tables;

import com.dimotim.kubSolver.kernel.Cubie;
import com.dimotim.kubSolver.kernel.CubieKoordinateConverter;
import com.dimotim.kubSolver.kernel.HodTransforms;

import java.io.Serializable;

import static com.dimotim.kubSolver.kernel.Tables.*;

public final class MoveTables implements Serializable {
    public final char[][] x1Move;
    public final char[][] y1Move;
    public final char[][] z1Move;
    public final char[][] x2Move;
    public final char[][] y2Move;
    public final char[][] z2Move;
    public final char[][] y2CombMove;

    public MoveTables(){
        x1Move=createX1Move();
        y1Move=createY1Move();
        z1Move=createZ1Move();
        x2Move=createX2Move();
        y2Move=createY2Move();
        z2Move=createZ2Move();
        y2CombMove=createY2CombMove();
    }

    private static char[][] createY2CombMove(){
        int[] convertPovorot= HodTransforms.getP10To18();
        int[] r_p=new int[12];
        char[][] table=new char[11][Y_2_COMB];
        for(int pos = 0; pos< Y_2_COMB; pos++){
            for(int pov=0;pov<11;pov++){
                Cubie.povorotRP(CubieKoordinateConverter.y2CombToRp(pos),r_p,convertPovorot[pov]);
                table[pov][pos]= (char) CubieKoordinateConverter.rpToY2Comb(r_p);
            }
        }
        return table;
    }

    private static char[][] createX1Move(){
        int[] u_o=new int[8];
        char[][] table=new char[19][X_1_MAX];
        for(int pos = 0; pos< X_1_MAX; pos++){
            for(int pov=0;pov<19;pov++){
                Cubie.povorotUO(CubieKoordinateConverter.x1ToCubie(pos),u_o,pov);
                table[pov][pos]= (char) CubieKoordinateConverter.uoToX1(u_o);
            }
        }
        return table;
    }
    private static char[][] createY1Move(){
        int[] r_o=new int[12];
        char[][] table=new char[19][Y_1_MAX];
        for(int pos = 0; pos< Y_1_MAX; pos++){
            for(int pov=0;pov<19;pov++){
                Cubie.povorotRO(CubieKoordinateConverter.y1ToCubie(pos),r_o,pov);
                table[pov][pos]= (char) CubieKoordinateConverter.roToY1(r_o);
            }
        }
        return table;
    }
    private static char[][] createZ1Move(){
        int[] r_p=new int[12];
        char[][] table=new char[19][Z_1_MAX];
        for(int pos = 0; pos< Z_1_MAX; pos++){
            for(int pov=0;pov<19;pov++){
                Cubie.povorotRP(CubieKoordinateConverter.z1ToCubie(pos),r_p,pov);
                table[pov][pos]= (char) CubieKoordinateConverter.rpToZ1(r_p);
            }
        }
        return table;
    }
    private static char[][] createX2Move(){
        int[] convertPovorot= HodTransforms.getP10To18();
        int[] u_p=new int[8];
        char[][] table=new char[HodTransforms.NUM_HODS_2][X_2_MAX];
        for(int pos = 0; pos< X_2_MAX; pos++){
            for(int pov=0;pov<HodTransforms.NUM_HODS_2;pov++){
                Cubie.povorotUP(CubieKoordinateConverter.x2ToCubie(pos),u_p,convertPovorot[pov]);
                table[pov][pos]= (char) CubieKoordinateConverter.upToX2(u_p);
            }
        }
        return table;
    }
    private static char[][] createY2Move(){
        int[] convertPovorot=HodTransforms.getP10To18();
        int[] r_p=new int[12];
        char[][] table=new char[HodTransforms.NUM_HODS_2][Y_2_MAX];
        for(int pos = 0; pos< Y_2_MAX; pos++){
            for(int pov=0;pov<HodTransforms.NUM_HODS_2;pov++){
                Cubie.povorotRP(CubieKoordinateConverter.y2ToCubie(pos),r_p,convertPovorot[pov]);
                table[pov][pos]=(char) CubieKoordinateConverter.rpToY2(r_p);
            }
        }
        return table;
    }
    private static char[][] createZ2Move(){
        int[] convertPovorot=HodTransforms.getP10To18();
        int[] r_p=new int[12];
        char[][] table=new char[HodTransforms.NUM_HODS_2][Z_2_MAX];
        for(int pos = 0; pos< Z_2_MAX; pos++){
            for(int pov=0;pov<HodTransforms.NUM_HODS_2;pov++){
                Cubie.povorotRP(CubieKoordinateConverter.z2ToCubie(pos),r_p,convertPovorot[pov]);
                table[pov][pos]=(char) CubieKoordinateConverter.rpToZ2(r_p);
            }
        }
        return table;
    }
}
