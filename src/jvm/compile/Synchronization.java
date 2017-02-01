package jvm.compile;

class Foo{}

/**
 * 	在JVM,Synchronization的实现:
 * 		显式的--(monitorenter 和 monitorexit)，
 * 		隐式的--通过方法调用和返回指令
 * 
 * 	【指令】
 * 		monitorenter
			获得对象的锁，用于同步方法或同步块
		monitorexit
			释放对象的锁，用于同步方法或同步块
 */
public class Synchronization {

	/**
	 * 	void onlyMe(Foo)编译为:
	 * 
	 * 	descriptor: (Llearn/compile/Foo;)V
    	flags:
    	Code:
      		stack=2, locals=3, args_size=2
	 * 	 0: aload_1				// Push f
         1: dup					// Duplicate it on the stack
         2: astore_2			// Store duplicate in local variable 2
         3: monitorenter		// Enter the monitor associated with f
         4: aload_0				// Holding the monitor, pass this and...
         5: invokevirtual #16   // ...call doSomething()V
         8: aload_2				// Push local variable 2 (f)
         9: monitorexit			// Exit the monitor associated with f
        10: goto          16	// Complete the method normally
        13: aload_2				// Push local variable 2 (f)
        14: monitorexit			// Be sure to exit the monitor!
        15: athrow	
        16: return				// Return in the normal case
      Exception table:
         from    to  target type
             4    10    13   any
            13    15    13   any
	 */
	void onlyMe(Foo f) {
		synchronized(f) {
			doSomething();
		}
	}
	
//-------------------------------------------------------------------------
	
	/**
	 * 	synchronized void theOnly()编译为:
	 * 
	 * 	descriptor: ()V
    	flags: ACC_SYNCHRONIZED
    	Code:
      		stack=1, locals=1, args_size=1
         	0: aload_0
         	1: invokevirtual #16 	// Method doSomething:()V
         	4: return
	 *
	 *	【注意】
	 *		这里synchronized的编译，没有用 monitorenter / monitorexit指令，
	 *		而方法的 flags 为 < ACC_SYNCHRONIZED >
	 *		enter / exit  monitor由JVM完成。
	 */
	synchronized void theOnly(){
		doSomething();
	}
	
	
	void doSomething(){}
	
	
	
}
