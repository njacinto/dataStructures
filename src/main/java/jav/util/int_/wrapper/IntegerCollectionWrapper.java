/*
 * Copyright (C) 2015 nuno.
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
package jav.util.int_.wrapper;

import jav.util.int_.IntCollection;
import java.util.Collection;
import java.util.Objects;
import java.util.PrimitiveIterator;

/**
 *
 * @author nuno
 */
public class IntegerCollectionWrapper implements Collection<Integer> {
    
    private final IntCollection collection;

    public IntegerCollectionWrapper(IntCollection collection) {
        this.collection = collection;
    }

    @Override
    public int size() {
        return collection.size();
    }

    @Override
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return o!=null && o instanceof Number && collection.contains(((Number)o).intValue());
    }

    @Override
    public PrimitiveIterator.OfInt iterator() {
        return collection.iterator();
    }

    @Override
    public Integer[] toArray() {
        int tmpSize = collection.size();
        PrimitiveIterator.OfInt it = collection.iterator();
        Integer []ret = new Integer[tmpSize];
        int i = 0;
        while(it.hasNext()){
            ret[i++] = it.nextInt();
        }
        return ret;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        int tmpSize = collection.size();
        PrimitiveIterator.OfInt it = collection.iterator();
        if(a.length<tmpSize){
            return (T[])toArray();
        } else {
            int i = 0;
            while(it.hasNext()){
                a[i++] = (T)it.next();
            }
            return a;
        }
    }

    @Override
    public boolean add(Integer e) {
        return collection.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return o!=null && o instanceof Number 
                && collection.remove(((Number)o).intValue());
        
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (e != null && e instanceof Number 
                    && collection.contains(((Number)e).intValue())) {
                continue;
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c) {
        Objects.requireNonNull(c);
        boolean ret = true;
        for (Integer e : c) {
            ret &= e!=null && collection.add(e);
        }
        return ret;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean ret = true;
        for (Object e : c) {
            ret &= e!=null && e instanceof Number 
                    && collection.remove(((Number)e).intValue());
        }
        return ret;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        PrimitiveIterator.OfInt it = collection.iterator();
        while (it.hasNext()) {
            if(!c.contains(it.next())){ 
                it.remove();
            }
        }
        return collection.size()==c.size();
    }

    @Override
    public void clear() {
        collection.clear();
    }
}
