package a.test;

public class TestThread {
	
	// 这里不能用Integer对象计数，因为Integer是对象不可变对象。
	private static Integer count = 100000000;
//	private static CountWarpper count = new CountWarpper(0);
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("----" + System.identityHashCode(count));
		Object lock = new Object();
		new MyThread(count, lock).start();
		new MyThread(count, lock).start();
		Thread.sleep(2000);
		System.out.println(count);
	}
	
	

}

class MyThread extends Thread {
	private Integer count;
	
	private Object lock;
	
	public MyThread(Integer count, Object lock) {
		super();
		this.count = count;
		this.lock = lock;
		System.out.println(System.identityHashCode(count));
	}
	
	@Override
	public void run() {
		synchronized (lock) {
			count = count + 1;
//			count.setCount(count.getCount() + 1);
			System.out.println(Thread.currentThread().getName() + "======" + count);
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}

class CountWarpper {
	
	private Integer count;

	public CountWarpper(Integer count) {
		super();
		this.count = count;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	/*@Override
	public String toString() {
		return "CountWarpper [count=" + count + "]";
	}*/
	
	
	
}
