package jvm.innerClass;
/**
 * ----------------------	成员内部类		-----------------------
 * 【问题1】
 * 	?为什么成员内部类可以无条件访问外部类的成员?
 * 	
 * 	在 Outter$Inner里面的Constant pool，有
 * 		>>	final learn.innerClass.Outter this$0;	这是一个指向外部类对象的指针
 * 
 * 	编译器会默认为成员内部类添加了一个指向外部类对象的引用,这个引用是如何赋初值的呢?
 * 		>>	learn.innerClass.Outter$Inner(learn.innerClass.Outter);
 * 	
 * 	我们在定义的内部类的构造器是无参构造器，编译器还是会默认添加一个参数，
 * 	该参数的类型为指向外部类对象的一个引用，所以成员内部类中的Outter this&0 指针
 * 	便指向了外部类对象，因此可以在成员内部类中随意访问外部类的成员。
 * 
 * 	--->	
 * 		间接说明了成员内部类是依赖于外部类的，如果没有创建外部类的对象，
 * 		则无法对Outter this&0引用进行初始化赋值，也就无法创建成员内部类的对象了
 * 
 * <<<<	【那么为什么连OutterClass 的private 成员也可以访问呢？】	<<<<<<
 * 	在Outer.class里面， 有	static int access$0(learn.innerClass.Outter);
 * 	这样内部类Inner中的打印语句：
                     System.out.println(data);
         实际上运行的时候调用的是： 
         System.out.println(this$0.access$0(Outer));            
 * 
 * 
 * 【问题2】
 *  	为什么外部类可以创建内部类的对象?
 *  	>>	learn.innerClass.Outter$Inner(learn.innerClass.Outter);
 *  	这是调用了内部类的constructor(包可见)
 *  
 *  	首先编译器将外、内部类编译后放在同一个包中。在内部类中附加一个包可见构造器。
 *  	这样， 虚拟机运行Outer类中Inner in=new Inner(); 实际上调用的是包可见构造函数,
 *  	从而使外部类获得了创建权限.
 * 
 */

class Outter {
	private Inner inner = null;
	private int i = 0;
	private int c = 1;
	
	public Outter() {	
	}
	
	/*
	 * 	在外部类中如果要访问成员内部类的成员，必须先创建一个成员内部类的对象，
	 * 	再通过指向这个对象的引用来访问
	 */
	public void accessInnerClass(){
		getInnerInstance().print();
	}
	
	public Inner getInnerInstance() {
		if(inner == null)
            inner = new Inner();
        return inner;
	}
	
	class Inner {

		private Inner(){}
		private void print() {
            System.out.println(i+c);  //外部类的private成员
            
        }
		
		/*
		 * 成员内部类不能有static 的field 和 method.
		 * 原因 :
		 * 	类加载顺序是先static然后instance的。
		 */
//		private static int y = 0;
//		private static void staticPrint() {
//		}
	}
	
	
}

public class MemberInternalClasses {
	
	public static void main(String[] args)  {
		/*  			创建成员内部类对象  
		 * 	成员内部类是依附外部类而存在的，也就是说，如果要创建成员内部类的对象，
		 * 	前提是必须存在一个外部类的对象
		 */

		//第一种方式：
		/*Outter outter = new Outter();
        Outter.Inner inner = outter.new Inner();  //必须通过Outter对象来创建
        */
        //第二种方式：
		Outter outter = new Outter();
        Outter.Inner inner1 = outter.getInnerInstance();
	}

}
