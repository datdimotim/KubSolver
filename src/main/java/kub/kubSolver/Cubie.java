package kub.kubSolver;
import kub.kubSolver.utills.BigDecimalConverter;
import kub.kubSolver.utills.Combinations;

import java.math.BigDecimal;
import java.util.Arrays;

final class Cubie {
    private final int[] u_p;
    private final int[] u_o;
    private final int[] r_p;
    private final int[] r_o;

    Cubie() {
        r_o=new int[12];
        u_o=new int[8];
        u_p = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        r_p = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    }
    Cubie(int[][][] grani) throws InvalidPositionException {
        int[] facelet= KubGrani.graniToFacelet(grani);
        u_p=KubFacelet.faceletToUP(facelet);
        r_p=KubFacelet.faceletToRP(facelet);
        u_o=KubFacelet.faceletToUO(facelet);
        r_o=KubFacelet.faceletToRO(facelet);
        valid();
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
    static Cubie valueOf(BigDecimal pos){
        if(pos.compareTo(BigDecimalConverter.MAX_POS)>=0)
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
        try {
            return new Cubie(u_o,u_p,r_o,r_p1);
        } catch (InvalidPositionException e) {
            try {
                return new Cubie(u_o,u_p,r_o,r_p2);
            } catch (InvalidPositionException e1) {
                throw new RuntimeException();
            }
        }
    }
    BigDecimal toNumberPos(){
        int uo=Combinations.schetOrientation(u_o,3);
        int up=Combinations.schetPerestanovka(u_p);
        int rp=Combinations.schetPerestanovka(r_p)/2;
        int ro=Combinations.schetOrientation(r_o,2);
        return BigDecimalConverter.pack(uo,ro,up,rp);
    }
    int[] toKoordinates1(){
        return new int[]{KubCubie.uoToX1(u_o),KubCubie.roToY1(r_o),KubCubie.rpToZ1(r_p)};
    }
    int[] toKoordinates2(){
        int[] k1=toKoordinates1();
        for (int k:k1)if(k!=0)throw new IllegalStateException("k1="+ Arrays.toString(k1));
        return new int[]{KubCubie.upToX2(u_p),KubCubie.rpToY2(r_p),KubCubie.rpToZ2(r_p)};
    }
    Cubie povorot(int np){
        Cubie out=new Cubie();
        KubCubie.povorotRO(r_o,out.r_o,np);
        KubCubie.povorotRP(r_p,out.r_p,np);
        KubCubie.povorotUO(u_o,out.u_o,np);
        KubCubie.povorotUP(u_p,out.u_p,np);
        return out;
    }
    int[][][] toGrani(){
        int[][][] grani=KubFacelet.faceletToGrani(KubCubie.cubieToFacelet(u_o,u_p,r_o,r_p));
        for (int i=0;i<grani.length;i++)grani[i][1][1]=i;
        return grani;
    }
    private void valid() throws InvalidPositionException {
        {
            int sum = 0;
            for (int s : u_o) sum += s;
            if (sum != sum / 3 * 3) throw new InvalidPositionException(InvalidPositionException.Trable.INVALID_UGOL_SUM_ORIENTATION);
        }
        {
            int sum=0;
            for(int s:r_o)sum+=s;
            if(sum!=sum/2*2) throw new InvalidPositionException(InvalidPositionException.Trable.INVALID_REBRO_SUM_ORIENTATION);
        }
        {
            for (int i:u_p)if(i<=0||i>8) throw new InvalidPositionException(InvalidPositionException.Trable.INVALID_UGOL_CUBIE);
            boolean[] m=new boolean[8];
            for(int i:u_p){
                if(m[i-1]) throw new InvalidPositionException(InvalidPositionException.Trable.UGOL_ALREADY_PRESENT);
                else m[i-1]=true;
            }

        }
        {
            for (int i:r_p)if(i<=0||i>12) throw new InvalidPositionException(InvalidPositionException.Trable.INVALID_REBRO_CUBIE);
            boolean[] m=new boolean[12];
            for(int i:r_p)if(m[i-1]) throw new InvalidPositionException(InvalidPositionException.Trable.REBRO_ALREADY_PRESENT);
            else m[i-1]=true;

        }
        {
            boolean p1 = Combinations.chetNechetPerestanovka(r_p);
            boolean p2 = Combinations.chetNechetPerestanovka(u_p);
            if (!((p1 & p2) || (!p1 & !p2))) throw new InvalidPositionException(InvalidPositionException.Trable.INVALID_SUM_PERESTANOVKA);
        }
    }

    @Override
    public String toString() {
        return "up="+Arrays.toString(u_p)+"\n" +
                "uo="+Arrays.toString(u_o)+"\n"+
                "rp="+Arrays.toString(r_p)+"\n" +
                "ro="+Arrays.toString(r_o);
    }
}
