package jvm.classFileFormat.Attribute;

/**
 *			****     InnerClasses 属性             *******
 *	Code属性是在 ClassFile 结构中的属性表的一个可变长度的属性。
 *	【作用】:用于记录内部类与其宿主类之间的关联。
 *	【结构】:
 *		InnerClasses_attribute {
			u2 attribute_name_index;
			u4 attribute_length;
			u2 number_of_classes;
			{ 
				u2 inner_class_info_index;
				u2 outer_class_info_index;
				u2 inner_name_index;
				u2 inner_class_access_flags;
			} classes[number_of_classes];
		}
 *	<item> attribute_name_index
 *		constant_pool entry的index,该entry是一个表示“InnerClasses”
 *		字符串的CONSTANT_Utf8_info。
 *
 *	<item> attribute_length
 *		这个属性的长度，不包括前6字节.
 *
 *	<item> number_of_classes
 *		classes[]中entry的数目。
 *
 *	<item> classes[]
 *		每一个classes[]中entry,都是一个内部类的描述。
 *		~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *		<a> inner_class_info_index
 *			指向常量池中的一个表示内部类的CONSTANT_Class_info。
 *		<b> outer_class_info_index
 *			指向常量池中的一个表示外部类的CONSTANT_Class_info。
 *		<c> inner_name_index
 *			如果所描述的内部类是匿名内部类,则inner_name_index = 0.
 *			否则,inner_name_index的值是一个表示inner class的CONSTANT_Utf8_info。
 *		<d> inner_class_access_flags
 *			内部类的访问标志。
 *		~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

public class InnerClassesAttribute {

	public class A{
		
	}
	
	public static class B{
		
	}

	public Thread t = new Thread(){
		
		public void run(){
			
		}
	};
}
