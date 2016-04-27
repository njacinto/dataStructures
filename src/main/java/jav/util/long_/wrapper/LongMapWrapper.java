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

import jav.util.long_.LongMap;
import jav.util.long_.LongMapUtil;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author nuno
 */
public class LongMapWrapper implements Map<Long,Long> {
    private final LongMap map;

    public LongMapWrapper(LongMap map) {
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
        return key!=null && key instanceof Number &&  map.containsKey(((Number)key).longValue());
    }

    @Override
    public boolean containsValue(Object value) {
        return value!=null && value instanceof Number && map.containsValue(((Number)value).longValue());
    }

    @Override
    public Long get(Object key) {
        return key!=null && key instanceof Number ? map.get(((Number)key).longValue()) : null;
    }

    @Override
    public Long put(Long key, Long value) {
        return map.put(key, value);
    }

    @Override
    public Long remove(Object key) {
        return key!=null && key instanceof Number ? map.remove(((Number)key).longValue()) : null;
    }

    @Override
    public void putAll(Map<? extends Long, ? extends Long> m) {
        for(Map.Entry<? extends Long, ? extends Long> entry : m.entrySet()){
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
    public Set<Long> keySet() {
        return new LongSetWrapper(map.longKeySet());
    }

    @Override
    public Collection<Long> values() {
        return new LongCollectionWrapper(map.longValues());
    }

    @Override
    public Set<Entry<Long, Long>> entrySet() {
        return new LongMapUtil.MapEntrySet(map.entrySet(), map);
    }
    
}
