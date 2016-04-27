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
package jav.util.long_.wrapper;

import jav.util.long_.LongCollection;
import java.util.Collection;
import java.util.Objects;
import java.util.PrimitiveIterator;

/**
 *
 * @author nuno
 */
public class LongCollectionWrapper implements Collection<Long> {
    
    private final LongCollection collection;

    public LongCollectionWrapper(LongCollection collection) {
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
        return o!=null && o instanceof Number && collection.contains(((Number)o).longValue());
    }

    @Override
    public PrimitiveIterator.OfLong iterator() {
        return collection.iterator();
    }

    @Override
    public Long[] toArray() {
        int tmpSize = collection.size();
        PrimitiveIterator.OfLong it = collection.iterator();
        Long []ret = new Long[tmpSize];
        int i = 0;
        while(it.hasNext()){
            ret[i++] = it.nextLong();
        }
        return ret;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        int tmpSize = collection.size();
        PrimitiveIterator.OfLong it = collection.iterator();
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
    public boolean add(Long e) {
        return collection.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return o!=null && o instanceof Number 
                && collection.remove(((Number)o).longValue());
        
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (e != null && e instanceof Number 
                    && collection.contains(((Number)e).longValue())) {
                continue;
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Long> c) {
        Objects.requireNonNull(c);
        boolean ret = true;
        for (Long e : c) {
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
                    && collection.remove(((Number)e).longValue());
        }
        return ret;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        PrimitiveIterator.OfLong it = collection.iterator();
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
