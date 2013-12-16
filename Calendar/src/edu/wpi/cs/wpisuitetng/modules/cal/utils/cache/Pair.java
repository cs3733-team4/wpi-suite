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

	public void setA(A a) {
		this.a = a;
	}

	public B getB() {
		return b;
	}

	public void setB(B b) {
		this.b = b;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof Pair))
		{
			return false;
		}
		Pair<?, ?> casted = (Pair<?, ?>)other;
		return casted.getA().equals(getA()) && casted.getB().equals(getB());
	}
	
	@Override
	public int hashCode()
	{
		return this.getA().hashCode() ^ this.getB().hashCode();
	}
}
