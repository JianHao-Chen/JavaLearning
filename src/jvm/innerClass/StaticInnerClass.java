package jvm.innerClass;

/**
 *	----------------	静态内部类		-----------------
 *
 *	静态内部类是不需要依赖于外部类的，这点和类的静态成员属性有点类似，
 *	并且它	【不能使用外部类的非static成员变量或者方法】，
 *	这点很好理解，因为在没有外部类的对象的情况下，可以创建静态内部类的对象，
 *	如果允许访问外部类的非static成员就会产生矛盾，因为外部类的非static
 *	成员必须依附于具体的对象
 *
 *	进行反编译，可以看到:
 *	class learn.innerClass.Outter1$Inner1{
 *		learn.innerClass.Outter1$Inner1();
 *
 *		public void f();
 *	}
 *	Inner1 Class 没有一个指向外围类对象的引用	final Outer this$0;
 *	自然也就无法访问外围类的非静态成员了
 *
 */

class Outter1{
	private int i = 0;
	private static int b = 1;
	
	public Inner1 inner = new Inner1();
	public Outter1() {}
	
	private static class Inner1 {
		public void f(){
			b = 2;
		//  can't access instance variable in outter class
		//	i = 0;
		}
	}
}
public class StaticInnerClass {

}
