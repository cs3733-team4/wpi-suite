package edu.wpi.cs.wpisuitetng.modules.cal.utils.cache;

import java.util.HashMap;
import java.util.Map;

public class Cache<K, V>{
	
	Map<K, AccessOrderedList<TimeOrderedList<V>>> cache = 
			new HashMap<K, AccessOrderedList<TimeOrderedList<V>>>();
	
	AccessOrderedList<TimeOrderedList<V>> head;
	TimeOrderedList<V> latest;
	
	public Cache(V value)
	{
		TimeOrderedList<V> newLatest = new TimeOrderedList<V>(value, latest);
		latest = newLatest;
		AccessOrderedList<TimeOrderedList<V>> newHead = new AccessOrderedList<TimeOrderedList<V>>(newLatest);
		head = newHead.access(head).getB();
	}
	
	
	/**
	 * insert a key value pair into the cache
	 * set it as most recently accessed as well
	 * as most recently inserted
	 * 
	 * @param key the key to store
	 * @param value the value to get
	 */
	public void put(K key, V value)
	{
		TimeOrderedList<V> newLatest = new TimeOrderedList<V>(value, latest);
		latest = newLatest;
		AccessOrderedList<TimeOrderedList<V>> newHead = new AccessOrderedList<TimeOrderedList<V>>(newLatest);
		cache.put(key, newHead);
		head = newHead.access(head).getB();
	}
	
	/**
	 * removes an item from the AccessOrderedList,
	 * but not from the TimeOrderedList
	 * 
	 * 
	 * @param key the key from the KVP to remove
	 */
	public void remove(K key)
	{
		AccessOrderedList<TimeOrderedList<V>> fromMap = this.cache.get(key);
		if (fromMap != null)
		{ //move it to the front so that the whole is cleaned up, then delete it from the map
			fromMap.access(head);
			cache.remove(key);
		}
	}
	
	/**
	 * Adds a new change at the top of the queue
	 * @param value the value to add to the list
	 */
	public void pushChange(V value)
	{
		TimeOrderedList<V> newLatest = new TimeOrderedList<V>(value, latest);
		latest = newLatest;
	}
	
	/**
	 * get a value by key from the cache
	 * set it as most recently accessed
	 * but not as most recently inserted
	 * 
	 * @param key they key to pull data from the cache
	 * @return
	 */
	public V access(K key)
	{
		AccessOrderedList<TimeOrderedList<V>> fromMap = this.cache.get(key);
		if (fromMap != null)
		{
			Pair<TimeOrderedList<V>, AccessOrderedList<TimeOrderedList<V>>> result = fromMap.access(head);
			head = result.getB();
			return result.getA().getValue();
		}
		return null;
	}
	
	/**
	 * Get an iterator over the elements in the order that they were last accessed or inserted
	 * NOTE: 
	 * 	this iterator will start at the least recently used element and end at the
	 *  most recently used element. iterating over the elements in this way will not
	 *  update their order
	 * 
	 * @param key the key from the KVP to start the iterator at
	 * @return an iterator over the values in access order
	 */
	public Iterable<TimeOrderedList<V>> accessOrderedCallIterator(K key)
	{
		return cache.get(key);
	}
	
	/**
	 * get an iterator over the elements in the order that they were inserted
	 * NOTE:
	 *  this iterator will start at the least recently inserted element
	 *  it would be advisable, though not required, to apply each element
	 *  to the AccessOrderedList so that the tail events will simply drop
	 *  off and get scooped up by the garbage collection
	 * 
	 * @param key the key from the KVP to start the iterator at
	 * @return an iterator over the values in time order
	 */
	public TimeOrderedList<V> timeOrderedCallIterator(K key)
	{
		if (cache.get(key) == null)
			cache.put(key, new AccessOrderedList<TimeOrderedList<V>>(latest));
		return cache.get(key).access(head).getA();
	}
	
}
