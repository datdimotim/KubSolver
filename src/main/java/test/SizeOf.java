package test;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.IdentityHashMap;

public class SizeOf {
    private static final int SIZE_OF_REFERENCE=8;

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