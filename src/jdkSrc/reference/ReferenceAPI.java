package jdkSrc.reference;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 *			*********	强引用 、软引用、弱引用、虚引用 	************
 *
 *	<1> Strong Reference(强引用 )
 *		<a> 强引用(StrongReference)强引用是使用最普遍的引用。
 *		<b> 如果一个对象具有强引用,那垃圾回收器绝不会回收它。
 *		<c> 当内存空间不足，Java虚拟机宁愿抛出OutOfMemoryError错误，使程序异常终止，也不会靠随意
 *			回收具有强引用的对象来解决内存不足的问题。
 *
 *	<2> SoftReference(软引用)
 *		<a> 如果一个对象只具有软引用，则内存空间足够，垃圾回收器就不会回收它;
 *		<b> 如果内存空间不足了，就会回收这些对象的内存。只要垃圾回收器没有回收它，该对象就可以被程序使用。
 *		<c> 软引用可用来实现内存敏感的高速缓存。 
 *		<d> 软引用可以和一个引用队列（ReferenceQueue）联合使用,如果软引用所引用的对象被垃圾回收器回收,
 *			Java虚拟机就会把这个软引用加入到与之关联的引用队列中。
 *
 *	<3>	WeakReference(弱引用)
 *		<a> 只具有弱引用的对象拥有更短暂的生命周期。在垃圾回收器线程扫描它所管辖的内存区域的过程中，
 *			一旦发现了只具有弱引用的对象，不管当前内存空间足够与否，都会回收它的内存。不过，由于
 *			垃圾回收器是一个优先级很低的线程，因此不一定会很快发现那些只具有弱引用的对象。
 *		<b> 弱引用可以和一个引用队列（ReferenceQueue）联合使用，如果弱引用所引用的对象被垃圾回收，
 *			Java虚拟机就会把这个弱引用加入到与之关联的引用队列中。
 *
 *	<4> PhantomReference(虚引用)
 *		<a> 顾名思义，就是形同虚设，与其他几种引用都不同，虚引用并不会决定对象的生命周期。
 *			如果一个对象仅持有虚引用，那么它就和没有任何引用一样，在任何时候都可能被垃圾回收器回收。 
 *		<b> 虚引用主要用来跟踪对象被垃圾回收器回收的活动。
 *		<c> 虚引用与软引用和弱引用的一个区别在于:
 *				虚引用必须和引用队列 （ReferenceQueue）联合使用。
 *		<d> 当垃圾回收器准备回收一个对象时，如果发现它还有虚引用，就会在回收对象的内存之前，
 *			把这个虚引用加入到与之 关联的引用队列中。
 *--------------------------------------------------------------------------
 *
 *	程序可以通过判断引用队列中是否已经加入了虚引用，来了解被引用的对象是否将要被垃圾回收。
 *	如果程序发现某个虚引用已经被加入到引用队列，那么就可以在所引用的对象的内存被回收之前采取必要的行动。
 *	
 */

public class ReferenceAPI {
	
	/*
	 * 这里显示 WeakReference 的用法
	 */
	public void WeakReferenceExample(){
		
		//创建一个强引用   
		String str = new String("hello");  
		//创建引用队列, 为范型标记，表明队列中存放String对象的引用
		ReferenceQueue rq = new ReferenceQueue();  
		//创建一个弱引用，它引用"hello"对象，并且与rq引用队列关联
		WeakReference wf = new WeakReference(str, rq);
		
	//	-------------------------------------------------------------
	/*	此时 :
	 * 	<1> 在堆区存在 : "hello"对象 ,  WeakReference对象 , ReferenceQueue对象
	 * 	<2> "hello"对象 被 str 强引用(str在栈), 同时被 WeakReference对象弱引用着。
	 * 		因此,"hello"对象不会被GC回收。
	 */
	//	-------------------------------------------------------------
		
		//取消"hello"对象的强引用
		str=null;
	//	-------------------------------------------------------------
	/*	此时(置为null的一瞬间,还没有GC) :
	 * 	<1> 在堆区存在 : "hello"对象 ,  WeakReference对象 , ReferenceQueue对象
	 * 	<2> "hello"对象只被 WeakReference对象弱引用着。
	 * 		因此,"hello"对象可能被GC回收。
	 */
	//	-------------------------------------------------------------		
		
		/**
		 * 假设"hello"对象没有被GC
		 */
		// 可以再次强引用 "hello"对象
		//String str1 = (String)wf.get();
		// 从ReferenceQueue中poll()会得到Null
		//Reference ref = rq.poll();
		
		
		/**
		 * 假设"hello"对象已经被GC
		 */
		// 已经不能通过  WeakReference 得到 "hello"对象 , o == null
		Object o = wf.get();
		// 从ReferenceQueue中poll()会得到 WeakReference对象
		Reference ref = rq.poll();
		
		
	}
	
	
	public static void main(String[] args) {
		ReferenceAPI api = new ReferenceAPI();
		api.WeakReferenceExample();
	}

}
