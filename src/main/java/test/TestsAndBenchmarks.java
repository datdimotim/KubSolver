package test;

import kub.kubSolver.*;
import parallel_solver.ParallelSymmetrySolver;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.IdentityHashMap;

public class TestsAndBenchmarks {
    public static void main(String[] args) throws IOException {
        speedSolve();

        //parallelSolve();
        //infiniteSolve();
        //solveAndView();
        //while (true)computeTables();
    }
    public static void speedSolve() throws IOException {
        KubSolver kubSolver=new KubSolver();
        Kub kub = new Kub(false);
        final int time=1000;
        long st=System.currentTimeMillis();
        long kol=0;
        while (true) {
            kub.randomPos();
            Solution solution = kubSolver.solve(kub, null, 1);
            kol++;
            long th=System.currentTimeMillis();
            if(th-st>time){
                System.out.println(kol*1000/(th-st));
                st=th;
                kol=0;
            }
        }
    }
    public static void parallelSolve() throws IOException {
        KubSolver kubSolver=new KubSolver();
        Kub kub = new Kub(true);
        Solution solution= ParallelSymmetrySolver.solve(kub,kubSolver,1000);
        System.out.println(solution);
        for(int p:solution.getHods())kub.povorot(p);
        System.out.println(kub);
    }
    public static void infiniteSolve() throws IOException {
        KubSolver kubSolver=new KubSolver();
        Kub kub = new Kub(true);
        Solution solution=null;
        while (true) {
            solution = kubSolver.solve(kub, solution, 1);
            System.out.println(solution);
        }
    }
    public static void solveAndView(){
        Kub kub=new Kub(true);
        System.out.println(kub);

        KubSolver kubSolver=new KubSolver();
        Solution solution=kubSolver.solve(kub,null,1);
        System.out.println(solution);
        for(int p:solution.getHods())kub.povorot(p);

        System.out.println(kub);
    }
}

class SizeOf {
    public static final int SIZE_OF_REFERENCE=8;

    private final IdentityHashMap set=new IdentityHashMap();
    private int size=0;
    private SizeOf(){}
    public static void sizeof(Object instance){
        new SizeOf().start(instance);
    }
    private void start(Object instance){
        if(instance==null)throw new RuntimeException();
        try {
            if(instance.getClass().isArray())processArray(instance,"");
            else processObject(instance,instance.getClass(),"");
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
        }
        System.out.println("\nTotalSize= "+size);
    }
    private void processArray(Object array,String prefix) throws IllegalAccessException {
        Class elementType = array.getClass().getComponentType();
        int arraySize = Array.getLength(array);
        if (elementType.isPrimitive()){
            size+=primitiveSize(elementType)*arraySize;
        }
        else{
            size+=SIZE_OF_REFERENCE*arraySize;
            for (int i = 0; i < arraySize; i++) {
                Object arrayElement = Array.get(array, i);
                if (arrayElement != null) {
                    if(set.containsKey(arrayElement))continue;
                    else set.put(arrayElement,null);
                    if(arrayElement.getClass().isArray())processArray(arrayElement,prefix+"\t");
                    else processObject(arrayElement, arrayElement.getClass(),prefix+"\t");
                }
            }
        }
    }
    private void processObject(Object instance, Class clazz, String prefix) throws IllegalAccessException {
        int start=size;
        System.out.println(prefix + "class " +clazz.getName()+" {");
        final Class superclass;
        final ArrayList<Field> fields;
        superclass=clazz.getSuperclass();
        fields= new ArrayList<>();
        for (Field field:clazz.getDeclaredFields())if(!Modifier.isStatic(field.getModifiers()))fields.add(field);

        if(superclass!=null) processObject(instance, superclass, prefix+"\t");


        for(Field f:fields) {
            int holdSize=size;
            if (f.getType().isPrimitive()) {
                size+=primitiveSize(f.getType());
            }
            else {
                size+=SIZE_OF_REFERENCE;
                f.setAccessible(true);
                Object contain = f.get(instance);
                if (contain != null) {
                    if(set.containsKey(contain))continue;
                    set.put(contain,null);
                    if (!contain.getClass().isArray()) processObject(contain, contain.getClass(),prefix+"\t");
                    else processArray(contain,prefix+"\t");
                }
            }
            System.out.println(prefix+"\t"+f.getName()+"  // size="+(size-holdSize));
        }
        System.out.println(prefix +"}  // classSize="+(size-start));
    }
    private static int primitiveSize(Class primitive){
        if(primitive.equals(byte.class))return 1;
        if(primitive.equals(char.class))return 2;
        if(primitive.equals(short.class))return 2;
        if(primitive.equals(int.class))return 4;
        if(primitive.equals(float.class))return 4;
        if(primitive.equals(boolean.class))return 4;
        if(primitive.equals(long.class))return 8;
        if(primitive.equals(double.class))return 8;
        throw new RuntimeException();
    }
}

class T{
    public static void main(String[] args) {
        SizeOf.sizeof(new KubSolver());
    }
}

