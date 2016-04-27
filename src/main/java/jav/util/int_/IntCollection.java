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

import java.util.Collection;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;


public interface IntCollection extends IntIterable {
    int size();
    boolean isEmpty();
    boolean contains(int element);
    int[] toArray();
    int[] toArray(int fromIndex, int toIndex);
    boolean add(int element);
    boolean remove(int element);
    boolean containsAll(int ... c);
    boolean containsAll(IntCollection c);
    boolean containsAll(Collection<? extends Integer> c);
    boolean addAll(int ... c);
    boolean addAll(IntCollection c);
    boolean addAll(Collection<? extends Integer> c);
    boolean removeAll(int ... c);
    boolean removeAll(IntCollection c);
    boolean removeAll(Collection<? extends Integer> c);
    boolean retainAll(int ... c);
    boolean retainAll(IntCollection c);
    boolean retainAll(Collection<? extends Integer> c);
    void clear();
    @Override
    boolean equals(Object o);
    @Override
    int hashCode();
    
    @Override
    PrimitiveIterator.OfInt iterator();
    
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
    default boolean removeIf(IntPredicate filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final PrimitiveIterator.OfInt each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.nextInt())) {
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
    Spliterator.OfInt spliterator();

    /**
     * Returns a sequential {@code Stream} with this collection as its source.
     *
     * @return a sequential {@code Stream} over the elements in this collection
     */
    default IntStream stream() {
        return StreamSupport.intStream(spliterator(), false);
    }

    /**
     * Returns a possibly parallel {@code Stream} with this collection as its
     * source.
     *
     * @return a possibly parallel {@code Stream} over the elements in this
     * collection
     */
    default IntStream parallelStream() {
        return StreamSupport.intStream(spliterator(), true);
    }
}
