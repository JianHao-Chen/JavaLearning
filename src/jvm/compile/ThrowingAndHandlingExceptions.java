package jvm.compile;




class TestExc extends Exception{
}

public class ThrowingAndHandlingExceptions {

	/**
	 *	void cantBeZero(int) throws learn.compile.TestExc 编译为:
	 *
	 * 		0: 	iload_1					// Push argument 1 (i)
	        1: 	ifne          12
	        4: 	new           #17       // Create instance of TestExc
	        7: 	dup						// One reference goes to its constructor
	        8: 	invokespecial #19       // Method TestExc.<init>:()V
	       11: 	athrow					// Second reference is thrown
	       12: 	return					// Never get here if we threw TestExc
	 *
	 *
	 *	athrow : 将栈顶的异常抛出
	 */
	void cantBeZero(int i) throws TestExc {
		if (i == 0) {
			throw new TestExc();
		}
	}
	
//--------------------------------------------------------------------
	/**
	 * 	void catchOne() 编译为:
	 * 
	 * 	0: 	aload_0
        1: 	invokevirtual #24     	// Method tryItOut:()V
        4: 	goto          13		// End of try block; normal return
        7: 	astore_1				// Store thrown value in local var 1
        8: 	aload_0					// Push this
        9: 	aload_1					// Push thrown value
       10: 	invokevirtual #27      	// Invoke handler method:
       								// Method handleExc:(TestExc;)V
       13: 	return					// Return after handling TestExc
       Exception table:
         from    to  target type
          0      4     7   	Class learn/compile/TestExc
	 *
	 *	【要点】
	 *	编译器生成 Exception table。
	 *	这里的一个Entry表示  : catchOne()方法的catch clause可以处理的异常，
	 *	当0到4的指令执行时，某个值是TestExc的实例被抛出，程序会跳转到指令7。
	 *	
	 */
	void catchOne() {
		try {
			tryItOut();
		}
		catch (TestExc e) {
			handleExc(e);
		}
	}
	
	void tryItOut()throws TestExc{
		;
	}
	
	void handleExc(TestExc e){
		
	}
}
