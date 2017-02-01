package jvm.innerClass;

/**
 * 			-----------		匿名内部类		----------------
 * 	匿名内部类是唯一一种没有构造器的类(指的是我们不能call constructor)。
 * 
 * 【问题: 匿名内部类只能访问局部final变量】
 * 原因:
 * 	当go方法执行完毕之后，变量filed的生命周期就结束了,而此时Thread对象的
 * 	生命周期很可能还没有结束，那么在Thread的run方法中继续访问变量a就变成不可能了，
 * 	但是又要实现这样的效果，怎么办呢？Java采用了 <复制>  的手段来解决这个问题。
 * 
 * 反编译Outter_A$1.class文件:
 * 
 * 	class Outter_A$1 extends Thread{
 * 		final Outter_A this$0;
 * 		Outter_A$1(Outter_A, String);
 * 		public void run();
 * 	}
 * 
 * 	其中 , run方法的内容如下:
 * ------------------------------------------------------------------------------------------------
 * 	public void run();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=2, locals=1, args_size=1
         0: getstatic     #24                 // Field java/lang/System.out:Ljava/io/PrintStream;
         3: bipush        12
         5: invokevirtual #30                 // Method java/io/PrintStream.println:(I)V
         8: return
      LineNumberTable:
        line 9: 0
        line 10: 8
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       9     0  this   Llearn/innerClass/Outter_A$1;
 * ------------------------------------------------------------------------------------------------
 *	在run方法中有一条指令	:
 *				bipush        12
 *	这条指令表示将操作数12压栈，表示使用的是一个本地局部变量。
 *	这个过程是在编译期间由编译器默认进行，如果这个变量的值在编译期间可以确定，
 *	则编译器默认会在匿名内部类（局部内部类）的常量池中添加一个内容相等的字面量
 *	或直接将相应的字节码嵌入到执行字节码中。这样一来，匿名内部类使用的变量是
 *	另一个局部变量，只不过值和方法中局部变量的值相等，因此和方法中的局部变量完全独立开。
 *
 *
 *	对Outter_A$2.class
 *	可以看到，构造器变成
 *		>>>	Outter_A$2(Outter_A, String, int);
 *	这里是将变量goWithParameter方法中的形参field以参数的形式传进来对
 *	匿名内部类中的拷贝（变量field的拷贝）进行赋值初始化。
 *
 *【总结】
 *	如果局部变量的值在编译期间就可以确定，则直接在匿名内部里面创建一个拷贝。
 *	如果局部变量的值无法在编译期间确定，则通过构造器传参的方式来对拷贝进行初始化赋值。
 *
 *
 *	为解决数据不一致性问题， java规定了将参数限制为final.
 */

class Outter_A{
	
	public void go(){
		final int filed = 12;
		Thread t = new Thread(""){
			public void run(){
				System.out.println(filed);
			}
		};
	}
	
	
	public void goWithParameter(final int field){
		Thread t = new Thread(""){
			public void run(){
				System.out.println(field);
			}
		};
	}
	
	
}


public class AnonymousInnerClasses {

}
