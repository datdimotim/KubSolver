package com.dimotim.kubSolver.kernel;

public final class Combinations{
    public static int[] intToComb(int x,int k,int length) {
        x++;
        int[] a=new int[k];
        for(int ai=0;ai<k;ai++) {
            for (int i = 1; i < length+1; i++) {
                if (C(i, k-ai) >= x) {
                    a[ai] = i;
                    x = x + C(i - 1, k-1-ai) - C(i, k-ai);
                    break;
                }
            }
        }
        return a;
    }
    public static int combToInt(int[] c){
        int ret=0;
        for(int i=0;i<c.length;i++)ret+=C(c[i],c.length-i)-C(c[i]-1,c.length-1-i);
        return  ret;
    }
    public static int posNumberToInt(int[] m, int max){
        int x = 0;
        int pow=1;
        for (int i = m.length-1; i > 0; i--){
            x +=m[i]*pow;
            pow*=max;
        }
        return x;
    }
    public static int[] intToPosNumber(int ch, int max, int length){
        int[] m = new int[length];
        for (int i = length-1; i > 0; i--) {
            m[i] = ch - ((ch / max) * max);
            ch = ch / max;
        }
        int s = 0;
        for (int i = 1; i < length; i++) s = s + m[i];
        m[0]=max-s+s/max*max;
        if(m[0]==max)m[0]=0;
        return m;
    }
    public static int perestanovkaToInt(int[] m){
        int ch = 0;
        int n = m.length;
        int[] poradok = new int[n - 1];
        for (int i = 0; i < n - 1; i++) {
            int sum = 0;
            for (int aM : m) {
                if (aM > i + 1) sum = sum + 1;
                if (aM == i + 1) {
                    poradok[i] = sum;
                    break;
                }
            }
        }
        int factorial = 1;
        for (int i = n - 2; i > -1; i--) {
            factorial = factorial * (n - 1 - i);
            ch = ch + poradok[i] * factorial;
        }
        return ch;
    }
    public static int[] intToPerestanovka(int ch, int length){
        int[] m = new int[length];
        int[] poradok = new int[length - 1];
        for (int i = 0; i < length; i++) {
            m[i] = length;
        }
        for (int i = 0; i < length - 1; i++) {
            poradok[length - 1 - i - 1] = ch - ch / (i + 1 + 1) * (i + 1 + 1);
            ch = ch / (i + 1 + 1);
        }
        for (int i = 0; i < length - 1; i++) {
            int sum = 0;
            for (int j = 0; j < length; j++) {
                if (m[j] > i + 1) sum = sum + 1;
                if (sum == poradok[i] + 1) {
                    m[j] = i + 1;
                    break;
                }
            }
        }
        return m;
    }

    public static int C(int n, int k) {
        int c = 1;
        for (int i = n; i >= n - k + 1; i--) {
            c = c * i;
        }
        for (int i = 1; i <= k; i++) {
            c = c / i;
        }
        return c;
    }

    public static boolean chetNechetPerestanovka(int[] mIn) {
        int[] m = new int[mIn.length];
        System.arraycopy(mIn, 0, m, 0, mIn.length);
        boolean perest = false;
        main:
        for (int i = 0; i < m.length; i++) {
            if (m[i] != i + 1) {
                for (int j = i + 1; j < m.length; j++) {
                    if (m[j] == i + 1) {
                        perestPair(m, i, j);
                        perest = !perest;
                        continue main;
                    }
                }
                throw new RuntimeException("Perestanovka Error");
            }
        }
        return perest;
    }

    private static void perestPair(int[] m, int i, int j) {
        int tmp = m[i];
        m[i] = m[j];
        m[j] = tmp;
    }
}
