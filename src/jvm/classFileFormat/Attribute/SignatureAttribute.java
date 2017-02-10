package jvm.classFileFormat.Attribute;

/**
 *			********     Signature 属性              ***********
 *	
 * 	Signature是一个可选的定长的属性,可用于 ClassFiel、method_info、field_info的
 * 	属性表中。
 * 	【作用】:
 * 		任何类、接口、初始化方法、或成员的泛型签名如果包含了类型变量(Type variables)或
 * 		参数化类型(Parameterized Types),则Signature属性会为它记录泛型签名信息。
 * 
 * 	【原因】:
 * 		使用Signature 属性来记录泛型签名信息，是因为 Java的泛型采用的是擦除法实现的
 * 		伪泛型。
 * 	【结构】:
 * 		Signature_attribute {
			u2 attribute_name_index;
			u4 attribute_length;
			u2 signature_index;
		}
 *		
 *	<item> attribute_name_index
 *		constant_pool entry的index,该entry是一个表示“Signature”
 *		字符串的CONSTANT_Utf8_info。
 *
 *	<item> attribute_length
 *		这个属性的长度,这里一定为2.
 *
 *	<item> signature_index
 *		constant_pool entry的index,该entry是一个表示一个class的签名的
 *		字符串的CONSTANT_Utf8_info。
 *
 */
public class SignatureAttribute {

}
