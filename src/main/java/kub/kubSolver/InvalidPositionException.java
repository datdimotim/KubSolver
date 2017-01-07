package kub.kubSolver;

public class InvalidPositionException extends Exception{
    private final Trable trable;
    InvalidPositionException(Trable msg){
        super(msg.name());
        trable=msg;
    }
    public Trable getTrable(){return trable;}
    public enum Trable{
        INVALID_REBRO_CUBIE,
        REBRO_ALREADY_PRESENT,
        INVALID_UGOL_CUBIE,
        UGOL_ALREADY_PRESENT,
        INVALID_REBRO_SUM_ORIENTATION,
        INVALID_UGOL_SUM_ORIENTATION,
        INVALID_SUM_PERESTANOVKA
    }
}