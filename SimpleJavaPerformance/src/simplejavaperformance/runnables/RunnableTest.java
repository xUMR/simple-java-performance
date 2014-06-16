package simplejavaperformance.runnables;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RunnableTest {

	/**
	 * The list that contains methods.
	 */
	private ArrayList<AbstractRunnableMethod> listMethod;

	/**
	 * Total time taken by the tests in milliseconds.
	 */
	private long timeElapsed;

	/**
	 * Methods will be scored out of this value.
	 */
	private int outof = 10;

	/**
	 * A warning message will be displayed if a timeout occurs.
	 * 
	 * @param outof
	 *            Methods will be scored out of this value.
	 */
	public RunnableTest(int outof) {
		this.outof = outof;
		listMethod = new ArrayList<AbstractRunnableMethod>();
	}

	/**
	 * The default highest score is 10.
	 */
	public RunnableTest() {
		this(10);
	}

	/**
	 * Add e to the method list. The test is attached to the method.
	 * 
	 * @param e
	 *            Method to be added.
	 * @return Returns itself for chaining.
	 */
	public final RunnableTest addMethod(AbstractRunnableMethod e) {
		if (!listMethod.contains(e)) {
			listMethod.add(e);
		}

		return this;
	}

	/**
	 * Remove e from the method list. The test is detached from the method.
	 * 
	 * @param e
	 *            Method to be removed.
	 * @return Returns itself for chaining.
	 */
	public final RunnableTest removeMethod(AbstractRunnableMethod e) {
		if (listMethod.contains(e)) {
			listMethod.remove(e);
		}

		return this;
	}

	/**
	 * Template method for running methods to be evaluated.
	 */
	public final void run() {
		timeElapsed = System.nanoTime();
		
		for (AbstractRunnableMethod method : listMethod) {
			method.start(); 
		}
	}

	/**
	 * Normalize method runtime durations, and compute method's performance. The
	 * default value for the highest score is 1.
	 */
	public final void evaluate() {
		evaluate(1);
	}

	/**
	 * Normalize method runtime durations, and compute method's performance.
	 * 
	 * @param highscore
	 *            The highest possible score
	 */
	public final void evaluate(int highscore) {
		double sum = 0;

		try {
			for (AbstractRunnableMethod method : listMethod) {
				method.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		timeElapsed = System.nanoTime() - timeElapsed;

		// Normalize the performance values (operations per second)
		sum += AbstractRunnableMethod.getTotalTimeTaken();

		DecimalFormat format = (highscore >= 10) ? new DecimalFormat("0.0")
				: new DecimalFormat("0.00");

		System.out.println("Time taken: "
				+ format.format(((double) timeElapsed / 1000000000L)) + " s");
		System.out.println("\nMethod\t\tScore (/" + outof + ")\tOperations");

		for (AbstractRunnableMethod method : listMethod) {

			System.out.print(method.getName() + "\t");

			if (method.getName().length() <= 7)
				System.out.print("\t");

			long ops = method.getOpsPerSec();
			System.out.print(format.format(outof * ((double) ops / sum)));

			System.out.println("\t\t" + method.getOpsPerSec());
		}
	}
}
