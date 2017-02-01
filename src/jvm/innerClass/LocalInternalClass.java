package jvm.innerClass;

/**	
 * 	局部内部类(也叫方法内部类):
 * 		定义在一个方法或者一个作用域里面的类，
 * 		它和成员内部类的区别在于局部内部类的访问仅限于方法内或者该作用域内。
 * 
 * 	局部内部类就像是方法里面的一个局部变量一样，是不能有
 * 		public、protected、private以及static修饰符的。
 * 
 * 【注意】
 * 	方法内部类只能够访问该方法中的局部变量，而且这些局部变量一定要是final修饰的常量。
 * 【?局部变量一定要final的原因?】
 * 
 * 	1. 对Outter_L类进行分析
 * 	class learn.innerClass.Outter_L{
 * 		learn.innerClass.Outter_L();
 * 		public void outMethod();
 * 	}
 * 	Man没有返回私有域的隐藏方法了.
 * 	
 * 	2.对Woman类进行分析
 * 	class learn.innerClass.Outter_L$1$Inner_L{
 * 		final learn.innerClass.Outter_L this$0;
 * 		learn.innerClass.Outter_L$1$Inner_L(learn.innerClass.Outter_L);	//<init>
 * 	}
 * 
 * 
 */

class Outter_L{
	
	public void outMethod(){
		final int b = 0;
		final int c = 0;
		class Inner_L{   //局部内部类
			int b_inner = b;
		}
		Inner_L inner = new Inner_L();
		
	}
}


public class LocalInternalClass {
	public static void main(String[] args)  {
		
	}
}
