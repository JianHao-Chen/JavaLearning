package jvm.compile;

/**
 *		invokespecial instruction use to invoke:
 *		<a>	初始化方法	<b> 私有方法		<c> 父类方法
 *
 */

public class showInvokeSpecial {
	
	private int privateMethod(int i){return 0;}
	
	/**
	 * 	 0: new           #1       // class learn/compile/showInvokeSpecial
         3: invokespecial #19      // Method "<init>":()V
         6: return
         
         in constant pool:
        	#1 = Class              #2    // learn/compile/showInvokeSpecial
   			#2 = Utf8               learn/compile/showInvokeSpecial
   		
   			#19 = Methodref         #1.#9   // learn/compile/showInvokeSpecial."<init>":()V
   			#9 = NameAndType        #5:#6    // "<init>":()V
   		
   			#5 = Utf8               <init>
   			#6 = Utf8               ()V
	 */
	public void callInit(){
		new showInvokeSpecial();
	}
	
	
	/**
	 * 	 0: aload_0
         1: iconst_1
         2: invokespecial #21    // Method privateMethod:(I)I
         5: pop
         6: return
         
         #21 = Methodref          #1.#22         // learn/compile/showInvokeSpecial.privateMethod:(I)I
         #22 = NameAndType        #14:#15        // privateMethod:(I)I
         #14 = Utf8               privateMethod
  		 #15 = Utf8               (I)I
	 */
	
	public void callprivate(){
		privateMethod(1);
	}
	
	/**
	 * 	 0: aload_0
         1: invokespecial #24                 // Method java/lang/Object.toString:()Ljava/lang/String;
         4: pop
         5: return
         
         #24 = Methodref          #3.#25     // java/lang/Object.toString:()Ljava/lang/String;
         #3 = Class               #4         // java/lang/Object
         #4 = Utf8                java/lang/Object
         #25 = NameAndType        #26:#27    // toString:()Ljava/lang/String;
  		 #26 = Utf8               toString
  		 #27 = Utf8               ()Ljava/lang/String;
	 */
	public void callSuper(){
		super.toString();
	}
	
	
}
