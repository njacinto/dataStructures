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

import jav.util.long_.LongListIterator;
import java.util.ListIterator;

/**
 *
 * @author nuno
 */
public class LongListIteratorWrapper implements ListIterator<Long> {
    
    private final LongListIterator itList;

    public LongListIteratorWrapper(LongListIterator itList) {
        this.itList = itList;
    }

    @Override
    public boolean hasNext() {
        return itList.hasNext();
    }

    @Override
    public Long next() {
        return itList.nextLong();
    }

    @Override
    public boolean hasPrevious() {
        return itList.hasPrevious();
    }

    @Override
    public Long previous() {
        return itList.previousLong();
    }

    @Override
    public int nextIndex() {
        return itList.nextIndex();
    }

    @Override
    public int previousIndex() {
        return itList.previousIndex();
    }

    @Override
    public void remove() {
        itList.remove();
    }

    @Override
    public void set(Long e) {
        itList.set(e);
    }

    @Override
    public void add(Long e) {
        itList.add(e);
    }
    
}
