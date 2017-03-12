package jdkSrc.serialize;


/**
 *  JAVA规定被序列化的对象必须实现java.io.Serializable这个接口.
 *   
 *  transient关键字:
 *      将不需要序列化的属性前添加关键字transient，序列化对象的时候，这个属性就不会
 *      序列化到指定的目的地中
 *----------------------------------------------------------------
 *
 *  ArrayList的elementData被transient修饰:
 *          private transient E[] elementData;
 *  
 *  ArrayList解决序列化:
 *  
 *  1. ArrayList实现了writeObject()、readObject()方法:
 *  
 *      private void writeObject(java.io.ObjectOutputStream s) 
 *          throws java.io.IOException;
 *          
 *      private void readObject(java.io.ObjectInputStream s)
 *          throws java.io.IOException, ClassNotFoundException;
 *          
 *  2. 如果一个类不仅实现了Serializable接口，而且定义了 
 *          readObject（ObjectInputStream in）和
 *          writeObject(ObjectOutputStream out)方法,
 *      那么将按照如下的方式进行序列化和反序列化：    
 *    
 *      ObjectOutputStream会调用这个类的writeObject方法进行序列化，
 *      ObjectInputStream会调用相应的readObject方法进行反序列化。
 *      
 *      
 *      
 *  ★ 为什么使用transient修饰elementData ？
 *    <1> ArrayList的自动扩容机制，elementData数组相当于容器，当容器不足时就会
 *          再扩充容量，但是容器的容量往往都是大于或者等于ArrayList所存元素的个数。
 *    <2> 所以ArrayList将elementData设计为transient，然后在writeObject
 *          方法中手动将其序列化，并且只序列化了实际存储的那些元素，而不是整个数组。
 */
public class SerializeAPI {

}
