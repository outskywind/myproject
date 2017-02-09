package concurrency.threadlocal;


public  class Threadlocaltest {
	//
	public  class Member{
		private int count=0;
		
		public void set(int i) {
			count=i;
		}
		
		public int get(){
			return count;
		}
		
	}
	
	private static ThreadLocal<Member> counterLocal = new ThreadLocal<Member>();
	
	public static void init(){
		setMember(new Threadlocaltest().new Member());
	}
	
	
	public static Member getMember(){
		return counterLocal.get();
	}
	
	public static void setMember(Member member){
		
		System.out.println(Thread.currentThread().getId()+" setMember:"+member);
		counterLocal.set(member);
	}
	
	
	private void _init(){
		new Member();
	}

}
