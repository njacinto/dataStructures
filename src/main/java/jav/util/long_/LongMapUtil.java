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
package jav.util.long_;

import jav.util.long_.wrapper.LongMapEntryWrapper;
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
public abstract class LongMapUtil {
    
    // Iterator
    public static class KeyLongIterator extends MapLongIterator {

        public KeyLongIterator(Iterator<LongMap.Entry> it) {
            super(it);
        }
        
        @Override
        protected long getValue(LongMap.Entry ret) {
            return ret.getKey();
        }
        
    }
    
    public static class ValueLongIterator extends MapLongIterator {

        public ValueLongIterator(Iterator<LongMap.Entry> it) {
            super(it);
        }
        
        @Override
        protected long getValue(LongMap.Entry ret) {
            return ret.getValue();
        }
        
    }
    
    public abstract static class MapLongIterator implements PrimitiveIterator.OfLong {

        private final Iterator<LongMap.Entry> it;
        
        public MapLongIterator(Iterator<LongMap.Entry> it){
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
        
        protected abstract long getValue(LongMap.Entry ret);
    }
    
    // Set
    public static class KeyLongSet extends ValueLongCollection implements LongSet {

        public KeyLongSet(Iterable<LongMap.Entry> iterable, LongMap map) {
            super(iterable, map);
        }
        
        @Override
        public PrimitiveIterator.OfLong iterator() {
            return new KeyLongIterator(iterable.iterator());
        }
        
        @Override
        protected boolean containsValue(long num){
            return map.containsKey(num);
        }
        
        @Override
        protected long getValue(LongMap.Entry entry){
            return entry.getKey();
        }
    }
    
    // Collection
    public static class ValueLongCollection implements LongCollection, Iterable<Long> {
        protected final Iterable<LongMap.Entry> iterable;
        protected final LongMap map;

        public ValueLongCollection(Iterable<LongMap.Entry> iterable, LongMap map) {
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
            return new ValueLongIterator(iterable.iterator());
        }

        @Override
        public long[] toArray() {
            long[] ret = new long[map.size()];
            Iterator<LongMap.Entry> it = iterable.iterator();
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
                Iterator<LongMap.Entry> it = iterable.iterator();
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
        public <T> T[] toArray(T[] a) {
            if(a.length<map.size()){
                a = (T[])new Long[map.size()];
            } 
            Iterator<LongMap.Entry> it = iterable.iterator();
            int i = 0;
            while(it.hasNext() && i<a.length){
                a[i++] = (T)(Long)getValue(it.next());
            }
            return a;
        }

        @Override
        public boolean add(long e) {
            throw new UnsupportedOperationException("Add operation not supported");
        }

        @Override
        public boolean remove(long value) {
            Iterator<LongMap.Entry> it = iterable.iterator();
            while(it.hasNext()){
                if(it.next().getValue()==value){
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
                    if(!map.containsValue(value)){
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
                    if(!map.containsValue(value)){
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
            return map.containsValue(num);
        }
        
        protected long getValue(LongMap.Entry entry){
            return entry.getValue();
        }
        
        //
        private boolean removeAll(boolean exits, long ... elements){
            Iterator<LongMap.Entry> it = iterable.iterator();
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
            Iterator<LongMap.Entry> it = iterable.iterator();
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
            Iterator<LongMap.Entry> it = iterable.iterator();
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
    
    
    public static class EntrySet implements Set<LongMap.Entry> {
        private final Iterable<LongMap.Entry> iterable;
        private final LongMap map;

        public EntrySet(Iterable<LongMap.Entry> iterable, LongMap map) {
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
            return (o != null &&  o instanceof LongMap.Entry &&
                    map.containsKey(((LongMap.Entry)o).getKey()));
        }

        @Override
        public Iterator<LongMap.Entry> iterator() {
            return iterable.iterator();
        }

        @Override
        public LongMap.Entry[] toArray() {
            LongMap.Entry[] ret = new LongMap.Entry[map.size()];
            Iterator<LongMap.Entry> it = iterable.iterator();
            int i = 0;
            while(it.hasNext() && i<ret.length){
                ret[i++] = it.next();
            }
            return ret;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T[] toArray(T[] a) {
            if(a.length<map.size()){
                return (T[])toArray();
            } else {
                Iterator<LongMap.Entry> it = iterable.iterator();
                int i = 0;
                while(it.hasNext() && i<a.length){
                    a[i++] = (T)it.next();
                }
                return a;
            }
        }

        @Override
        public boolean add(LongMap.Entry e) {
            throw new UnsupportedOperationException("Add operation not supported");
        }

        @Override
        public boolean remove(Object o) {
            if(o != null &&  o instanceof LongMap.Entry){
                map.remove(((LongMap.Entry)o).getKey());
                return true;
            } 
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            if(c!=null && !c.isEmpty()){
                for(Object o : c){
                    if(o==null || !(o instanceof LongMap.Entry) || 
                            !map.containsKey(((LongMap.Entry)o).getKey())){
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends LongMap.Entry> c) {
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
            Iterator<LongMap.Entry> it = iterable.iterator();
            LongMap.Entry entry;
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
    
    
    public static class LongMapEntryIterator implements Iterator<java.util.Map.Entry<Long,Long>> {
        
        private final Iterator<LongMap.Entry> it;

        public LongMapEntryIterator(Iterator<LongMap.Entry> it) {
            this.it = it;
        }
        

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Map.Entry<Long, Long> next() {
            return new LongMapEntryWrapper(it.next());
        }
        
    }
    
    public static class MapEntrySet implements Set<java.util.Map.Entry<Long,Long>> {
        private final Iterable<LongMap.Entry> iterable;
        private final LongMap map;

        public MapEntrySet(Iterable<LongMap.Entry> iterable, LongMap map) {
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
            return (o != null &&  o instanceof LongMap.Entry &&
                    map.containsKey(((LongMap.Entry)o).getKey()));
        }

        @Override
        public Iterator<java.util.Map.Entry<Long,Long>> iterator() {
            return new LongMapEntryIterator(iterable.iterator());
        }

        @Override
        public java.util.Map.Entry[] toArray() {
            java.util.Map.Entry[] ret = new java.util.Map.Entry[map.size()];
            Iterator<LongMap.Entry> it = iterable.iterator();
            int i = 0;
            while(it.hasNext() && i<ret.length){
                ret[i++] = new LongMapEntryWrapper(it.next());
            }
            return ret;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T[] toArray(T[] a) {
            if(a.length<map.size()){
                return (T[])toArray();
            } else {
                Iterator<LongMap.Entry> it = iterable.iterator();
                int i = 0;
                while(it.hasNext() && i<a.length){
                    a[i++] = (T)new LongMapEntryWrapper(it.next());
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
                    if(o==null || !(o instanceof LongMap.Entry) || 
                            !map.containsKey(((LongMap.Entry)o).getKey())){
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends java.util.Map.Entry<Long,Long>> c) {
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
            Iterator<LongMap.Entry> it = iterable.iterator();
            LongMap.Entry entry;
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
        
        private static boolean equals(LongMap.Entry entry, java.util.Map.Entry mapEntry){
            return mapEntry!=null && mapEntry.getKey()!=null && mapEntry.getValue()!=null
                    && entry.getKey()==((Number)mapEntry.getKey()).longValue() 
                    && entry.getValue()==((Number)mapEntry.getValue()).longValue(); 
        }
    }
}
