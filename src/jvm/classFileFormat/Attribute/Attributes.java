package jvm.classFileFormat.Attribute;

/**
 * ~~~~~~~~~~~~	属性表(attribute_info)	~~~~~~~~~~~~~~~~
 * 
 * 	在Class文件、字段表(field_info)、方法表(method_info)都可以带自己的属性表集合。
 * 	
 * 	所有的属性表都有以下的结构:
 * 	attribute_info {
		u2 attribute_name_index;
		u4 attribute_length;
		u1 info[attribute_length];
	}
 *	
 *	<item> attribute_name_index
 *	必须是一个有效的指向constant_pool中 entry的index，这个entry必须是一个
 *	CONSTANT_Utf8_info 的数据结构，它表示一个这个 attribute 的名字。
 *
 *	<item> attribute_length
 *	指出接下来的attribute信息有多少byte。
 *
 *	<item> info[]
 *	各种预定义的类文件 attribute:
 *
 */
public class Attributes {

}
