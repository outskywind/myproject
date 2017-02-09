package concurrency.threadlocal;

import concurrency.threadlocal.Threadlocaltest.Member;



public class MainTest {
	
	public static void main(String[] args){
		
		Threadlocaltest.init();
		//内部类不能直接实例化
		
		ObjectThread t1 = new ObjectThread();
		ObjectThread t2 = new ObjectThread();
		ObjectThread t3 = new ObjectThread();
		
		
		
		
		
	}
	
	
class pt implements Runnable{
		
		private Member member;
		
		public pt(){
			
		}
		
		public pt(Member member ){
			this.member = member;
		}

		@Override
		public void run() {
			
			try {
				//initValue
				Threadlocaltest.init();
				
				this.member = Threadlocaltest.getMember();
				
				System.out.println(this.member.get());
				
				this.member.set(10) ;
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}




}
