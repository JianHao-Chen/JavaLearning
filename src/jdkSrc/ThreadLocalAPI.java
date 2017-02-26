package jdkSrc;

import java.lang.ThreadLocal;

/**
 * 				~~~~~~		ThreadLocal		~~~~~~
 * 	
 * 	## get() ##
 * 	
 * 	<1> 首先获取当前线程
 * 	<2> 根据当前线程获取一个Map
 * 	<3> 如果获取的Map不为空，则在Map中以ThreadLocal的引用作为key来在Map中获取对应的value e,
 * 		 否则转到5
 * 	<4> 如果e不为null，则返回e.value，否则转到5
 * 	<5> Map为空或者e为空，则通过initialValue函数获取初始值value,然后用ThreadLocal的引用和
 * 		value作为firstKey和firstValue创建一个新的Map
 * 
 * 	Thread类中包含一个成员变量：
 * 		ThreadLocal.ThreadLocalMap threadLocals = null;
 * 
 * 	所以,ThreadLocal的设计思路:
 * 
 * 	【每个Thread维护一个ThreadLocalMap映射表，这个映射表的key是ThreadLocal实例本身，
 * 	value是真正需要存储的Object。】
 * 
 * 	这样设计的优势:
 * 	<1> 这样设计之后每个Map的Entry数量变小了:
 * 		之前是Thread的数量，现在是ThreadLocal的数量，能提高性能
    <2>	当Thread销毁之后对应的ThreadLocalMap也就随之销毁了，能减少内存使用量。
 *
 *
 *				～～～！！！	注意		！！！～～～
 *
 *		****  ThreadLocalMap是使用ThreadLocal的弱引用作为Key的	  *****
 *
 *	于是, 对象之间的关系:
 *
 *		栈							堆
 *
 *		ThreadLocalRef------>ThreadLocal
 *
 *		Thread Ref	--------> Thread ---> ThreadLocalMap ---->Entry(key,value)
 *
 *		其中, key是 一个 WeakReference对象,有一个弱引用指向 ThreadLocal
 *			 value是一个强引用,指向用户设置的对象。
 *
 *---------------------------------------------------------------------------
 *
 *	【	关于ThreadLocal会引发内存泄露的问题	  】
 *	
 *	～～	问题	～～
 *	<1> ThreadLocalMap使用ThreadLocal的弱引用作为key，如果一个ThreadLocal没有外部强引用
 *		来引用它，那么系统 GC 的时候，这个ThreadLocal势必会被回收，
 *	<2> 这样一来，ThreadLocalMap中就会出现key为null的Entry，就没有办法访问这些key为null的
 *		Entry的value
 *	<3> 如果当前线程再迟迟不结束的话，这些key为null的Entry的value就会一直存在一条强引用链：
 *			Thread Ref -> Thread -> ThreaLocalMap -> Entry -> value
 *		永远无法回收，造成内存泄漏。 
 *
 *	
 *	ThreadLocalMap的设计中已经考虑到这种情况，也加上了一些防护措施:
 *		在ThreadLocal的get(),set(),remove()的时候都会清除线程ThreadLocalMap里所有
 *		key为null的value。
 *
 *	$$ 根源  $$
 *	ThreadLocal内存泄漏的根源是:
 *		由于ThreadLocalMap的生命周期跟Thread一样长，如果没有手动删除对应key就会导致内存泄漏,
 *		而不是因为弱引用。
 *
 *	**	JDK的建议   **
 *		JDK建议将ThreadLocal变量定义成private static的，这样的话ThreadLocal
 *		的生命周期就更长，由于一直存在ThreadLocal的强引用，所以ThreadLocal也就不会被
 *		回收，也就能保证任何时候都能根据ThreadLocal的弱引用访问到Entry的value值，然后
 *		remove它，防止内存泄露。
 *	
 */

public class ThreadLocalAPI {
    private static final ThreadLocal<Integer> value = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };
    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            new Thread(new MyThread(i)).start();
        }
    }
    
	static class MyThread implements Runnable {
        private int index;
        public MyThread(int index) {
            this.index = index;
        }
        public void run() {
            System.out.println("线程" + index + "的初始value:" + value.get());
            for (int i = 0; i < 10; i++) {
                value.set(value.get() + i);
            }
            System.out.println("线程" + index + "的累加value:" + value.get());
        }
		
	}
}
