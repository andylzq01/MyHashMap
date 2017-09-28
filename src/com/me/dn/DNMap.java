package com.me.dn;

public interface DNMap<K, V> {

	public V put(K k, V v);

	public V get(K k);

	public interface DMEntry<K, V> {

		public K getKey();

		public V getValue();

	}

}
