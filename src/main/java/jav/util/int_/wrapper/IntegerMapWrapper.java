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

import jav.util.int_.IntMap;
import jav.util.int_.IntMapUtil;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author nuno
 */
public class IntegerMapWrapper implements Map<Integer,Integer> {
    private final IntMap map;

    public IntegerMapWrapper(IntMap map) {
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
    public boolean containsKey(Object key) {
        return key!=null && key instanceof Number &&  map.containsKey(((Number)key).intValue());
    }

    @Override
    public boolean containsValue(Object value) {
        return value!=null && value instanceof Number && map.containsValue(((Number)value).intValue());
    }

    @Override
    public Integer get(Object key) {
        return key!=null && key instanceof Number ? map.get(((Number)key).intValue()) : null;
    }

    @Override
    public Integer put(Integer key, Integer value) {
        return map.put(key, value);
    }

    @Override
    public Integer remove(Object key) {
        return key!=null && key instanceof Number ? map.remove(((Number)key).intValue()) : null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Integer> m) {
        for(Map.Entry<? extends Integer, ? extends Integer> entry : m.entrySet()){
            if(entry!=null){
                map.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<Integer> keySet() {
        return new IntegerSetWrapper(map.intKeySet());
    }

    @Override
    public Collection<Integer> values() {
        return new IntegerCollectionWrapper(map.intValues());
    }

    @Override
    public Set<Entry<Integer, Integer>> entrySet() {
        return new IntMapUtil.MapEntrySet(map.entrySet(), map);
    }
    
}
