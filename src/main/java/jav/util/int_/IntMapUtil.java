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
package jav.util.int_;

import jav.util.int_.wrapper.IntMapEntryWrapper;
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
public abstract class IntMapUtil {
    
    // Iterator
    public static class KeyIntIterator extends MapIntIterator {

        public KeyIntIterator(Iterator<IntMap.Entry> it) {
            super(it);
        }
        
        @Override
        protected int getValue(IntMap.Entry ret) {
            return ret.getKey();
        }
        
    }
    
    public static class ValueIntIterator extends MapIntIterator {

        public ValueIntIterator(Iterator<IntMap.Entry> it) {
            super(it);
        }
        
        @Override
        protected int getValue(IntMap.Entry ret) {
            return ret.getValue();
        }
        
    }
    
    public abstract static class MapIntIterator implements PrimitiveIterator.OfInt {

        private final Iterator<IntMap.Entry> it;
        
        public MapIntIterator(Iterator<IntMap.Entry> it){
            this.it = it;
        }
        
        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public int nextInt() {
            return getValue(it.next());
        }

        @Override
        public void remove() {
            it.remove();
        }
        
        protected abstract int getValue(IntMap.Entry ret);
    }
    
    // Set
    public static class KeyIntSet extends ValueIntCollection implements IntSet {

        public KeyIntSet(Iterable<IntMap.Entry> iterable, IntMap map) {
            super(iterable, map);
        }
        
        @Override
        public PrimitiveIterator.OfInt iterator() {
            return new KeyIntIterator(iterable.iterator());
        }
        
        @Override
        protected boolean containsValue(int num){
            return map.containsKey(num);
        }
        
        @Override
        protected int getValue(IntMap.Entry entry){
            return entry.getKey();
        }
    }
    
    // Collection
    public static class ValueIntCollection implements IntCollection, Iterable<Integer> {
        protected final Iterable<IntMap.Entry> iterable;
        protected final IntMap map;

        public ValueIntCollection(Iterable<IntMap.Entry> iterable, IntMap map) {
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
        public boolean contains(int value) {
            return containsValue(value);
        }

        @Override
        public PrimitiveIterator.OfInt iterator() {
            return new ValueIntIterator(iterable.iterator());
        }

        @Override
        public int[] toArray() {
            int[] ret = new int[map.size()];
            Iterator<IntMap.Entry> it = iterable.iterator();
            int i = 0;
            while(i<ret.length && it.hasNext()){
                ret[i++] = getValue(it.next());
            }
            return ret;
        }

        @Override
        public int[] toArray(int fromIndex, int toIndex) {
            if(fromIndex<toIndex && fromIndex>-1 && toIndex<=map.size()){
                int[] ret = new int[toIndex-fromIndex];
                Iterator<IntMap.Entry> it = iterable.iterator();
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
                a = (T[])new Integer[map.size()];
            } 
            Iterator<IntMap.Entry> it = iterable.iterator();
            int i = 0;
            while(it.hasNext() && i<a.length){
                a[i++] = (T)(Integer)getValue(it.next());
            }
            return a;
        }

        @Override
        public boolean add(int e) {
            throw new UnsupportedOperationException("Add operation not supported");
        }

        @Override
        public boolean remove(int value) {
            Iterator<IntMap.Entry> it = iterable.iterator();
            while(it.hasNext()){
                if(it.next().getValue()==value){
                    it.remove();
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean containsAll(int... c) {
            if(c.length>0){
                for(int value : c){
                    if(!map.containsValue(value)){
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public boolean containsAll(IntCollection c) {
            if(!c.isEmpty()){
                PrimitiveIterator.OfInt it = c.iterator();
                int value;
                while(it.hasNext()){
                    value = it.nextInt();
                    if(!map.containsValue(value)){
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public boolean containsAll(Collection<? extends Integer> c) {
            if(c!=null && !c.isEmpty()){
                for(Object o : c){
                    if(o==null || !(o instanceof Integer) || 
                            !containsValue((Integer)o)){
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public boolean addAll(IntCollection c) {
            throw new UnsupportedOperationException("Add all operation not supported");
        }

        @Override
        public boolean addAll(int ... c) {
            throw new UnsupportedOperationException("Add all operation not supported");
        }

        @Override
        public boolean addAll(Collection<? extends Integer> c) {
            throw new UnsupportedOperationException("Add all operation not supported");
        }

        @Override
        public boolean retainAll(int ... elements) {
            Objects.requireNonNull(elements);
            return removeAll(false, elements);
        }

        @Override
        public boolean retainAll(IntCollection c) {
            Objects.requireNonNull(c);
            return removeAll(false, c);
        }

        @Override
        public boolean retainAll(Collection<? extends Integer> c) {
            Objects.requireNonNull(c);
            return removeAll(false, c);
        }

        @Override
        public boolean removeAll(int ... elements) {
            Objects.requireNonNull(elements);
            return removeAll(true, elements);
        }

        @Override
        public boolean removeAll(IntCollection c) {
            Objects.requireNonNull(c);
            return removeAll(true, c);
        }

        @Override
        public boolean removeAll(Collection<? extends Integer> c) {
            Objects.requireNonNull(c);
            return removeAll(true, c);
        }

        @Override
        public void clear() {
            map.clear();
        }

        @Override
        public Spliterator.OfInt spliterator() {
            return Spliterators.spliterator(iterator(), size(), 0);
        }
        
        //
        protected boolean containsValue(int num){
            return map.containsValue(num);
        }
        
        protected int getValue(IntMap.Entry entry){
            return entry.getValue();
        }
        
        //
        private boolean removeAll(boolean exits, int ... elements){
            Iterator<IntMap.Entry> it = iterable.iterator();
            int value;
            int size = map.size();
            boolean found;
            while(it.hasNext()){
                value = getValue(it.next());
                found = false;
                for(int o : elements){
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
        
        private boolean removeAll(boolean exits, IntCollection elements){
            Iterator<IntMap.Entry> it = iterable.iterator();
            PrimitiveIterator.OfInt elementsIt;
            int value;
            boolean found;
            int size = map.size();
            while(it.hasNext()){
                value = getValue(it.next());
                elementsIt = elements.iterator();
                found = false;
                while(elementsIt.hasNext()){
                    if(value==elementsIt.nextInt()){
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
            Iterator<IntMap.Entry> it = iterable.iterator();
            int value;
            boolean found;
            int size = map.size();
            while(it.hasNext()){
                value = getValue(it.next());
                found = false;
                for(Object o : elements){
                    if(o!=null && o instanceof Integer && value==(Integer)o){
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
    
    
    public static class EntrySet implements Set<IntMap.Entry> {
        private final Iterable<IntMap.Entry> iterable;
        private final IntMap map;

        public EntrySet(Iterable<IntMap.Entry> iterable, IntMap map) {
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
            return (o != null &&  o instanceof IntMap.Entry &&
                    map.containsKey(((IntMap.Entry)o).getKey()));
        }

        @Override
        public Iterator<IntMap.Entry> iterator() {
            return iterable.iterator();
        }

        @Override
        public IntMap.Entry[] toArray() {
            IntMap.Entry[] ret = new IntMap.Entry[map.size()];
            Iterator<IntMap.Entry> it = iterable.iterator();
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
                Iterator<IntMap.Entry> it = iterable.iterator();
                int i = 0;
                while(it.hasNext() && i<a.length){
                    a[i++] = (T)it.next();
                }
                return a;
            }
        }

        @Override
        public boolean add(IntMap.Entry e) {
            throw new UnsupportedOperationException("Add operation not supported");
        }

        @Override
        public boolean remove(Object o) {
            if(o != null &&  o instanceof IntMap.Entry){
                map.remove(((IntMap.Entry)o).getKey());
                return true;
            } 
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            if(c!=null && !c.isEmpty()){
                for(Object o : c){
                    if(o==null || !(o instanceof IntMap.Entry) || 
                            !map.containsKey(((IntMap.Entry)o).getKey())){
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends IntMap.Entry> c) {
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
            Iterator<IntMap.Entry> it = iterable.iterator();
            IntMap.Entry entry;
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
    
    
    public static class IntegerMapEntryIterator implements Iterator<java.util.Map.Entry<Integer,Integer>> {
        
        private final Iterator<IntMap.Entry> it;

        public IntegerMapEntryIterator(Iterator<IntMap.Entry> it) {
            this.it = it;
        }
        

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Map.Entry<Integer, Integer> next() {
            return new IntMapEntryWrapper(it.next());
        }
        
    }
    
    public static class MapEntrySet implements Set<java.util.Map.Entry<Integer,Integer>> {
        private final Iterable<IntMap.Entry> iterable;
        private final IntMap map;

        public MapEntrySet(Iterable<IntMap.Entry> iterable, IntMap map) {
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
            return (o != null &&  o instanceof IntMap.Entry &&
                    map.containsKey(((IntMap.Entry)o).getKey()));
        }

        @Override
        public Iterator<java.util.Map.Entry<Integer,Integer>> iterator() {
            return new IntegerMapEntryIterator(iterable.iterator());
        }

        @Override
        public java.util.Map.Entry[] toArray() {
            java.util.Map.Entry[] ret = new java.util.Map.Entry[map.size()];
            Iterator<IntMap.Entry> it = iterable.iterator();
            int i = 0;
            while(it.hasNext() && i<ret.length){
                ret[i++] = new IntMapEntryWrapper(it.next());
            }
            return ret;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T[] toArray(T[] a) {
            if(a.length<map.size()){
                return (T[])toArray();
            } else {
                Iterator<IntMap.Entry> it = iterable.iterator();
                int i = 0;
                while(it.hasNext() && i<a.length){
                    a[i++] = (T)new IntMapEntryWrapper(it.next());
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
                map.remove(((Number)((java.util.Map.Entry)o).getKey()).intValue());
                return true;
            } 
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            if(c!=null && !c.isEmpty()){
                for(Object o : c){
                    if(o==null || !(o instanceof IntMap.Entry) || 
                            !map.containsKey(((IntMap.Entry)o).getKey())){
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends java.util.Map.Entry<Integer,Integer>> c) {
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
            Iterator<IntMap.Entry> it = iterable.iterator();
            IntMap.Entry entry;
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
        
        private static boolean equals(IntMap.Entry entry, java.util.Map.Entry mapEntry){
            return mapEntry!=null && mapEntry.getKey()!=null && mapEntry.getValue()!=null
                    && entry.getKey()==((Number)mapEntry.getKey()).intValue() 
                    && entry.getValue()==((Number)mapEntry.getValue()).intValue(); 
        }
    }
}
