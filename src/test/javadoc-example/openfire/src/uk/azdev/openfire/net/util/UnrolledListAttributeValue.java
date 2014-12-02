/*
 * OpenFire - a Java API to access the XFire instant messaging network.
 * Copyright (C) 2007 Iain McGinniss
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package uk.azdev.openfire.net.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import uk.azdev.openfire.net.attrvalues.AttributeValue;
import uk.azdev.openfire.net.attrvalues.ListAttributeValue;

public class UnrolledListAttributeValue<T> implements List<T> {

	private ListAttributeValue wrappedList;
	
	public UnrolledListAttributeValue(ListAttributeValue wrappedList) {
		this.wrappedList = wrappedList;
	}

	public boolean add(T e) {
		throw new IllegalStateException("unrolled lists are immutable");
	}

	public void add(int index, T element) {
		throw new IllegalStateException("unrolled lists are immutable");
	}

	public boolean addAll(Collection<? extends T> c) {
		throw new IllegalStateException("unrolled lists are immutable");
	}

	public boolean addAll(int index, Collection<? extends T> c) {
		throw new IllegalStateException("unrolled lists are immutable");
	}

	public void clear() {
		throw new IllegalStateException("unrolled lists are immutable");
	}

	public boolean contains(Object o) {
		for(AttributeValue<?> attrVal : wrappedList.getValue()) {
			if(o.equals(attrVal.getValue())) {
				return true;
			}
		}
		
		return false;
	}

	public boolean containsAll(Collection<?> c) {
		throw new IllegalStateException("containsAll is not implemented for unrolled lists");
	}

	@SuppressWarnings("unchecked")
	public T get(int index) {
		return (T)wrappedList.getValue().get(index).getValue();
	}

	public int indexOf(Object o) {
		throw new IllegalStateException("indexOf is not implemented for unrolled lists");
	}

	public boolean isEmpty() {
		return wrappedList.getValue().isEmpty();
	}

	public Iterator<T> iterator() {
		return new UnrolledListAttributeValueIterator<T>(wrappedList);
	}

	public int lastIndexOf(Object o) {
		throw new IllegalStateException("lastIndexOf is not implemented for unrolled lists");
	}

	public ListIterator<T> listIterator() {
		throw new IllegalStateException("listIterator is not implemented for unrolled lists");
	}

	public ListIterator<T> listIterator(int index) {
		throw new IllegalStateException("listIterator is not implemented for unrolled lists");
	}

	public boolean remove(Object o) {
		throw new IllegalStateException("unrolled lists are immutable");
	}

	public T remove(int index) {
		throw new IllegalStateException("unrolled lists are immutable");
	}

	public boolean removeAll(Collection<?> c) {
		throw new IllegalStateException("unrolled lists are immutable");
	}

	public boolean retainAll(Collection<?> c) {
		throw new IllegalStateException("unrolled lists are immutable");
	}

	public T set(int index, T element) {
		throw new IllegalStateException("unrolled lists are immutable");
	}

	public int size() {
		return wrappedList.getValue().size();
	}

	public List<T> subList(int fromIndex, int toIndex) {
		throw new IllegalStateException("subList is not implemented for unrolled lists");
	}

	public Object[] toArray() {
		Object[] values = new Object[wrappedList.getValue().size()];
		
		int i=0;
		for(AttributeValue<?> attrVal : wrappedList.getValue()) {
			values[i++] = attrVal.getValue();
		}
		
		return values;
	}

	@SuppressWarnings("unchecked")
	public <U> U[] toArray(U[] a) {
		U[] uList = a;
		if(a.length < wrappedList.getValue().size()) {
			uList = (U[])java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), wrappedList.getValue().size());
		}
		
		int i = assignValuesToArray(uList);
		
		if(i < uList.length) {
			uList[i] = null;
		}
		
		return uList;
	}

	@SuppressWarnings("unchecked")
	private <U> int assignValuesToArray(U[] uList) {
		int i = 0;
		for(AttributeValue<?> attrVal : wrappedList.getValue()) {
			uList[i++] = (U) attrVal.getValue();
		}
		return i;
	}
}
