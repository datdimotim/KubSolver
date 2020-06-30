package com.dimotim.kubSolver;

import com.dimotim.kubSolver.kernel.Combinations;
import com.dimotim.kubSolver.kernel.Cubie;
import com.dimotim.kubSolver.kernel.GraniCubieConverter;
import com.dimotim.kubSolver.tables.FullSymTables2x2;
import com.dimotim.kubSolver.tables.SymTables;

import java.math.BigDecimal;
import java.util.Random;

import static com.dimotim.kubSolver.Kub2x2.KUB_ERROR.*;


public class Kub2x2 {
    public static void main(String[] args) throws InvalidPositionException {
        while (true) {
            Kub2x2 kub2x2 = new Kub2x2(true);
            Solution solution=kub2x2.solve();
            System.out.println(solution);
        }
    }

    private static final FullSymTables2x2 solver = new FullSymTables2x2();

    private CubieSet cubieSet = new CubieSet();

    private static int[][][] toGrani3x3(int[][][] grani) {
        int[][][] res = new int[6][3][3];
        for (int i = 0; i < 6; i++) {
            res[i][0][0] = grani[i][0][0];
            res[i][0][2] = grani[i][0][1];
            res[i][2][0] = grani[i][1][0];
            res[i][2][2] = grani[i][1][1];
        }
        return res;
    }

    private static int[][][] fromGrani3x3(int[][][] grani) {
        int[][][] res = new int[6][2][2];
        for (int i = 0; i < 6; i++) {
            res[i][0][0] = grani[i][0][0];
            res[i][0][1] = grani[i][0][2];
            res[i][1][0] = grani[i][2][0];
            res[i][1][1] = grani[i][2][2];
        }
        return res;
    }

    public Solution solve() {
        return solver.solve(this);
    }

    public Kub2x2(Kub2x2 kub) {
        cubieSet = new CubieSet(kub.cubieSet);
    }

    public Kub2x2(boolean isRandom) {
        if (isRandom) randomPos();
    }

    public Kub2x2(int[][][] grani) throws InvalidPositionException {
        cubieSet = new CubieSet(toGrani3x3(grani));
    }

    public Kub2x2(BigDecimal pos) {
        cubieSet = CubieSet.valueOf(pos);
    }

    public BigDecimal getNumberPos() {
        return cubieSet.toNumberPos();
    }

    public void randomPos() {
        cubieSet = CubieSet.randomPos();
    }

    public boolean isSolved(){
        return getNumberPos().equals(BigDecimal.ZERO);
    }

    public int[][][] getGrani() {
        return fromGrani3x3(cubieSet.toGrani());
    }

    public int[][][] getGrani3x3() {
        return cubieSet.toGrani();
    }

    public String toString() {
        int[][][] grani = getGrani();
        StringBuilder str = new StringBuilder("gran 0         gran 1         gran 2         gran 3         gran 4         gran 5\n");
        for (int i = 0; i < 2; i++) {
            for (int g = 0; g < 6; g++) {
                for (int j = 0; j < 2; j++) {
                    str.append(grani[g][i][j]);
                }
                str.append("            ");
            }
            str.append("\n");
        }
        return str.toString();
    }

    public void povorot(int np) {
        cubieSet.povorot(np);
    }


    public static class InvalidPositionException extends Exception {
        private final KUB_ERROR trable;

        private InvalidPositionException(KUB_ERROR msg) {
            super(msg.name());
            trable = msg;
        }

        public KUB_ERROR getTrable() {
            return trable;
        }

    }

    public enum KUB_ERROR {
        INVALID_UGOL_CUBIE,
        UGOL_ALREADY_PRESENT,
        INVALID_UGOL_SUM_ORIENTATION
    }

    private final static class CubieSet {
        private final int[] u_p;
        private final int[] u_o;
        private final int[] tmp_u_p = new int[8];
        private final int[] tmp_u_o = new int[8];


        private CubieSet(CubieSet cubieSet) {
            u_p = cubieSet.u_p.clone();
            u_o = cubieSet.u_o.clone();
        }

        private CubieSet() {
            u_o = new int[8];
            u_p = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        }

        private CubieSet(int[][][] grani) throws InvalidPositionException {
            u_p = GraniCubieConverter.graniToUP(grani);
            u_o = GraniCubieConverter.graniToUO(grani);
            valid();
        }

        private static CubieSet randomPos() {
            return valueOf(CubieSet.BigDecimalConverter.randomPos());
        }

        private CubieSet(int[] u_o, int[] u_p) throws InvalidPositionException {
            this.u_o = new int[8];
            this.u_p = new int[8];
            System.arraycopy(u_o, 0, this.u_o, 0, 8);
            System.arraycopy(u_p, 0, this.u_p, 0, 8);
            valid();
        }

        private static CubieSet valueOf(BigDecimal pos) {
            if (pos.compareTo(CubieSet.BigDecimalConverter.MAX_POS) >= 0 || pos.compareTo(BigDecimal.ZERO) < 0)
                throw new IllegalArgumentException(pos + ">=posMax=" + CubieSet.BigDecimalConverter.MAX_POS);
            int[] k = CubieSet.BigDecimalConverter.unpack(pos);
            int uo = k[0];
            int up = k[1];
            int[] u_o = Combinations.intToPosNumber(uo, 3, 8);
            int[] u_p = Combinations.intToPerestanovka(up, 8);
            try {
                return new CubieSet(u_o, u_p);
            } catch (InvalidPositionException e) {
                throw new RuntimeException();
            }
        }

        private BigDecimal toNumberPos() {
            int uo = Combinations.posNumberToInt(u_o, 3);
            int up = Combinations.perestanovkaToInt(u_p);
            return CubieSet.BigDecimalConverter.pack(uo, up);
        }

        private void povorot(int np) {
            System.arraycopy(u_o, 0, tmp_u_o, 0, u_o.length);
            System.arraycopy(u_p, 0, tmp_u_p, 0, u_p.length);
            Cubie.povorotUO(tmp_u_o, u_o, np);
            Cubie.povorotUP(tmp_u_p, u_p, np);
        }

        private int[][][] toGrani() {
            int[][][] grani = GraniCubieConverter.cubieToGrani(u_o, u_p, new int[12], new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});
            for (int i = 0; i < grani.length; i++) grani[i][1][1] = i;
            return grani;
        }

        private void valid() throws InvalidPositionException {
            {
                int sum = 0;
                for (int s : u_o) sum += s;
                if (sum != sum / 3 * 3) throw new InvalidPositionException(INVALID_UGOL_SUM_ORIENTATION);
            }
            {
                for (int i : u_p) if (i <= 0 || i > 8) throw new InvalidPositionException(INVALID_UGOL_CUBIE);
                boolean[] m = new boolean[8];
                for (int i : u_p) {
                    if (m[i - 1]) throw new InvalidPositionException(UGOL_ALREADY_PRESENT);
                    else m[i - 1] = true;
                }

            }
        }

        private static final class BigDecimalConverter {
            private static final Random random = new Random();
            private static final BigDecimal
                    UO_MAX = BigDecimal.valueOf(SymTables.X_1_MAX),
                    UP_MAX = BigDecimal.valueOf(SymTables.X_2_MAX);
            private static final BigDecimal MAX_POS = umnozit(UO_MAX, UP_MAX);

            private static BigDecimal plus(BigDecimal a, BigDecimal b) {
                return a.add(b);
            }

            private static BigDecimal minus(BigDecimal a, BigDecimal b) {
                return a.subtract(b);
            }

            private static BigDecimal umnozit(BigDecimal a, BigDecimal b) {
                return a.multiply(b);
            }

            private static BigDecimal delit(BigDecimal a, BigDecimal b) {
                return a.divide(b, BigDecimal.ROUND_DOWN);
            }

            private static BigDecimal pack(int uoInt, int upInt) {
                BigDecimal uo = BigDecimal.valueOf(uoInt);
                BigDecimal up = BigDecimal.valueOf(upInt);

                BigDecimal tmp = BigDecimal.ZERO;
                tmp = plus(tmp, uo);
                tmp = umnozit(tmp, UP_MAX);
                tmp = plus(tmp, up);
                return tmp;
            }

            private static int[] unpack(BigDecimal p) {
                int[] k = new int[2];
                k[1] = minus(p, umnozit(delit(p, UP_MAX), UP_MAX)).intValue();
                k[0] = delit(p, UP_MAX).intValue();
                return k;
            }

            private static BigDecimal randomPos() {
                int uo = random.nextInt(UO_MAX.intValue());
                int up = random.nextInt(UP_MAX.intValue());
                return pack(uo, up);
            }
        }
    }
}
