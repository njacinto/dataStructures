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

import java.util.Collection;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.LongPredicate;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;


public interface LongCollection extends LongIterable {
    int size();
    boolean isEmpty();
    boolean contains(long element);
    long[] toArray();
    long[] toArray(int fromIndex, int toIndex);
    boolean add(long element);
    boolean remove(long element);
    boolean containsAll(long ... c);
    boolean containsAll(LongCollection c);
    boolean containsAll(Collection<? extends Long> c);
    boolean addAll(long ... c);
    boolean addAll(LongCollection c);
    boolean addAll(Collection<? extends Long> c);
    boolean removeAll(long ... c);
    boolean removeAll(LongCollection c);
    boolean removeAll(Collection<? extends Long> c);
    boolean retainAll(long ... c);
    boolean retainAll(LongCollection c);
    boolean retainAll(Collection<? extends Long> c);
    void clear();
    @Override
    boolean equals(Object o);
    @Override
    int hashCode();
    
    @Override
    PrimitiveIterator.OfLong iterator();
    
    /**
     * Removes all of the elements of this collection that satisfy the given
     * predicate.
     *
     * @param filter a predicate which returns {@code true} for elements to be
     *        removed
     * @return {@code true} if any elements were removed
     * @throws NullPointerException if the specified filter is null
     * @throws UnsupportedOperationException if elements cannot be removed
     *         from this collection.  Implementations may throw this exception if a
     *         matching element cannot be removed or if, in general, removal is not
     *         supported.
     */
    default boolean removeIf(LongPredicate filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final PrimitiveIterator.OfLong each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.nextLong())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }
    
    /**
     * Creates a {@link Spliterator} over the elements in this collection.
     *
     * @return a {@code Spliterator} over the elements in this collection
     */
    //@Override
    @Override
    Spliterator.OfLong spliterator();

    /**
     * Returns a sequential {@code Stream} with this collection as its source.
     *
     * @return a sequential {@code Stream} over the elements in this collection
     */
    default LongStream stream() {
        return StreamSupport.longStream(spliterator(), false);
    }

    /**
     * Returns a possibly parallel {@code Stream} with this collection as its
     * source.
     *
     * @return a possibly parallel {@code Stream} over the elements in this
     * collection
     */
    default LongStream parallelStream() {
        return StreamSupport.longStream(spliterator(), true);
    }
}
