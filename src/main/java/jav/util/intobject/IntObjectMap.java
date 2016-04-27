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
package jav.util.intobject;

import jav.util.int_.IntSet;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface IntObjectMap<T> {
    
    public int size();
    public boolean isEmpty();
    public boolean containsKey(int key);
    public boolean containsValue(T value);
    public T get(int key);
    public T put(int key, T value);
    public T remove(int key);
    public void putAll(IntObjectMap<T> m);
    public void putAll(Map<? extends Integer, ? extends T> m);
    public void clear();
    public IntSet intKeySet();
    public Collection<T> values();
    public Set<IntObjectMap.Entry<T>> entrySet();
    
    interface Entry<T> {
        int getKey();
        T getValue();
        T setValue(T value);
    };
}
