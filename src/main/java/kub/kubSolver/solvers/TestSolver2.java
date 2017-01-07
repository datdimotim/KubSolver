package kub.kubSolver.solvers;

import kub.kubSolver.Tables;

import java.util.ArrayList;
import java.util.Arrays;

public class TestSolver2 extends Fase2Solver.AbstractSolver2{
    public TestSolver2(){

    }
    @Override
    public void solve(int x, int y, int z, int[] hods) {
        int[] hods_x_tmp=new int[Tables.MAX_DEEP +1];
        int[] hods_y_tmp=new int[Tables.MAX_DEEP +1];
        int[] hods_z_tmp=new int[Tables.MAX_DEEP +1];
        hods_x_tmp[0]=x;
        hods_y_tmp[0]=y;
        hods_z_tmp[0]=z;
        int deep=1;
        ArrayList<K> listPos=new ArrayList<>();
        mega: while(deep<=Tables.MAX_DEEP) {
            for(int np = hods[deep];np<=10;np++) {
                if(!hodPredHod(np,hods[deep-1]))continue;
                int xt = x2Move[np][hods_x_tmp[deep-1]];
                int yt = y2Move[np][hods_y_tmp[deep-1]];
                int zt = z2Move[np][hods_z_tmp[deep-1]];
                listPos.add(new K(xt,yt,zt));
                if (xz2Deep[xt][zt]>Tables.MAX_DEEP - deep||yz2Deep[yt][zt]>Tables.MAX_DEEP - deep){
                    continue;
                }
                else {
                    hods[deep] = np;
                    hods_x_tmp[deep] = xt;
                    hods_y_tmp[deep] = yt;
                    hods_z_tmp[deep] = zt;
                    deep++;
                    continue mega;
                }
            }
            hods[deep]=0;
            deep--;
            hods[deep]++;
        }
        K k[]=listPos.toArray(new K[listPos.size()]);
        Arrays.sort(k);
        int kol=0;
        K pred=k[0];
        for(int i=1;i<k.length;i++){
            if(pred.equals(k[i]))kol++;
            pred=k[i];
        }
        System.out.println("Total= "+k.length+"\tdoubles= "+kol+"\tpercent= "+kol*100/k.length);
    }
    class K implements Comparable<K>{
        private final int x,y,z;

        K(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object obj) {
            return compareTo((K)obj)==0;
        }

        @Override
        public int compareTo(K obj) {
            int r=Integer.compare(x,obj.x);
            if(r!=0)return r;
            r=Integer.compare(y,obj.y);
            if(r!=0)return r;
            else return Integer.compare(z,obj.z);
        }
    }
}
