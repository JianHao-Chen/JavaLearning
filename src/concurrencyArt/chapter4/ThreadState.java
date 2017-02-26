package concurrencyArt.chapter4;

/**
 *		**************		线程的状态			**************
 *	
 *	在给定的一个时刻,线程只能处于其中的一个状态:
 *	<NEW> 			初始状态,线程被创建,但是还没有调用start()方法。
 *	<RUNNABLE>		运行状态,Java线程将操作系统中的就绪和运行2种状态笼统称作"运行中"。
 *	<BLOCKED>		阻塞状态,表示线程阻塞于锁
 *	<WAITING>		等待状态,表示线程进入等待状态，进入该状态表示当前线程需要等待其他线程
 *					做出一些特定的动作(通知或中断)
 *	<TIME_WAITING>	超时等待状态,该状态不同于 WAITING,它是可以在指定的时间自行返回的。
 *	<TERMINATED>	终止状态,表示线程已经执行完毕。
 *
 *	--> 键入JPS , 得到进程 ID 3544
 *	--> 键入 jstack 3544 , 得到运行中的线程状态。
 *
 *							【线程状态变迁】
 *
 *	<1> NEW -> RUNNABLE(包含RUNNING、READY)	:	Thread.start()
 *
 *	<2> RUNNING -> READY : yield()
 *		READY -> RUNNING : 系统调度
 *
 *	<3> RUNNABLE -> WAITING : Object.wait()  || Object.join()
 *		WAITING -> RUNNABLE : Object.notify() || Object.notifyAll()
 *
 *	<4> RUNNABLE -> TIME_WAITING : Thread.sleep() || Object.wait(long)
 *		TIME_WAITING -> RUNNABLE : Object.notify() || Object.notifyAll()
 *
 *	<5> RUNNABLE -> BLOCKED : 等待进入synchronized块/方法
 *		BLOCKED -> RUNNABLE : 获取到锁.
 *
 *
 *	
 */

public class ThreadState {

	public static void main(String[] args) {
		
		// Thread.State: TIMED_WAITING (sleeping)
		new Thread(new TimeWaiting(),"TimeWaitingThread").start();
		
		// Thread.State: WAITING (on object monitor)
		new Thread(new Waiting(),"WaitingThread").start();
		
		//使用2个Blocked线程,一个获取锁成功,另一个被阻塞
		//	Thread.State: TIMED_WAITING (sleeping)
		new Thread(new Blocked(),"BlockedThread-1").start();
		//	Thread.State: BLOCKED (on object monitor)
		new Thread(new Blocked(),"BlockedThread-2").start();
	}
	
	
	// 该线程不断的进行睡眠
	static class TimeWaiting implements Runnable{
		public void run(){
			while(true){
				SleepUtils.second(100);
			}
		}
	}
	
	// 该线程在Waiting.class实例上等待
	static class Waiting implements Runnable{
		public void run(){
			while(true){
				synchronized(Waiting.class){
					try{
						Waiting.class.wait();
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	//该线程在Blocked.class实例上加锁后,不会释放该锁
	static class Blocked implements Runnable{
		public void run(){
			synchronized(Blocked.class){
				while(true){
					SleepUtils.second(100);
				}
			}
		}
	}
}
