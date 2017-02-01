package jvm.compile;




class MyObj{
}

public class WorkingWithClassInstance {

	
	/**
	 *	>>>>	类实例被传递、返回时，是以reference类型的。
	 *
	 *	example()编译为:
	 *
	 *		0: 	new           #18      // class MyObj
	        3: 	dup
	        4: 	invokespecial #20      // Method MyObj."<init>":()V
	        7: 	astore_1
	        8: 	aload_0
	        9: 	aload_1
	       10: 	invokevirtual #21     // Method silly:(LMyObj;)LMyObj;
	       13: 	areturn
	       
	    silly(MyObj)编译为:
	    	0: 	aload_1
	        1: 	ifnull        6
	        4: 	aload_1
	        5: 	areturn
	        6: 	aload_1
	        7: 	areturn
	 */
	
	MyObj example() {
		MyObj o = new MyObj();
		return silly(o);
	}
	
	MyObj silly(MyObj o) {
		if (o != null) {
			return o;
		} else {
			return o;
		}
	}
	
	
/**
 *------------------------------------------------------------------
 *
 *	>>>>	访问类实例的属性(field)，使用  	getfield / putfield 指令。
 *
 *	void setIt(int)编译为:
 *		0: aload_0
        1: iload_1
        2: putfield      #30     // Field i:I
        5: return
        
 *	int getIt()编译为:
 *		0: aload_0
        1: getfield      #30     // Field i:I
        4: ireturn
 *
 * 	<补充>
 * 	【域操作指令系列】
 * 		getstatic --> 获取指定类的静态域，并将其值压入栈顶
		putstatic --> 用栈顶的值为指定的类的静态域赋值
		getfield  --> 获取指定类的实例域，并将其值压入栈顶
		putfield  --> 用栈顶的值为指定的类的实例域赋值
		
		例如 代码的	“i = value;” 编译为上面的  “putfield  #30  // Field i:I”。
		可以看出这是通过常量池来对field的引用。
		
 * 	【常量池】
 * 		常量池就是该类所用常量的一个有序集合，包括直接常量（String ,integer
 * 		和floating point常量）和对其他类型、字段和方法的符号引用。池中的数据项
 * 		就像数组一样是通过索引访问的。因为常量池存储了相应类型所用到的所有类型、字段
 * 		和方法的符号引用，所以它在Java程序的动态连接中起着核心作用。
 * 
 * 
*/
	
	
	int i; // An instance variable
	
	void setIt(int value) {
		i = value;
	}
	int getIt() {
		return i;
	}
}
