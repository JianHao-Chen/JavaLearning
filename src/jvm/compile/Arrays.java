package jvm.compile;



public class Arrays {
	
	/**
	 *	使用 	<code> newarray </code> 创建指定原始类型的数组。
	 *	
	 *	void createBuffer()编译为:
	 *	
		 	0: 	bipush        100	// Push int constant 100 (bufsz)
		    2: 	istore_2			// Store bufsz in local variable 2	
		    3: 	bipush        12	// Push int constant 12 (value)
		    5: 	istore_3			// Store value in local variable 3
		    6: 	iload_2				// Push bufsz...
		    7: 	newarray      int	// ...and create new int array of that length
		    9: 	astore_1			// Store new array in buffer
		   10: 	aload_1				// Push buffer
		   11: 	bipush        10	// Push int constant 10
		   13: 	iload_3				// Push value
		   14: 	iastore				// Store value at buffer[10]
		   15: 	aload_1				// Push buffer
		   16: 	bipush        11	// Push int constant 11
		   18: 	iaload				// Push value at buffer[11]...
		   19: 	istore_3			// ...and store it in value
		   20: 	return
	 *
	 *	【要点】
	 *<1>
	 *	newarray :
	 *		创建一个指定原始类型（如int, float, char…）的数组，并将其引用值压入栈顶
	 *
	 *<2>
	 *	iastore :
	 *		将栈顶int型数值存入指定数组的指定索引位置
	 *	 iaload :
	 *		将int型数组指定索引的值推送至栈顶
	 */
	void createBuffer() {
		
		int buffer[];
		int bufsz = 100;
		int value = 12;
		buffer = new int[bufsz];
		buffer[10] = value;
		value = buffer[11];
	}
	
//-----------------------------------------------------------------
	/**
	 * 使用 	<code> anewarray </code> 创建引用型数组。
	 * 
	 * 	void createThreadArray()编译为:
	 * 
	 * 	 0: 	bipush        10		// Push int constant 10
         2: 	istore_2				// Initialize count to that
         3: 	iload_2					// Push count, used by anewarray
         4: 	anewarray     #21		// Create new array of class Thread
         7: 	astore_1				// Store new array in threads
         8: 	aload_1					// Push value of threads
         9: 	iconst_0				// Push int constant 0
        10: 	new           #21		// Create instance of class Thread
        13: 	dup						// Make duplicate reference...
        14: 	invokespecial #23		// ...for Thread's constructor
        								// Method java/lang/Thread."<init>":()V
        17: 	aastore					// Store new Thread in array at 0
        18: 	return
	 *
	 *【要点】
	 *<1>
	 *	anewarray :
	 *		创建一个引用型（如类，接口，数组）的数组，并将其引用值压入栈顶
	 */
	
	void createThreadArray() {
		Thread threads[];
		int count = 10;
		threads = new Thread[count];
		threads[0] = new Thread();
	}
	
	
//----------------------------------------------------------------------
	
	/**
	 * 	使用 	<code> multianewarray </code> 创建指定类型和指定维度的多维数组。
	 * 
	 * 	int[][][] create3DArray()编译为:
	 * 	
	 * 	 0: 	bipush        10			// Push int 10 (dimension one)
         2: 	iconst_5					// Push int 5 (dimension two)
         3: 	multianewarray #29,  2		// Class [[[I, a three-dimensional int array;
         									//  only create the first two dimensions
         7: 	astore_1					// Store new array...
         8: 	aload_1						// ...then prepare to return it
         9: 	areturn
	 *
	 *	【要点】
	 *<1>
	 *	multianewarray :
	 *		创建指定类型和指定维度的多维数组（执行该指令时，操作栈中必须包含各维度的长度值），
	 *		并将其引用值压入栈顶
	 */
	
	int[][][] create3DArray() {
		int grid[][][];
		grid = new int[10][5][];
		return grid;
	}
	
}
