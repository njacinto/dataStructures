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

import jav.util.int_.IntArrayCollection;
import java.util.Iterator;

/**
 *
 * @author nuno
 */
public class IntegerCollectionWrapperIteratorTest extends IntegerIteratorTest {

    @Override
    protected Iterator<Integer> iterator() {
        return new IntegerCollectionWrapper(new IntArrayCollection()).iterator();
    }

    @Override
    protected Iterator<Integer> iterator(int... nums) {
        return new IntegerCollectionWrapper(new IntArrayCollection(nums)).iterator();
    }
    
}
