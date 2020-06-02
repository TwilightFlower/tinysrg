package io.github.nuclearfarts.tinyutil.util;


import java.util.Map;

public class Pair<K, V> implements Map.Entry<K, V> {

	public final K left;
	public final V right;
	
	public Pair(K k, V v) {
		this.left = k;
		this.right = v;
	}

	@Override
	public K getKey() {
		return left;
	}

	@Override
	public V getValue() {
		return right;
	}

	@Override
	public V setValue(V value) {
		throw new UnsupportedOperationException("attempt to set value on pair");
	}
	
	@Override
	public String toString() {
		return String.format("Pair(%s, %s)", left, right);
	}
	
	@Override
	public int hashCode() {
		return (31 * left.hashCode()) + right.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if(!(object instanceof Pair)) return false;
		Pair<?, ?> p = (Pair<?, ?>) object;
		return left.equals(p.left) && right.equals(p.right);
	}
}
