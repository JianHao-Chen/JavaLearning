package jvm.classFileFormat;

/**
 *	A class file consists of a single ClassFile structure:
 *
 *	ClassFile {
		u4 				magic;
		u2 				minor_version;
		u2 				major_version;
		u2 				constant_pool_count;
		cp_info 		constant_pool[constant_pool_count-1];
		u2 				access_flags;
		u2 				this_class;
		u2 				super_class;
		u2 				interfaces_count;
		u2 				interfaces[interfaces_count];
		u2 				fields_count;
		field_info 		fields[fields_count];
		u2 				methods_count;
		method_info 	methods[methods_count];
		u2 				attributes_count;
		attribute_info 	attributes[attributes_count];
	}
	
	上面每一个item的【解释】:
	
	<magic>
		提供 magic number ,用于识别 文件的格式，这里作为一个class文件
		时 value = 0xCAFEBABE。
	
	<minor_version>和<major_version>
		一起决定class 文件的版本。
	
	<constant_pool_count>
		它的值是 constant_pool table上面entry的数目 +1.
		
	< constant_pool[constant_pool_count-1]>
		它是一个table,每一项元素都是一个“数据结构”(会在后面提到),用于
		表示各种string常量，类、接口的名字，还有其他在这个ClassFile 引用到
		的常量。
		
	<access_flags>
		这是一个掩码，用于表示访问权限，类、接口的属性
		
	<this_class>
		它的值必须是一个有效的 constant_pool 的 index。
		并且，在constant_pool[index]处的entry,必须是一个	表示类、接口的
		CONSTANT_Class_info 结构。
	
	<super_class>
		与 <this_class>类似，表示 <this_class>的直接父类。
		
	<interfaces_count>
		它的值表示这个类/接口的直接 继承的接口的数目。
		
	<interfaces[interfaces_count]>
		interfaces[]的每一个值， 都是constant_pool中的一个有效index,
		而其中每一个 constant_pool[index]处的entry,必须是一个	表示接口的
		CONSTANT_Class_info 结构。
		
		他们的顺序是按照源码中给定的，从左到右。
		
	<fields_count>
		它的值表示 fields table 中 field_info structures的数目，
		field_info structures表示这个类/接口声明的所有变量(包括类变量和实例变量)。
		
	<fields[]>
		fields table 的每一个值 fields[i]，都必须是一个 field_info structure。
		field_info是对一个field的完整描述的数据结构。
		fields table 只包含在本类声明的field,不包含父类/接口的field。
		
	<methods_count>
		它的值表示 methods table 中 method_info structures的数目。
		
	<methods[]>
		methods table 的每一个值 methods[i],都必须是一个 method_info structure.
		method_info 是对一个方法的完整描述的数据结构。
		methods table只包含在本类/接口声明的所有方法(包含类方法，实例方法，构造函数
		类初始化函数)。 不包含父类 /接口继承的来的方法。
	
	<attributes_count>
		它的值表示 attributes table 中 attribute_info structure的数目。
		
	<attributes[]>
		attributes table的每一个值attributes[i],都必须是一个attribute_info structure.
		
 *
 */
public class ClassFileStructure {

}
