package jvm.classFileFormat.Attribute;

/**
 *		*******      EnclosingMethod属性                        ********
 *	EnclosingMethod属性是在 ClassFile 结构中的属性表的一个可变长度的属性。
 *	【作用】:LocalInner class（局部内部类）或匿名内部类拥有的属性
 *	【结构】:   
 * 		EnclosingMethod_attribute {
			u2 attribute_name_index;
			u4 attribute_length;
			u2 class_index;
			u2 method_index;
		}
 *	<item> attribute_name_index
 *		constant_pool entry的index,该entry是一个表示“EnclosingMethod”字符串的
 *		CONSTANT_Utf8_info。
 *
 *	<item> attribute_length
 *		这个属性的长度,为4.
 *
 *	<item> class_index
 *		constant_pool entry的index,该entry是一个表示之间包含此内部类的外部类的
 *		CONSTANT_Class_info。
 *
 *	<item> method_index
 *		如果当前内部类不是被方法包含,则method_index = 0;
 *		否则,method_index的值是constant_pool entry的index,该entry是一个表示
 *		包含当前类的方法的一个CONSTANT_NameAndType_info。
 */
public class EnclosingMethodAttribute {

	public void fun(){
		class inner{
			
		}
	}
}
