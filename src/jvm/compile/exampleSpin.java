package jvm.compile;

/**
 * 		instruction form:
 * 	<index> <opcode> [ <operand1> [ <operand2>... ]] [<comment>]
 * 
 * 
 * 	JVM对spin()方法编译为:
 * 
 * 	0 	iconst_0 			// Push int constant 0
	1 	istore_1 			// Store into local variable 1 (i=0)
	2 	goto 8 				// First time through don't increment
	5 	iinc 1 1 			// Increment local variable 1 by 1 (i++)
	8 	iload_1 			// Push local variable 1 (i)
	9 	bipush 100 			// Push int constant 100
	11 	if_icmplt 5 		// Compare and loop if less than (i < 100)
	14 	return 				// Return void when done
	
	【要点】
	
  <1>
	上面把0和100两个常量push到操作数栈，用了2个不同的指令。
	push 0 用的是 	 <code> iconst_0 </code>, 100用的是 <code> bipush 100 </code>
 *	JVM使用 iconst_<i> instructions （int constants -1, 0, 1, 2, 3, 4 and 5 in the case）
 *	这样可以省略操作数。（节省存储空间，不用去提取(并解析)操作数）
 *
 *<2>
 *	int i保存在局部变量表(local variable)的1号slot.
 *
 *<3>
 *	istore_1 表示从操作数栈弹出一个int ，保存在局部变量表的1号slot.
 *	iinc 1 1 表示局部变量表的1号slot的
 *
 *<4>
 *	bipush 表示把100 作为int push到操作数栈。
 *
 *<5>
 *	if_icmplt 5  从操作数栈弹出一个int,并且和 当前栈顶元素比较(这里是i),
 *				如果 i<100, 控制跳转到 index=5 的地方。
 */


public class exampleSpin {

	void spin() {
		int i;
		for (i = 0; i < 100; i++) {
			; // Loop body is empty
		}
	}
}
