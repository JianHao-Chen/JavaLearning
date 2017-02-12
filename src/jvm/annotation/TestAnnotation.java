package jvm.annotation;


/**
 *	 TestAnnotation的字节码如下:
 *
 *	public class jvm.annotation.TestAnnotation
  
  	RuntimeVisibleAnnotations:
    	0: #31(#26=I#35)
 
  	flags: ACC_PUBLIC, ACC_SUPER
  	
	Constant pool:
	   #1 = Class              #2             //  jvm/annotation/TestAnnotation
	   #2 = Utf8               jvm/annotation/TestAnnotation
	   #3 = Class              #4             //  java/lang/Object
	   #4 = Utf8               java/lang/Object
	   #5 = Utf8               <init>
	   #6 = Utf8               ()V
	   #7 = Utf8               Code
	   #8 = Methodref          #3.#9          //  java/lang/Object."<init>":()V
	   #9 = NameAndType        #5:#6          //  "<init>":()V
	  #10 = Utf8               LineNumberTable
	  #11 = Utf8               LocalVariableTable
	  #12 = Utf8               this
	  #13 = Utf8               Ljvm/annotation/TestAnnotation;
	  #14 = Utf8               main
	  #15 = Utf8               ([Ljava/lang/String;)V
	  #16 = Class              #17            //  jvm/annotation/MyAnnotation
	  #17 = Utf8               jvm/annotation/MyAnnotation
	  #18 = Methodref          #19.#21        //  java/lang/Class.getAnnotation:(Ljava/lang/Class;)Ljava/lang/annotation/Annotation;
	  #19 = Class              #20            //  java/lang/Class
	  #20 = Utf8               java/lang/Class
	  #21 = NameAndType        #22:#23        //  getAnnotation:(Ljava/lang/Class;)Ljava/lang/annotation/Annotation;
	  #22 = Utf8               getAnnotation
	  #23 = Utf8               (Ljava/lang/Class;)Ljava/lang/annotation/Annotation;
	  #24 = InterfaceMethodref #16.#25        //  jvm/annotation/MyAnnotation.count:()I
	  #25 = NameAndType        #26:#27        //  count:()I
	  #26 = Utf8               count
	  #27 = Utf8               ()I
	  #28 = Utf8               args
	  #29 = Utf8               [Ljava/lang/String;
	  #30 = Utf8               annotation
	  #31 = Utf8               Ljvm/annotation/MyAnnotation;
	  #32 = Utf8               SourceFile
	  #33 = Utf8               TestAnnotation.java
	  #34 = Utf8               RuntimeVisibleAnnotations
	  #35 = Integer            11
{
  public jvm.annotation.TestAnnotation();
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #8                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 5: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
               0       5     0  this   Ljvm/annotation/TestAnnotation;

  public static void main(java.lang.String[]);
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=2, args_size=1
         0: ldc           #1                  // class jvm/annotation/TestAnnotation
         2: ldc           #16                 // class jvm/annotation/MyAnnotation
         4: invokevirtual #18                 // Method java/lang/Class.getAnnotation:(Ljava/lang/Class;)Ljava/lang/annotation/Annotation;
         7: checkcast     #16                 // class jvm/annotation/MyAnnotation
        10: astore_1
        11: aload_1
        12: invokeinterface #24,  1           // InterfaceMethod jvm/annotation/MyAnnotation.count:()I
        17: pop
        18: return
      LineNumberTable:
        line 8: 0
        line 9: 11
        line 10: 18
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
               0      19     0  args   [Ljava/lang/String;
              11       8     1 annotation   Ljvm/annotation/MyAnnotation;
}
 *
 */

@MyAnnotation(count=11)
public class TestAnnotation {

	public static void main(String[] args) {
		MyAnnotation annotation = TestAnnotation.class.getAnnotation(MyAnnotation.class);
		annotation.count();
	}
}
