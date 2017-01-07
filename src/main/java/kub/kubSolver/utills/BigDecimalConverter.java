package kub.kubSolver.utills;

import kub.kubSolver.Tables;

import java.math.BigDecimal;
import java.util.Random;

public final class BigDecimalConverter {
    private static Random random=new Random();
    private static final BigDecimal
            UO_MAX=BigDecimal.valueOf(Tables.x1_max),
            RO_MAX=BigDecimal.valueOf(Tables.y1_max),
            UP_MAX=BigDecimal.valueOf(Tables.x2_max),
            RP_MAX=BigDecimal.valueOf(Combinations.factorial(12)/2);
    public static final BigDecimal MAX_POS=umnozit(umnozit(umnozit(UO_MAX,RO_MAX),UP_MAX),RP_MAX);
    private static BigDecimal plus(BigDecimal a, BigDecimal b){
        return a.add(b);
    }
    private static BigDecimal minus(BigDecimal a, BigDecimal b){
        return a.subtract(b);
    }
    private static BigDecimal umnozit(BigDecimal a, BigDecimal b){
        return a.multiply(b);
    }
    private static BigDecimal delit(BigDecimal a, BigDecimal b){
        return a.divide(b,BigDecimal.ROUND_DOWN);
    }
    public static BigDecimal pack(int uoInt,int roInt,int upInt, int rpInt){
        BigDecimal uo=BigDecimal.valueOf(uoInt);
        BigDecimal ro=BigDecimal.valueOf(roInt);
        BigDecimal up=BigDecimal.valueOf(upInt);
        BigDecimal rp=BigDecimal.valueOf(rpInt);

        BigDecimal tmp=BigDecimal.ZERO;
        tmp=plus(tmp,uo);
        tmp=umnozit(tmp,RO_MAX);
        tmp=plus(tmp,ro);
        tmp=umnozit(tmp,UP_MAX);
        tmp=plus(tmp,up);
        tmp=umnozit(tmp,RP_MAX);
        tmp=plus(tmp,rp);
        return tmp;
    }
    public static int[] unpack(BigDecimal p){
        int[] k=new int[4];
        k[3]=minus(p,umnozit(delit(p,RP_MAX),RP_MAX)).intValue();
        p=delit(p,RP_MAX);
        k[2]=minus(p,umnozit(delit(p,UP_MAX),UP_MAX)).intValue();
        p=delit(p,UP_MAX);
        k[1]=minus(p,umnozit(delit(p,RO_MAX),RO_MAX)).intValue();
        p=delit(p,RO_MAX);
        k[0]=p.intValue();
        return k;
    }
    public static BigDecimal randomPos(){
        int uo=random.nextInt(UO_MAX.intValue());
        int ro=random.nextInt(RO_MAX.intValue());
        int up=random.nextInt(UP_MAX.intValue());
        int rp=random.nextInt(RP_MAX.intValue());
        return pack(uo,ro,up,rp);
    }
}