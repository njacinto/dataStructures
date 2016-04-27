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

/**
 *
 */
public interface IntQueue extends IntCollection {
    /**
     * Inserts the specified element into the queue.
     *
     * @param e the element to add
     * @return true if the element was added, otherwise throws IllegalStateException
     * @throws IllegalStateException if the element cannot be added.
     */
    boolean add(int e);

    /**
     * Inserts the specified element into the queue.
     *
     * @param e the element to add
     * @return true if the element was added, false otherwise
     */
    boolean offer(int e);

    /**
     * Retrieves and removes the head of the queue.
     *
     * @return the head of the queue
     * @throws NoSuchElementException if this queue is empty
     */
    int remove();

    /**
     * Retrieves and removes the head of the queue,
     * or the NULL defined value if queue is empty.
     *
     * @return the head of the queue or the NULL defined value if queue is empty
     */
    int poll();

    /**
     * Retrieves the head of the queue, without removing it. 
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    int element();

    /**
     * Retrieves the head of the queue, without removing it. 
     * If the queue is empty, the NULL defined value will be returned.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    int peek();
    
}
