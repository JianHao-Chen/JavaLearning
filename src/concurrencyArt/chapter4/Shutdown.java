package concurrencyArt.chapter4;

import java.util.concurrent.TimeUnit;

public class Shutdown {
	
	public static void main(String[] args) throws Exception {
		Runner one = new Runner();
		Thread countThread = new Thread(one,"CountThread");
		countThread.start();
		// 睡眠 1 秒,main线程对 CountThread进行中断,使CountThread能
		// 感知中断而结束
		TimeUnit.SECONDS.sleep(1);
		countThread.interrupt();
		
		
		Runner two = new Runner();
		countThread = new Thread(two,"CountThread");
		countThread.start();
		// 睡眠 1 秒,main线程对 CountThread进行中断,使CountThread能
		// 感知on为false而结束
		TimeUnit.SECONDS.sleep(1);
		two.cancel();
	}
	
	private static class Runner implements Runnable{
		private long i;
		private boolean on = true;
		
		public void run(){
			while(on && !Thread.currentThread().isInterrupted()){
				i++;
			}
			System.out.println("Count i = "+i );
		}
		
		public void cancel(){
			on = false;
		}
	}
}
