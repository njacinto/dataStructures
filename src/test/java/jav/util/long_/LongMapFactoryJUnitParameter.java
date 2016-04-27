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

import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author nuno
 */
public class LongMapFactoryJUnitParameter {

    private LongMapFactoryJUnitParameter() {
    }
    
    public static Collection<LongMapProvider[]> getLongMaps(){
        return Arrays.asList(
            new LongMapProvider[]{  
                    new LongMapProvider(){
                        @Override
                        public LongMap getMap(){
                            return new LongHashMap();
                        }
                    } 
            },
            new LongMapProvider[]{ 
                    new LongMapProvider(){
                        @Override
                        public LongMap getMap(){
                            return new LongTreeMap();
                        }
                    } 
            }
        );
    }
    
    public static interface LongMapProvider {
        LongMap getMap();
    }
}
