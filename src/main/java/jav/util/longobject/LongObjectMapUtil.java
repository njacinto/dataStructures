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
package jav.util.longobject;

import jav.util.longobject.wrapper.LongObjectMapEntryWrapper;
import jav.util.long_.LongCollection;
import jav.util.long_.LongSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 *
 * @author nuno
 */
public abstract class LongObjectMapUtil {
    
    // Iterator
    public static class KeyLongIterator<T> extends MapLongObjectIterator<T> {

        public KeyLongIterator(Iterator<LongObjectMap.Entry<T>> it) {
            super(it);
        }
        
        @Override
        protected long getValue(LongObjectMap.Entry ret) {
            return ret.getKey();
        }
        
    }
    
    public static class ValueLongObjectIterator<T> implements Iterator<T> {
        private final Iterator<LongObjectMap.Entry<T>> it;
        
        public ValueLongObjectIterator(Iterator<LongObjectMap.Entry<T>> it){
            this.it = it;
        }
        
        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public T next() {
            LongObjectMap.Entry<T> val = it.next();
            return val==null ? null : val.getValue();
        }

        @Override
        public void remove() {
            it.remove();
        }
    }
    
    public abstract static class MapLongObjectIterator<T> implements PrimitiveIterator.OfLong {

        private final Iterator<LongObjectMap.Entry<T>> it;
        
        public MapLongObjectIterator(Iterator<LongObjectMap.Entry<T>> it){
            this.it = it;
        }
        
        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public long nextLong() {
            return getValue(it.next());
        }

        @Override
        public void remove() {
            it.remove();
        }
        
        protected abstract long getValue(LongObjectMap.Entry ret);
    }
    
    // Set
    public static class KeyLongObjectSet<T> implements LongSet {
        protected final Iterable<LongObjectMap.Entry<T>> iterable;
        protected final LongObjectMap map;

        public KeyLongObjectSet(Iterable<LongObjectMap.Entry<T>> iterable, LongObjectMap<T> map) {
            this.iterable = iterable;
            this.map = map;
        }
        
        @Override
        public int size() {
            return map.size();
        }

        @Override
        public boolean isEmpty() {
            return map.isEmpty();
        }

        @Override
        public boolean contains(long value) {
            return containsValue(value);
        }

        @Override
        public PrimitiveIterator.OfLong iterator() {
            return new KeyLongIterator<T>(iterable.iterator());
        }

        @Override
        public long[] toArray() {
            long[] ret = new long[map.size()];
            Iterator<LongObjectMap.Entry<T>> it = iterable.iterator();
            int i = 0;
            while(i<ret.length && it.hasNext()){
                ret[i++] = getValue(it.next());
            }
            return ret;
        }

        @Override
        public long[] toArray(int fromIndex, int toIndex) {
            if(fromIndex<toIndex && fromIndex>-1 && toIndex<=map.size()){
                long[] ret = new long[toIndex-fromIndex];
                Iterator<LongObjectMap.Entry<T>> it = iterable.iterator();
                int i = -1;
                while(++i<fromIndex && it.hasNext()){
                    it.next();
                }
                i = 0;
                while(i<ret.length && it.hasNext()){
                    ret[i++] = getValue(it.next());
                }
                return ret;
            }
            throw new IndexOutOfBoundsException();
        }

        @SuppressWarnings("unchecked")
        public <V> V[] toArray(V[] a) {
            if(a.length<map.size()){
                a = (V[])new Long[map.size()];
            } 
            Iterator<LongObjectMap.Entry<T>> it = iterable.iterator();
            int i = 0;
            while(it.hasNext() && i<a.length){
                a[i++] = (V)(Long)getValue(it.next());
            }
            return a;
        }

        @Override
        public boolean add(long e) {
            throw new UnsupportedOperationException("Add operation not supported");
        }

        @Override
        public boolean remove(long value) {
            Iterator<LongObjectMap.Entry<T>> it = iterable.iterator();
            while(it.hasNext()){
                if(it.next().getKey()==value){
                    it.remove();
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean containsAll(long... c) {
            if(c.length>0){
                for(long value : c){
                    if(!map.containsKey(value)){
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public boolean containsAll(LongCollection c) {
            if(!c.isEmpty()){
                PrimitiveIterator.OfLong it = c.iterator();
                long value;
                while(it.hasNext()){
                    value = it.nextLong();
                    if(!map.containsKey(value)){
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public boolean containsAll(Collection<? extends Long> c) {
            if(c!=null && !c.isEmpty()){
                for(Object o : c){
                    if(o==null || !(o instanceof Long) || 
                            !containsValue((Long)o)){
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public boolean addAll(LongCollection c) {
            throw new UnsupportedOperationException("Add all operation not supported");
        }

        @Override
        public boolean addAll(long ... c) {
            throw new UnsupportedOperationException("Add all operation not supported");
        }

        @Override
        public boolean addAll(Collection<? extends Long> c) {
            throw new UnsupportedOperationException("Add all operation not supported");
        }

        @Override
        public boolean retainAll(long ... elements) {
            Objects.requireNonNull(elements);
            return removeAll(false, elements);
        }

        @Override
        public boolean retainAll(LongCollection c) {
            Objects.requireNonNull(c);
            return removeAll(false, c);
        }

        @Override
        public boolean retainAll(Collection<? extends Long> c) {
            Objects.requireNonNull(c);
            return removeAll(false, c);
        }

        @Override
        public boolean removeAll(long ... elements) {
            Objects.requireNonNull(elements);
            return removeAll(true, elements);
        }

        @Override
        public boolean removeAll(LongCollection c) {
            Objects.requireNonNull(c);
            return removeAll(true, c);
        }

        @Override
        public boolean removeAll(Collection<? extends Long> c) {
            Objects.requireNonNull(c);
            return removeAll(true, c);
        }

        @Override
        public void clear() {
            map.clear();
        }

        @Override
        public Spliterator.OfLong spliterator() {
            return Spliterators.spliterator(iterator(), size(), 0);
        }
        
        //
        protected boolean containsValue(long num){
            return map.containsKey(num);
        }
        
        protected long getValue(LongObjectMap.Entry entry){
            return entry.getKey();
        }
        
        //
        private boolean removeAll(boolean exits, long ... elements){
            Iterator<LongObjectMap.Entry<T>> it = iterable.iterator();
            long value;
            int size = map.size();
            boolean found;
            while(it.hasNext()){
                value = getValue(it.next());
                found = false;
                for(long o : elements){
                    if(value==o){
                        found = true;
                        break;
                    }
                }
                if(found==exits){
                    it.remove();
                }
            }
            return size != map.size();
        }
        
        private boolean removeAll(boolean exits, LongCollection elements){
            Iterator<LongObjectMap.Entry<T>> it = iterable.iterator();
            PrimitiveIterator.OfLong elementsIt;
            long value;
            boolean found;
            int size = map.size();
            while(it.hasNext()){
                value = getValue(it.next());
                elementsIt = elements.iterator();
                found = false;
                while(elementsIt.hasNext()){
                    if(value==elementsIt.nextLong()){
                        found = true;
                        break;
                    }
                }
                if(found==exits){
                    it.remove();
                }
            }
            return size != map.size();
        }
        
        private boolean removeAll(boolean exits, Collection<?> elements){
            Iterator<LongObjectMap.Entry<T>> it = iterable.iterator();
            long value;
            boolean found;
            int size = map.size();
            while(it.hasNext()){
                value = getValue(it.next());
                found = false;
                for(Object o : elements){
                    if(o!=null && o instanceof Long && value==(Long)o){
                        found = true;
                        break;
                    }
                }
                if(found==exits){
                    it.remove();
                }
            }
            return size != map.size();
        }
    }
    
    // Collection
    public static class ValueLongObjectCollection<T> implements Collection<T>, Iterable<T> {
        protected final Iterable<LongObjectMap.Entry<T>> iterable;
        protected final LongObjectMap<T> map;

        public ValueLongObjectCollection(Iterable<LongObjectMap.Entry<T>> iterable, LongObjectMap<T> map) {
            this.iterable = iterable;
            this.map = map;
        }
        
        @Override
        public int size() {
            return map.size();
        }

        @Override
        public boolean isEmpty() {
            return map.isEmpty();
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object value) {
            return containsValue((T)value);
        }

        @Override
        public Iterator<T> iterator() {
            return new ValueLongObjectIterator<T>(iterable.iterator());
        }

        @Override
        @SuppressWarnings("unchecked")
        public T[] toArray() {
            Object[] ret = new Object[map.size()];
            Iterator<LongObjectMap.Entry<T>> it = iterable.iterator();
            int i = 0;
            while(i<ret.length && it.hasNext()){
                ret[i++] = getValue(it.next());
            }
            return (T[])ret;
        }

        @SuppressWarnings("unchecked")
        public T[] toArray(int fromIndex, int toIndex) {
            if(fromIndex<toIndex && fromIndex>-1 && toIndex<=map.size()){
                Object[] ret = new Object[toIndex-fromIndex];
                Iterator<LongObjectMap.Entry<T>> it = iterable.iterator();
                int i = -1;
                while(++i<fromIndex && it.hasNext()){
                    it.next();
                }
                i = 0;
                while(i<ret.length && it.hasNext()){
                    ret[i++] = getValue(it.next());
                }
                return (T[])ret;
            }
            throw new IndexOutOfBoundsException();
        }

        @SuppressWarnings("unchecked")
        @Override
        public <V> V[] toArray(V[] a) {
            if(a.length<map.size()){
                a = (V[])new Object[map.size()];
            } 
            Iterator<LongObjectMap.Entry<T>> it = iterable.iterator();
            int i = 0;
            while(it.hasNext() && i<a.length){
                a[i++] = (V)getValue(it.next());
            }
            return a;
        }

        @Override
        public boolean add(T e) {
            throw new UnsupportedOperationException("Add operation not supported");
        }

        @Override
        public boolean remove(Object value) {
            Iterator<LongObjectMap.Entry<T>> it = iterable.iterator();
            T val;
            while(it.hasNext()){
                val = it.next().getValue();
                if((val==null && value==null)||(value!=null && value.equals(val))){
                    it.remove();
                    return true;
                }
            }
            return false;
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean containsAll(Collection<?> c) {
            if(c!=null && !c.isEmpty()){
                for(Object o : c){
                    if(o==null || !containsValue((T)o)){
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            throw new UnsupportedOperationException("Add all operation not supported");
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            Objects.requireNonNull(c);
            return removeAll(false, c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            Objects.requireNonNull(c);
            return removeAll(true, c);
        }

        @Override
        public void clear() {
            map.clear();
        }

        @Override
        public Spliterator<T> spliterator() {
            return Spliterators.spliterator(iterator(), size(), 0);
        }
        
        //
        protected boolean containsValue(T num){
            return map.containsValue(num);
        }
        
        protected T getValue(LongObjectMap.Entry<T> entry){
            return entry==null ? null : entry.getValue();
        }
        
        //
        
        private boolean removeAll(boolean exits, Collection<?> elements){
            Iterator<LongObjectMap.Entry<T>> it = iterable.iterator();
            T value;
            boolean found;
            int size = map.size();
            while(it.hasNext()){
                value = getValue(it.next());
                found = false;
                for(Object o : elements){
                    if((o!=null && value.equals(o))||(o==null && value==null)){
                        found = true;
                        break;
                    }
                }
                if(found==exits){
                    it.remove();
                }
            }
            return size != map.size();
        }
    }
    
    
    public static class EntrySet<T> implements Set<LongObjectMap.Entry<T>> {
        private final Iterable<LongObjectMap.Entry<T>> iterable;
        private final LongObjectMap map;

        public EntrySet(Iterable<LongObjectMap.Entry<T>> iterable, LongObjectMap map) {
            this.iterable = iterable;
            this.map = map;
        }
        
        @Override
        public int size() {
            return map.size();
        }

        @Override
        public boolean isEmpty() {
            return map.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return (o != null &&  o instanceof LongObjectMap.Entry &&
                    map.containsKey(((LongObjectMap.Entry)o).getKey()));
        }

        @Override
        public Iterator<LongObjectMap.Entry<T>> iterator() {
            return iterable.iterator();
        }

        @Override
        public LongObjectMap.Entry<T>[] toArray() {
            @SuppressWarnings("unchecked")
            LongObjectMap.Entry<T>[] ret = new LongObjectMap.Entry[map.size()];
            Iterator<LongObjectMap.Entry<T>> it = iterable.iterator();
            int i = 0;
            while(it.hasNext() && i<ret.length){
                ret[i++] = it.next();
            }
            return ret;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <V> V[] toArray(V[] a) {
            if(a.length<map.size()){
                return (V[])toArray();
            } else {
                Iterator<LongObjectMap.Entry<T>> it = iterable.iterator();
                int i = 0;
                while(it.hasNext() && i<a.length){
                    a[i++] = (V)it.next();
                }
                return a;
            }
        }

        @Override
        public boolean add(LongObjectMap.Entry<T> e) {
            throw new UnsupportedOperationException("Add operation not supported");
        }

        @Override
        public boolean remove(Object o) {
            if(o != null &&  o instanceof LongObjectMap.Entry){
                map.remove(((LongObjectMap.Entry)o).getKey());
                return true;
            } 
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            if(c!=null && !c.isEmpty()){
                for(Object o : c){
                    if(o==null || !(o instanceof LongObjectMap.Entry) || 
                            !map.containsKey(((LongObjectMap.Entry)o).getKey())){
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends LongObjectMap.Entry<T>> c) {
            throw new UnsupportedOperationException("Add all operation not supported");
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            Objects.requireNonNull(c);
            removeAll(false, c);
            return true;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            Objects.requireNonNull(c);
            removeAll(true, c);
            return true;
        }

        @Override
        public void clear() {
            map.clear();
        }
        
        private void removeAll(boolean exits, Collection<?> elements){
            Iterator<LongObjectMap.Entry<T>> it = iterable.iterator();
            LongObjectMap.Entry entry;
            boolean found;
            while(it.hasNext()){
                entry = it.next();
                found = false;
                for(Object o : elements){
                    if(o!=null && entry.equals(o)){
                        found = true;
                        break;
                    }
                }
                if(found==exits){
                    it.remove();
                }
            }
        }
    }
    
    
    public static class LongMapEntryIterator<T> implements Iterator<java.util.Map.Entry<Long,T>> {
        
        private final Iterator<LongObjectMap.Entry<T>> it;

        public LongMapEntryIterator(Iterator<LongObjectMap.Entry<T>> it) {
            this.it = it;
        }
        

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Map.Entry<Long, T> next() {
            return new LongObjectMapEntryWrapper<T>(it.next());
        }
        
    }
    
    public static class MapEntrySet<T> implements Set<java.util.Map.Entry<Long,T>> {
        private final Iterable<LongObjectMap.Entry<T>> iterable;
        private final LongObjectMap<T> map;

        public MapEntrySet(Iterable<LongObjectMap.Entry<T>> iterable, LongObjectMap<T> map) {
            this.iterable = iterable;
            this.map = map;
        }
        
        @Override
        public int size() {
            return map.size();
        }

        @Override
        public boolean isEmpty() {
            return map.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return (o != null &&  o instanceof LongObjectMap.Entry &&
                    map.containsKey(((LongObjectMap.Entry)o).getKey()));
        }

        @Override
        public Iterator<java.util.Map.Entry<Long,T>> iterator() {
            return new LongMapEntryIterator<T>(iterable.iterator());
        }

        @Override
        @SuppressWarnings("unchecked")
        public java.util.Map.Entry<Long,T>[] toArray() {
            java.util.Map.Entry[] ret = new java.util.Map.Entry[map.size()];
            Iterator<LongObjectMap.Entry<T>> it = iterable.iterator();
            int i = 0;
            while(it.hasNext() && i<ret.length){
                ret[i++] = new LongObjectMapEntryWrapper<T>(it.next());
            }
            return ret;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <V> V[] toArray(V[] a) {
            if(a.length<map.size()){
                return (V[])toArray();
            } else {
                Iterator<LongObjectMap.Entry<T>> it = iterable.iterator();
                int i = 0;
                while(it.hasNext() && i<a.length){
                    a[i++] = (V)new LongObjectMapEntryWrapper<T>(it.next());
                }
                return a;
            }
        }

        @Override
        public boolean add(java.util.Map.Entry e) {
            throw new UnsupportedOperationException("Add operation not supported");
        }

        @Override
        public boolean remove(Object o) {
            if(o != null &&  o instanceof java.util.Map.Entry){
                map.remove(((Number)((java.util.Map.Entry)o).getKey()).longValue());
                return true;
            } 
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            if(c!=null && !c.isEmpty()){
                for(Object o : c){
                    if(o==null || !(o instanceof LongObjectMap.Entry) || 
                            !map.containsKey(((LongObjectMap.Entry)o).getKey())){
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends java.util.Map.Entry<Long,T>> c) {
            throw new UnsupportedOperationException("Add all operation not supported");
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            Objects.requireNonNull(c);
            removeAll(false, c);
            return true;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            Objects.requireNonNull(c);
            removeAll(true, c);
            return true;
        }

        @Override
        public void clear() {
            map.clear();
        }
        
        private void removeAll(boolean exits, Collection<?> elements){
            Iterator<LongObjectMap.Entry<T>> it = iterable.iterator();
            LongObjectMap.Entry<T> entry;
            boolean found;
            while(it.hasNext()){
                entry = it.next();
                found = false;
                for(Object o : elements){
                    if(o instanceof java.util.Map.Entry && equals(entry, (java.util.Map.Entry)o)){
                        found = true;
                        break;
                    }
                }
                if(found==exits){
                    it.remove();
                }
            }
        }
        
        private static boolean equals(LongObjectMap.Entry entry, java.util.Map.Entry mapEntry){
            return mapEntry!=null && mapEntry.getKey()!=null && mapEntry.getValue()!=null
                    && entry.getKey()==((Number)mapEntry.getKey()).longValue() 
                    && (entry.getValue()==mapEntry.getValue() || 
                    (entry.getValue()!=null && entry.getValue().equals(mapEntry.getValue()))); 
        }
    }
}
