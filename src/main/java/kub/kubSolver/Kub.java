package kub.kubSolver;

import java.math.BigDecimal;
import java.util.Random;

import static kub.kubSolver.Kub.KUB_ERROR.*;

public final class Kub{
    private Cubie cubie=new Cubie();
    public Kub(Kub kub){
        cubie=new Cubie(kub.cubie);
    }
    public Kub(boolean isRandom){if(isRandom)randomPos();}
    public Kub(int[][][] grani) throws InvalidPositionException {
        cubie=new Cubie(grani);
    }
    public Kub(BigDecimal pos){
        cubie=Cubie.valueOf(pos);
    }
    public BigDecimal getNumberPos(){
        return cubie.toNumberPos();
    }
    public void randomPos(){
        cubie=Cubie.randomPos();
    }
    public int[][][] getGrani(){
        return cubie.toGrani();
    }
    public String toString(){
        int[][][] grani=getGrani();
        StringBuilder str= new StringBuilder("gran 0         gran 1         gran 2         gran 3         gran 4         gran 5\n");
        for(int i=0;i<3;i++){
            for(int g=0;g<6;g++){
                for(int j=0;j<3;j++){
                    str.append(grani[g][i][j]);
                }
                str.append("            ");
            }
            str.append("\n");
        }
        return str.toString();
    }
    public void povorot(int np){
        cubie.povorot(np);
    }
    public static class InvalidPositionException extends Exception{
        private final KUB_ERROR trable;
        private InvalidPositionException(KUB_ERROR msg){
            super(msg.name());
            trable=msg;
        }
        public KUB_ERROR getTrable(){return trable;}

    }
    public enum KUB_ERROR {
        INVALID_REBRO_CUBIE,
        REBRO_ALREADY_PRESENT,
        INVALID_UGOL_CUBIE,
        UGOL_ALREADY_PRESENT,
        INVALID_REBRO_SUM_ORIENTATION,
        INVALID_UGOL_SUM_ORIENTATION,
        INVALID_SUM_PERESTANOVKA
    }
    private final static class Cubie {
        private final int[] u_p;
        private final int[] u_o;
        private final int[] r_p;
        private final int[] r_o;
        private final int[] tmp_u_p=new int[8];
        private final int[] tmp_u_o=new int[8];
        private final int[] tmp_r_p=new int[12];
        private final int[] tmp_r_o=new int[12];


        private Cubie(Cubie cubie){
            u_p=cubie.u_p.clone();
            r_p=cubie.r_p.clone();
            u_o=cubie.u_o.clone();
            r_o=cubie.r_o.clone();
        }

        private Cubie(){
            r_o=new int[12];
            u_o=new int[8];
            u_p = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
            r_p = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        }
        private Cubie(int[][][] grani) throws InvalidPositionException {
            int[] facelet= KubGrani.graniToFacelet(grani);
            u_p=KubFacelet.faceletToUP(facelet);
            r_p=KubFacelet.faceletToRP(facelet);
            u_o=KubFacelet.faceletToUO(facelet);
            r_o=KubFacelet.faceletToRO(facelet);
            valid();
        }
        private static Cubie randomPos(){
            return valueOf(BigDecimalConverter.randomPos());
        }
        private Cubie(int[] u_o, int[] u_p, int[] r_o, int[] r_p) throws InvalidPositionException {
            this.r_o=new int[12];
            this.u_o=new int[8];
            this.u_p = new int[8];
            this.r_p = new int[12];
            System.arraycopy(r_o,0,this.r_o,0,12);
            System.arraycopy(r_p,0,this.r_p,0,12);
            System.arraycopy(u_o,0,this.u_o,0,8);
            System.arraycopy(u_p,0,this.u_p,0,8);
            valid();
        }
        private static Cubie valueOf(BigDecimal pos){
            if(pos.compareTo(BigDecimalConverter.MAX_POS)>=0||pos.compareTo(BigDecimal.ZERO)<0)
                throw new IllegalArgumentException(pos+">=posMax="+BigDecimalConverter.MAX_POS);
            int[] k= BigDecimalConverter.unpack(pos);
            int uo=k[0];
            int ro=k[1];
            int up=k[2];
            int rp=k[3];
            int[] u_o= Combinations.schetOrientation(uo,3,8);
            int[] r_o=Combinations.schetOrientation(ro,2,12);
            int[] u_p=Combinations.schetPerestanovka(up,8);
            int[] r_p1=Combinations.schetPerestanovka(rp*2,12);
            int[] r_p2=Combinations.schetPerestanovka(rp*2+1,12);

            boolean r1 = Combinations.chetNechetPerestanovka(r_p1);
            boolean r2 = Combinations.chetNechetPerestanovka(r_p2);
            boolean u = Combinations.chetNechetPerestanovka(u_p);

            if(r1==r2)throw new RuntimeException();
            try {
                if(r1==u)return new Cubie(u_o,u_p,r_o,r_p1);
                else return new Cubie(u_o,u_p,r_o,r_p2);
            }
            catch (InvalidPositionException e){
                throw new RuntimeException(e);
            }
        }
        private BigDecimal toNumberPos(){
            int uo=Combinations.schetOrientation(u_o,3);
            int up=Combinations.schetPerestanovka(u_p);
            int rp=Combinations.schetPerestanovka(r_p)/2;
            int ro=Combinations.schetOrientation(r_o,2);
            return BigDecimalConverter.pack(uo,ro,up,rp);
        }

        private void povorot(int np){
            System.arraycopy(r_o,0,tmp_r_o,0,r_o.length);
            System.arraycopy(r_p,0,tmp_r_p,0,r_p.length);
            System.arraycopy(u_o,0,tmp_u_o,0,u_o.length);
            System.arraycopy(u_p,0,tmp_u_p,0,u_p.length);
            KubCubie.povorotRO(tmp_r_o,r_o,np);
            KubCubie.povorotRP(tmp_r_p,r_p,np);
            KubCubie.povorotUO(tmp_u_o,u_o,np);
            KubCubie.povorotUP(tmp_u_p,u_p,np);
        }
        private int[][][] toGrani(){
            int[][][] grani=KubFacelet.faceletToGrani(KubCubie.cubieToFacelet(u_o,u_p,r_o,r_p));
            for (int i=0;i<grani.length;i++)grani[i][1][1]=i;
            return grani;
        }
        private void valid() throws InvalidPositionException{
            {
                int sum = 0;
                for (int s : u_o) sum += s;
                if (sum != sum / 3 * 3) throw new InvalidPositionException(INVALID_UGOL_SUM_ORIENTATION);
            }
            {
                int sum=0;
                for(int s:r_o)sum+=s;
                if(sum!=sum/2*2) throw new InvalidPositionException(INVALID_REBRO_SUM_ORIENTATION);
            }
            {
                for (int i:u_p)if(i<=0||i>8) throw new InvalidPositionException(INVALID_UGOL_CUBIE);
                boolean[] m=new boolean[8];
                for(int i:u_p){
                    if(m[i-1]) throw new InvalidPositionException(UGOL_ALREADY_PRESENT);
                    else m[i-1]=true;
                }

            }
            {
                for (int i:r_p)if(i<=0||i>12) throw new InvalidPositionException(INVALID_REBRO_CUBIE);
                boolean[] m=new boolean[12];
                for(int i:r_p)if(m[i-1]) throw new InvalidPositionException(REBRO_ALREADY_PRESENT);
                else m[i-1]=true;

            }
            {
                boolean p1 = Combinations.chetNechetPerestanovka(r_p);
                boolean p2 = Combinations.chetNechetPerestanovka(u_p);
                if (p1!=p2) throw new InvalidPositionException(INVALID_SUM_PERESTANOVKA);
            }
        }
        private static final class BigDecimalConverter {
            private static Random random=new Random();
            private static final BigDecimal
                    UO_MAX=BigDecimal.valueOf(SymTables.X_1_MAX),
                    RO_MAX=BigDecimal.valueOf(SymTables.Y_1_MAX),
                    UP_MAX=BigDecimal.valueOf(SymTables.X_2_MAX),
                    RP_MAX=BigDecimal.valueOf(479001600/2); // factorial(12)/2
            private static final BigDecimal MAX_POS=umnozit(umnozit(umnozit(UO_MAX,RO_MAX),UP_MAX),RP_MAX);
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
            private static BigDecimal pack(int uoInt,int roInt,int upInt, int rpInt){
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
            private static int[] unpack(BigDecimal p){
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
            private static BigDecimal randomPos(){
                int uo=random.nextInt(UO_MAX.intValue());
                int ro=random.nextInt(RO_MAX.intValue());
                int up=random.nextInt(UP_MAX.intValue());
                int rp=random.nextInt(RP_MAX.intValue());
                return pack(uo,ro,up,rp);
            }
        }
    }
}
