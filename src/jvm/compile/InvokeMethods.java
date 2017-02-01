package jvm.compile;


/**
 *		方法 add12And13() 编译为:
 *
 *		0 	aload_0 			// Push local variable 0 (this)
		1 	bipush 12 			// Push int constant 12
		3 	bipush 13 			// Push int constant 13
		5 	invokevirtual #4 	// Method Example.addtwo(II)I
		8 	ireturn 			// Return int on top of operand stack;
								// it is the int result of addTwo()
		
		方法 addTwo(int i,int j) 编译为:
 *
 *		0	iload_1				//push i
        1	iload_2				//push j
        2 	iadd				// i+j
        3 	ireturn				// return (i+j)
		
 *	【要点】
 *	<1>
 *		add12And13()方法依次push reference this， constant 12， 13.
 *		当addTwo（）的frame创建以后，刚才push的3个值，会成为addTwo()的
 *		局部变量表中的初始值，因此我们看到addTwo()一开始的iload_1，iload_2,
 *		就是从局部变量表读取。
 *
 *	<2>
 *		当addTwo（）返回时，他的返回值push到add12And13()的frame中的操作数栈。
 *		并且将控制权交给add12And13()。
 *
 *	<3>
 *		invokevirtual #4，其中 #4是对实例方法的符号引用，保存在常量池。
 *
 *
 */
public class InvokeMethods {
	
	int addTwo(int i,int j){
		return i+j;
	}
	
	int add12And13(){
		return addTwo(12,13);
	}

}
