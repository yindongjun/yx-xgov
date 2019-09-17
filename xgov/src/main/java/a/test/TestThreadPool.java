package a.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 等待子线程执行完后，主线程在往下执行。
 *
 */
public class TestThreadPool {
	
	static ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
	static {
		threadPool.initialize();
		threadPool.setCorePoolSize(5);
		threadPool.setMaxPoolSize(5);
		threadPool.setQueueCapacity(200);
		threadPool.setKeepAliveSeconds(300);
		threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
	}
	
	/**
	 * 测试内层catch以后又抛出异常，外层的finally是否执行。
	 * @param args
	 */
	public static void main(String[] args) {
		String string = null;
		try {
			try {
//				string = "jfiejfie";
				System.out.println(string.length());
			} catch (Exception e) {
				System.out.println("里面的catch");
				throw new RuntimeException();
			}
		} catch (Exception e) {
			System.out.println("外面的catch");
			throw new RuntimeException();
		} finally {
			System.out.println("finally");
		}
	}
	
	/**
	 * 测试在countDownLatch.await();执行之前，子线程先执行了countDownLatch.countDown();
	 * @param args
	 */
	public static void main2(String[] args) {
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		long start = System.currentTimeMillis();
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				countDownLatch.countDown();
				System.out.println("子线程中执行了countDown");
			}
		});
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			countDownLatch.await();
			System.out.println("主线程中执行了count await");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("主线程完了" + (System.currentTimeMillis() - start) / 1000);
	}
	
	/**
	 * 测试同一个线程池中创建两批线程，CountDownLatch是否会生效。
	 * @param args
	 */
	public static void main1(String[] args) {
		
		/*ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
		threadPool.initialize();
		threadPool.setCorePoolSize(10);
		threadPool.setMaxPoolSize(20);
		threadPool.setQueueCapacity(200);
		threadPool.setKeepAliveSeconds(300);
		threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());*/
		long startTime = System.currentTimeMillis();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				final CountDownLatch countDownLatch = new CountDownLatch(6);
				long start = System.currentTimeMillis();
				for (int i = 0; i < 5; i++) {
					threadPool.execute(new Runnable() {
						
						@Override
						public void run() {
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							countDownLatch.countDown();
						}
					});
				}
				threadPool.execute(new Runnable() {
					
					@Override
					public void run() {
						
						try {
							Thread.sleep(8000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						countDownLatch.countDown();
					}
				});
				try {
					countDownLatch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("线程1完了" + (System.currentTimeMillis() - start) / 1000);
			}
		}).start();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				final CountDownLatch countDownLatch = new CountDownLatch(4);
				long start = System.currentTimeMillis();
				for (int i = 0; i < 3; i++) {
					threadPool.execute(new Runnable() {
						
						@Override
						public void run() {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							countDownLatch.countDown();
						}
					});
				}
				threadPool.execute(new Runnable() {
					
					@Override
					public void run() {
						
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						countDownLatch.countDown();
					}
				});
				try {
					countDownLatch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("线程2完了" + (System.currentTimeMillis() - start) / 1000);
			}
		}).start();
		System.out.println("主线程完了" + (System.currentTimeMillis() - startTime) / 1000);
		
	}

}
