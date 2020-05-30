package io.github.nuclearfarts.tinysrg.util;


public class Triple<T, U, V> {

	public final T first;
	public final U second;
	public final V third;
	
	public Triple(T t, U u, V v) {
		first = t;
		second = u;
		third = v;
		if(t == null || u == null || v == null) {
			System.out.println("Triple with null constructed: " + this);
		}
	}
	
	@Override
	public int hashCode() {
		return (31 * (31 * first.hashCode()) + second.hashCode()) + third.hashCode();
	}
	
	@Override
	public boolean equals(Object object) {
		if(!(object instanceof Triple)) return false;
		Triple<?, ?, ?> t = (Triple<?, ?, ?>) object;
		return first.equals(t.first) && second.equals(t.second) && third.equals(t.third);
	}
	
	@Override
	public String toString() {
		return String.format("Triple(%s, %s, %s)", first, second, third);
	}
}
