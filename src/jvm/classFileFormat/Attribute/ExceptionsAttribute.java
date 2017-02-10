package jvm.classFileFormat.Attribute;

/**
 *	 				**    Exceptions属性        **
 *
 *	此处的Exceptions属性,是与Code属性平级的属性。
 *	它也是 method_info 结构中的属性表的一个可变长度的属性。
 *	【作用】: 列出方法中可能抛出的受查异常(checked Exception),即方法描述时在throws
 *			后面列举的异常。
 *	【结构】:
 *		Exceptions_attribute {
			u2 attribute_name_index;
			u4 attribute_length;
			u2 number_of_exceptions;
			u2 exception_index_table[number_of_exceptions];
		}
		
 *	  <item> attribute_name_index
 *		constant_pool entry的index,该entry是一个表示“Exceptions”字符串
 *		的CONSTANT_Utf8_info。
 *
 *	  <item> attribute_length
 *		这个属性的长度，不包括前6字节。
 *	
 *	  <item> number_of_exceptions
 *		exception_index_table[]中item的数目。
 *
 *	  <item> exception_index_table[]
 *		constant_pool entry的index,该entry是一个表示异常类型的
 *		CONSTANT_Class_info。
 *		
 */
public class ExceptionsAttribute {

}
