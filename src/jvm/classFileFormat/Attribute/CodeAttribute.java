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
 *	这个方法的操作数栈的最大深度。
 *	
 *	<item> max_locals
 *	这个方法的局部变量的数目。
 *
 *	<item> code_length
 *	code[]的byte的数目，这个一定不能为0，即code[]不能为空。
 *
 *	<item> code[]
 *	包含这个method的真正的字节码的byte.
 *
 *	<item> exception_table_length
 *
 */
public class CodeAttribute {

}
