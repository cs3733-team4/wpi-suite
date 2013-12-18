package edu.wpi.cs.wpisuitetng.modules.cal.utils.cache;

import java.util.Iterator;

public class TimeOrderedList<V, K> implements Iterable<V>{

	private V value;
	private TimeOrderedList<V, K> later;
	private K keyedOn;
	private Cache<K, V> inCache;
	
	/**
	 * 
	 * @param value the value of the list
	 * @param later the rest of the list
	 */
	public TimeOrderedList(V value, TimeOrderedList<V, K> later)
	{
		this.value = value;
		this.addLater(later);
	}
	
	/**
	 * 
	 * @param later the list of events that happened after
	 */
	public void addLater( TimeOrderedList<V, K> later)
	{
		this.later = later;
	}
	
	/**
	 * Prepares this item to be updated via iterator. Not thread safe. Only one iterator may be used at a time;
	 * @param cache cache we are stored in
	 * @param key Key we are stored at
	 */
	public TimeOrderedList<V, K> boundOn(Cache<K, V> cache, K key)
	{
		inCache = cache;
		keyedOn = key;
		return this;
	}
	
	/**
	 * 
	 * @return the value at this point in the list
	 */
	public V getValue()
	{
		return this.value;
	}
	
	
	@Override
	public Iterator<V> iterator()
	{
		return new TimeOrderedListIterator();
	}

	private class TimeOrderedListIterator implements Iterator<V>, Iterable<V>
	{
		private TimeOrderedList<V, K> current;
		
		/**
		 * 
		 * @param current the current spot in the list from where to start the iterator
		 */
		public TimeOrderedListIterator()
		{
			this.current = TimeOrderedList.this;
		}
		
		@Override
		public boolean hasNext() {
			return current != null && current.later != null;
		}

		@Override
		public V next() {
			V elem = this.current.later.value;
			remove();
			return elem;
		}

		@Override
		public void remove() {
			inCache.cache.get(keyedOn).setValue(current.later);
			this.current = current.later;
		}

		@Override
		public Iterator<V> iterator() {
			return this;
		}
		
	}

}
