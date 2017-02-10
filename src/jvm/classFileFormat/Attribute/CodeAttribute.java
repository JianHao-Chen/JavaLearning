package jvm.classFileFormat.Attribute;

/**
 * <attribute> Code	
 *	Code属性是在 method_info 结构中的属性表的一个可变长度的属性。
 *	Code属性包含一个方法的JVM指令和补充信息。
 *
 *	如果method是一个native或abstract,它的 method_info 一定不能有 Code属性。
 *	相反,其他方法的method_info一定有一个Code属性。
 *	
 *	Code属性的格式:
 *		Code_attribute {
			u2 attribute_name_index;
			u4 attribute_length;
			u2 max_stack;
			u2 max_locals;
			u4 code_length;
			u1 code[code_length];
			u2 exception_table_length;
			{ 	u2 start_pc;
				u2 end_pc;
				u2 handler_pc;
				u2 catch_type;
			} exception_table[exception_table_length];
			u2 attributes_count;
			attribute_info attributes[attributes_count];
		}
 *
 *	<item> attribute_name_index
 *	constant_pool entry的index,该entry是一个表示“Code”字符串的CONSTANT_Utf8_info。
 *
 *	<item> attribute_length
 *	这个属性的长度，不包括前6字节。
 *
 *	<item> max_stack
 *	这个方法的操作数栈的最大深度。运行时，JVM根据这个值来分配栈帧中的操作栈深度。
 *	
 *	<item> max_locals
 *	  <-> 这个方法的局部变量表所需的存储空间。
	  <-> 单位是slot,slot是虚拟机为局部变量分配内存所使用的最小单位。
			byte,char,float,int,short,boolean,returnAddress都占用1个slot,
			double,long需要2个slot.
	  <-> 方法参数(包括实例方法的this),显式异常处理器的参数（catch块中定义的异常）,
			方法中定义的局部变量,都需要使用局部变量表来存放。
	  <-> 局部变量表中的slot可以重用。Javac编译器会根据变量的作用域来分配slot,
	  		然后计算出max_locals的大小。
 *
 *	<item> code_length
 *	code[]的byte的数目，这个一定不能为0，即code[]不能为空。
 *
 *	<item> code[]
 *	包含这个method的真正的字节码的byte.
 *
 *	<item> exception_table_length
 *	exception_table[]中entry的数目
 *
 *	<item> exception_table[]
 *	每一个entry描述一个在code[]中的异常handler.每一个entry包含以下4种item:
 *		start_pc, end_pc , handler_pc , catch_type
 *		<含义> :	当字节码在第code[start_pc]处，到code[end_pc]之间,出现了
 *				catch_type或其子类的异常
 *				(catch_type是指向一个CONSTANT_Class_info的索引)，
 *				则转到code[handler_pc]处执行。
 *				如果catch_type==0，表示任意异常都转到code[handler_pc]处执行。
 *		
 *
 *	<item> attributes_count
 *	这个Code Attribute包含的属性的个数。
 *	<item> attributes[]
 *	可以有以下的Attribute:	
 *	LineNumberTable , LocalVariableTable ,LocalVariableTypeTable
	StackMapTable
 */
public class CodeAttribute {
	
	//异常表的运作
	/*
	 * 运行结果应该是:
	 * 	如果没有异常发生,返回1.
	 * 	如果发生Exception异常,返回2.
	 * 	如果发生Exception以外的异常，没有返回值。
	 */
	public int inc(){
		int x;
		try{
			x = 1;
			return x;
		}
		catch(Exception e){
			x = 2;
			return x;
		}
		finally{
			x = 3;
		}
	}
	
	/**
	 * 得到字节码:
	 * 
	 * Code:
      stack=1, locals=5, args_size=1
         0: iconst_1	// 把常量1送到栈顶
         1: istore_1	// 把栈顶的值保存到1号本地变量	(1号slot就是x)
         2: iload_1		// 把1号本地变量的值送到栈顶
         3: istore   4  // 把栈顶的值保存到4号本地变量
         5: iconst_3	// 把常量3送到栈顶
         6: istore_1	// 把栈顶的值保存到1号本地变量
         7: iload    4	// 把4号本地变量的值送到栈顶
         9: ireturn		// return，返回栈顶的值 (即 1)
        10: astore_2	// 把catch中的e保存到2号slot中
        11: iconst_2	// 把常量2送到栈顶
        12: istore_1	// 把栈顶的值保存到1号本地变量
        13: iload_1		// 把1号本地变量的值送到栈顶
        14: istore   4	// 把栈顶的值保存到4号本地变量
        16: iconst_3	// 把常量3送到栈顶
        17: istore_1	// 把栈顶的值保存到1号本地变量
        18: iload    4	// 把4号本地变量的值送到栈顶
        20: ireturn		// return，返回栈顶的值 (即 1)
        21: astore_3	// 把异常变量(不属于Exception)保存到3号slot中
        22: iconst_3	// 把常量3送到栈顶
        23: istore_1	// 把栈顶的值保存到1号本地变量
        24: aload_3		// 将异常送到栈顶，抛出
        25: athrow
      Exception table:
         from    to  target type
             0     5    10   Class java/lang/Exception
             0     5    21   any
            10    16    21   any
	 */

}
