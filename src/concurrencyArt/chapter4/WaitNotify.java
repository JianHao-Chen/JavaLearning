package concurrencyArt.chapter4;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 *			****	Wait / Notify	****
 *	
 *	####   Object.wait()  ####
 *	
 *	<1>
 *		当前线程执行对象A的wait方法使当前线程的状态由 RUNNABLE->WAITING,
 *		直到有某个线程执行对象A的notify()或notifyAll()方法。
 *	<2>
 *		线程想执行对象A的wait()方法,必须先获取对象A的monitor锁。
 *		否则,会抛出IllegalMonitorStateException异常.
 *
 *	<3>
 *		线程会释放对象的monitor锁,进入等待,直到某个线程执行notify操作。
 *		线程还是要等待直到它再次获取对象的monitor锁。
 *
 *	《4》
 *		对象A的wait()方法使得当前线程(称作T),把自己放到对象A的等待队列中,
 *		并且释放已获取的monitor锁。线程T变成不可调度的(处于休眠),直到以下3种情况之一发生:
 *			<s1>
 *				某个线程执行对象A的notify()方法，并且线程T刚好被选中来唤醒。
 *			<s2>
 *				某个线程执行对象A的notifyAll()方法。
 *			<s3>
 *				某个线程执行T的interrupt()方法
 *
 *		随后,线程T被移出对象A的等待队列,并且可以重新参加调度。
 *		【线程T还是要与其他的线程竞争对象A的monitor锁，一旦它获取到锁,它才从wait()方法返回】。
 *		
 *
 *	<5>
 *		执行wait()方法的线程进入对象A的等待队列并释放已获取的monitor锁，注意，仅仅是释放
 *		对象A的monitor锁，而线程T获取到的其他对象的monitor锁，仍然保持。
 *
 *	<6> 
 *		wait()方法通常的使用方法:
 *		synchronized (lockObject) {
 *         while (condition==false)
 *             obj.wait(timeout);
 *     }
 *     
 *     使用 while(condition)循环是因为: 
 *     <A> spurious wakeup(虚假唤醒)导致线程被唤醒,但是等待的条件还是不满足。
 *     <B> 多个线程同时wait(),有可能其他线程更早醒来修改了状态。
 *
 */


public class WaitNotify {

	static boolean flag = true;
	static Object lock = new Object();
	
	public static void main(String[] args) throws Exception{
		Thread waitThread = new Thread(new Wait(),"WaitThread");
		waitThread.start();
		TimeUnit.SECONDS.sleep(1);
		Thread notifyThread = new Thread(new Notify(),"NotifyThread");
		notifyThread.start();
	}
	
	static class Wait implements Runnable{
		public void run(){
			// 加锁,拥有 lock的Monitor
			synchronized(lock){
				// 当条件不满足,继续wait,同时释放lock的锁
				while(flag){
					try{
						System.out.println(Thread.currentThread()+" flag is true. wait" +
								" @ "+new SimpleDateFormat("HH:mm:ss").format(new Date()));
						lock.wait();
					}
					catch(InterruptedException e){
						System.out.println("Got InterruptedException while wait()");
					}
				}
				// 条件满足,完成工作
				System.out.println(Thread.currentThread()+" flag is false. running" +
								" @ "+new SimpleDateFormat("HH:mm:ss").format(new Date()));
			}
		}
	}
	
	
	static class Notify implements Runnable{
		public void run() {
			// 加锁,拥有 lock的Monitor
			synchronized(lock){
				// 获取锁以后，进行通知，通知时不会释放lock的锁,
				// 直到当前线程释放了lock的锁,WaitThread才能从wait()返回。
				System.out.println(Thread.currentThread()+" hold lock. notify" +
								" @ "+new SimpleDateFormat("HH:mm:ss").format(new Date()));
				lock.notifyAll();
				flag = false;
				SleepUtils.second(5);
			}
			
		}
	}
}
