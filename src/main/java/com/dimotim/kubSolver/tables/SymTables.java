package com.dimotim.kubSolver.tables;

import com.dimotim.kubSolver.kernel.CubieKoordinateConverter;
import com.dimotim.kubSolver.kernel.Tables;

import java.io.*;

public final class SymTables implements Tables<SymTables.KubState> {
    private final SymMoveTable x1;
    private final SymMoveTable y1;
    private final SymMoveTable z1;
    private final SymMoveTable x2;
    private final SymMoveTable y2;
    private final SymMoveTable z2;
    private final SymMoveTable x2Comb;
    private final SymDeepTable xy1;
    private final SymDeepTable xz1;
    private final SymDeepTable yz1;
    private SymDeepTable xy2;
    private final SymDeepTable xz2;
    private final SymDeepTable yz2;
    private final SymDeepTable yX2Comb;
    public SymTables() {
        MoveTables moveTables = new MoveTables();//System.out.println("raw move tables created");
        x1 = new SymMoveTable(moveTables.x1Move, X_1_SYM_CLASSES,8);//System.out.println("x1 move created");
        y1 = new SymMoveTable(moveTables.y1Move, Y_1_SYM_CLASSES,8);//System.out.println("y1 move created");
        z1 = new SymMoveTable(moveTables.z1Move, Z_1_SYM_CLASSES,8);//System.out.println("z1 move created");
        x2 = new SymMoveTable(moveTables.x2Move, X_2_SYM_CLASSES,16);//System.out.println("x2 move created");
        y2 = new SymMoveTable(moveTables.y2Move, Y_2_SYM_CLASSES,16);//System.out.println("y2 move created");
        z2 = new SymMoveTable(moveTables.z2Move, Z_2_SYM_CLASSES,16);//System.out.println("z2 move created");
        x2Comb =new SymMoveTable(moveTables.x2CombMove, X_2_COMB_SYM_CLASSES,16);//System.out.println("x2Comb move created");
        xy1 = new SymDeepTable(x1, y1);//System.out.println("xy1 deep created");
        xz1 = new SymDeepTable(x1, z1);//System.out.println("xz1 deep created");
        yz1 = new SymDeepTable(y1, z1);//System.out.println("yz1 deep created");
        //xy2 = new SymDeepTable(x2, y2);System.out.println("xy2 deep created");
        xz2 = new SymDeepTable(x2, z2);//System.out.println("xz2 deep created");
        yz2 = new SymDeepTable(y2, z2);//System.out.println("yz2 deep created");
        yX2Comb =new SymDeepTable(y2, x2Comb);//System.out.println("yX2Comb deep created");
    }
    public void proof(){
        MoveTables moveTables = new MoveTables();
        x1.proofMove(moveTables.x1Move);System.out.println("x1 move tested");
        y1.proofMove(moveTables.y1Move);System.out.println("y1 move tested");
        z1.proofMove(moveTables.z1Move);System.out.println("z1 move tested");
        x2.proofMove(moveTables.x2Move);System.out.println("x2 move tested");
        y2.proofMove(moveTables.y2Move);System.out.println("y2 move tested");
        z2.proofMove(moveTables.z2Move);System.out.println("z2 move tested");
        x2Comb.proofMove(moveTables.x2CombMove);System.out.println("x2Comb move tested");
        xy1.proofDeepTable();System.out.println("xy1 deep tested");
        xz1.proofDeepTable();System.out.println("xz1 deep tested");
        yz1.proofDeepTable();System.out.println("yz1 deep tested");
        //xy2.proofDeepTable();System.out.println("xy2 deep tested");
        xz2.proofDeepTable();System.out.println("xz2 deep tested");
        yz2.proofDeepTable();System.out.println("yz2 deep tested");
        yX2Comb.proofDeepTable();System.out.println("yX2Comb deep tested");
    }

    public static int initDepth(SymDeepTable table, int s, int r){
        int si=s;
        int ri=r;
        int so;
        int ro;
        int count=0;
        int deepPred=21+table.getDepth(s,r); // 21 mod 3 = 0
        for(int np = 1; np<table.symPart.getCountOfMoves(); np++){
            so=table.symPart.doMove(si,np);
            ro=table.rawPart.doMove(ri,np);
            if(track(table.getDepth(so,ro),deepPred)<deepPred){
                deepPred=track(table.getDepth(so,ro),deepPred);
                count++;
                np=0;
                si=so;
                ri=ro;
            }
        }
        return count;
    }

    public static int track(int mod, int deepPred){
        int old=deepPred%3;
        if(mod==old)return deepPred;
        if(old==2){
            if(mod==1)return deepPred-1;
            else return deepPred+1;
        }
        if(old==1){
            if(mod==0)return deepPred-1;
            else return deepPred+1;
        }
        if(old==0){
            if(mod==2)return deepPred-1;
            else return deepPred+1;
        }
        throw new RuntimeException("mod="+mod+" deepPred="+deepPred);
    }

    @Override
    public KubState initKubStateFase1(int x,int y,int z){
        KubState kubState=new KubState();
        kubState.x=x1.rawToSym(x);
        kubState.y=y1.rawToSym(y);
        kubState.z=z1.rawToSym(z);
        kubState.xyDeep=initDepth(xy1,kubState.x,kubState.y);
        kubState.xzDeep=initDepth(xz1,kubState.x,kubState.z);
        kubState.yzDeep=initDepth(yz1,kubState.y,kubState.z);
        return kubState;
    }

    @Override
    public int moveAndGetDepthFase1(KubState in, KubState out, int np){
        out.x=x1.doMove(in.x,np);
        out.y=y1.doMove(in.y,np);
        out.z=z1.doMove(in.z,np);
        out.xyDeep=track(xy1.getDepth(out.x,out.y),in.xyDeep);
        out.xzDeep=track(xz1.getDepth(out.x,out.z),in.xzDeep);
        out.yzDeep=track(yz1.getDepth(out.y,out.z),in.yzDeep);
        return Math.max(out.xyDeep,Math.max(out.xzDeep,out.yzDeep));
    }

    @Override
    public KubState initKubStateFase2(int x,int y,int z){
        KubState kubState=new KubState();
        kubState.x=x2.rawToSym(x);
        kubState.y=y2.rawToSym(y);
        kubState.z=z2.rawToSym(z);
        kubState.x2Comb = x2Comb.rawToSym(CubieKoordinateConverter.upToX2Comb(CubieKoordinateConverter.x2ToCubie(x)));
        kubState.xyDeep=xy2 == null ? 0 : initDepth(xy2,kubState.x,kubState.y);
        kubState.xzDeep=initDepth(xz2,kubState.x,kubState.z);
        kubState.yzDeep=initDepth(yz2,kubState.y,kubState.z);
        kubState.xY2CombDeep=initDepth(yX2Comb,kubState.y,kubState.x2Comb);
        return kubState;
    }

    @Override
    public int moveAndGetDepthFase2(KubState in, KubState out, int np){
        out.x=x2.doMove(in.x,np);
        out.y=y2.doMove(in.y,np);
        out.z=z2.doMove(in.z,np);
        out.x2Comb = x2Comb.doMove(in.x2Comb,np);
        out.xyDeep=xy2 == null ? 0 : track(xy2.getDepth(out.x,out.y),in.xyDeep);
        out.xzDeep=track(xz2.getDepth(out.x,out.z),in.xzDeep);
        out.yzDeep=track(yz2.getDepth(out.y,out.z),in.yzDeep);
        out.xY2CombDeep=track(yX2Comb.getDepth(out.y,out.x2Comb),in.xY2CombDeep);
        return Math.max(Math.max(out.xyDeep,Math.max(out.xzDeep,out.yzDeep)),out.xY2CombDeep);
    }

    @Override
    public int getDepthInState(KubState kubState) {
        return Math.max(kubState.xyDeep,Math.max(kubState.xzDeep,kubState.yzDeep));
    }

    @Override
    public KubState newKubState(){
        return new KubState();
    }

    @Override
    public KubState[] newArrayKubState(int length){
        return new KubState[length];
    }

    // run first for initialization
    public static void main(String[] args)throws IOException {
        SymTables tables=new SymTables();
        tables.proof();
        ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("src/main/resources/tables.object"));
        oos.writeObject(tables);
        oos.close();
    }

    public static SymTables readTables(){
        try(InputStream fis = SymTables.class.getResourceAsStream("/tables.object")) {
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (SymTables) ois.readObject();
        }
        catch (IOException | ClassNotFoundException e){
            throw new RuntimeException("Can't read tables",e);
        }
    }
    public static final class KubState{
        private int x;
        private int y;
        private int z;
        private int x2Comb;
        private int xyDeep;
        private int xzDeep;
        private int yzDeep;
        private int xY2CombDeep;
    }

}