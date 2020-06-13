package com.dimotim.kubSolver.tables;

import com.dimotim.kubSolver.Kub2x2;
import com.dimotim.kubSolver.Solution;
import com.dimotim.kubSolver.kernel.CubieKoordinateConverter;
import com.dimotim.kubSolver.kernel.GraniCubieConverter;
import com.dimotim.kubSolver.kernel.Tables;

import java.io.*;

import static com.dimotim.kubSolver.kernel.HodTransforms.hodPredHod1Fase;
import static com.dimotim.kubSolver.tables.SymTables.track;

public class FullSymTables2x2 implements Tables<FullSymTables2x2.KubState> {
    // run first for initialization
    public static void main(String[] args)throws IOException {
        FullSymTables2x2 tables=new FullSymTables2x2();
        ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("src/main/resources/tables2x2full.object"));
        oos.writeObject(tables);
        oos.close();
    }

    public static FullSymTables2x2 readTables(){
        try(InputStream fis = SymTables.class.getResourceAsStream("/tables2x2full.object")) {
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (FullSymTables2x2) ois.readObject();
        }
        catch (IOException | ClassNotFoundException e){
            throw new RuntimeException("Can't read tables",e);
        }
    }

    private final SymMoveTable x1move;
    private final SymMoveTable x2move;
    private final SymDeepTable deep;

    public FullSymTables2x2(){
        x1move=new SymMoveTable(new MoveTables().x1Move,X_1_16_SYM_CLASSES,16);
        x1move.proofMove(new MoveTables().x1Move);
        x2move=new SymMoveTable(MoveTables.createX2MoveFof18Povorots(),X_2_SYM_CLASSES,16);
        x2move.proofMove(MoveTables.createX2MoveFof18Povorots());
        deep=new SymDeepTable(x2move,x1move);
        deep.proofDeepTable();
    }

    public Solution solve(Kub2x2 kub2x2){
        int[] hods=new int[20];
        int[][][] grani=kub2x2.getGrani3x3();

        int x1= CubieKoordinateConverter.uoToX1(GraniCubieConverter.graniToUO(grani));
        int x2=CubieKoordinateConverter.upToX2(GraniCubieConverter.graniToUP(grani));
        solve(x1,x2,hods);
        return new Solution(1,hods);
    }

    private void solve(int x1, int x2, int[] hods) {
        if(hods.length==0)return;
        KubState[] state=newArrayKubState(hods.length);
        for(int i=0;i<state.length;i++)state[i]=newKubState();
        state[0]= initKubStateFase1(x1,x2,0);
        if(getDepthInState(state[0])==0)return;
        int deep=1;
        //int count=0;
        mega: while(deep<hods.length) {
            for(int np = hods[deep];np<=18;np++) {
                //count++;
                if(!hodPredHod1Fase(np,hods[deep-1]))continue;
                if (moveAndGetDepthFase1(state[deep-1],state[deep],np)<=hods.length-deep-1) {
                    hods[deep] = np;
                    deep++;
                    continue mega;
                }
            }
            hods[deep]=0;
            deep--;
            if(deep<1)throw new RuntimeException();
            hods[deep]++;
        }

        //System.out.println("count="+count);
    }

    @Override
    public KubState initKubStateFase1(int x, int y, int z) {
        KubState kubState=new KubState();
        kubState.x1=x1move.rawToSym(x);
        kubState.x2=x2move.rawToSym(y);
        kubState.deep=SymTables.initDepth(deep,kubState.x2,kubState.x1);
        return kubState;
    }

    @Override
    public KubState initKubStateFase2(int x, int y, int z) {
        throw new RuntimeException();
    }
    @Override
    public int moveAndGetDepthFase2(KubState in, KubState out, int np) {
        throw new RuntimeException();
    }
    @Override
    public int moveAndGetDepthFase1(KubState in, KubState out, int np) {
        out.x1=x1move.doMove(in.x1,np);
        out.x2=x2move.doMove(in.x2,np);
        out.deep= track(deep.getDepth(out.x2,out.x1),in.deep);
        return out.deep;
    }


    @Override
    public int getDepthInState(KubState kubState) {
        return kubState.deep;
    }

    @Override
    public KubState newKubState() {
        return new KubState();
    }

    @Override
    public KubState[] newArrayKubState(int length) {
        return new KubState[length];
    }

    class KubState{
        private int x1;
        private int x2;
        private int deep;
    }
}
