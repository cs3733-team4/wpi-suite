package edu.wpi.cs.wpisuitetng.modules.cal.utils.cache;

import java.util.Iterator;

public class TimeOrderedList<V> implements Iterable<V>{

	private V value;
	private TimeOrderedList<V> later;
	
	/**
	 * 
	 * @param value the value of the list
	 * @param later the rest of the list
	 */
	public TimeOrderedList(V value, TimeOrderedList<V> later)
	{
		this.value = value;
		this.addLater(later);
	}
	
	/**
	 * Sets the current element
	 * @param value new value
	 */
	public void setValue(V value)
	{
		this.value = value;
	}
	
	/**
	 * 
	 * @param later the list of events that happened after
	 */
	private void addLater( TimeOrderedList<V> later)
	{
		this.later = later;
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
		return new TimeOrderedListIterator<V>(this);
	}
	

	private class TimeOrderedListIterator<K> implements Iterator<K>, Iterable<K>
	{
		private TimeOrderedList<K> current;
		
		/**
		 * 
		 * @param current the current spot in the list from where to start the iterator
		 */
		public TimeOrderedListIterator(TimeOrderedList<K> current)
		{
			this.current = current;
		}
		
		@Override
		public boolean hasNext() {
			return current != null && current.later != null;
		}

		@Override
		public K next() {
			K elem = this.current.later.value;
			remove();
			return elem;
		}

		@Override
		public void remove() {
			this.current = current.later;
		}

		@Override
		public Iterator<K> iterator() {
			return this;
		}
		
	}

}
