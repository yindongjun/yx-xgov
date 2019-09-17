package a.test;

/**
 * 当threadlocal遇上线程池，因为线程池中的线程对象会被复用，所以在每个线程完成后需要
 * 调用remove()方法清理threadlocal.
 * @author yx-hj
 *
 */
public class ThreadLocalTest {
	
	// 只能在当前线程中获取到当前线程set的值。
//	public static ThreadLocal<String> threadLocal = new ThreadLocal<>();
	
	// 在当前线程的子线程中也可以获取到当前线程set的值。
	public static InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<>();
	
	public static void main(String[] args) {
		threadLocal.set("aa");
		System.out.println(threadLocal.get());
		
		new Thread() {
			@Override
			public void run() {
				threadLocal.set("b");
				System.out.println("t1:" + threadLocal.get());
				
				new Thread() {
					@Override
					public void run() {
						System.out.println("t1.1:" + threadLocal.get());
					};
				}.start();
			};
		}.start();
		
		new Thread() {
			
			@Override
			public void run() {
				threadLocal.set("c");
				System.out.println("t2:" + threadLocal.get());
			};
			
		}.start();
	}

}
