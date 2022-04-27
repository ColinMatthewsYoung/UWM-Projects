package edu.uwm.cs351.util;

import java.util.Map;

/**
 * An entry in a Map
 * @see {@link java.util.Map.Entry}
 */
public abstract class AbstractEntry<K,V> implements Map.Entry<K,V> {

	abstract public K getKey();

	abstract public V getValue();

	public V setValue(V v) {
		throw new UnsupportedOperationException("setValue");
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Map.Entry))
			return false;
		Map.Entry<?,?> e = (Map.Entry<?,?>)o;
		return eq(getKey(), e.getKey()) && eq(getValue(), e.getValue());
	}

	public int hashCode() {
		K key = getKey();
		V value = getValue();
		return ((key   == null) ? 0 :   key.hashCode()) ^
		       ((value == null) ? 0 : value.hashCode());
	}

	public String toString() {
		return getKey() + "=" + getValue();
	}

	private static boolean eq(Object o1, Object o2) {
		return (o1 == null ? o2 == null : o1.equals(o2));
	}
}