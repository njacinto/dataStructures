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
package jav.util.intobject;

import jav.util.int_.IntCollection;
import jav.util.int_.IntSet;
import jav.util.intobject.wrapper.IntegerObjectMapEntryWrapper;
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
public abstract class IntObjectMapUtil {
    
    // Iterator
    public static class KeyIntIterator<T> extends MapIntObjectIterator<T> {

        public KeyIntIterator(Iterator<IntObjectMap.Entry<T>> it) {
            super(it);
        }
        
        @Override
        protected int getValue(IntObjectMap.Entry ret) {
            return ret.getKey();
        }
        
    }
    
    public static class ValueIntObjectIterator<T> implements Iterator<T> {
        private final Iterator<IntObjectMap.Entry<T>> it;
        
        public ValueIntObjectIterator(Iterator<IntObjectMap.Entry<T>> it){
            this.it = it;
        }
        
        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public T next() {
            IntObjectMap.Entry<T> val = it.next();
            return val==null ? null : val.getValue();
        }

        @Override
        public void remove() {
            it.remove();
        }
    }
    
    public abstract static class MapIntObjectIterator<T> implements PrimitiveIterator.OfInt {

        private final Iterator<IntObjectMap.Entry<T>> it;
        
        public MapIntObjectIterator(Iterator<IntObjectMap.Entry<T>> it){
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
        
        protected abstract int getValue(IntObjectMap.Entry ret);
    }
    
    // Set
    public static class KeyIntObjectSet<T> implements IntSet {
        protected final Iterable<IntObjectMap.Entry<T>> iterable;
        protected final IntObjectMap map;

        public KeyIntObjectSet(Iterable<IntObjectMap.Entry<T>> iterable, IntObjectMap<T> map) {
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
            return new KeyIntIterator<T>(iterable.iterator());
        }

        @Override
        public int[] toArray() {
            int[] ret = new int[map.size()];
            Iterator<IntObjectMap.Entry<T>> it = iterable.iterator();
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
                Iterator<IntObjectMap.Entry<T>> it = iterable.iterator();
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
                a = (V[])new Integer[map.size()];
            } 
            Iterator<IntObjectMap.Entry<T>> it = iterable.iterator();
            int i = 0;
            while(it.hasNext() && i<a.length){
                a[i++] = (V)(Integer)getValue(it.next());
            }
            return a;
        }

        @Override
        public boolean add(int e) {
            throw new UnsupportedOperationException("Add operation not supported");
        }

        @Override
        public boolean remove(int value) {
            Iterator<IntObjectMap.Entry<T>> it = iterable.iterator();
            while(it.hasNext()){
                if(it.next().getKey()==value){
                    it.remove();
                    return true;
                }
            }
            return false;
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean containsAll(int... c) {
            if(c.length>0){
                for(int value : c){
                    if(!map.containsKey(value)){
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
                    if(!map.containsKey(value)){
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
                            !contains((Integer)o)){
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
            return map.containsKey(num);
        }
        
        protected int getValue(IntObjectMap.Entry entry){
            return entry.getKey();
        }
        
        //
        private boolean removeAll(boolean exits, int ... elements){
            Iterator<IntObjectMap.Entry<T>> it = iterable.iterator();
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
            Iterator<IntObjectMap.Entry<T>> it = iterable.iterator();
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
            Iterator<IntObjectMap.Entry<T>> it = iterable.iterator();
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
    
    // Collection
    public static class ValueIntObjectCollection<T> implements Collection<T>, Iterable<T> {
        protected final Iterable<IntObjectMap.Entry<T>> iterable;
        protected final IntObjectMap<T> map;

        public ValueIntObjectCollection(Iterable<IntObjectMap.Entry<T>> iterable, IntObjectMap<T> map) {
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
            return new ValueIntObjectIterator<T>(iterable.iterator());
        }

        @Override
        @SuppressWarnings("unchecked")
        public T[] toArray() {
            Object[] ret = new Object[map.size()];
            Iterator<IntObjectMap.Entry<T>> it = iterable.iterator();
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
                Iterator<IntObjectMap.Entry<T>> it = iterable.iterator();
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

        @Override
        @SuppressWarnings("unchecked")
        public <V> V[] toArray(V[] a) {
            if(a.length<map.size()){
                a = (V[])new Object[map.size()];
            } 
            Iterator<IntObjectMap.Entry<T>> it = iterable.iterator();
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
            Iterator<IntObjectMap.Entry<T>> it = iterable.iterator();
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

        @SuppressWarnings("unchecked")
        public boolean containsAll(T... c) {
            if(c.length>0){
                for(T value : c){
                    if(!map.containsValue(value)){
                        return false;
                    }
                }
            }
            return true;
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
        
        protected T getValue(IntObjectMap.Entry<T> entry){
            return entry==null ? null : entry.getValue();
        }
        
        //
        
        private boolean removeAll(boolean exits, Collection<?> elements){
            Iterator<IntObjectMap.Entry<T>> it = iterable.iterator();
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
    
    
    public static class EntrySet<T> implements Set<IntObjectMap.Entry<T>> {
        private final Iterable<IntObjectMap.Entry<T>> iterable;
        private final IntObjectMap map;

        public EntrySet(Iterable<IntObjectMap.Entry<T>> iterable, IntObjectMap map) {
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
            return (o != null &&  o instanceof IntObjectMap.Entry &&
                    map.containsKey(((IntObjectMap.Entry)o).getKey()));
        }

        @Override
        public Iterator<IntObjectMap.Entry<T>> iterator() {
            return iterable.iterator();
        }

        @Override
        @SuppressWarnings("unchecked")
        public IntObjectMap.Entry<T>[] toArray() {
            IntObjectMap.Entry<T>[] ret = new IntObjectMap.Entry[map.size()];
            Iterator<IntObjectMap.Entry<T>> it = iterable.iterator();
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
                Iterator<IntObjectMap.Entry<T>> it = iterable.iterator();
                int i = 0;
                while(it.hasNext() && i<a.length){
                    a[i++] = (V)it.next();
                }
                return a;
            }
        }

        @Override
        public boolean add(IntObjectMap.Entry e) {
            throw new UnsupportedOperationException("Add operation not supported");
        }

        @Override
        public boolean remove(Object o) {
            if(o != null &&  o instanceof IntObjectMap.Entry){
                map.remove(((IntObjectMap.Entry)o).getKey());
                return true;
            } 
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            if(c!=null && !c.isEmpty()){
                for(Object o : c){
                    if(o==null || !(o instanceof IntObjectMap.Entry) || 
                            !map.containsKey(((IntObjectMap.Entry)o).getKey())){
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends IntObjectMap.Entry<T>> c) {
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
            Iterator<IntObjectMap.Entry<T>> it = iterable.iterator();
            IntObjectMap.Entry entry;
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
    
    
    public static class IntegerMapEntryIterator<T> implements Iterator<java.util.Map.Entry<Integer,T>> {
        
        private final Iterator<IntObjectMap.Entry<T>> it;

        public IntegerMapEntryIterator(Iterator<IntObjectMap.Entry<T>> it) {
            this.it = it;
        }
        

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Map.Entry<Integer, T> next() {
            return new IntegerObjectMapEntryWrapper<T>(it.next());
        }
        
    }
    
    public static class MapEntrySet<T> implements Set<java.util.Map.Entry<Integer,T>> {
        private final Iterable<IntObjectMap.Entry<T>> iterable;
        private final IntObjectMap map;

        public MapEntrySet(Iterable<IntObjectMap.Entry<T>> iterable, IntObjectMap map) {
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
            return (o != null &&  o instanceof IntObjectMap.Entry &&
                    map.containsKey(((IntObjectMap.Entry)o).getKey()));
        }

        @Override
        public Iterator<java.util.Map.Entry<Integer,T>> iterator() {
            return new IntegerMapEntryIterator<T>(iterable.iterator());
        }

        @Override
        public java.util.Map.Entry[] toArray() {
            java.util.Map.Entry[] ret = new java.util.Map.Entry[map.size()];
            Iterator<IntObjectMap.Entry<T>> it = iterable.iterator();
            int i = 0;
            while(it.hasNext() && i<ret.length){
                ret[i++] = new IntegerObjectMapEntryWrapper<T>(it.next());
            }
            return ret;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <V> V[] toArray(V[] a) {
            if(a.length<map.size()){
                return (V[])toArray();
            } else {
                Iterator<IntObjectMap.Entry<T>> it = iterable.iterator();
                int i = 0;
                while(it.hasNext() && i<a.length){
                    a[i++] = (V)new IntegerObjectMapEntryWrapper<T>(it.next());
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
                    if(o==null || !(o instanceof IntObjectMap.Entry) || 
                            !map.containsKey(((IntObjectMap.Entry)o).getKey())){
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends java.util.Map.Entry<Integer,T>> c) {
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
            Iterator<IntObjectMap.Entry<T>> it = iterable.iterator();
            IntObjectMap.Entry entry;
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
        
        private static boolean equals(IntObjectMap.Entry entry, java.util.Map.Entry mapEntry){
            return mapEntry!=null && mapEntry.getKey()!=null && mapEntry.getValue()!=null
                    && entry.getKey()==((Number)mapEntry.getKey()).intValue() 
                    && (entry.getValue()==mapEntry.getValue() || 
                    (entry.getValue()!=null && entry.getValue().equals(mapEntry.getValue()))); 
        }
    }
}
