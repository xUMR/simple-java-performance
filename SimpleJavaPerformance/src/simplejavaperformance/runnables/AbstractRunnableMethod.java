package simplejavaperformance.runnables;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;

public abstract class AbstractRunnableMethod implements Runnable {

	/**
	 * Identifier for the method. It will be truncated if it exceeds 15
	 * characters.
	 */
	private final String name;

	/**
	 * Execution time of the method in nanoseconds.
	 */
	private static long executionTime;
	
	private static boolean executionTimeLock;
	
	/**
	 * Thread that executes the method.
	 */
	private Thread thread;
	
	/**
	 * Constant to convert s to ns.
	 */
	private static final long StoNS = 1000000000L;
	
	/**
	 * Operations per second, to be used for performance comparison.
	 */
	private long operationsPerSecond;
	
	private static ArrayList<Thread> threads;
	
	private static boolean anyThreadIsAlive;
	
	/**
	 * Total time taken by all instances.
	 */
	private static long totalTime;
	
	private static ThreadMXBean threadBean;

	static {
		threads = new ArrayList<Thread>();
		threadBean = ManagementFactory.getThreadMXBean();
	}
	
	/**
	 * The default execution time value is 10 seconds. 
	 * @param name Method name.
	 */
	public AbstractRunnableMethod(String name) {
		this(name, 10);
	}

	/**
	 * 
	 * @param name Method name.
	 * @param executiontime Execution time in seconds.
	 */
	public AbstractRunnableMethod(String name, int exectime) {
		this.name = (name.length() >= 15) ? name.substring(0, 15) : name;
		setExecutionTime(exectime);
		totalTime = 0;
		thread = new Thread(this);
		threads.add(thread);
//		threadBean = ManagementFactory.getThreadMXBean();
	}
	
	/**
	 * Template method for running the method and notifying the observers. Don't
	 * call this method. Call start() instead.
	 */
	public final void run() {

		executionTimeLock = true;

		long op = 0, cpuTime = 0;
		for (; cpuTime < executionTime; ++op) {
			method();
			cpuTime = threadBean.getCurrentThreadCpuTime();
		}

		double realRuntime = (double) cpuTime / StoNS;
		operationsPerSecond = (long) (op / realRuntime);

		totalTime += operationsPerSecond;
		
		anyThreadIsAlive = false;
		for (Thread t : threads) {
			if (t.isAlive())
				anyThreadIsAlive = true;
		}
		
		if (!anyThreadIsAlive)
			executionTimeLock = false;
	}
	
	public final void start() {
		thread.start();
	}
	
	public final void join() throws InterruptedException {
		thread.join();
	}
	
	public final void join(long arg0) throws InterruptedException {
		thread.join(arg0);
	}

	public final String getName() {
		return name;
	}
	
	public final long getOpsPerSec() {
		return operationsPerSecond;
	}
	
	/**
	 * Sets execution time if none of the methods has started running yet. The
	 * unit is seconds.
	 * 
	 * @param t
	 */
	public static final void setExecutionTime(long t) {
		if (!executionTimeLock)
			executionTime = t * StoNS;
	}
	
	public static final long getExecutionTime() {
		return executionTime;
	}
	
	public static final long getTotalTimeTaken() {
		return totalTime;
	}

	/**
	 * The method for performance measurement. Implementing an infinite loop is
	 * not recommended.
	 */
	public abstract void method();
}
