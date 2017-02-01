package jvm.classFileFormat;

/**
 *		JVM 指令不依赖于类、接口运行时的布局(layout)，事实上，它引用 constant_pool
 *		的符号信息。
 *
 *		constant_pool中的每一项 entry都有如下格式:
 *			cp_info{
 *				u1 tag;		// 用于指示这个entry是什么类型（1 byte长度）
 *				u1 info[];	// 根据 tag，会有不同的结构(长度也各不相同)。
 *			}
 *		
 *-----------------------------------------------------------------------
 *	『各种常量结构的介绍』
 *	
 *	<1> CONSTANT_Class_info
 *		用于表示一个类或接口。
 *		CONSTANT_Class_info {
			u1 tag;
			u2 name_index;
		}	
 *		
 *		tag item 的值:
 *			CONSTANT_Class （7）
 *		name_index 的值:
 *			必须是一个constant_pool的有效的index，由这个index指向的entry,
 *			必须是一个 CONSTANT_Utf8_info 类型的 常量，它表示这个类或接口的全限定名。
 *
 *	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *
 *	<2> CONSTANT_Fieldref_info 和
 *		CONSTANT_Methodref_info 和
 *		CONSTANT_InterfaceMethodref_info
 *
 *		它们3个的结构都类似 :
 *
 *		CONSTANT_Fieldref_info {
			u1 tag;
			u2 class_index;
			u2 name_and_type_index;
		}
		
		tag 的值:	CONSTANT_Fieldref （9）
		class_index 的值:
			必须是一个constant_pool的有效的index，由这个index指向的entry,
			必须是一个 CONSTANT_Class_info 类型的常量。
		name_and_type_index的值:
			必须是一个constant_pool的有效的index，由这个index指向的entry,
			必须是一个 CONSTANT_NameAndType_info类型的常量，这个常量用于描述
			名字和(field/method)的描述符
 *	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *
 *	<3> CONSTANT_String_info
 *		用于表示 String 类型的常量。
 *		CONSTANT_String_info {
			u1 tag;
			u2 string_index;
		}
		
 *		tag item 的值:	CONSTANT_String (8).
 *		string_index 的值:
 *		必须是一个constant_pool的有效的index，由这个index指向的entry,
		必须是一个 CONSTANT_Utf8_info 类型的 常量，它表示这个String的值。
		
 *	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *
 *	<4> CONSTANT_NameAndType_info
 *		用于表示 一个Field 或一个 Method，但不包括指示出它属于哪个类/接口。
 *		CONSTANT_NameAndType_info {
			u1 tag;
			u2 name_index;
			u2 descriptor_index;
		}
 *
 *		tag item 的值:	CONSTANT_NameAndType (12).
 *		name_index 的值:	
 *		必须是一个constant_pool的有效的index，由这个index指向的entry,
 *		必须是一个 CONSTANT_Utf8_info 类型的 常量，用于表示 Field/Method的名字。
 *		descriptor_index 的值:
 *		必须是一个constant_pool的有效的index，由这个index指向的entry,
 *		必须是一个 CONSTANT_Utf8_info 类型的 常量，用于表示 Field/Method的descriptor。
 *
 * 	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 
 * 	<5> CONSTANT_Utf8_info
 * 		用于表示String常量的值。
 * 		CONSTANT_Utf8_info {
			u1 tag;
			u2 length;
			u1 bytes[length];
		}
 *
 *		tag item 的值:	CONSTANT_Utf8 (1).
 *		length	  的值:	
 *			给出后面byte数组的长度，而不是这个String有多少个字符。
 *		bytes	:
 *			包含所表示的String的byte。
 *
 */
public class ConstantPool {

}
