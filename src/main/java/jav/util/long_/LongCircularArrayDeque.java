/*
 * Copyright (C) 2015.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package jav.util.long_;

import java.io.Serializable;
import java.util.NoSuchElementException;

/**
 *
 */
public class LongCircularArrayDeque extends LongCircularArrayCollection 
            implements LongDeque, Serializable {

    public LongCircularArrayDeque() {
        super();
    }
    
    public LongCircularArrayDeque(int initialCapacity) {
        super(initialCapacity);
    }
    
    public LongCircularArrayDeque(int initialCapacity, int maxCapacity) {
        super(initialCapacity, maxCapacity);
    }

    @Override
    public long getFirst() {
        return getFirstElement();
    }

    @Override
    public long getLast() {
        return getLastElement();
    }
    
    @Override
    public boolean offer(long e) {
        if (size() < getMaxCapacity()) {
            return addElementToEnd(e);
        }
        return false;
    }

    @Override
    public long remove() {
        if (size() > 0) {
            return super.removeFirstElement();
        }
        throw new NoSuchElementException("Queue is empty");
    }

    @Override
    public long poll() {
        if (size() > 0) {
            return removeFirstElement();
        }
        return getNullValue();
    }

    @Override
    public long element() {
        if(size()>0){
            return getFirstElement();
        }
        throw new NoSuchElementException("Queue is empty");
    }

    @Override
    public long peek() {
        return (size() > 0) ? getFirstElement() : getNullValue();
    }

    @Override
    public void addFirst(long e) {
        if (size() < getCapacity()) {
            addElementToStart(e);
        }
        throw new IllegalStateException("Queue is full");
    }

    @Override
    public void addLast(long e) {
        if (size() < getCapacity()) {
            addElementToEnd(e);
        }
        throw new IllegalStateException("Queue is full");
    }

    @Override
    public boolean offerFirst(long e) {
        if (size() < getCapacity()) {
            return addElementToStart(e);
        }
        return false;
    }

    @Override
    public boolean offerLast(long e) {
        if (size() < getCapacity()) {
            return addElementToEnd(e);
        }
        return false;
    }

    @Override
    public long removeFirst() {
        if (size() > 0) {
            return super.removeFirstElement();
        }
        throw new NoSuchElementException("Queue is empty");
    }

    @Override
    public long removeLast() {
        if (size() > 0) {
            return super.removeLastElement();
        }
        throw new NoSuchElementException("Queue is empty");
    }

    @Override
    public long pollFirst() {
        if (size() > 0) {
            return super.removeFirstElement();
        }
        return getNullValue();
    }

    @Override
    public long pollLast() {
        if (size() > 0) {
            return super.removeLastElement();
        }
        return getNullValue();
    }

    @Override
    public long peekFirst() {
        if (size() > 0) {
            return super.getFirstElement();
        }
        return getNullValue();
    }

    @Override
    public long peekLast() {
        if (size() > 0) {
            return super.getLastElement();
        }
        return getNullValue();
    }

    @Override
    public boolean removeFirstOccurrence(long e) {
        int index = indexOfElement(e);
        if(index!=-1){
            removeElementAtIndex(index);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(long e) {
        int index = lastIndexOfElement(e);
        if(index!=-1){
            removeElementAtIndex(index);
            return true;
        }
        return false;
    }

    @Override
    public void push(long e) {
        addElementToEnd(e);
    }

    @Override
    public long pop() {
        return removeLastElement();
    }
}
