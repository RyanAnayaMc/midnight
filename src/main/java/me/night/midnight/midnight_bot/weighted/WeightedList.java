package me.night.midnight.midnight_bot.weighted;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class WeightedList<T> implements List<T> {
	private List<T> items;
	private List<Integer> weights;
	private int wt;
	
	public WeightedList() {
		items = new ArrayList<T>();
		weights = new ArrayList<Integer>();
		wt = 0;
	}

	/**
	 * Adds an item with a weight of 1
	 * @param e The item to add
	 */
	@Override
	public boolean add(T e) {
		add(e, 1);
		
		return true;
	}
	
	/**
	 * Adds an item with a given weight
	 * @param e The item to add
	 * @param weight The weight to give the item
	 */
	public void add(T e, int weight) {
		items.add(e);
		weights.add(weight);
		wt += weight;
	}

	/**
	 * Adds an item to a certain index with a weight of 1
	 * @param arg0 Where to add the item
	 * @param arg1 The item to add
	 */
	@Override
	public void add(int arg0, T arg1) {
		add(arg0, arg1, 1);
	}
	
	public void add(int index, T item, int weight) {
		items.add(index, item);
		weights.add(index, weight);
		wt += weight;
	}

	/**
	 * Adds all items from a given Collection with weight 1 for each
	 */
	@Override
	public boolean addAll(Collection<? extends T> c) {
		for (T arg : c)
			add(arg);
		return true;
	}

	/**
	 * Adds all items from a given Collection with weight 1 for each at a given index
	 */
	@Override
	public boolean addAll(int arg0, Collection<? extends T> arg1) {
		for (T arg : arg1)
			add(arg0, arg);
		return true;
	}

	@Override
	public void clear() {
		items.clear();
		weights.clear();
		wt = 0;
	}

	@Override
	public boolean contains(Object o) {
		return items.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return items.containsAll(c);
	}

	/**
	 * Gets an item from the list based on weighted indices
	 */
	@Override
	public T get(int index) {
		if (index >= wt || index < 0)
			throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for WeightedList with wt = " + wt);
		
		for (int i = 0; i < items.size(); i++) {
			index -= weights.get(i);
			if (index < 0)
				return items.get(i);
		}
		return null;
	}
	
	/**
	 * Returns the weight of an item by true index, ignoring weight
	 * @param index The true index of the weight to get
	 * @return The weight, or null if not found
	 */
	public int getWeightOf(int index) {
		return weights.get(index);
	}
	
	/**
	 * Gets an item by index, ignoring weight.
	 * @param index The index of the item to return
	 * @return The item, or null if not found
	 */
	public T getByTrueIndex(int index) {
		return items.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return items.indexOf(o);
	}
	
	/**
	 * Returns the weight of a given object
	 * @param o The object to get the weight of
	 * @return The object's weight, or -1 if not in the list
	 */
	public int weightOf(Object o) {
		int index = items.indexOf(o);
		if (index == -1)
			return -1;
		
		return weights.get(index);
	}

	@Override
	public boolean isEmpty() {
		return items.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return items.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return items.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return items.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return items.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		int index = items.indexOf(o);
		if (index == -1)
			return false;
		
		wt -= weights.remove(index);
		items.remove(index);
		return true;
	}

	@Override
	public T remove(int index) {
		wt -= weights.remove(index);
		return items.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean retVal = false;
		
		for (int i = items.size() - 1; i >= 0; i--) {
			if (c.contains(items.get(i))) {
				remove(i);
				retVal = true;
			}
		}
		
		return retVal;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean retVal = false;
		
		for (int i = items.size() - 1; i >= 0; i--) {
			if (!c.contains(items.get(i))) {
				remove(i);
				retVal = true;
			}
		}
		
		return retVal;
	}

	/**
	 * Sets an item in the list with weight 1
	 */
	@Override
	public T set(int arg0, T arg1) {
		return set(arg0, arg1, 1);
	}
	
	public T set(int index, T item, int weight) {
		wt -= weights.set(index, weight);
		wt += weight;
		return items.set(index, item);
	}

	/**
	 * Returns the weight of the list
	 */
	@Override
	public int size() {
		return wt;
	}
	
	/**
	 * Returns the number of items in the list, ignoring weight
	 * @return The number of items
	 */
	public int length() {
		return items.size();
	}

	@Override
	public List<T> subList(int arg0, int arg1) {
		WeightedList<T> list = new WeightedList<T>();
		list.items = items.subList(arg0, arg1);
		list.weights = weights.subList(arg0, arg1);
		
		for (int w : list.weights)
			list.wt += w;
		
		return list;
	}

	@Override
	public Object[] toArray() {
		return items.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		return items.toArray(a);
	}
	
	/**
	 * Retreives a random element from the list. Affected by weight.
	 * @return
	 */
	public T getRandom() {
		return get(new Random().nextInt(wt));
	}
}
