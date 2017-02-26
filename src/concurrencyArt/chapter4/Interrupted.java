package concurrencyArt.chapter4;

import java.util.concurrent.TimeUnit;


/**
 *		【    Thread.interrupt()方法  】
 *	如果这个线程 
 *		<1> 阻塞于 Object.wait() 或  join() 或  sleep(long),那么它的中断标志被清除,
 *			并且它会受到一个InterruptedException。
 *		<2> 阻塞于 java.nio.channels.InterruptibleChannel上的I/O操作，那么这个
 *			channel会被关闭，这个线程的中断标志被设置，这个线程会受到 java.nio.channels.ClosedByInterruptException
 *		<3> 阻塞于 java.nio.channels.Selector,那么这个线程会从selection操作马上返回，
 *			就像是 Selector.wakeup方法被调用
 *		<4> 没有以上情况时,这个线程的中断标志被设置
 *
 *
 *		【    Thread.interrupted()方法  】
 *	测试这个线程的中断标志是否被设置(是否被interrupted),如果有，这个标志会被清除。
 *
 *
 *		【    Thread.isInterrupted()方法  】
 *	测试这个线程的中断标志是否被设置(是否被interrupted),并且中断标志不会被改变。
 *
 *
 */

public class Interrupted {

	public static void main(String[] args) throws Exception {
		// sleepThread 不停尝试睡眠
		Thread sleepThread = new Thread(new SleepRunner(),"SleepThread");
		sleepThread.setDaemon(true);
		
		// busyThread 不停的运行
		Thread busyThread = new Thread(new BusyRunner(),"SleepThread");
		busyThread.setDaemon(true);
		
		sleepThread.start();
		busyThread.start();
		
		// 睡眠 5 秒,让 sleepThread和busyThread充分运行
		TimeUnit.SECONDS.sleep(5);
		sleepThread.interrupt();
		busyThread.interrupt();
		
		System.out.println("SleepThread interrupted is "+
				sleepThread.isInterrupted());
		System.out.println("BusyThread interrupted is "+
				busyThread.isInterrupted());
		
		// 防止 sleepThread和busyThread 立即退出
		TimeUnit.SECONDS.sleep(2);
	}
	
	static class SleepRunner implements Runnable{
		public void run(){
			while(true){
				SleepUtils.second(10);
			}
		}
	}
	
	static class BusyRunner implements Runnable{
		public void run(){
			while(true){
			}
		}
	}
}
