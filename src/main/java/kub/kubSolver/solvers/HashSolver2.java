package kub.kubSolver.solvers;
import kub.kubSolver.Tables;

import java.util.HashMap;
import java.util.Map;

public class HashSolver2 extends Fase2Solver.AbstractSolver2{
    public HashSolver2(){

    }
    @Override
    public void solve(int x, int y, int z, int[] hods) {
        Map<K2,Byte> map=new HashMap<>(10000000);
        fase2Recurcive(x,y,z,hods,1,map);
        //System.out.println(map.size());
    }
    private boolean fase2Recurcive(final int x,final int y,final int z,final int[] hods,final int deep,Map<K2,Byte> map){
        for(int np = hods[deep];np<=10;np++) {
            if(!hodPredHod(np,hods[deep-1]))continue;
            int xt=x2Move[np][x];int yt=y2Move[np][y];int zt=z2Move[np][z];
            if ((xz2Deep[xt][zt]>Tables.MAX_DEEP - deep||yz2Deep[yt][zt]>Tables.MAX_DEEP - deep))continue;

            //K2 k2=new K2(xt,yt,zt);
            //Byte depth=map.get(k2);
            //if(depth!=null&&depth>Tables.MAX_DEEP - deep)continue;
            //depth= (byte) (Tables.MAX_DEEP - deep-1);
            //map.put(k2,depth);

            hods[deep]=np;
            if(deep==Tables.MAX_DEEP)return true;
            else if(fase2Recurcive(xt, yt, zt, hods, deep + 1,map))return true;
        }
        hods[deep]=0;
        return false;
    }
}
final class K2 implements Comparable<K2>{
    final int x,y,z,hash;
    K2(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        hash=cacheHash();
    }

    private int cacheHash(){
        int hash = 16769023;//37
        hash = hash*1073676287 + x;//17
        hash = hash*1073676287 + y;
        hash = hash*1073676287 + z;
        return hash;
    }
    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof K2){
            K2 other=(K2)obj;
            return x==other.x&&y==other.y&&z==other.z;
        }
        else return false;
    }

    @Override
    public int compareTo(K2 k) {
        int c=Integer.compare(x,k.x);
        if(c!=0)return c;
        c=Integer.compare(y,k.y);
        if(c!=0)return c;
        return Integer.compare(z,k.z);
    }
}
