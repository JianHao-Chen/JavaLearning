package concurrencyArt.chapter4;


/**
 *			~~~~~~		Thread.join()		~~~~~~~
 *
 * 	当前线程A执行 thread.join()方法,线程A会等待thread线程终止以后才会从join()方法返回。
 * 
 * 	join的实现:
 * 		通过对线程对象加锁:
 * 		public final synchronized void join()throws InterruptedException{
 * 			while (isAlive()) {
        		wait(0);
        	}
        }
 *
 *		而当一个线程终止时,会通过调用自身的notifyAll()方法,来通知所有等待在本线程对象的线程。
 *		
 *	
 */

public class Join {

	static class Domino implements Runnable{
		private Thread thread;
		public Domino(Thread thread){
			this.thread = thread;
		}
		
		public void run(){
			try{
				thread.join();
			}catch (Exception e) {
			}
			System.out.println(Thread.currentThread().getName() + "terminate.");
		}
	}
	
	public static void main(String[] args) {
		Thread previous = Thread.currentThread();
		for(int i = 0 ;i < 10 ; i++){
			Thread thread = new Thread(new Domino(previous),String.valueOf(i));
			thread.start();
			previous = thread;
		}
	}
}
