package jvm.classFileFormat;

/**
 * 	描述符(descriptor)是一个用于在『class文件』表示一个Field或方法的类型的字符串。
 * 	
 * 	签名(signature)是一个用于在『class文件』表示一个Field或方法的 generic type。
 * 
 * ---------------------------------------------------------------------
 * 
 * 							<< 描述符  >>
 * 
 * 	【Field 描述符】
 * 		FieldType
 * 
 * 		FieldType可以是:	BaseType  ObjectType  ArrayType
 * 			<li> BaseType
 * 				B(byte) 	C(char) 
 * 				D(double) 	F(float) 
 * 				I(int) 		J(long) 
 * 				S(short) 	Z(boolean)
 * 		
 * 			<li> ObjectType
 * 				L ClassName;
 * 				例如:	Ljava/lang/Object;
 * 
 * 			<li> ArrayType
 * 				[ComponentType
 * 				
 * 				ComponentType :
 * 					FieldType
 * 	
 * 【Method 描述符】
 * 		( ParameterDescriptor* ) ReturnDescriptor
 * 	ParameterDescriptor是传递到这个方法的参数。
 * 		它的类型是 	:	FieldType
 * 
 * 	ReturnDescriptor是这个方法返回的值的类型。
 * 		它的类型是 	:	FieldType 或  VoidDescriptor
 * 
 * 	例子:
 * 		方法	Object m(int i, double d, Thread t) {..}的描述符:
 * 			(IDLjava/lang/Thread;)Ljava/lang/Object;
 * 
 * 	【注意】
 * 		方法m是类方法还是实例方法，它的描述符都是一样的。尽管实例方法会传递一个this
 * 		引用作为它的参数，但是这个操作是在调用实例方法时，由JVM 调用方法invoke指令时，
 * 		隐式传递的。
 *
 *
 *-----------------------------------------------------------------------
 *							
 *
 *
 *
 */

public class DescriptorsAndSignatures {
}
