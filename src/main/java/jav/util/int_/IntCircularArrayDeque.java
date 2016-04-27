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
package jav.util.int_;

import java.io.Serializable;
import java.util.NoSuchElementException;

/**
 *
 */
public class IntCircularArrayDeque extends IntCircularArrayCollection implements IntDeque, Serializable {

    public IntCircularArrayDeque() {
        super();
    }
    
    public IntCircularArrayDeque(int initialCapacity) {
        super(initialCapacity);
    }
    
    public IntCircularArrayDeque(int initialCapacity, int maxCapacity) {
        super(initialCapacity, maxCapacity);
    }

    @Override
    public int getFirst() {
        return getFirstElement();
    }

    @Override
    public int getLast() {
        return getLastElement();
    }
    
    @Override
    public boolean offer(int e) {
        if (size() < getMaxCapacity()) {
            return addElementToEnd(e);
        }
        return false;
    }

    @Override
    public int remove() {
        if (size() > 0) {
            return super.removeFirstElement();
        }
        throw new NoSuchElementException("Queue is empty");
    }

    @Override
    public int poll() {
        if (size() > 0) {
            return removeFirstElement();
        }
        return getNullValue();
    }

    @Override
    public int element() {
        if(size()>0){
            return getFirstElement();
        }
        throw new NoSuchElementException("Queue is empty");
    }

    @Override
    public int peek() {
        return (size() > 0) ? getFirstElement() : getNullValue();
    }

    @Override
    public void addFirst(int e) {
        if (size() < getCapacity()) {
            addElementToStart(e);
        }
        throw new IllegalStateException("Queue is full");
    }

    @Override
    public void addLast(int e) {
        if (size() < getCapacity()) {
            addElementToEnd(e);
        }
        throw new IllegalStateException("Queue is full");
    }

    @Override
    public boolean offerFirst(int e) {
        if (size() < getCapacity()) {
            return addElementToStart(e);
        }
        return false;
    }

    @Override
    public boolean offerLast(int e) {
        if (size() < getCapacity()) {
            return addElementToEnd(e);
        }
        return false;
    }

    @Override
    public int removeFirst() {
        if (size() > 0) {
            return super.removeFirstElement();
        }
        throw new NoSuchElementException("Queue is empty");
    }

    @Override
    public int removeLast() {
        if (size() > 0) {
            return super.removeLastElement();
        }
        throw new NoSuchElementException("Queue is empty");
    }

    @Override
    public int pollFirst() {
        if (size() > 0) {
            return super.removeFirstElement();
        }
        return getNullValue();
    }

    @Override
    public int pollLast() {
        if (size() > 0) {
            return super.removeLastElement();
        }
        return getNullValue();
    }

    @Override
    public int peekFirst() {
        if (size() > 0) {
            return super.getFirstElement();
        }
        return getNullValue();
    }

    @Override
    public int peekLast() {
        if (size() > 0) {
            return super.getLastElement();
        }
        return getNullValue();
    }

    @Override
    public boolean removeFirstOccurrence(int e) {
        int index = indexOfElement(e);
        if(index!=-1){
            removeElementAtIndex(index);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(int e) {
        int index = lastIndexOfElement(e);
        if(index!=-1){
            removeElementAtIndex(index);
            return true;
        }
        return false;
    }

    @Override
    public void push(int e) {
        addElementToEnd(e);
    }

    @Override
    public int pop() {
        return removeLastElement();
    }
}
