Simple Java Performance Library
=========

A simple library to compare method execution times.

Sample usage
----
```sh
RunnableTest test = new RunnableTest();

AbstractRunnableMethod method1 = new AbstractRunnableMethod("Sine") {
	
	@Override
	public void method() {
		Math.sin(Math.random());
	}
};
		
AbstractRunnableMethod method2 = new AbstractRunnableMethod("Sqrt") {
	
	@Override
	public void method() {
		Math.sqrt(Math.random());
	}
};

AbstractRunnableMethod.setExecutionTime(30);
test.addMethod(method1);
test.addMethod(method2);

test.run();
test.evaluate();

/* Console output:

Time taken: 29.98 s

Method      Score (/10) Operations
Sine		4.76		4320709
Sqrt		5.24		4747605

*/

```

License
----

GNU GPLv2
