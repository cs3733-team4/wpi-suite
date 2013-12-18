package edu.wpi.cs.wpisuitetng.modules.cal.utils.cache;

public class Pair<A, B> {

	private A a;
	private B b;
	
	public Pair(A a, B b)
	{
		this.setA(a);
		this.setB(b);
	}

	public A getA() {
		return a;
	}

	private void setA(A a) {
		this.a = a;
	}

	public B getB() {
		return b;
	}

	private void setB(B b) {
		this.b = b;
	}
}
