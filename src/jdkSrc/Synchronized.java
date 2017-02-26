package jdkSrc;

/**
 *				******	  synchronized的实现原理		****** 
 *
 *	<一> Java对象头
 *		synchronized用的锁是存在Java对象头里的。关于对象头,有以下知识点:
 *		
 *		<☆>	对象在堆里的逻辑结构:
 *			{  对象头 , 实例变量   }
 *		<☆> 如果对象是数组类型,虚拟机使用3个字宽存储对象头(用于存储数组长度),否则使用2个字宽。
 *			32位机,1字宽  = 4字节
 *		<☆> 对象头的结构:
 *			{  
 *				Mark Word , 				//	存储对象的hashCode 或锁信息
 *				Class Metadata Address , 	//  存储到对象类型数据的指针。
 *				Array Length				//	数组的长度(如果当前对象是数组)
 *			}
 *			对象头的信息都是与对象自身定义的数据无关的额外存储成本,考虑到虚拟机的空间效率,
 *			Mark Word被设计成一个非固定的数据结构以便尽量存储多点信息,它会根据对象锁的
 *			状态复用自己的存储空间。
 *
 *		<☆> Mark Word的状态变化
 *		-----------------------------------------------------------
 *			存储内容						标志位				状态
 *		-----------------------------------------------------------
 *		对象哈希码、对象分代年龄				01					未锁定
 *		-----------------------------------------------------------
 *		指向锁记录的指针						00					轻量级锁定
 *		-----------------------------------------------------------
 *		指向重量级锁的指针					10					膨胀(重量级锁定)
 *		-----------------------------------------------------------
 *		空,不需要记录信息						11					GC标记
 *		-----------------------------------------------------------
 *		偏向线程的ID、偏向时间戳、对象分代年龄		01					可偏向
 *		-----------------------------------------------------------
 *
 *	<二> 锁的升级
 *		<◆> 为了减少获得锁和释放锁的开销,引入"偏向锁"和"轻量级锁"。
 *			Java1.6中,锁一共有4种状态,级别从低到高分别是:
 *			～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～
 *				无锁状态 -> 偏向锁状态 -> 轻量级锁状态 -> 重量级锁状态
 *			～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～
 *			这几个状态会随着竞争情况逐渐升级,锁可以升级但不能降级。
 *
 *	  ---------------------------------------------------------------
 *	  ■ 偏向锁
 *
 */

public class Synchronized {
	
}