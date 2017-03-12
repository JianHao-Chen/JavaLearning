package jdkSrc.serialize;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

/**
 *  关于  ArrayList的elementData被transient修饰 的原因,有这个说法:
 *  
 *      因为elementData数组中存储的  “元素”其实仅是对这些元素的一个引用，并不是真正的对象，
 *      序列化一个对象的引用是毫无意义的，因为序列化是为了反序列化，当你反序列化时，这些对象的
 *      引用已经不可能指向原来的对象了。所以在这儿需要手工的对ArrayList的元素进行序列化操作。
 *      这就是writeObject()的作用。
 *  
 *  
 *  实际上,下面这个自定义的MyArrayList,把transient去掉，write/readObject采用默认
 *  方式,这里就是数组的序列化。可以看到,反序列化得到的数组大小为5(实际上有效的值只有3个)。
 *  
 *  
 */
public class serializeReference {

    public static void main(String[] args) throws Exception{
        
        MyArrayList al = new MyArrayList<String>();
        al.add("sssssssssssssssss");  
        al.add("bbbbbbbbbbbbbbbbbbt");  
        al.add("gggggggggggggggggg");
        
        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("al.tmp"));
        oos.writeObject(al); 
        
        ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("al.tmp"));  
        
        MyArrayList<String> a = (MyArrayList<String>)ois.readObject();  
        for(int i=0;i<a.size();i++){  
            System.out.println(a.get(i));  
        }  
    }
}



class MyArrayList<E> extends AbstractList<E> 
    implements List<E>, RandomAccess, Cloneable, java.io.Serializable{
    
    private static final long serialVersionUID = 8683452581122892189L;

    /** 
     * The array buffer into which the elements of the ArrayList are stored. 
     * The capacity of the ArrayList is the length of this array buffer. 
     */  
    private Object[] elementData;
    
    private int size;
    
    public MyArrayList(){
        this.elementData = new Object[5];
        size = 0;
    }
    
    private void writeObject(java.io.ObjectOutputStream s)  
    throws java.io.IOException{ 
        
        System.out.println("writeObject is called");
        s.defaultWriteObject();
    }
    
    private void readObject(java.io.ObjectInputStream s)  
    throws java.io.IOException, ClassNotFoundException {
        
        System.out.println("readObject is called");
        s.defaultReadObject();
    }
    
    public boolean add(E e){
        elementData[size++] = e;
        return true;
    }
    
    public E get(int index) {
        return (E) elementData[index];
    }
    public int size() {
        return size;
    }
}