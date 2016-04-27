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
import java.util.Objects;

/**
 *
 * @author nuno
 */
public class LongMapEntryWrapper implements java.util.Map.Entry<Long,Long> {
    
    private final LongMap.Entry entry;

    public LongMapEntryWrapper(LongMap.Entry entry) {
        this.entry = entry;
    }

    @Override
    public Long getKey() {
        return entry.getKey();
    }

    @Override
    public Long getValue() {
        return entry.getValue();
    }

    @Override
    public Long setValue(Long value) {
        Objects.requireNonNull(value);
        return entry.setValue(((Number)value).longValue());
    }
    
}
