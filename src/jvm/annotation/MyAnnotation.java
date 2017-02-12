package jvm.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * 	这里 MyAnnotation的字节码是:
 * 
 * 	public interface MyAnnotation extends java.lang.annotation.Annotation
 
  	RuntimeVisibleAnnotations:
    	0: #14(#15=[e#16.#17])
    	1: #18(#15=e#19.#20)
 
  	flags: ACC_PUBLIC, ACC_INTERFACE, ACC_ABSTRACT, ACC_ANNOTATION

	Constant pool:
	   #1 = Class              #2             //  jvm/annotation/MyAnnotation
	   #2 = Utf8               jvm/annotation/MyAnnotation
	   #3 = Class              #4             //  java/lang/Object
	   #4 = Utf8               java/lang/Object
	   #5 = Class              #6             //  java/lang/annotation/Annotation
	   #6 = Utf8               java/lang/annotation/Annotation
	   #7 = Utf8               count
	   #8 = Utf8               ()I
	   #9 = Utf8               AnnotationDefault
	  #10 = Integer            1
	  #11 = Utf8               SourceFile
	  #12 = Utf8               MyAnnotation.java
	  #13 = Utf8               RuntimeVisibleAnnotations
	  #14 = Utf8               Ljava/lang/annotation/Target;
	  #15 = Utf8               value
	  #16 = Utf8               Ljava/lang/annotation/ElementType;
	  #17 = Utf8               TYPE
	  #18 = Utf8               Ljava/lang/annotation/Retention;
	  #19 = Utf8               Ljava/lang/annotation/RetentionPolicy;
	  #20 = Utf8               RUNTIME
	{
  	public abstract int count();
    	flags: ACC_PUBLIC, ACC_ABSTRACT
    	AnnotationDefault:
      		default_value: I#10
 *	}
 */


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {
	int count() default 1;
}
