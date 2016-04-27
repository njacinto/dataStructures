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
public class IntCircularArrayQueue extends IntCircularArrayCollection implements IntQueue, Serializable {

    public IntCircularArrayQueue() {
        super();
    }
    
    public IntCircularArrayQueue(int initialCapacity) {
        super(initialCapacity);
    }
    
    public IntCircularArrayQueue(int initialCapacity, int maxCapacity) {
        super(initialCapacity, maxCapacity);
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
    
}
