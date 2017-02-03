package jvm.classFileFormat;

/**
 *	 Field(包括类变量和实例变量),用于描述field的数据结构应该包含什么信息？
 *	  <li>字段的作用域(public、 private、 protected 修饰符)、 
 *	  <li>是类变量还是实例变量(static 修饰符)
 *	  <li>可变性 (final)
 *	  <li>并发可见性（volatile修饰符,是否强制从主内存读写）
 *	  <li>可否被序列化(transient修饰符)
 *-------------------------------------------------------------------
 *	  <li>字段数据类型(基本类型、对象、数组)
 *	  <li>字段名称
 *
 *	可以看出: 各个修饰符可以用标志位来表示，而字段名称、字段的数据类型则只能引用常量池的
 *			常量来描述。
 *
 *	使用 field_info 来描述 Field:
 *
 *	field_info {
		u2 				access_flags;
		u2 				name_index;
		u2 				descriptor_index;
		u2 				attributes_count;
		attribute_info 	attributes[attributes_count];
	}
	
 *························
 *· <item> access_flags  ·
 *························
 *  	它的值是一个flag的掩码(mask)，用于指示这个field的访问权限和属性。
 *  列出每一个flag:
 *  ----------------------------------------------------------------------
 *  ACC_PUBLIC 		0x0001 		Declared public; may be accessed from outside its package.
	ACC_PRIVATE 	0x0002 		Declared private; usable only within the defining class.
	ACC_PROTECTED 	0x0004 		Declared protected; may be accessed within subclasses.
	ACC_STATIC 		0x0008 		Declared static.
	ACC_FINAL 		0x0010 		Declared final; never directly assigned to after object construction (JLS §17.5).
	ACC_VOLATILE 	0x0040 		Declared volatile; cannot be cached.
	ACC_TRANSIENT 	0x0080 		Declared transient; not written or read by a persistent object manager.
	ACC_SYNTHETIC 	0x1000 		Declared synthetic; not present in the source code.
	ACC_ENUM 		0x4000 		Declared as an element of an enum.
 *  ----------------------------------------------------------------------
 *	<flag> ACC_SYNTHETIC
 *		如果一个field不存在与源码而是由编译器生成的，那么它会被mark成ACC_SYNTHETIC。
 *
 *	<flag> ACC_ENUM
 *		如果这个field的类型是 一个枚举类型，那么它会被mark成ACC_ENUM。
 *
 *	【注意】
 *	<1> 一个field最多只会设置 ACC_PUBLIC ， ACC_PRIVATE ， ACC_PROTECTED的一个。
 *	<2> 一定不能同时设置 ACC_FINAL ， ACC_VOLATILE。
 *	<3> 接口中定义的field，应该都设置ACC_PUBLIC ， ACC_STATIC ， ACC_FINAL。
 *
 *
 *~~~~~~~~~~~~~~~~~~~~~~~~
 *~ <item> name_index    ~
 *~~~~~~~~~~~~~~~~~~~~~~~~
 *	必须是一个有效的指向constant_pool中 entry的index，这个entry必须是一个
 *	CONSTANT_Utf8_info 的数据结构，它表示一个这个field的名字。
 *
 *~~~~~~~~~~~~~~~~~~~~~~~~~
 *~<item> descriptor_index~
 *~~~~~~~~~~~~~~~~~~~~~~~~~
 *	必须是一个有效的指向constant_pool中 entry的index，这个entry必须是一个
 *	CONSTANT_Utf8_info 的数据结构，它表示一个这个field的descriptor。
 *
 *~~~~~~~~~~~~~~~~~~~~~~~~~~
 *~ <item> attributes_count~
 *~~~~~~~~~~~~~~~~~~~~~~~~~~
 *	它的值指出这个field的additional attributes的数目。
 *
 *~~~~~~~~~~~~~~~~~~~~~~~
 *~ <item> attributes[] ~
 *~~~~~~~~~~~~~~~~~~~~~~~
 *	它的每一项元素都是一个 attribute 结构，一个 field 可以有多个attribute与之关联。
 *
 */
public class Fields {

}
