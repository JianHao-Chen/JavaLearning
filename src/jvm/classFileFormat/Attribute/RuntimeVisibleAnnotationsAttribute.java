package jvm.classFileFormat.Attribute;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
/**
 *		*********  RuntimeVisibleAnnotations属性        **************
 *	
 *	【结构】: 
 *		RuntimeVisibleAnnotations_attribute {
			u2 			attribute_name_index;
			u4 			attribute_length;
			u2 			num_annotations;
			annotation 	annotations[num_annotations];
		}
 *	<item> attribute_name_index
 *		constant_pool entry的index,该entry是一个表示“RuntimeVisibleAnnotations”
 *		字符串的CONSTANT_Utf8_info。
 *
 *	<item> attribute_length
 *		这个属性的长度，不包括前6字节.
 *
 *	<item> num_annotations
 *		annotations[]中item的数目
 *
 *	<item> annotations[]
 *		每一个annotations[]中entry,都是一个annotation结构。
 *		annotation的结构:
 *		~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *		annotation {
			u2 type_index;
			u2 num_element_value_pairs;
			{ 
				u2 element_name_index;
				element_value value;
			} element_value_pairs[num_element_value_pairs];
		}
		
		<item> type_index
			constant_pool entry的index,该entry是一个表示当前annotation类型的
 *			字符串的CONSTANT_Utf8_info。
 *			
 *		<item> num_element_value_pairs
 *			element_value_pairs[]中item的数目。
 *
 *		<item> 
 *			每一个element_value_pairs[]中的元素,都包含以下2部分:
 *			<1> element_name_index
 *				
 *			<2> value -- (element_value)
 *
 *			【element_value的结构】 
 *	#########################################################
 *		element_value {
			u1 tag;
			union {
				u2 const_value_index;
				{ 
					u2 type_name_index;
					u2 const_name_index;
				} enum_const_value;
				u2 class_info_index;
				annotation annotation_value;
				{ 
					u2 num_values;
					element_value values[num_values];
				} array_value;
			} value;
		}
 *	
 *		<item> tag
 *		这个注解的element-value pair的类型
 *		<item> value
 *		这个注解的element,这是一个union。tag的值,决定这个value的union是哪一个。
 *			<union> const_value_index
 *				如果tag 是  B ,  C ,  D ,  F ,I ,  J ,  S ,  Z , or  s
 *			 	则 这个union是一个常量池entry(指向基本类型、string)的index。
 *
 *			<union> enum_const_value
 *				如果tag 是  e ,则 这个union包含2部分:
 *				<enum> type_name_index
 *					enum 的类型
 *				<enum> const_name_index
 *					enum 的值
 *
 *			<union> class_info_index
 *				如果tag 是  c , 这个union是一个常量池entry(表示class)的index。
 *			
 *			<union> annotation_value
 *			<union> array_value
 * 			
 *		
 *	#########################################################
 *	
 *
 *
 *		~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
 */
public class RuntimeVisibleAnnotationsAttribute {
	
}



