package com.me.dn;

import java.util.ArrayList;
import java.util.List;

public class DNHashMap<K, V> implements DNMap<K, V> {

	// 默认数组大小
	private static int defaultSize = 16;
	// 定义Map骨架，也就是Entry 数组
	private Entry<K, V>[] table = null;
	// 扩容因子 0.75f useSize/数组总长度 > 0.75
	private static double defaultAddSizeFactor = 0.75;
	// 使用数组位置的长度
	private int useSize;

	public DNHashMap() {
		this(defaultSize, defaultAddSizeFactor);
	}

	public DNHashMap(int length, double defaultAddSizeFactor) {
		if (length <= 0) {
			throw new IllegalArgumentException("参数不能为负数" + length);
		}
		if (defaultAddSizeFactor <= 0 || Double.isNaN(defaultAddSizeFactor)) {
			throw new IllegalArgumentException("扩展因子必须大于0的数字" + defaultAddSizeFactor);
		}

		this.defaultSize = length;
		this.defaultAddSizeFactor = defaultAddSizeFactor;
		this.table = new Entry[defaultSize];
	}

	// 快存，通过hash算法
	@Override
	public V put(K k, V v) {
		// TODO Auto-generated method stub
		if (useSize > defaultSize * defaultAddSizeFactor) {
			up2Size();
		}

		// 通过Key获取数组的存储位置(下标)
		int index = getIndex(k, table.length);
		Entry<K, V> entry = table[index];
		if (entry == null) {
			// 表示table 里面的entry ,也就是当hash冲突时保留原来的entry
			table[index] = new Entry(k, v, null);
			useSize++;
		} else {
			table[index] = new Entry(k, v, entry);
		}
		return table[index].getValue();
	}

	/**
	 * 通过自身的key 和长度来确定存储位置
	 * 
	 * @param k
	 * @param length
	 * @return
	 */
	private int getIndex(K k, int length) {
		// TODO Auto-generated method stub
		// hashcode与运算法
		int m = length - 1;
		int index = hash(k.hashCode()) & m;

		return index >= 0 ? index : -index;
	}

	/**
	 * 自己的hash算法
	 * 
	 * @param hashCode
	 * @return
	 */
	private int hash(int hashCode) {
		// TODO Auto-generated method stub
		hashCode = hashCode ^ ((hashCode >>> 20) ^ (hashCode >>> 12));
		return hashCode ^ ((hashCode >>> 7) ^ (hashCode >>> 4));
	}

	/**
	 * 扩容
	 */
	private void up2Size() {
		Entry<K, V>[] newTable = new Entry[defaultSize * 2];
		// 旧的数组含有很多entry对象，这个对象位置散落在各个角落，再次散列
		againHash(newTable);

	}

	private void againHash(Entry[] newTable) {
		// TODO Auto-generated method stub
		// 数组里面的数据封装到list
		List<Entry<K, V>> entryList = new ArrayList<Entry<K, V>>();
		for (int i = 0; i < table.length; i++) {
			if (table[i] == null)
				continue;
			foundEntryByNext(table[i], entryList);
		}
		if (entryList.size() > 0) {
			useSize = 0;
			defaultSize = 2 * defaultSize;
			table = newTable;

			for (Entry<K, V> entry : entryList) {
				if (entry.next != null) {
					// 取消链表关系
					entry.next = null;
				}
				put(entry.getKey(), entry.getValue());
			}
		}

	}

	private void foundEntryByNext(DNHashMap<K, V>.Entry<K, V> entry, List<Entry<K, V>> entryList) {
		// TODO Auto-generated method stub
		// entry 已经成了链表对象
		if (entry != null && entry.next != null) {
			entryList.add(entry);
			// 递归
			foundEntryByNext(entry, entryList);
		} else {
			entryList.add(entry);
		}

	}

	@Override
	public V get(K k) {
		// TODO Auto-generated method stub
		// 通过Key获取数组的存储位置(下标)
		int index = getIndex(k, table.length);
		if(table[index] == null){
			throw new NullPointerException();
		}
		return findValueByEqualsKey(k,table[index]);
	}

	private V findValueByEqualsKey(K k, DNHashMap<K, V>.Entry<K, V> entry) {

		if(k == entry.getKey() || k.equals(entry.getKey())){
			return entry.getValue();
		}else if(entry.next != null){
			return findValueByEqualsKey(k, entry.next);
		}
		return null;
	}

	class Entry<K, V> implements DNMap.DMEntry<K, V> {
		K k;
		V v;
		Entry<K, V> next;

		public Entry(K k, V v, Entry<K, V> next) {
			super();
			this.k = k;
			this.v = v;
			this.next = next;
		}

		@Override
		public K getKey() {
			// TODO Auto-generated method stub
			return k;
		}

		@Override
		public V getValue() {
			// TODO Auto-generated method stub
			return v;
		}

	}

	public Entry<K, V>[] getTable() {
		return table;
	}

	public void setTable(Entry<K, V>[] table) {
		this.table = table;
	}

	public int getUseSize() {
		return useSize;
	}

	public void setUseSize(int useSize) {
		this.useSize = useSize;
	}
	
	public int size(){
		return useSize;
	}

}
