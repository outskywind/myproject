package concurrency;

import java.util.Random;

public class Threadtest {
	private static boolean ready;
	private static int number;
	private static unsafethread unsafethread;
	private static Random rd=new Random();
	public static class Threadrun extends Thread{
		public void run() {
			while(!ready) {
				Thread.yield();
			}
			System.out.println(Threadrun.currentThread().getId()+" "+number);
			unsafethread.getfactor(rd.nextInt(99));
		}
		
	}
	
	public static void main(String[] args) {
		unsafethread = new unsafethread();
		for(int i=0; i<100;i++) {
			new Threadrun().start();
		}
		ready=true;
		number=42;
	}
}
