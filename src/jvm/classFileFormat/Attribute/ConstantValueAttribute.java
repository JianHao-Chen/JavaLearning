package jvm.classFileFormat.Attribute;

/**
	<attribute> ConstantValue
*	它是 field_info 结构的属性表中的，一个长度固定的属性。
*	只有被static 关键字修饰的变量才可以使用这项属性。
*	它的格式:
*	ConstantValue_attribute {
		u2 attribute_name_index;	//表示这个 attribute 的名字("ConstantValue")
		u4 attribute_length;
		u2 constantvalue_index;
	}
	
*	【补充】
*	JVM对 "int x=123" 和 "static int x=123"，这2种变量的赋值方式是完全不同的。
*	对实例变量，赋值是在实例构造函数<init>方法中进行。
*	对类变量，赋值有2种方式:
*		<1> 类构造器 <clinit>方法
*		<2> 使用 ConstantValue 属性。
*
*	当前Sun Javac 编译器的选择是（！！！ 这是 编译器添加的限制 ！！！）:
*	-- 	如果 同时使用 final 和 static 来修饰一个变量(或者叫常量)，并且这个常量的数据类型
*		是基本类型或 java.lang.String，那么生成 ConstantValue 属性进行初始化。
*	-- 	否则 ， 使用 <clinit>方法进行初始化。
*
*	对于 "final static int a = 12;" 和 "static int a = 12;",
*	得到的字节码代码如下:
*	
*	#有final的情况#
*		static final int a;
   	descriptor: I
   	flags: ACC_STATIC, ACC_FINAL
   	ConstantValue: int 12
   
     public void fun();
   	0: getstatic     #19     // Field System.out:PrintStream;
   	3: bipush        12
   	5: invokevirtual #25     // Method PrintStream.println:(I)V
   	8: return
*
*	#没有final的情况#
*		static int a;
   	descriptor: I
   	flags: ACC_STATIC
   	
     public void fun();
   	 0: getstatic     #20  // Field System.out:PrintStream;
        3: getstatic     #10  // Field a:I
        6: invokevirtual #26  // Method PrintStream.println:(I)V
        9: return
   	
     static {};
   	 0: bipush        12
        2: putstatic     #10  // Field a:I
        5: return
*/
public class ConstantValueAttribute {
	static int a = 12;
	
	public void fun(){
		System.out.println(a);
	}
}
