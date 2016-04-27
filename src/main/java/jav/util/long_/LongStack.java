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

/**
 *
 */
public interface LongStack extends LongCollection {
    
    /**
     * Pushes an element onto the stack. The element will be added to the first 
     * position of the collection.
     *
     *
     * @param e the element to add
     * @throws IllegalStateException if the element cannot be added
     */
    void push(long e);

    /**
     * Retrieves and removes the first element of the collection.
     *
     * @return the first element of the collection. 
     * @throws NoSuchElementException if this collection is empty
     */
    long pop();
}